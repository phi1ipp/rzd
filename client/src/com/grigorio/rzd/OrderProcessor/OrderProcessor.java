package com.grigorio.rzd.OrderProcessor;

import com.grigorio.rzd.Main;
import com.grigorio.rzd.TicketServiceWSProxy.*;
import com.grigorio.rzd.crypto.Signer;

import java.io.*;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

/** Class which processes orders from a queue,
 * i.e. take an order out, make a request to WS and delivers a result to a client
 * Created by philipp on 10/10/14.
 */

public class OrderProcessor implements Runnable {
    private final static String TAG = OrderProcessor.class.getName();
    private final Logger logger = Logger.getLogger(TAG);

    private final BlockingQueue<TicketServiceJob> queue;

    public OrderProcessor(BlockingQueue<TicketServiceJob> q) {
        queue = q;
    }

    @Override
    public void run() {
        logger.entering(TAG, "run");
        Preferences prefs = Preferences.userRoot().node("com.grigorio.rzd");

        logger.info("Processor thread started");
        while (true) {
            try {
                TicketServiceJob job = queue.take();

                if (job.getId() == 0) {
                    logger.info("Signal to exit!");
                    break;
                } else {
                    logger.fine("New order to process, type=" + job.getType() + " id: " + job.getId());

                    // generate signature
                    Map<String,Object> mapSignature = Signer.sign(job.getId());
                    if (mapSignature == null) {
                        logger.severe("Error generating a signature. Request not sent.");
                        continue;
                    }

                    TicketService ticketService = new TicketService();
                    GetInfo getInfo = ticketService.getPortTicketService();

                    switch (job.getType()) {
                        case "Order" :
                            Order ord = (Order) job;

                            logger.info("processing sale order");
                            logger.info("OrderId=" + ord.getId());
                            logger.info("Tickets=" + ord.getTickets().toString());

                            SaleRequest req = new SaleRequest();
                            req.setOrderId(ord.getId());

                            // set user from preferences and token from order in a queue
                            req.setUser(prefs.get(Main.Preferences.stridUsername, ""));
                            req.setLtpaToken2(ord.getToken());

                            // set create time
                            req.setCreateTime(mapSignature.get("time").toString());

                            // set nonce
                            req.setNonce(Integer.parseInt(mapSignature.get("nonce").toString()));

                            // set signature
                            req.setSignature(mapSignature.get("signature").toString());

                            // create and set mappings
                            TicketMappingList lstMappings = new TicketMappingList();
                            ord.getTickets().forEach(ticket -> {
                                TicketMapping mapping = new TicketMapping();
                                mapping.setTicketId(ticket.getlTicketId());
                                mapping.setTicketNum(ticket.getTicketNum());

                                lstMappings.getMapping().add(mapping);
                            });
                            req.setTicketMappings(lstMappings);

                            try {
                                TransInfoXMLResponse resp = getInfo.saleRequest(req);
                                if (!saveXML(job.getType(), job.getId(), unCDATA(resp.getResponseXMLData()))) {
                                    logger.severe("Can't save response to a file");
                                }
                                logger.finest("XML response: " + resp.getResponseXMLData());
                            } catch (Exception e) {
                                logger.log(Level.SEVERE, "Error requesting an answer...", e);
                            }
                            break;

                        case "Refund" :
                            Refund refund = (Refund) job;
                            logger.info("processing refund");
                            logger.info("OrderId=" + refund.getOrderId() + " TicketId=" + refund.getTicketId());

                            RefundRequest reqRef = new RefundRequest();
                            reqRef.setNonce(Integer.parseInt(mapSignature.get("nonce").toString()));
                            reqRef.setCreateTime(mapSignature.get("time").toString());
                            reqRef.setOrderId(refund.getOrderId());
                            reqRef.setTicketId(refund.getTicketId());
                            reqRef.setUser(prefs.get(Main.Preferences.stridUsername, ""));
                            reqRef.setSignature(mapSignature.get("signature").toString());
                            reqRef.setLtpaToken2(job.getToken());

                            try {
                                RefundXMLResponse respRef = getInfo.requestRefund(reqRef);
                                if (!saveXML(job.getType(), job.getId(), unCDATA(respRef.getResponseXMLData()))) {
                                    logger.severe("Can't save response to a file");
                                }
                                logger.finest(respRef.getResponseXMLData());
                            } catch (Exception e) {
                                logger.log(Level.SEVERE, "Error processing a web-service call...", e);
                            }
                            break;
                        default:
                            logger.warning("Unknown job in a queue: " + job.getType());
                    }
                }
            } catch (InterruptedException e) {
                logger.warning("Interrupted... Exiting!");
                return;
            }
        }
        logger.info("Processor thread finished");
        logger.exiting(TAG, "run");
    }

    private boolean saveXML(String strJobType, long lId, String strContent) {
        logger.entering(TAG, "saveXML", new StringBuilder(strJobType).append(":").append(lId)
                .append(":").append(strContent));

        boolean bRet = false;

        // path for output
        String strPath = Preferences.userRoot().node("com.grigorio.rzd").get(Main.Preferences.stridOutDir, "");

        String strFileName = strJobType + lId + ".xml";

        try {
            File file = new File(strPath + "/" + strFileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(strContent.getBytes());
            fos.close();

            logger.info("XML response saved to " + file.getName());
            bRet = true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Can't create output stream for a file", e);
        }

        logger.exiting(TAG, "saveXML", bRet);
        return bRet;
    }

    /**
     * Unwrap XML from CDATA wrap
     * @param strXML XML CDATA-wrapped
     * @return unwrapped XML
     */
    private String unCDATA(String strXML) {
        logger.entering(TAG, "unCDATA", strXML);

        String strRet = strXML.replace("<![CDATA[","").replace("]]>","");

        logger.exiting(TAG, "unCDATA", strRet);
        return strRet;
    }
}

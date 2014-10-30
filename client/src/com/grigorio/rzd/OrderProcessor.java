package com.grigorio.rzd;

import com.grigorio.rzd.TicketServiceWSProxy.*;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

/** Class which processes orders from a queue,
 * i.e. take an order out, make a request to WS and delivers a result to a client
 * Created by philipp on 10/10/14.
 */

public class OrderProcessor implements Runnable {
    private final static String TAG = OrderProcessor.class.getCanonicalName();
    private final Logger logger = Logger.getLogger(TAG);

    private final BlockingQueue<TicketServiceJob> queue;

    OrderProcessor(BlockingQueue<TicketServiceJob> q) {
        queue = q;
    }

    @Override
    public void run() {
        Preferences prefs = Preferences.userRoot().node("com.grigorio.rzd");

        System.out.println("Processor thread started");
        while (true) {
            try {
                TicketServiceJob job = queue.take();

                if (job.getId() == 0) {
                    System.out.println("Signal to exit!");
                    break;
                } else {
                    System.out.println("New order to process, type=" + job.getType());

                    // get time string in GMT
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                    String strTime = dateFormat.format(new Date());

                    // get nonce
                    int iNonce = new Random().nextInt();

                    String strSignature = null;
                    try {
                        String strPrivKeyLoc = prefs.get(Main.Preferences.stridPrivKeyLoc, "");
                        PrivateKey privKey = getPemPrivateKey(strPrivKeyLoc, "RSA");

                        Signature sigInst = Signature.getInstance("SHA256withRSA");
                        sigInst.initSign(privKey);

                        sigInst.update(String.format("%d%d%s", iNonce, job.getId(), strTime).getBytes());

                        strSignature = Base64.encode(sigInst.sign());
                    } catch (Exception e) {
                        System.err.println("Error generating a signature");
                        e.printStackTrace();
                    }

                    TicketService ticketService = new TicketService();
                    GetInfo getInfo = ticketService.getPortTicketService();

                    switch (job.getType()) {
                        case "Order" :
                            Order ord = (Order) job;
                            TransInfoRequest req = new TransInfoRequest();
                            req.setOrderId(ord.getId());

                            // set user from preferences and token from order in a queue
                            req.setUser(prefs.get(Main.Preferences.stridUsername, ""));
                            req.setLtpaToken2(ord.getToken());

                            // set create time
                            req.setCreateTime(strTime);

                            // set nonce
                            req.setNonce(iNonce);

                            // set signature
                            req.setSignature(strSignature);

                            try {
                                TransInfoXMLResponse resp = getInfo.getTransInfoXML(req);
                                if (!saveXML(job.getType(), job.getId(), unCDATA(resp.getResponseXMLData()))) {
                                    System.err.println("Can't save response to a file");
                                }
                                System.out.println(resp.getResponseXMLData());
                            } catch (Exception e) {
                                System.err.println("Error requesting an answer...");
                                e.printStackTrace();
                            }
                            break;

                        case "Refund" :
                            Refund refund = (Refund) job;

                            RefundRequest reqRef = new RefundRequest();
                            reqRef.setNonce(iNonce);
                            reqRef.setCreateTime(strTime);
                            reqRef.setOrderId(refund.getOrderId());
                            reqRef.setTicketId(refund.getTicketId());
                            reqRef.setUser(prefs.get(Main.Preferences.stridUsername, ""));
                            reqRef.setSignature(strSignature);
                            reqRef.setLtpaToken2(job.getToken());

                            try {
                                RefundXMLResponse respRef = getInfo.requestRefund(reqRef);
                                if (!saveXML(job.getType(), job.getId(), unCDATA(respRef.getResponseXMLData()))) {
                                    System.err.println("Can't save response to a file");
                                }
                                System.out.println(respRef.getResponseXMLData());
                            } catch (Exception e) {
                                System.err.println("Error processing a web-service call...");
                                e.printStackTrace();
                            }
                            break;
                        default:
                            System.err.println("Unknown job in a queue: " + job.getType());
                    }
                }
            } catch (InterruptedException e) {
                System.err.println("Interrupted... Exiting!");
                return;
            }
        }
        System.out.println("Processor thread finished");
    }

    /**
     * Function to load private key from a PEM coded PKCS8 format
     * @param filename String with a file name
     * @param algorithm Key algorithm
     * @return Private key
     * @throws Exception
     */
    private PrivateKey getPemPrivateKey(String filename, String algorithm) throws Exception {
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();

        String temp = new String(keyBytes);
        String privKeyPEM = temp.replace("-----BEGIN PRIVATE KEY-----\n", "");
        privKeyPEM = privKeyPEM.replace("-----END PRIVATE KEY-----", "");

        byte[] decoded = Base64.decode(privKeyPEM);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance(algorithm);
        return kf.generatePrivate(spec);
    }

    private boolean saveXML(String strJobType, int iId, String strContent) {
        logger.entering(TAG, "saveXML", new StringBuilder(strJobType).append(":").append(iId)
                .append(":").append(strContent));

        boolean bRet = false;

        // path for output
        String strPath = Preferences.userRoot().node("com.grigorio.rzd").get(Main.Preferences.stridOutDir, "");

        String strFileName = strJobType + iId;

        try {
            File file = new File(strPath + "/" + strFileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(strContent.getBytes());
            fos.close();

            bRet = true;
        } catch (Exception e) {
            System.err.println("Can't create output stream for a file");
            e.printStackTrace();
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

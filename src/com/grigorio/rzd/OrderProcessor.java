package com.grigorio.rzd;

import com.grigorio.rzd.TicketServiceWSProxy.GetInfo;
import com.grigorio.rzd.TicketServiceWSProxy.TicketService;
import com.grigorio.rzd.TicketServiceWSProxy.TransInfoRequest;
import com.grigorio.rzd.TicketServiceWSProxy.TransInfoXMLResponse;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.BlockingQueue;
import java.util.prefs.Preferences;

/** Class which processes orders from a queue,
 * i.e. take an order out, make a request to WS and delivers a result to a client
 * Created by philipp on 10/10/14.
 */

public class OrderProcessor implements Runnable {
    private final BlockingQueue<Order> queue;
    private final String PRIVATE_KEY_FILE_NAME = "priv.pk8";
    private byte[] pemKey;

    OrderProcessor(BlockingQueue q) {
        queue = q;
    }

    @Override
    public void run() {
        Preferences prefs = Preferences.userRoot().node("com.grigorio.rzd");

        System.out.println("Processor thread started");
        while (true) {
            try {
                Order ord = queue.take();

                if (ord.getId() == 0) {
                    System.out.println("Signal to exit!");
                    break;
                } else {
                    System.out.println("New order to process, id=" + ord.getId());

                    TicketService ticketServiceService = new TicketService();
                    GetInfo getInfo = ticketServiceService.getPortTicketService();

                    TransInfoRequest req = new TransInfoRequest();
                    req.setOrderId(ord.getId());

                    // set user from preferences and token from order in a queue
                    req.setUser(prefs.get(Main.Preferences.stridUsername, ""));
                    req.setLtpaToken2(ord.getStrToken());

                    // get time string in GMT
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                    String strTime = dateFormat.format(new Date());

                    // set create time
                    req.setCreateTime(strTime);

                    // set nonce
                    int iNonce = new Random().nextInt();
                    req.setNonce(iNonce);

                    String strSignature = null;
                    try {
                        String strPrivKeyLoc = prefs.get(Main.Preferences.stridPrivKeyLoc, "");
                        PrivateKey privKey = getPemPrivateKey(strPrivKeyLoc, "RSA");

                        Signature sigInst = Signature.getInstance("SHA256withRSA");
                        sigInst.initSign(privKey);

                        sigInst.update(String.format("%d%d%s", iNonce, ord.getId(), strTime).getBytes());

                        Base64 b64Sig = new Base64();
                        strSignature = b64Sig.encode(sigInst.sign());
                    } catch (Exception e) {
                        System.err.println("Error generating a signature");
                        e.printStackTrace();
                    }

                    req.setSignature(strSignature);

                    try {
                        TransInfoXMLResponse resp = getInfo.getTransInfoXML(req);
                        System.out.println(resp.getResponseXMLData());
                    } catch (Exception e) {
                        System.err.println("Error requesting an answer...");
                        e.printStackTrace();
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

        Base64 b64 = new Base64();
        byte[] decoded = b64.decode(privKeyPEM);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance(algorithm);
        return kf.generatePrivate(spec);
    }
}

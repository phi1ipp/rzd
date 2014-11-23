package com.grigorio.rzd.crypto;

import com.grigorio.rzd.Main;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.prefs.Preferences;

/**
 * Created by philipp on 11/16/14.
 */
public class Signer {
    public static Map<String,Object> sign(Long lOrder) {
        HashMap<String,Object> retMap = new HashMap<String,Object>();

        // get time string in GMT
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String strTime = dateFormat.format(new Date());

        // get nonce
        int iNonce = new Random().nextInt();

        try {
            String strPrivKeyLoc = Preferences.userRoot().node("com.grigorio.rzd").get(Main.Preferences.stridPrivKeyLoc, "");
            PrivateKey privKey = getPemPrivateKey(strPrivKeyLoc, "RSA");

            Signature sigInst = Signature.getInstance("SHA256withRSA");
            sigInst.initSign(privKey);

            if (lOrder != null)
                sigInst.update(String.format("%d%d%s", iNonce, lOrder, strTime).getBytes());
            else
                sigInst.update(String.format("%d%s", iNonce, strTime).getBytes());

            String strSignature = Base64.encode(sigInst.sign());

            retMap.put("nonce", iNonce);
            retMap.put("time", strTime);
            retMap.put("id", lOrder);
            retMap.put("signature", strSignature);
        } catch (Exception e) {
            System.err.println("Error generating a signature.");
            e.printStackTrace();
        }

        return retMap;
    }

    /**
     * Function to load private key from a PEM coded PKCS8 format
     * @param filename String with a file name
     * @param algorithm Key algorithm
     * @return Private key
     * @throws Exception
     */
    private static PrivateKey getPemPrivateKey(String filename, String algorithm) throws Exception {
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();

        String temp = new String(keyBytes).replace("\r","");    // replace all Windows "\r"
        String privKeyPEM = temp.replace("-----BEGIN PRIVATE KEY-----\n", "");
        privKeyPEM = privKeyPEM.replace("-----END PRIVATE KEY-----", "");

        byte[] decoded = Base64.decode(privKeyPEM);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance(algorithm);
        return kf.generatePrivate(spec);
    }
}

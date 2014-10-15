<?php
/**
 * Created by PhpStorm.
 * User: philipp
 * Date: 10/9/14
 * Time: 9:23 PM
 */


function checkSignature($request) {
    global $logger;
    $logger->trace("checkSignature entered...");

    $nonce = $request->nonce;
    $time = $request->createTime;
    $signature = $request->signature;
    $orderId = $request->orderId;

    //TODO add datetime expiration check

    // search for a public key in a DB
    $logger->trace("Getting user's public key from DB");
    if (!($userPubKey = getPublicKeyForUser($request->User))) {
        $logger->warn("Public key was not found");
        return false;
    }

    // load a key from a PEM
    if(!($pubkey = openssl_get_publickey($userPubKey))) {
        $logger->warn("Can't load public key" . openssl_error_string());
        die;
    } else {
        $logger->trace("Public key loaded...");
    }

    $logger->trace("Verifying signature...");
    $signature = base64_decode($signature);
    $ok = openssl_verify($nonce.$orderId.$time, $signature, $pubkey, "sha256WithRSAEncryption");
    if ($ok == 1) {
        $logger->info("Signature is good");
        return true;
    } elseif ($ok == 0) {
        $logger->info("Signature is bad");
        return false;
    } else {
        $logger->error("Error validating a signature " . openssl_error_string());
        return false;
    }
}
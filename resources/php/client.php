<?php # HelloClient.php
# Copyright (c) 2005 by Dr. Herong Yang
#

$client = new SOAPClient("TicketService.wsdl", array('proxy_host'     => "localhost",
                                            'proxy_port'     => 8080,
                                            'trace' => true));
$nonce = mt_rand();
$token = "tekH2wMNA8BswQrpJPa4JA0keZflc+rdEyQnmhSw/i+O0JAf4R5aSeqlmvj51gGQGKKKg6dfPfmZv7q6yIGuVkNDIfHY1Oy3Mb0oaJvlObvPVNw2Ao4cAhXaonZvlHdMewsKTNMuqvVMLbSM/BSoT78feG146KyC2LDGrHawtkGrXYpH8woA1AbPZztUja1trUtZdgwn7xk2gBX7jyvBiGgf89cAHdkiGDrkRDUOCAQHo5aqS6HiHg+9yusKSiwSceGL66CMyV8JCSK9wA6COGE+F3lX7jm3UK6SP3fEbacXwnF5acRWtatpfM6J4MVqh9cOTT3V/E0HT+9lN4rlrYKbZei6HcOlmbzd6DDisTjXoSlA4hXfCLNbhErg67RByPXUg9TOd/GkllgVEVKSo908xfgSKjNRlCuYiZGVFYXkiv7xuSjuQpT2Ae8ZvAeF/J/IQm8gTvJTANyoKUJwxuU6Zd1CJ5L8oPbotpdwouulRRWs68VgqCnx2IPJpN7tuTkDLETu7qdHG3S+QL7UPWhx87EuT4coRn6KwVCipd0fiqy0P55yFg2Bx/BZ6JX6zzI+fFyLMZ/g0bSrB4H01Wy2zJ+xNnz+qw7twVHjL75G5kgIr9XyzTAaYLHmon6R";
$time = gmdate('Y-m-d\TH:i:s\Z');
$orderId = 104281612;

//TODO add passphrase for a key
if (!($priv_key = openssl_get_privatekey("file://private.pem"))) {
    error_log("Can't load private key");
    die;
}

if (!openssl_sign($nonce.$orderId.$time, $signature, $priv_key, "sha256WithRSAEncryption")) {
    error_log("Can't sign the message" . openssl_error_string());
    die;
} else {
    openssl_free_key($priv_key);
}

echo ("nonce: $nonce time: $time");
$signature = base64_encode($signature);
echo $signature;

error_log("Signature: $signature");

$resp = $client->getTransInfo(array('orderId' => $orderId, "User" => "gabii",
    'LtpaToken2'=>$token,
    'nonce'=>$nonce,
    'createTime'=>$time,
    'signature'=>$signature));

echo var_dump($resp) . "\n\n<br>";
echo "Last response: " . $client->__getLastResponse() . "\n\n<br>";
echo "From: " . $resp->StationFrom . "<br>";
echo "Last response headers: " . $client->__getLastResponseHeaders() . "\n\n<br>";
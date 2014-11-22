<?php # HelloClient.php
# Copyright (c) 2005 by Dr. Herong Yang
#

$client = new SOAPClient("TicketService.wsdl", array('proxy_host'     => "localhost",
                                            'proxy_port'     => 8080,
                                            'trace' => true));
$nonce = mt_rand();
$token = "iUcuX3kJ3Xse1qh8p4cQgPTyIrJNi+pykRYegkSbivEF0M2ayh8RmPxJHhS5XNa0iEVXkgtBlhPAbtsGen9nO8UkzlWPg99/YM7npELdSla2hRJoBXGMEIaDayVpJXvjeAVVLEtpO3v1xnvV4O1tbMHXbK63vTcBhwrWcL0AuIXNclgdPlqa/5t3FGhqNXH8Y+T56YAuksY/Un0JH16QnfkRJucBQE2xHds4IMO5JO9dkINZhtPU3RjNsn2HHf5s7Wo0m96kHF7Dw4h1UAgPuXBPEa1KaiARkSzDG3DLJRvF9eeWKjbODdyxBXmfVyI1NLVvJCzkVSnz29ws2uGXFAXrEibaC+MqOBW3Ul7H3qZABbeZbEc+H4SlmD4KzKn2Pra+jlTa4V+USYz4yQT+IXJJpG97cXUkZCSvOToWuheFXXQ27LwHPYAdlRThV/46nrX311TCQ0IWwoPGArhhtX1SZ2aPLVXqG3DTjETwdbyZMEfB7AbIGBH3DKrWyXFhS5BpAD9Ews0Msz0udQczaVp2Nkd4MG8Sbjr+FhjByz8cKNUOLZ60YOlHlecK7Rf58l2cnduWYPMbnai8Y36kx4/0NGllocSOWs8GFw12DcMAd6aCYsxWP9h/WeHb6dIl";
$time = gmdate('Y-m-d\TH:i:s\Z');

    $user = "gabii";    
    $orderId = 107011422;
    $ticketMappings = array();

    $mapping = new stdClass;
    $mapping->ticketNum = 70050481514552;
    $mapping->ticketId = 115981079;
    $ticketMappings[] = new SoapVar($mapping, SOAP_ENC_OBJECT, null, null, "Mapping");

    $mapping = new stdClass;
    $mapping->ticketNum = 70050481514563;
    $mapping->ticketId = 115981080;
    $ticketMappings[] = new SoapVar($mapping, SOAP_ENC_OBJECT, null, null, "Mapping");
    
    $mapping = new stdClass;
    $mapping->ticketNum = 70050481514574;
    $mapping->ticketId = 115981081;
    $ticketMappings[] = new SoapVar($mapping, SOAP_ENC_OBJECT, null, null, "Mapping");
    
    $mapping = new stdClass;
    $mapping->ticketNum = 70050481514585;
    $mapping->ticketId = 115981082;
    $ticketMappings[] = new SoapVar($mapping, SOAP_ENC_OBJECT, null, null, "Mapping");
    
    //$ticketMappingEl = new SoapVar($ticketMappings, SOAP_ENC_ARRAY, "TicketMappingList", "urn:TicketServiceSchema", "ticketMappings");
    $ticketMappingEl = new SoapVar($ticketMappings, SOAP_ENC_OBJECT, null, null, "ticketMappings");
    var_dump($ticketMappingEl);
    
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

$signature = base64_encode($signature);

$resp = $client->saleRequest(
        array(
            "User" => $user,
            'LtpaToken2'=>$token,
            'nonce'=>$nonce,
            'createTime'=>$time,
            'signature'=>$signature,
            'orderId'=>$orderId,
            'ticketMappings'=>$ticketMappingEl
            ));

echo var_dump($resp) . "\n\n<br>";
echo "Last response: " . $client->__getLastResponse() . "\n\n<br>";
echo "Last response headers: " . $client->__getLastResponseHeaders() . "\n\n<br>";
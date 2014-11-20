<?php # HelloClient.php
# Copyright (c) 2005 by Dr. Herong Yang
#

$client = new SOAPClient("TicketService.wsdl", array('proxy_host'     => "localhost",
                                            'proxy_port'     => 8080,
                                            'trace' => true));
$nonce = mt_rand();
$token = "Ot1oZbE8itgFprRfN1VioBoiOwEfh/r1BfnLr6VEAAFHaGWGVrWrI3PvSrWG3wI1Mew/t5+Yy678mQDzj9yH+rQtrHb76koO70bmfd8j6oEF/H9QEi0GdKl9xY7FyxVQO7l5KNC1IvA0sdx9dTlcOUKo4FXHxAu7J1sAwyH1RuYynMYh4dWCJ+59sscM/ccD7D684lPaZ7wx+UjFPxCS2M2NGWRxRIaogLgbeGSDJLYLUgl03F+K8jyQzJG3t4kM/7S/z3AqXCiJVwcocLyiu36SmQmiNEHfQZndZn0w7JvH9AATeu+B2dLm8hBheIbeCJw8ml85nypihRVIWp7jBV1BSsTxFmqH8uB+tiE4jyT1fpftRI2/VneVDddXueSmrjBpuQPZ6/4KynyQHNJT/oKFUn0M4xGCn6VyJ8LFamQQZ6NDQO6qeS7uKv9n+KLSw7HIMmsttruFPf5rTyAPoBjw6a9eZGGK0Mrljmo86ly2DzdabshCk44qB9ObvySXQORkTzzKb46IZuY+PsdYFNaPBlAg3wr3ivhbLOmEXRQ60MQO8mqZ6i7K5uEVeE+GuBT7fhEgJoldCYZuXmpQZuJGA/xnZ8J91RYpLrG3vlRrGuvOcq8x4dwuSpUWSQcI";
$time = gmdate('Y-m-d\TH:i:s\Z');

    $user = "gabii";
    
    $ticketNum = null;
    $lastName = 'полоз';
    $stationFrom = null;
    $stationTo = null;
    $dateFrom = null;
    $dateTo = null;
    $companyName = null;
    $allCriteria = 1;
    
if (!($priv_key = openssl_get_privatekey("file://private.pem"))) {
    error_log("Can't load private key");
    die;
}

if (!openssl_sign($nonce.$ticketNum.$time, $signature, $priv_key, "sha256WithRSAEncryption")) {
    error_log("Can't sign the message" . openssl_error_string());
    die;
} else {
    openssl_free_key($priv_key);
}

$signature = base64_encode($signature);

$resp = $client->searchTicket(
        array(
            "User" => $user,
            'LtpaToken2'=>$token,
            'nonce'=>$nonce,
            'createTime'=>$time,
            'signature'=>$signature,
            'ticketNum'=>$ticketNum,
            'lastName'=>$lastName,
            'stationFrom'=>$stationFrom,
            'stationTo'=>$stationTo,
            'departureFrom'=>$dateFrom,
            'departureTo'=>$dateTo,
            'companyName'=>$companyName,
            'allCriteria'=>false
            ));

echo var_dump($resp) . "\n\n<br>";
echo "Last response: " . $client->__getLastResponse() . "\n\n<br>";
echo "Last response headers: " . $client->__getLastResponseHeaders() . "\n\n<br>";
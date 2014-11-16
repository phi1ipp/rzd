<?php # HelloClient.php
# Copyright (c) 2005 by Dr. Herong Yang
#

$client = new SOAPClient("TicketService.wsdl", array('proxy_host'     => "localhost",
                                            'proxy_port'     => 8080,
                                            'trace' => true));
$nonce = mt_rand();
$token = "KMDVciKbyuVqTW+M0uXQMfQmDahUcExP2NBkWY9pIyeFDOZDIb8qH8JtrHa7mxwfeVQz+sHSb1Xw1HZQawbGCKUz+RnkZVWqK7zW5OygCud2qBlufQXo2JjsrqihRjXE9SitH3oSP+lbxd9Ea2CTaEpkHA2KsTN1yXbw+L54qefYg2wTIPcH9NNCmXanwb86FsWA7o9EYAhu/RUtJ8XNm9awFUEfXnWKf2gpxQF8TeXl/m4+K2To53V5ndOS0WJgr7TIkDSkxl8JscyO52VCENjdpYOSXxMbP8ahpa9eIUQCsr4sRGU2C0MEbIRdJsZ0WZzs279lQgE3c5zztr18lGr3n1KL/tW3oHab3Ta/qKx8SGRvc4Z5bwSfKjJlPvdSmGqiZP4wCtvpV2w13wG5o4zF698BbrOcmDIgykugcKlUg7q/LkDLCFeXW8CUw0gUFYiOOWrfOp/4ulFOGjJ7L1SU5TvGUew1SR+Tjl7oRdx60pF+oOFo4/kZxJI0VzsEIBUxFyAjfebY/+6l3j9hHzY5IEOoYrSQLu+mWEQ8NgeAxSJHhsdQf08B1tgwq5ocK2uokHZxGDtr5hESZtEblqRB/mo1XzZ4PUnfV25Beg6bXGtvmILZ/PlGEx1mBFka";
$time = gmdate('Y-m-d\TH:i:s\Z');

    $user = "gabii";
    
    $ticketNum = null;
    $lastName = 'полух';
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
<?php
require_once "WsdlToPhp/samples/TicketService/TicketServiceAutoload.php";

define('rzdURL', "https://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=5235&layer_id=5422&ORDER_ID=");

function getTransInfo($request) {
    error_log("Request: " . $request);
    $url_to_request = rzdURL . $request->orderId;
    $rzd = curl_init($url_to_request);
    
    curl_setopt($rzd, CURLOPT_PROXY, "localhost");
    curl_setopt($rzd, CURLOPT_PROXYPORT, 8080);
    curl_setopt($rzd, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($rzd, CURLOPT_FOLLOWLOCATION, true);
    curl_setopt($rzd, CURLOPT_RETURNTRANSFER, true);

    curl_setopt($rzd, CURLOPT_COOKIESESSION, true);
    curl_setopt($rzd, CURLOPT_COOKIE, "LtpaToken2=" . $request->LtpaToken2);

    error_log("token: " . $request->LtpaToken2);
    error_log("order_id: " . $request->orderId);


    // get response and close connection
    $resp = curl_exec($rzd);
    curl_close($rzd);

    // load xsl template
    $xsldoc = new DOMDocument();
    $xsldoc->load("./ticket.xsl");

    $xsl = new XSLTProcessor();
    $xsl->importStyleSheet($xsldoc);

    $xmldoc = new DOMDocument();
    $xmldoc->loadHTML($resp);
    $xmldoc->saveHTMLFile("response.html");

    $xsl->setParameter('','timestamp',(string) time());
    $xsl->setParameter('','terminal',"maxavia_01");
    $xsl->setParameter('','orderNum',(string)$request->orderId);

    $xmldoc->loadXML($xsl->transformToXML($xmldoc));
    $node = $xmldoc->getElementsByTagName('UFS_RZhD_Gate')->item(0);

    $ret = new SoapVar( $xmldoc->saveXML($node), XSD_ANYXML, null, "http://www.w3.org/2001/XMLSchema", "payload", "urn:TicketServiceSchema");
    return $ret;
//    return new TicketServiceStructTransInfoResponse($request->orderId);
    
}
// server
$ss = new SOAPServer("TicketService.wsdl",
    array('soap_version' => SOAP_1_2));

$ss->addFunction("getTransInfo");
$ss->handle();

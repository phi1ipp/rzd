<?php
require_once "WsdlToPhp/samples/TicketService/TicketServiceAutoload.php";

define(rzdURL, "https://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=5235&layer_id=5422&ORDER_ID=23108471");

function getTransInfo($request) {
    error_log("orderId:" . $request->orderId . "\n");
    error_log("user_cn:" . $request->Usercn . "\n");
    error_log("ltpa:" . $request->LtpaToken2 . "\n");

    $rzd = curl_init(rzdURL);
    error_log(curl_setopt($rzd, CURLOPT_FOLLOWLOCATION, true));
    error_log(curl_setopt($rzd, CURLOPT_RETURNTRANSFER, true));
    error_log(curl_setopt($rzd, CURLOPT_CONNECTTIMEOUT, 0));
    curl_setopt($rzd, CURLOPT_PROXY, "localhost");
    curl_setopt($rzd, CURLOPT_PROXYPORT, 8080);
    curl_setopt($rzd, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($rzd, CURLOPT_COOKIESESSION, true);
    curl_setopt($rzd, CURLOPT_COOKIE, "LtpaToken2=" . $request->LtpaToken2
//                . ";_ga=" . $request->ga);
//                ";JSESSIONID=" . $request->JSESSIONID .
//                ";lang=" . $request->lang .
//                ";AuthFlag"
    );

    // get response and close connection
    $resp = curl_exec($rzd);
    curl_close($rzd);

    // load xsl template
    $xsldoc = new DOMDocument();
    error_log("Result of xslt file load: ". $xsldoc->load("./ticket.xsl"));

    $xsl = new XSLTProcessor();
    error_log("Resulte of xsl parsing:" . $xsl->importStyleSheet($xsldoc));

    $xmldoc = new DOMDocument();
    error_log("Result of a response load into DOM: " . $xmldoc->loadHTML($resp));
    $xmldoc->saveHTMLFile("response.html");

    error_log($xsl->transformToXML($xmldoc));
    return new TicketServiceStructTransInfoResponse($request->orderId);
}
// server
$ss = new SOAPServer("TicketService.wsdl",
    array('soap_version' => SOAP_1_2));

$ss->addFunction("getTransInfo");
$ss->handle();
?>
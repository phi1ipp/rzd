<?php
require_once "dbhelper.php";
require_once "crypto.php";
require_once "searchTicket.php";
require_once "saleRequest.php";

include ("Logger/src/main/php/Logger.php");

define('rzdURL', "https://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=5235&layer_id=5422&ORDER_ID=");
define('refundURL', "https://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=5235&layer_id=5422&ORDER_ID=%d&ticket_id=%d");

/** Function to connect to RZD site with a given URL and token
 * @param $url_to_request URL to get
 * @param $token token
 * Returns a response string
 */
function get_response($url_to_request, $token) {
    global $logger;
    $logger->trace("get_response entered");

    $rzd = curl_init($url_to_request);

    curl_setopt($rzd, CURLOPT_FOLLOWLOCATION, true);
    curl_setopt($rzd, CURLOPT_RETURNTRANSFER, true);

    curl_setopt($rzd, CURLOPT_COOKIESESSION, true);
    curl_setopt($rzd, CURLOPT_COOKIE, "LtpaToken2=$token");

    $logger->trace("Requesting rzd");
    // get response and close connection
    $ret = curl_exec($rzd);
    curl_close($rzd);

    $logger->trace("exiting get_response");
    return $ret;
}

function check_response(DOMDocument $xmlDoc) {
    global $logger;
    $logger->trace("check_response entered...");
    
    $xpath = new DOMXPath($xmlDoc);
    if ($xpath->query("//form[@id='selfcare_logon_form']")->length > 0) {
        $logger->info("login form detected in response, returning false");
        return false;
    }
    
    $logger->trace("exiting check_response");
    return true;
}

function getTransInfo($request) {
    global $logger;

    $logger->trace("getTransInfo entered");

//TODO sanitize values
    $nonce = $request->nonce;
    $time = $request->createTime;
    $token = $request->LtpaToken2;
    $signature = $request->signature;
    $orderId = $request->orderId;
    $user = $request->User;

    $logger->debug("token: $token");
    $logger->debug("order_id: $orderId");
    $logger->debug("nonce: $nonce");
    $logger->debug("createTime: $time");
    $logger->debug("signature: $signature");

    $logger->info("Incoming request for decoding: [user=>$user, order=$orderId]");

    if (!checkSignature($request)) {
        $logger->error("Signature check failed");
        return new SoapFault("SOAP-ENV:Client", "Bad signature");
    } else {
        $logger->trace("Signature check successful");
    }

    $url_to_request = rzdURL . $orderId;
    $resp = get_response($url_to_request, $token);

    // save response into DOM
    $xmldoc = new DOMDocument();
    $xmldoc->loadHTML($resp);

    // load xsl template
    $xsldoc = new DOMDocument();
    $xsldoc->load("./ticket.xsl");

    // create processor and set it up
    $xsl = new XSLTProcessor();
    $xsl->importStyleSheet($xsldoc);

    // parameters of transformation (global vars for xsl)
    $xsl->setParameter('','timestamp',(string) time());
    $xsl->setParameter('','terminal',(string) $request->User);

    $logger->trace("Performing transformation");
    //perform transformation, load a result into DOM and select root
    $xmldoc->loadXML($xsl->transformToXML($xmldoc));

    //TODO remove in prd
    $xmldoc->save("/tmp/rezult.xml");

    $node = $xmldoc->getElementsByTagName('UFS_RZhD_Gate')->item(0);

    $ret = null;

    //log into db
    $logger->trace("Saving data into DB");
    if(save_request($request->orderId, $xmldoc)) {
        $logger->info("Data saved into DB, returning response to a client");
    } else {
        $logger->warn("Data wasn't save into DB");
    }

    $ret = new SoapVar($xmldoc->saveXML($node), XSD_ANYXML, null, "http://www.w3.org/2001/XMLSchema",
        "payload", "urn:TicketServiceSchema");

    $logger->trace("Exiting getTransInfo");
    return $ret;
}

function getTransInfoXML($request) {
    global $logger;

    $logger->trace("getTransInfo entered");

//TODO sanitize values
    $nonce = $request->nonce;
    $time = $request->createTime;
    $token = $request->LtpaToken2;
    $signature = $request->signature;
    $orderId = $request->orderId;
    $user = $request->User;

    $logger->debug("token: $token");
    $logger->debug("order_id: $orderId");
    $logger->debug("nonce: $nonce");
    $logger->debug("createTime: $time");
    $logger->debug("signature: $signature");

    $logger->info("Incoming request for decoding: [user=>$user, order=$orderId]");

    if (!checkSignature($request)) {
        $logger->error("Signature check failed");
        return new SoapFault("SOAP-ENV:Client", "Bad signature");
    } else {
        $logger->trace("Signature check successful");
    }

    $url_to_request = rzdURL . $orderId;
    $rzd = curl_init($url_to_request);

    //TODO remove proxy settings in prd
//    curl_setopt($rzd, CURLOPT_PROXY, "localhost");
//    curl_setopt($rzd, CURLOPT_PROXYPORT, 8080);
//    curl_setopt($rzd, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($rzd, CURLOPT_FOLLOWLOCATION, true);
    curl_setopt($rzd, CURLOPT_RETURNTRANSFER, true);

    curl_setopt($rzd, CURLOPT_COOKIESESSION, true);
    curl_setopt($rzd, CURLOPT_COOKIE, "LtpaToken2=$token");

    $logger->trace("Requesting rzd");
    // get response and close connection
    $resp = curl_exec($rzd);
    curl_close($rzd);

    // save response into DOM
    $xmldoc = new DOMDocument();
    $xmldoc->loadHTML($resp);

    // load xsl template
    $xsldoc = new DOMDocument();
    $xsldoc->load("./ticket.xsl");

    // create processor and set it up
    $xsl = new XSLTProcessor();
    $xsl->importStyleSheet($xsldoc);

    // parameters of transformation (global vars for xsl)
    $xsl->setParameter('','timestamp',(string) time());
    $xsl->setParameter('','terminal',(string) $request->User);
    $xsl->setParameter('','orderNum',(string) $request->orderId);

    $logger->trace("Performing transformation");
    //perform transformation, load a result into DOM and select root
    $xmldoc->loadXML($xsl->transformToXML($xmldoc));

    //TODO remove in prd
    $xmldoc->save("/tmp/rezult.xml");

    $node = $xmldoc->getElementsByTagName('UFS_RZhD_Gate')->item(0);

    $ret = null;

    //log into db
    $logger->trace("Saving data into DB");
    if(save_request($request->orderId, $xmldoc)) {
        $logger->info("Data saved into DB, returning response to a client");
    } else {
        $logger->warn("Data wasn't save into DB");
    }

    $transInfoXMLResp = new stdClass();
    $transInfoXMLResp->ResponseXMLData = "<![CDATA[".$xmldoc->saveXML($node)."]]>";
    $transInfoXMLRespType = new SoapVar($transInfoXMLResp, SOAP_ENC_OBJECT, "TransInfoXMLResponse", "urn:TicketServiceSchema");

    $ret = $transInfoXMLRespType;

    $logger->trace("Exiting getTransInfo");
    return $ret;
}

function requestRefund($request) {
    global $logger;

    $logger->trace("requestRefund entered");

//TODO sanitize values
    $nonce = $request->nonce;
    $time = $request->createTime;
    $token = $request->LtpaToken2;
    $signature = $request->signature;
    $orderId = $request->orderId;
    $ticketId = $request->ticketId;
    $user = $request->User;

    $logger->debug("token: $token");
    $logger->debug("ticketNum: $ticketId");
    $logger->debug("nonce: $nonce");
    $logger->debug("createTime: $time");
    $logger->debug("signature: $signature");

    $logger->info("Incoming request to get refund receipt: [user=>$user, order=$orderId, ticket=$ticketId]");

    if (!checkSignature($request)) {
        $logger->error("Signature check failed");
        //todo add standard soap fault
        return new SoapFault("SOAP-ENV:Client", "Bad signature");
    } else {
        $logger->trace("Signature check successful");
    }

    // check for order in DB
    $orderTime = getOrderTime($orderId);
    if ($orderTime == "") {
        $logger->warn("Order $orderId hasn't been found in DB");
        //todo add some logic to pull info and save?
    }
    // get the page from RZD
    $url_to_request = sprintf(refundURL, $orderId, $ticketId);
    $resp = get_response($url_to_request, $token);

    // save response into DOM
    $xmldoc = new DOMDocument();
    $xmldoc->loadHTML($resp);
    
    if (!check_response($xmldoc)) {
        $logger->error("Bad response from RZD, exiting...");
        return new SoapFault("SOAP-ENV:Server", "Bad response from RZD");
    }

    // need to save refund time from the response
    $xpath = new DOMXPath($xmldoc);
    $refundTime = $xpath->query("//div[@class='servdata']/div[position()=1]//tr[position()=3]/td[last()]/text()")
        ->item(0)->textContent;
    $logger->debug("Refund time: $refundTime");

    // load xsl template
    $xsldoc = new DOMDocument();
    $xsldoc->load("./refund.xsl");

    // create processor and set it up
    $xsl = new XSLTProcessor();
    $xsl->importStyleSheet($xsldoc);

    // parameters of transformation (global vars for xsl)
    $xsl->setParameter('','timestamp',(string) time());
    $xsl->setParameter('','terminal',(string) $request->User);
    $xsl->setParameter('','ordertime',(string) $orderTime);

    $logger->trace("Performing transformation");
    //perform transformation, load a result into DOM and select root
    $xmldoc->loadXML($xsl->transformToXML($xmldoc));

    //TODO remove in prd
    $xmldoc->save("/tmp/rezult.xml");

    $node = $xmldoc->getElementsByTagName('UFS_RZhD_Gate')->item(0);

    $ticketNum = $xmldoc->getElementsByTagName('TicketNum')->item(0)->textContent;
    $logger->debug("Processing ticket #$ticketNum");

    //log into db
    $logger->trace("Saving data into DB");
    if (saveRefund($ticketNum, $user, $refundTime)) {
        $logger->info("Data saved into DB, returning response to a client");
    } else {
        $logger->warn("Data wasn't save into DB");
    }

    $refundXMLResp = new stdClass();
    $refundXMLResp->ResponseXMLData = "<![CDATA[".$xmldoc->saveXML($node)."]]>";
    $refundXMLRespType = new SoapVar($refundXMLResp, SOAP_ENC_OBJECT, "RefundXMLResponse", "urn:TicketServiceSchema");

    $ret = $refundXMLRespType;

    $logger->trace("Exiting requestRefund");
    return $ret;
}

//TODO create user for mysql connections

/**
 * Main section
 */

// read config params
$config = parse_ini_file('../connection.cfg');
define("USER", $config["user"]);
define("PWD", $config["pwd"]);
define("HOST", $config["host"]);
define("DB", $config["db"]);

date_default_timezone_set('Europe/Moscow');
// logging params
Logger::configure("logger.xml");
$logger = Logger::getLogger("default");

// server
$logger->trace('Creating SOAP server');
$ss = new SOAPServer("TicketService.wsdl",
    array('soap_version' => SOAP_1_2));

$ss->addFunction("getTransInfo");
$ss->addFunction("getTransInfoXML");
$ss->addFunction("requestRefund");
$ss->addFunction("searchTicket");
$ss->addFunction("saleRequest");
$ss->handle();
<?php
require_once "dbhelper.php";
require_once "crypto.php";
require_once "Logger/src/main/php/Logger.php";

define('rzdURL', "https://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=5235&layer_id=5422&ORDER_ID=");

Class SimpleXMLElementExtended extends SimpleXMLElement {

    /**
     * Adds a child with $value inside CDATA
     * @param $name
     * @param $value
     */
    public function addChildWithCDATA($name, $value = NULL) {
        $new_child = $this->addChild($name);

        if ($new_child !== NULL) {
            $node = dom_import_simplexml($new_child);
            $no   = $node->ownerDocument;
            $node->appendChild($no->createCDATASection($value));
        }

        return $new_child;
    }
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
        return new SoapFault("BADSIG", "Bad signature");
    } else {
        $logger->trace("Signature check successful");
    }

    $url_to_request = rzdURL . $orderId;
    $rzd = curl_init($url_to_request);

    //TODO remove proxy settings in prd
    curl_setopt($rzd, CURLOPT_PROXY, "localhost");
    curl_setopt($rzd, CURLOPT_PROXYPORT, 8080);
    curl_setopt($rzd, CURLOPT_SSL_VERIFYPEER, false);
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
    $xmldoc->save("rezult.xml");

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
        return new SoapFault("BADSIG", "Bad signature");
    } else {
        $logger->trace("Signature check successful");
    }

    $url_to_request = rzdURL . $orderId;
    $rzd = curl_init($url_to_request);

    //TODO remove proxy settings in prd
    curl_setopt($rzd, CURLOPT_PROXY, "localhost");
    curl_setopt($rzd, CURLOPT_PROXYPORT, 8080);
    curl_setopt($rzd, CURLOPT_SSL_VERIFYPEER, false);
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
    $xmldoc->save("rezult.xml");

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
//TODO create user for mysql connections

// read config params
$config = parse_ini_file('../../connection.cfg');
define("USER", $config["user"]);
define("PWD", $config["pwd"]);
define("HOST", $config["host"]);
define("DB", $config["db"]);

// logging params
Logger::configure("logger.xml");
$logger = Logger::getLogger("default");

// server
$ss = new SOAPServer("TicketService.wsdl",
    array('soap_version' => SOAP_1_2));

$ss->addFunction("getTransInfo");
$ss->addFunction("getTransInfoXML");
$ss->handle();
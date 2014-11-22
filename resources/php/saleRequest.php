<?php
require_once "dbhelper.php";
require_once "crypto.php";

function saleRequest($request) {
    global $logger;

    $logger->trace("saleRequest entered");

//TODO sanitize values
    $nonce = $request->nonce;
    $time = $request->createTime;
    $token = $request->LtpaToken2;
    $signature = $request->signature;
    $orderId = $request->orderId;
    $user = $request->User;
    $ticketMappings = $request->ticketMappings;

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
    if(save_ticket_sale($request->orderId, $xmldoc, $ticketMappings)) {
        $logger->info("Data saved into DB, returning response to a client");
    } else {
        $logger->warn("Data wasn't save into DB");
    }

    $transInfoXMLResp = new stdClass();
    $transInfoXMLResp->ResponseXMLData = "<![CDATA[".$xmldoc->saveXML($node)."]]>";
    $transInfoXMLRespType = new SoapVar($transInfoXMLResp, SOAP_ENC_OBJECT, "TransInfoXMLResponse", "urn:TicketServiceSchema");

    $ret = $transInfoXMLRespType;

    $logger->trace("Exiting saleRequest...");
    return $ret;
}

/**
 * 
 * @param type $ticketMappings
 */
function findTicketIdByNum($ticket_num, $ticketMappings) {
    global $logger;
    $logger->trace("findTicketIdByNum entered");
    
    if ($ticketMappings == null) {
        $logger->warning("ticketMapping is empty, exiting...");
        return null;
    }
    
    $mappings = $ticketMappings->Mapping;
    foreach($mappings as $mapping) {
        $logger->debug("num:$mapping->ticketNum id:$mapping->ticketId");
        if ($mapping->ticketNum == $ticket_num) {
            $logger->debug("ticketId=$mapping->ticketId "
                    . "for ticketNum=$ticket_num");
            return $mapping->ticketId;
        }
    }
    
    $logger->debug("ticketNum=$ticket_num was not found");
    return null;
    $logger->trace("exiting findTicketIdByNum...");
}

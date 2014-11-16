<?php

/**
 * Searches for tickets based on criterias given
 */
function searchTicket($request) {
    global $logger;
    $logger->trace("searchTicket entered");
    
    //TODO sanitize values
    $nonce = $request->nonce;
    $time = $request->createTime;
    $token = $request->LtpaToken2;
    $signature = $request->signature;
    $user = $request->User;
    
    if ($request->ticketNum == 0) {
        $request->ticketNum = null;
    }
        
    $lastName = $request->lastName;
    $stationFrom = $request->stationFrom;
    $stationTo = $request->stationTo;
    $dateFrom = $request->departureFrom;
    $dateTo = $request->departureTo;
    $companyName = $request->companyName;
    $allCriteria = $request->allCriteria;

    $logger->debug("token: $token");
    $logger->debug("ticketNum: $ticketNum");
    $logger->debug("nonce: $nonce");
    $logger->debug("createTime: $time");
    $logger->debug("signature: $signature");
    $logger->debug("user: $user");
    
    /* as we don't have orderId in this request
     * we substitute it with ticketNum to use checkSignature func
     */
    $request->orderId = $ticketNum;
    
    if (!checkSignature($request)) {
        $logger->error("Signature check failed");
        return new SoapFault("SOAP-ENV:Client", "Bad signature");
    } else {
        $logger->trace("Signature check successful");
    }
    
    $tickets = searchForTickets($ticketNum, $lastName, $stationFrom, $stationTo, 
            $dateFrom, $dateTo, $companyName, $allCriteria, $user);
    
    // prepare response
    $res = array();
    foreach ($tickets as $ticket) {
        $a_ticket = new stdClass();
        $a_ticket->ticketNum = $ticket["ticketnum"];
        $a_ticket->lastName = $ticket["lastname"];
        $a_ticket->stationFrom = $ticket["from"];
        $a_ticket->stationTo = $ticket["to"];
        
        //convert to datetime format
        $a_ticket->departureDate = 
                date("Y-m-d",strtotime($ticket["tripon"]))."T"
                        .date("H:i:s",strtotime($ticket["tripon"]));
          
        $logger->trace("encoding Ticket...");
        $Ticket = new SoapVar($a_ticket, SOAP_ENC_OBJECT, "SearchTicketRecord", "urn:TicketServiceSchema", "Ticket");
        $res[] = $Ticket;
    }
    
    $resp = new SoapVar($res, SOAP_ENC_OBJECT);
    
    $logger->trace("exiting...");
    return $resp;
}



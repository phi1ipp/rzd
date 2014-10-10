<?php
/**
 * Created by PhpStorm.
 * User: philipp
 * Date: 10/5/14
 * Time: 9:55 AM
 */
//TODO make secure storage for credentials
define("USER", "philipp");
define("PWD", "@Or3oshk");
define("HOST", "localhost");
define("DB", "ticket_service");


function hex2str($func_string) {
    return hex2bin(str_replace('\x','',substr($func_string, 0, strpos($func_string, ' '))));
}


/** Saves information about user order in the DB
 * @param $order_id OrderId in rzd domain
 * @param DOMDocument $xml XMLDocument in UFS format
 * @return bool true on success
 */
function save_request($order_id, DOMDocument $xml ) {
    global $logger;

    $logger->trace("save_request entered");
    $ret = true;

    $conn = mysqli_connect(HOST, USER, PWD, DB) or die("Can't connect to DB..." . mysqli_connect_error());
    $logger->trace("connected...");

    $conn->set_charset('utf8');

    // get Terminal(username) from DOM
    $user = $xml->getElementsByTagName("Terminal")->item(0)->textContent;
    $logger->debug("User: " . $user);

    // get all ticket numbers from XML
    $tickets = $xml->getElementsByTagName("TicketNum");

    // get time of creation
    $time = $xml->getElementsByTagName("CreateTime")->item(0)->textContent;
    $logger->debug("CreateTime: " . $time);

    $xpath = new DOMXPath($xml);

    // sql to save data with two params: ticket number and last name
    // "save_ticket_ordered(order_id, ticket_num, 'last_name', 'time', 'user', @retcode)";
    $sql = "call save_ticket_ordered($order_id, ?, ?, '$time', '$user', @retcode)";

    // for each ticket
    foreach ($tickets as $tnum) {
        $ticket_num = $tnum->textContent;
        $logger->trace("Proccessing ticket #" . $ticket_num);

        $logger->trace("Preparing statement");
        $stmt = $conn->prepare($sql);
        if (!$stmt) {
            $logger->error("Can't prepare statement" . $conn->error);
            $ret = false;
            continue;
        }

        // get BlankID for ticket
        $blank_id = $xpath->query(sprintf("//Blank[./TicketNum=%d]/@ID", $ticket_num))->item(0)->value;
        $logger->debug("BlankID: " . $blank_id);

        // get Name
        $lastname =
            $xpath->query(sprintf("//Passenger[@BlankID='%d']/Name/text()", $blank_id))->item(0)->textContent;

        $logger->trace("Binding params...");
        $stmt->bind_param("ds", $ticket_num, $lastname);

        $logger->trace("Running query...");
        $stmt->execute();
        $stmt->close();

        $logger->trace("Fetching result...");
        if (!($res = $conn->query("select @retcode as rc"))) {
            $logger->error("Can't fetch a result of save: " . $conn->error);
            $ret = false;
            continue;
        }

        $row = $res->fetch_assoc();
        $logger->debug("RetCode: " . $row['rc']);

        if ($row['rc'] != 0) {
            $logger->info("Save unsuccessful");
            $ret = false;
        } else {
            $logger->info("Data has been successfully saved");
        }

        $res->free_result();
    }

    $logger->trace("Releasing resources...");
    $stmt->close();
    $conn->close();

    $logger->trace("Exiting save_request with ret: $ret");
    return $ret;
}

/** Function to get user's public key in PEM format from a DB
 * @param $userid
 * @return string
 */
function getPublicKeyForUser($userid) {
    global $logger;
    $logger->trace("getPublicKeyForUser entered");

    $logger->trace("connecting to DB...");
    if (!($conn = mysqli_connect(HOST, USER, PWD, DB))) {
        $logger->error("Can't connect to DB..." . mysqli_connect_error());
        return "";
    }

    $conn->set_charset('utf8');

    $logger->trace("Querying DB...");
    if (!($res = $conn->query("select users.key as pubkey from users where name='$userid'"))) {
        $logger->error("Error fetching user's ($userid) key: $conn->error");
        return "";
    }

    if (!($row = $res->fetch_assoc())) {
        $logger->error("Key is not set or empty: $conn->error");
        return "";
    }

    $logger->trace("Fetching data...");
    $key = $row["pubkey"];
    $logger->debug("Key: $key");

    $res->free_result();
    $conn->close();

    $logger->trace("exiting getPublicKeyForUser");
    return $key;
}
<?php
/**
 * Created by PhpStorm.
 * User: philipp
 * Date: 10/5/14
 * Time: 9:55 AM
 */

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
    $logger->debug("User: $user");

    // get all ticket numbers from XML
    $tickets = $xml->getElementsByTagName("TicketNum");

    // get time of creation
    $time = $xml->getElementsByTagName("CreateTime")->item(0)->textContent;
    $logger->debug("CreateTime: $time");

    $trip_time = $xml->getElementsByTagName("DepartTime")->item(0)->textContent;
    $logger->debug("Trip time: $trip_time");

    $xpath = new DOMXPath($xml);

    // sql to save data with two params: ticket number and last name
    // "save_ticket_ordered(order_id, ticket_num, 'last_name', ticket_price, 'trip_date', 'time', 'user', @retcode)";
    $sql = "call save_ticket_ordered($order_id, ?, ?, ?, '$trip_time', '$time', '$user', @retcode)";

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

        // get ticket price
        $price =
            floatval(
                $xpath->query(sprintf("//Blank[./TicketNum=%d]/Amount/text()", $ticket_num))->item(0)->textContent);

        $logger->trace("Binding params...");
        $logger->debug("Ticket #$ticket_num, Last name: $lastname, Price: $price");
        $stmt->bind_param("dsd", $ticket_num, $lastname, $price);

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

/** Retrieves formatted datetime from DB for a given order_id
 * @param $order_id Order ID from RZD domain
 * @return string Formatted datetime or null if not found
 */
function getOrderTime($order_id) {
    global $logger;

    $logger->trace("getOrderTime entered, order_id=$order_id");
    if (!($conn = mysqli_connect(HOST, USER, PWD, DB))) {
        $logger->error("Can't connect to DB: " . mysqli_connect_error());
        return "";
    }

    if (!($res = $conn->query("select date_format(createdon, '%d.%m.%Y %H:%i:%s') as createdon from orders where num=$order_id"))) {
        $logger->error("Can't query DB: " . $conn->error);
        return "";
    }

    $row = $res->fetch_assoc();
    $res = $row['createdon'];
    $logger->debug("Datetime for order: $res");

    $logger->trace("exiting getOrderTime");
    return $res;
}

function saveRefund($ticketNum, $user, $refundTime) {
//    PROCEDURE `save_refund`(ticketNum bigint, userLogin varchar(32), refundedon datetime, out retCode int)
    global $logger;

    $logger->trace("getOrderTime entered with $ticketNum, $user, $refundTime");
    $sql = "call save_refund($ticketNum, '$user', '$refundTime', @ret)";

    if (!($conn = mysqli_connect(HOST, USER, PWD, DB))) {
        $logger->error("Can't connect to DB: " . mysqli_connect_error());
        return false;
    }

    $conn->set_charset('utf8');

    if (!($conn->query("set @ret = 0"))) {
        $logger->error("Can't execute query: [$sql]. Error message: " . $conn->error);
        return false;
    }

    if (!($conn->query($sql))) {
        $logger->error("Can't execute query: [$sql]. Error message: " . $conn->error);
        return false;
    }

    if (!($res = $conn->query("select @ret as rc"))) {
        $logger->error("Can't get a return code: " . $conn->error);
        return false;
    }

    $row = $res->fetch_assoc();
    $ret = ($row["rc"] == 0 ? true : false);

    if ($ret) {
        $logger->info("Save successful");
    } else {
        $logger->info("Save unsuccessful");
    }

    $logger->trace("exiting from saveRefund with $ret");
    return $ret;
}
<?php
/**
 * Created by PhpStorm.
 * User: philipp
 * Date: 11/4/14
 * Time: 9:13 PM
 */
require_once "Logger/src/main/php/Logger.php";

function print_history_page($arr_from_json, $page) {
	echo "Total count: " . $arr_from_json["totalCount"] . "<br></br>";
	$slots = $arr_from_json["slots"];
	foreach ($slots as $key => $value) {
	 	echo "<h1>Page: #$page Record: #$key</h1>";
	 	foreach ($value["lst"] as $key => $val) {
	 		echo "<h2>Element #$key</h2>";
	 		echo "N: " . $val["N"] . "<br>";
	 		echo "id: " . $val["id"]  . "<br>";
	 		echo "From: " . $val["station0"]  . "<br>";
	 		echo "To: " . $val["station1"]  . "<br>";
	 		echo "Date: " . $val["date0"]  . "<br>";
	 		foreach ($val["lst"] as $key => $person) {
	 			echo "<h3>Person #$key</h3>";
	 			echo "n: " . $person["n"] . "<br>";
	 			echo "id: " . $person["id"] . "<br>";
	 			echo "name: " . $person["name"] . "<br>";
	 			echo "cost: " . $person["cost"] . "<br>";
	 		}
	 		//var_dump($val["lst"]);
	 		//var_dump($value);
	 	}
	 }	
}

/**
* Reads RZD self service history for dates $from - $to and $page using given $token
* Returns back JSON response
*/
function get_history_page_from_rzd($from, $to, $page, $token) {
	global $logger;
	$logger->trace("get_history_page_from_rzd entered");

	$url_template = "https://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=5235&layer_id=5419&date0=%s&date1=%s&page=%d";
	$url_to_request = sprintf($url_template, $from, $to, $page);

	$rzd = curl_init($url_to_request);

	//TODO remove this block in prd
//	curl_setopt($rzd, CURLOPT_PROXY, "localhost");
//	curl_setopt($rzd, CURLOPT_PROXYPORT, 8080);
//	curl_setopt($rzd, CURLOPT_SSL_VERIFYPEER, false);
	
	curl_setopt($rzd, CURLOPT_FOLLOWLOCATION, true);
	curl_setopt($rzd, CURLOPT_RETURNTRANSFER, true);

	curl_setopt($rzd, CURLOPT_COOKIESESSION, true);
	curl_setopt($rzd, CURLOPT_COOKIE, "LtpaToken2=$token");

	// get response
	$logger->trace("running curl_exec");
	$json = curl_exec($rzd);
	curl_close($rzd);

	$logger->trace("exiting get_history_page_from_rzd");
	return $json;
}

/**
* Reads all pages from history with curl_multi 
*/
function get_all_history_pages_from_rzd($from, $to, $token) {
	global $logger;
	$logger->trace("get_history_page_from_rzd entered");

	$url_template = "https://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=5235&layer_id=5419&date0=%s&date1=%s&page=%d";
	
	$logger->trace("getting a first page from rzd");
	$json = get_history_page_from_rzd($from, $to, 1, $token);

	// decode it
	$logger->trace("decoding reply");
	$arr_from_json = json_decode($json, true, 512);
	
	// array of results
	$res = array();
	$res[0] = $arr_from_json;

	// array of curl handles
	$i = 0;
	$ch = array();
	
	// curl multi handler
	$mh = curl_multi_init();

	$logger->trace("adding handlers into multi handler");
	for ($countToGo = $arr_from_json["totalCount"]-10; $countToGo > 0 ; $countToGo -= 10) { 
		$logger->debug("countToGo: $countToGo");
		$ch[$i] = curl_init();
		curl_setopt($ch[$i], CURLOPT_URL, sprintf($url_template, $from, $to, $i+2));
		curl_setopt($ch[$i], CURLOPT_RETURNTRANSFER, true);
		curl_setopt($ch[$i], CURLOPT_FOLLOWLOCATION, true);
		curl_setopt($ch[$i], CURLOPT_COOKIESESSION, true);
		curl_setopt($ch[$i], CURLOPT_COOKIE, "LtpaToken2=$token");

		curl_multi_add_handle($mh, $ch[$i++]);
		$logger->trace("a handle added");
	}

	$logger->trace("initiating multi curl run");
	$running = null;
	do {
		curl_multi_exec($mh, $running);
		curl_multi_select($mh);
	} while ($running > 0);

	$i = 1;
	$logger->trace("getting results and decoding them");
	// for every handle
	foreach ($ch as $h) {
		// get result
		$json = curl_multi_getcontent($h);
		$logger->debug("i=$i JSON: $json");
		// decode it
		$res[$i++] = json_decode($json, true, 512);
		// remove a handle from multi curl handle
		curl_multi_remove_handle($mh, $h);
	}

	// close multi url handle
	curl_multi_close($mh);

	$logger->trace("exiting ...");
	return $res;
}

/**
* Get stat for every order on a page
*/
function get_stats($arPage, $token) {
	global $logger;
	
	$logger->trace("get_stats entered");

	// order/ticket form request
	$url_order_template = "https://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=5235&layer_id=5422&ORDER_ID=%d";
	$url_ticket_template = "https://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=5235&layer_id=5422&ORDER_ID=%d&ticket_id=%d";

	// result array
	$res = array('orders' => array(), 'refunds' => array());

	// curl handles array
	$ch = array();
	$i = 0;
	// multi curl handle
	$mh = curl_multi_init();

	// for each order list
	foreach ($arPage["slots"] as $orderLst) {
		// for each order
		foreach ($orderLst["lst"] as $order) {
			$ch[$i] = curl_init();
			curl_setopt($ch[$i], CURLOPT_URL, sprintf($url_order_template, $order["N"]));
			curl_setopt($ch[$i], CURLOPT_RETURNTRANSFER, true);
			curl_setopt($ch[$i], CURLOPT_FOLLOWLOCATION, true);
			curl_setopt($ch[$i], CURLOPT_COOKIESESSION, true);
			curl_setopt($ch[$i], CURLOPT_COOKIE, "LtpaToken2=$token");

			curl_multi_add_handle($mh, $ch[$i++]);
			$logger->trace("a handle added");

            // for each ticket
            foreach ($order["lst"] as $ticket) {
            	// if it was refunded - get ticket form for refund time
            	if (array_key_exists("status", $ticket) && $ticket["status"] == "REFUNDED") {
            		$ch[$i] = curl_init();
					curl_setopt($ch[$i], CURLOPT_URL, sprintf($url_ticket_template, $order["N"], $ticket["n"]));
					curl_setopt($ch[$i], CURLOPT_RETURNTRANSFER, true);
					curl_setopt($ch[$i], CURLOPT_FOLLOWLOCATION, true);
					curl_setopt($ch[$i], CURLOPT_COOKIESESSION, true);
					curl_setopt($ch[$i], CURLOPT_COOKIE, "LtpaToken2=$token");

					curl_multi_add_handle($mh, $ch[$i++]);
					$logger->trace("a handle added");
            	}
            }
		}
	}

	$logger->trace("initiating multi curl run");
	$running = null;
	do {
		curl_multi_exec($mh, $running);
		curl_multi_select($mh);
	} while ($running > 0);

	$logger->trace("getting results and decoding them");
	// for every handle
	foreach ($ch as $h) {
		// get result html
		$html = curl_multi_getcontent($h);

		// load response in a DOM and prepare XPath
		$dom = new DOMDocument();
        $dom -> loadHTML($html);
        $xpath = new DOMXPath($dom);

		// check url for a refund one
		$url = curl_getinfo($h, CURLINFO_EFFECTIVE_URL);

		if (stripos($url, "ticket_id") > 0) {
			// it's about refund time
			$date = 
				$xpath->query("//div[@class='servdata']/div[position()=1]//td[contains(text(),'Дата и время возврата')]/following-sibling::td/text()")
						->item(0)->textContent . ":00";

			$ticket_num = substr($url, strpos($url, "ticket_id=") + strlen("ticket_id="));
			$res["refunds"]["$ticket_num"] = $date;
		} else {
			// it's about order time
	        $arDates =
	            $xpath->query("//div[@class='money']//td[contains(text(),'Дата и время оформления')]/following-sibling::td/text()");

	        // if at least one found -> use it (otherwise all tickets were refunded, no way to get order date)
	        if ($arDates->length > 0) {
	            $date = $arDates->item(0)->textContent;
	        } else {
	            // todo replace with something meaningful
	            $date = "01.01.2000 11:59:59";
	        }

	        $order_num = substr($url, strpos($url, "ORDER_ID=") + strlen("ORDER_ID="));
	        $res["orders"]["$order_num"] = $date;
		}

		// remove a handle from multi curl handle
		curl_multi_remove_handle($mh, $h);
	}

	// close multi url handle
	curl_multi_close($mh);

	$logger->trace("exiting ...");
	return $res;
}

/**
 * Function to check order/ticket status and save it into DB
 * @param $arr - array from JSON response
 */
function process_page($arr, $token) {
	global $logger;
	global $user;

	$logger->trace("process_page entered");

	// need to get order/refund dates, each requires a web call based off an order/ticket num
	$stats = get_stats($arr, $token);

	$logger->trace("connecting to mysql");
	if (!($conn = mysqli_connect(HOST, USER, PWD, DB))) {
        $logger->error("Can't open connection to DB: " . mysqli_connect_error());
        exit();
    }

    $logger->trace("setting charset...");
    if(!$conn->set_charset('utf8')) {
    	$logger->error("can't set charset to utf8: " . $conn->error);
    	exit();
    };

    /*
    PROCEDURE `save_ticket_ordered`
     * (order_num bigint, ticket_num bigint,
        lastname varchar(64), ticket_price float,
        tripdate varchar(32), tripFrom varchar(64), tripTo varchar(64),
        createdon varchar(32), createdby varchar(32),
        out retcode int)*/

    $logger->trace("preparing statements");
    if (!($stmt = $conn->prepare("call save_ticket_ordered(?, ?, ?, ?, ?, ?, ?, ?, ?, @ret)"))) {
    	$logger->error("Can't prepare statement: " . $conn->error);
        exit();
    }


	/* PROCEDURE `save_refund`(ticketNum bigint, userLogin varchar(32), 
								refundedon varchar(32), out retCode int)*/
    if (!($ref_stmt = $conn->prepare("call save_refund(?, ?, ?, @ret)"))) {
    	$logger->error("Can't prepare refund statement: " . $conn->error);
    	exit();
    }

    $logger->trace("binding params");
    if (!$stmt->bind_param("iisdsssss", 
            $order_num, $ticket_num, $lastname, $ticket_price, 
            $trip_date, $trip_from, $trip_to, $order_date, $user)) {
    	$logger->error("Can't bind params: " .  $conn->error);
    	exit();
    };

    if (!$ref_stmt->bind_param("iss", $ticket_num, $user, $refund_date)) {
    	$logger->error("Can't bind params: " .  $conn->error);
    	exit();
    }

    foreach ($arr["slots"] as $objkey=>$obj) {
    	$logger->info("processing obj#$objkey");
        foreach ($obj["lst"] as $order) {
            $order_num = $order["N"];
			$logger->info("processing order#$order_num");
            $trip_date = $order["date0"] . " " . $order["time0"] . ":00";
            $trip_from = $order["station0"];
            $trip_to = $order["station1"];
            
            foreach ($order["lst"] as $ticket) {
                $ticket_num = $ticket["id"];
                $order_date = $stats["orders"][$order_num];
                
                $logger->info("processing ticket#$ticket_num");
                $lastname = substr($ticket["name"], 0, strpos($ticket["name"], " "));
                $ticket_price = $ticket["cost"];

                if (!$conn->query("set @ret=0")) {
                	$logger->error("can't set @ret to 0: " . $conn->error);
                };

                $logger->trace("executing query");
                if (!$stmt->execute()) {
                    $logger->error("Can't execute a query: " . $stmt->error);
                    exit();
                }
                $logger->trace("fetching result");
                if (!($res = $conn->query("select @ret"))) {
                	$logger->error("can't run fetch query: " . $conn->error);
                	exit();
                };
                if (!($row = $res->fetch_row())) {
                	$logger->error("can't fetch row: " . $conn->error);
                	exit();
                }
                if ($row[0] != 0) {
                	$logger->warn("Data wasn't saved");
                } else {
                	$logger->info("Data was saved");
                }

                $res->free();

                if (array_key_exists("status", $ticket) && $ticket["status"] == "REFUNDED") {
                	$logger->trace("processing refund");

                	$refund_date = $stats["refunds"][$ticket["n"]];

                	$logger->debug("Ticket #$ticket_num Date: $refund_date");

	                if (!$conn->query("set @ret=0")) {
	                	$logger->error("can't set @ret to 0: " . $conn->error);
	                };

	                $logger->trace("executing query");
	                if (!$ref_stmt->execute()) {
	                    $logger->error("Can't execute a query: " . $ref_stmt->error);
	                    exit();
	                }
	                $logger->trace("fetching result");
	                if (!($res = $conn->query("select @ret"))) {
	                	$logger->error("can't run fetch query: " . $conn->error);
	                	exit();
	                };
	                if (!($row = $res->fetch_row())) {
	                	$logger->error("can't fetch row: " . $conn->error);
	                	exit();
	                }
	                if ($row[0] != 0) {
	                	$logger->warn("Refund wasn't saved");
	                } else {
	                	$logger->info("Refund was saved");
	                }

	                $res->free();
                }
            }
        }
    }
    $stmt->close();
    $ref_stmt->close();
    $conn->close();
}

/**
* Function to read history from Self Service and transfer into the DB 
* @param $from  - Departure date period starting with ("DD.MM.YYYY" format)
* @param $to    - Departure date period ending in ("DD.MM.YYYY" format)
* @param $token - Auth token to pass to rzd
*/
function transfer_history($from, $to, $token) {
	global $logger;
	$logger->trace("transfer_history entered");

	// get first page from the site
	$logger->trace("getting all pages from rzd");
	$json = get_all_history_pages_from_rzd($from, $to, $token);

	foreach ($json as $page => $arrPage) { 
		// process data into DB
		process_page($arrPage, $token);
		print_history_page($arrPage, $page);
	};
}

// read config params
$config = parse_ini_file('../connection.cfg');
define("USER", $config["user"]);
define("PWD", $config["pwd"]);
define("HOST", $config["host"]);
define("DB", $config["db"]);

// logging params
Logger::configure("logger.xml");
$logger = Logger::getLogger("default");

$token = "nzxIOyvtv0CF6jMzfayctVCz8YelLYwJRUD9k1chZEDfzLDUcXJhhy4042i5in039ghUNpHlq6872oSC3YKsqlQghZTBS5BV+df5dN9px0J2F9RxPxJDqBLW08wH9KC/O3ikZWJIVRaSukCJsUvnzlY4tNiGNEnCoLszLcycnmUJ+n2JJFKZl5+RYU3gf0WChotecqFYSQvP0pL4qUXmRwzuUD9Yv3c5Bi7Ht9b6599jF4LGJ+WtwbYvHj/5hqxX8MGUF3R0RrHFfnA25EqFS62+rMXtvIBRxGw3vrPjxDl2z2yr60Lrrv+QxQfhWW3EDVDggwjS4w+KTfhCsFalPgCpSIpmaKtcgDN6Ld+/c6l20PiJRqb+I5QCnVzcBqJ+eIbn+S46IOeNzWsVR0EaCUrZl2HJsUKO3dytKvcYNjGexNEc4UehqFUXm4dUnJg708e699Frr+sdiWQOqBS1NGUGH1DwrgxUz1rWXgrQxeCZ3WeZ3h1bosChYnYrQrHp/p4glkV5f6Y6QOyYOSjmigT4Quv2EDNx4zvLQpPw4daSywGBcHOpRlG5NoKCcr1H/7A/4lXq0AFzKQkuiH2QgSKdKwAHdAIvXHHEv0MRypKHmA4wtnchwW5laYjPWxp8";

$logger->trace("starting... " . date(DATE_RFC2822));

//TODO choose an algo to select a user
$user = "zuchra";

echo '<html><head><meta charset="UTF-8"></head><body>';
transfer_history("01.01.2014", "28.02.2014", $token);
/*
$json = get_history_page_from_rzd("06.11.2014", "07.11.2014", 1, $token);
var_dump(get_stats(json_decode($json, true, 512), $token));
*/
echo "</body></html>";

$logger->trace("finishing... " . date(DATE_RFC2822));
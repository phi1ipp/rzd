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
	curl_setopt($rzd, CURLOPT_PROXY, "localhost");
	curl_setopt($rzd, CURLOPT_PROXYPORT, 8080);
	curl_setopt($rzd, CURLOPT_SSL_VERIFYPEER, false);
	
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
 * Function to check order/ticket status and save it into DB
 * @param $arr - array from JSON response
 */
function process_page($arr) {
	global $logger;
	$logger->trace("process_page entered");

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
    PROCEDURE `save_ticket_ordered`(order_num bigint, ticket_num bigint,
										lastname varchar(64), ticket_price float,
										tripdate varchar(32),
										createdon varchar(32), createdby varchar(32),
										out retcode int)*/

    $logger->trace("preparing statement");
    if (!($stmt = $conn->prepare("call save_ticket_ordered(?, ?, ?, ?, ?, date_format(now(), '%d.%m.%Y %H:%i:%S'), ?, @ret)"))) {
    	$logger->error("Can't prepare statement: " . $conn->error);
        exit();
    }

    //TODO choose an algo to select a user
    $user = "gabii";

    $logger->trace("binding params");
    if (!$stmt->bind_param("iisdss", $order_num, $ticket_num, $lastname, $ticket_price, $trip_date, $user)) {
    	$logger->error("Can't bind params: " .  $conn->error);
    	exit();
    };

    foreach ($arr["slots"] as $objkey=>$obj) {
    	$logger->info("processing obj#$objkey");
        foreach ($obj["lst"] as $order) {
            $order_num = $order["N"];
			$logger->info("processing order#$order_num");
            $trip_date = $order["date0"] . " " . $order["time0"] . ":00";
            foreach ($order["lst"] as $ticket) {
                $ticket_num = $ticket["id"];
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
            }
        }
    }
    $stmt->close();
    $conn->close();
}

/**
* Function to read history from Self Service and transfer into the DB 
*/
function transfer_history($from, $to, $token) {
	global $logger;
	$logger->trace("transfer_history entered");
	$page = 1;

	// get first page from the site
	$logger->trace("getting a page #$page from rzd");
	$json = get_history_page_from_rzd($from, $to, $page, $token);

	// decode it
	$logger->trace("decoding reply");
	$arr_from_json = json_decode($json, true, 512);

	for ($countToGo = $arr_from_json["totalCount"]-10; $countToGo > 0 ; $countToGo -= 10) { 
		$logger->info("records to process: $countToGo");
		// print results
		print_history_page($arr_from_json, $page++);

		// process data into DB
		process_page($arr_from_json);

		// get next page from the site
		$logger->trace("getting page#$page from rzd");
		$json = get_history_page_from_rzd($from, $to, $page, $token);

		// decode it
		$logger->trace("decoding reply");
		$arr_from_json = json_decode($json, true, 512);
	};
}

// read config params
$config = parse_ini_file('../../connection.cfg');
define("USER", $config["user"]);
define("PWD", $config["pwd"]);
define("HOST", $config["host"]);
define("DB", $config["db"]);

// logging params
Logger::configure("logger.xml");
$logger = Logger::getLogger("default");

$token = "IVgVpbx5RfJullLWMfWuo/SHTo1NJuo/hv4bXVVKHsBUniLfd9e8fi2ELvdypAXbvsTZSMRdmDNPJpMkLXkSVC6sANZS61K3QSybgQ5GbLkDAldSl1wGZhmfRcTCCbMuGBHy9XUmbrfQ7MBCDmtf9ZSgOX5Mr12wH0IhJ8KFzxJk8ZgxopUkWfmiP3q+2Oxc0B0/AF5JrSDFdSkKzKBPDk4Q4YpeAJcPxZEazg2q8tsjZcr67wVSddPOqamRrk5Y946sDz/nDRJWIjJn/XTn+wWfwSnOE5jG5UPAgDzn9mFjypu40b47/tGcLVL9LPP1XiVtgcdcgWADWFSgQyFQQ2/8pJApLVvi3GFz4tk/3FqS+7wJ8oQ2o8ChKkCtFs1efVrPqzkJFyf/sPLEroiY7hYKY+zg2iSwTl1yWblHuqEzj74O6Tyc1d7Rp0lTjaiIeh8HKpZxk9pqNg0HreHBQZ+yfdDUteRiQEMGBg5m3qdTLVLYy1aOGJOBTyKJIUEpPUAhpugDl+gBf92hRcfQtGFSqJnE0TddEf6XjYG1oWEWsV7csSZhlL9/E+8S+b87vkXldYxqIYq4SNgmS+NoYXVMYaTbTUo8k5ayQUbWNKIg3u9aIGT4NWIRYUnI8QP7";

$logger->trace("starting... " . date(DATE_RFC2822));

echo '<html><head><meta charset="UTF-8"></head><body>';
transfer_history("06.11.2014", "07.11.2014", $token);
echo "</body></html>";

$logger->trace("finishing... " . date(DATE_RFC2822));
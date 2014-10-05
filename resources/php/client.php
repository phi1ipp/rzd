<?php # HelloClient.php
# Copyright (c) 2005 by Dr. Herong Yang
#

$client = new SOAPClient("TicketService.wsdl", array('proxy_host'     => "localhost",
                                            'proxy_port'     => 8080,
                                            'trace' => true));

$resp = $client->getTransInfo(array('orderId' => 103696202, "User" => "pgrigoryev",
    'LtpaToken2'=>"z5jy+6BN6tgmCvM9RPAEx+pqK355lXlKjtyCJ6MJVYFngSiz/qJtiIIfVeFjavUILJ4SWq+oHpBRRbP4R3VORFkLxHDTa4kSrtIh3bn7A4j1wiLoQ4W/Xe8S8WMBZDb+l/w8uUG/39zLO3afp6XtlDttOwUv4BeE5GymKsa+/jq6/aO3mHaVCcTlriX9L+t+VuHlhCDrvxSDP5u51sPxhk0Sc8t8O+jZ8R67rXHVKmSPgE0oCNIW0mFZJUzb6V0wLMqB3bKQRjEGzk6hxfO2VKviahCyMt+ugRMRkONrIOwdrLla6RbS22yNHMhZ125mf3v06XmmQ/pNErJmtzuE5AJalj50oxnlp9iCvpUZ22g8FYLaUuBY0WmM0Wi+aUO6Y2XL3jCBJdkbryMHi349OcOHSWkXZgTQ4zntaJiPm+1QCCi1M9Mje6zBxSXnFqbaxQmzHsEe7r3GuwO49ItWlCadWkNUKErNnvMJhlf8QomH1QzZhAid67NyC8XypN1jmh23bS5kYeSgW6VYINK6gE1S5wKbcNttbFec8N0LCyiiar4YjSNUxvbYTXock/hmSrrZBXCgAErbfvNsTXtOSBfYKdYweqYfzK+68unnFGk1TH9QLnilItJwAxKWqrYh"));

echo var_dump($resp) . "\n\n<br>";
echo "Last response: " . $client->__getLastResponse() . "\n\n<br>";
echo "From: " . $resp->StationFrom . "<br>";
echo "Last response headers: " . $client->__getLastResponseHeaders() . "\n\n<br>";
/*
 * $ticketServiceServiceGet = new TicketServiceServiceGet();
 * if($ticketServiceServiceGet->getTransInfo(new TicketServiceStructTransInfoRequest(23108471, "pgrigoryev",
    "strNhB4E/RYD/imtcdcK+PtRkN8ABZWr2a8wLCg9TMk+Lxx0cT81h0cGzCY2FWvvsNk8bXvvkAnK8pxRr4qg+UDf8MaAbedyaLLcYGyRgUb8/HwmyOkU7shWPUV4cuMMmgXZWTlUXcbuyCSuRNJHrNow0D6OoUR1gZV9CaPoddXzKTsmMTjdZKYJ4xjO8x+ut1osbKhhcUTCmEfhHGqX00Fra3JQsGC4+Fh6wISpX7mRsn1J3TBGl0h/mnLG0NL4n8UMhdbFk6g8uBGwqVmh6sw6ZLDIylo23/r/W5GEC5JKb3p/+WFtSmMFbG2+OeLpD4yn1dNaKV0VG0zMQYza+kDEGw/1rLsISjM5+BCb/idIT6pzoewlg8jKTclq0uNQjdIVS/RodbZG1lw2WEHnjS6dm1m9a8Y6V/eHKZCSBwuh2iJL0RBRDED8urMmnGHK94H/2uE9OztnyGYU8tpq7h4iHj9HT5BakmuXFVTonEMJBppc/fjb9SkRb+KpLFdjV3p1rEHkwcnQe5mdFK7GdTViyOpFUvYqMbG3fo+ekCjFYrrglmF3ZdHJ40tHNl82UCPXcOt5SIhEdLdPt01kdfOKfwtGF2Dgu7lsOBieiAJBGDNNsUbZoprkRImCXEOy"
    )))
    print_r($ticketServiceServiceGet->getResult());
else
    print_r($ticketServiceServiceGet->getLastError());*/
<?php # HelloClient.php
# Copyright (c) 2005 by Dr. Herong Yang
#

require_once "WsdlToPhp/samples/TicketService/TicketServiceAutoload.php";

$ticketServiceServiceGet = new TicketServiceServiceGet();

if($ticketServiceServiceGet->getTransInfo(new TicketServiceStructTransInfoRequest(123456, "pgrigoryev",
    "uJJi1MZ3afQq9mdUnPeRksFd94t1dhc2NNQ5kH7aTi6FoT95BEdKSz9e4gQ7lc0KRou5tLYWMyu7AWhKf1iQs6Dhgmx8UfBrgyGg5ZtnLBTBMOhAttoGw9n3/eJzfm3ghK5E6z5KyChMkI4yTCU+tdvFIBTt2zvJqJunHKnLxCrhRaOVRiCjKGFCsNhtucuprjkoCdAWfZIRvaUNzWDDuXi4lhkpwNdqDv032kaQxVrLB6jdVksJjak+lhZmBc5MHg/w3OuhyLGXuq1Q6LcKQqlIIxcihbO+n79sqgOowmCs+fACHw7WoqT4r42JIWUdg5SiLPC2DDRTkLJFxl23h9/8wiN0sPkMSpyMbcmA0s9LJd3uSKmt20tPg19OPTAI4jtEQTaQnPV2TEfVPLccJqdzAUzEFBgh0k9wjlIJQooemhEtT10Ag293+/lZ4hrekAJ+pP+LHzD/bFRNb3y1BA+RJoBbUhaK+LN6BG/0haHD8DNfKs7XIavFhyKIdGnxKi+tmaWwEsvvi2tDI/XM464Q63lcgVYj9eNzGBNjEvqVsLhtEB/IRzm/y03xLQMMfcWFnOoXg51DNzkrfIIJs2kKd1ti9znvhhRTzjP+jwndyiJsD56Ft8VlILupvicU"
    )))
    print_r($ticketServiceServiceGet->getResult());
else
    print_r($ticketServiceServiceGet->getLastError());
?>
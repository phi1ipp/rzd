$(document).ajaxComplete(function(event, xhr, settings){
	var result={};
	var re=/number='(\d+)'/;
	
	var saleOrder = jQuery.parseJSON(xhr.responseText);
	console.log(saleOrder);
	
	var orders = saleOrder.orders;
	result.orders_cnt = orders.length;
	result.orders = [];
	for (var i=0; i<orders.length; i++) {
		console.log(saleOrder.orders[i].orderId);
		var tickets = orders[i].tickets;

		var res_tickets = [];
		for (var j=0; j<tickets.length; j++) {
			var arr = re.exec(tickets[j].text);
			res_tickets.push({"ticketId" : tickets[j].ticketId, "ticketNum" : arr[1]});
		}
		result.orders.push({"tickets_cnt":res_tickets.length, "tickets":res_tickets, "orderId":orders[i].orderId});
	}
	console.log(result);
});

$.ajax({
	url     : 'https://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=735&layer_id=5378&refererLayerId=5375',
	type    : 'POST',
	data    : {rid:glbRID}
	});
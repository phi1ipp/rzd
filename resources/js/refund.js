var errMsg = "Неуспешный запрос. Пожалуйста повторите попытку";
var summa = "<b>Сумма, причитающаяся к возврату: $summa$ руб.</b>";
var div='<div id="TmAmount1" style="display:none">' + summa + 
	'<br/> Сумма будет переведена на банковскую карту, с которой производилась оплата.<br/> Внимание! В зависимости от времени возврата билета относительно отправления поезда, сумма к возврату может отличаться от стоимости билета.<br/> За дополнительной информацией по вопросам возврата денежных средств Вы можете обратиться в службу поддержки ЗАО "ВТБ24" по телефону 8-800-775-24-24 или по электронному адресу: ticket@bnk.ru. В случае возникновения спорных вопросов при списании или зачислении денежных средств на банковскую карту необходимо обратиться в банк, держателем банковской карты которого Вы являетесь.<br/>' + 
	'</div>';
$(div).appendTo("body").css("left","1000px").css("top","10px")
	.css("z-index","1000").css("position","absolute").css("background-color","#fff")
	.css("border","2px solid orange").css("font","inherit").css("padding","10px").css("box-shadow","4px 4px 4px 0 #777")
	.css("font", "12px/1.4 Verdana,sans-serif").append('<div id="buttons" align="right"><button id="btnOK">OK</button><button id="btnCancel">Cancel</button></div>');

$("#btnCancel").click(function(){
	$("#TmAmount1").fadeOut();
}).appendTo($("#buttons"));

$("<div align='center' style='margin-bottom:10px'><button>Оформить возврат</button></div>").click(function(){
	
	var ids=/ORDER_ID=(\d+)&ticket_id=(\d+)/.exec($(location).attr('href'));
	var url = "https://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=5235&layer_id=5421&ORDER_ID="+ids[1]+"&ticket_id="+ids[2]+"&action=PREVIEW";

	$.ajax(url,{
		dataType: "json"
	}).done(function(resp){
		if (resp.result == "RID") {
			$.ajax(url+"&rid="+resp.RID, {
				dataType: "json"
			}).done(function(refundData){
				if (refundData.result == "OK") {
					$("#TmAmount1").show().find("b").html(summa.replace(/\$summa\$/, refundData.sum));

					$("#btnOK").click(function(){
						var url = "https://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=5235&layer_id=5421&ORDER_ID="+ids[1]+"&ticket_id="+ids[2]+"&action=REFUND";
						$.ajax(url, {
							dataType: "json"
						}).done(function(resp){
							$("#TmAmount1").fadeOut();
							if (resp.result == "RID") {
								$.ajax(url + "&rid=" + resp.RID, {
									dataType: "json"
								}).done(function(data){
									if (data.result == "OK") {
										alert("Операция завершена успешно");
									}
								}).fail(function(data){
									alert(errMsg);
								});
							} else {
								alert(errMsg);
							}
						});
					});
				} else {
					alert(errMsg);
				}
			}).fail(function(){
				alert(errMsg);
			});
		}
	}).fail(function(data){
		console.log(errMsg);
	});
}).insertBefore("table.topinfo");
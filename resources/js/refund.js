var div='<div id="TmAmount1" style="display:none">' +
'<b> Сумма, причитающаяся к возврату: $summa$ руб. </b><br/> Сумма будет переведена на банковскую карту, с которой производилась оплата.<br/> Внимание! В зависимости от времени возврата билета относительно отправления поезда, сумма к возврату может отличаться от стоимости билета.<br/> За дополнительной информацией по вопросам возврата денежных средств Вы можете обратиться в службу поддержки ЗАО "ВТБ24" по телефону 8-800-775-24-24 или по электронному адресу: ticket@bnk.ru. В случае возникновения спорных вопросов при списании или зачислении денежных средств на банковскую карту необходимо обратиться в банк, держателем банковской карты которого Вы являетесь.<br/>' + 
'</div>';

$("<div align='center' style='margin-bottom:10px'><button>Оформить возврат</button></div>").click(function(){
	
	var ids=/ORDER_ID=(\d+)&ticket_id=(\d+)/.exec($(location).attr('href'));
	var url = "https://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=5235&layer_id=5421&ORDER_ID="+ids[1]+"&ticket_id="+ids[2]+"&action=PREVIEW";

	$.ajax(url).always(function(data){
		var resp = $.parseJSON(data.responseText);
		if (resp.result == "RID") {

			$.ajax(url+"&rid="+resp.RID)
			.always(function(refund){
				var refundData = $.parseJSON(refund.responseText);
				
				console.log(refundData);

				$(div.replace(/\$summa\$/, refundData.sum)).appendTo("body").css("display","block").css("left","1000px").css("top","10px")
				.css("z-index","1000").css("position","absolute").css("background-color","#fff")
				.css("border","2px solid orange").css("font","inherit").css("padding","10px").css("box-shadow","4px 4px 4px 0 #777")
				.css("font", "12px/1.4 Verdana,sans-serif").append('<div id="buttons" align="right"></div>');

				$("<button>OK</button>").appendTo($("#buttons"));
				$("<button>Cancel</button>").click(function(){
					$("#TmAmount1").fadeOut();
				}).appendTo($("#buttons"));

			});
		}
	});
}).insertBefore("table.topinfo");
package com.grigorio.rzd.ticket;

import com.grigorio.rzd.Main;
import com.grigorio.rzd.OrderProcessor.Refund;
import com.grigorio.rzd.utils.Web;
import com.grigorio.rzd.utils.WebViewPrinter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.util.logging.Logger;

/**
 * Class to show Ticket
 */
public class TicketViewController {
    private final Logger logger = Logger.getLogger(TicketViewController.class.getName());

    private Main app;

    public class JSHook {
        public void print() {
            WebViewPrinter.print(webView);
        }

        public void refund(int iOrdId, int iTicketId) {
            logger.entering(JSHook.class.getName(), "refund",
                    String.format("OrderId:%d TicketId:%d", iOrdId, iTicketId));

            String strToken =
                    webView.getEngine().executeScript("/LtpaToken2=([^;$]+)/.exec(document.cookie)[1];").toString();

            logger.finer(String.format("Adding a refund job to the queue: OrderId=%d TicketId=%d", iOrdId, iTicketId));
            app.getQueue().add(new Refund(iOrdId, iTicketId, strToken));

            logger.exiting(JSHook.class.getName(), "refund");
        }
    }

    private ObjectProperty<Ticket> ticket = new SimpleObjectProperty<>();

    public void setTicket(Ticket aTicket) {
        this.ticket.setValue(aTicket);
    }

    @FXML
    private WebView webView;

    @FXML
    void initialize() {
        ticket.addListener( (observable, oldTicket, newTicket) -> {
            String strTicketURL = "https://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=5235&layer_id=5422&" +
                    "ORDER_ID=%d&ticket_id=%d";

            if (newTicket != null) {
                try {
                    String strURL = String.format(strTicketURL, newTicket.getlOrderId(), newTicket.getlTicketId());
                    webView.getEngine().load(strURL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        webView.getEngine().setOnAlert( event -> {
            String strMsg = event.getData();

            VBox vBox = new VBox();
            Label lblMsg = new Label(strMsg);
            Button btnOK = new Button("OK");
            vBox.getChildren().addAll(lblMsg, btnOK);
            vBox.setAlignment(Pos.CENTER);
            vBox.setSpacing(10);
            vBox.setPadding(new Insets(5));

            Scene scene = new Scene(vBox);
            Stage stgPopup = new Stage();
            stgPopup.setScene(scene);

            btnOK.setOnAction(handler -> stgPopup.hide());

            stgPopup.show();
        });

        webView.getEngine().documentProperty().addListener(
                (observable, oldDoc, newDoc) -> Web.autoFillCredentials(newDoc, webView.getEngine())
        );

        webView.getEngine().getLoadWorker().stateProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        // set JS hooks
                        JSObject jsObject = (JSObject) webView.getEngine().executeScript("window");
                        jsObject.setMember("jsHook", new JSHook());

                        String strPrintHook =
                                "window.print = function () {$('#refund').hide();jsHook.print();$('#refund').show()}";
                        webView.getEngine().executeScript(strPrintHook);

                        if ((int) webView.getEngine().executeScript("$('table.refund').length;") == 0) {
                            String strScript =
                                    "var errMsg = \"Неуспешный запрос. Пожалуйста повторите попытку\";" +
                                            "var summa = \"<b>Сумма, причитающаяся к возврату: $summa$ руб.</b>\";" +
                                            "var div='<div id=\"TmAmount1\" style=\"display:none\">' + summa + " +
                                                "'<br/> Сумма будет переведена на банковскую карту, с которой производилась оплата.<br/> " +
                                                "Внимание! В зависимости от времени возврата билета относительно отправления поезда, " +
                                                "сумма к возврату может отличаться от стоимости билета.<br/> " +
                                                "За дополнительной информацией по вопросам возврата денежных средств Вы можете " +
                                                "обратиться в службу поддержки ЗАО \"ВТБ24\" по телефону 8-800-775-24-24 или " +
                                                "по электронному адресу: ticket@bnk.ru. В случае возникновения спорных вопросов " +
                                                "при списании или зачислении денежных средств на банковскую карту необходимо " +
                                                "обратиться в банк, держателем банковской карты которого Вы являетесь.<br/>' + " +
                                                "'</div>';" +
                                            "$(div).appendTo(\"body\").css(\"left\",\"300px\").css(\"top\",\"10px\")" +
                                                ".css(\"z-index\",\"1000\").css(\"position\",\"absolute\").css(\"background-color\",\"#fff\")" +
                                                ".css(\"border\",\"2px solid orange\").css(\"font\",\"inherit\")." +
                                                "css(\"padding\",\"10px\").css(\"box-shadow\",\"4px 4px 4px 0 #777\")" +
                                                ".css(\"font\", \"12px/1.4 Verdana,sans-serif\")" +
                                            ".append('<div id=\"buttons\" align=\"right\"><button id=\"btnOK\">OK</button>" +
                                                "<button id=\"btnCancel\">Cancel</button></div>');" +

                                            "$(\"#btnCancel\").click(function(){" +
                                                "$(\"#TmAmount1\").fadeOut();" +
                                            "}).appendTo($(\"#buttons\"));" +

                                            "$(\"<div id='refund' align='center' style='margin-bottom:10px'><button>Оформить возврат</button></div>\")" +
                                            ".click(function(){" +
                                                "var ids=/ORDER_ID=(\\d+)&ticket_id=(\\d+)/.exec($(location).attr('href'));" +
                                                "var url = \"https://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=5235&" +
                                                "layer_id=5421&ORDER_ID=\"+ids[1]+\"&ticket_id=\"+ids[2]+\"&action=PREVIEW\";" +

                                                "$.ajax(url,{" +
                                                    "dataType: \"json\"" +
                                                "}).done(function(resp){" +
                                                    "if (resp.result == \"RID\") {" +
                                                        "$.ajax(url+\"&rid=\"+resp.RID, {" +
                                                            "dataType: \"json\"" +
                                                        "}).done(function(refundData){" +
                                                            "if (refundData.result == \"OK\") {" +
                                                                "$(\"#TmAmount1\").show().find(\"b\").html(summa.replace(/\\$summa\\$/, refundData.sum));" +
                                                                "$(\"#btnOK\").click(function(){" +
                                                                    "var url = \"https://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=5235&layer_id=5421&ORDER_ID=\"+ids[1]+\"&ticket_id=\"+ids[2]+\"&action=REFUND\";" +
                                                                    "$.ajax(url, {" +
                                                                        "dataType: \"json\"" +
                                                                    "}).done(function(resp){" +
                                                                        "$(\"#TmAmount1\").fadeOut();" +
                                                                        "if (resp.result == \"RID\") {" +
                                                                            "$.ajax(url + \"&rid=\" + resp.RID, {" +
                                                                                "dataType: \"json\"" +
                                                                            "}).done(function(data){" +
                                                                                "if (data.result == \"OK\") {" +
                                                                                    "alert(\"Операция завершена успешно\");" +
                                                                                    "jsHook.refund(ids[1], ids[2]);" +
                                                                                "}" +
                                                                            "}).fail(function(data){" +
                                                                                "alert(errMsg);" +
                                                                            "});" +
                                                                        "} else {" +
                                                                            "alert(errMsg);" +
                                                                        "}" +
                                                                    "});" +
                                                                "});" +
                                                            "} else {" +
                                                                "alert(errMsg);" +
                                                            "}" +
                                                        "}).fail(function(){" +
                                                            "alert(errMsg);" +
                                                        "});" +
                                                    "}" +
                                                "}).fail(function(data){" +
                                                    "alert(errMsg);" +
                                                "});" +
                                            "}).insertBefore(\"table.topinfo\");";
                            webView.getEngine().executeScript(strScript);
                        }
                    }
                });
    }

    public void setApp(Main app) {
        this.app = app;
    }

}

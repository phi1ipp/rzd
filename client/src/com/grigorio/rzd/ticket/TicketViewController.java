package com.grigorio.rzd.ticket;

import com.grigorio.rzd.Main;
import com.grigorio.rzd.utils.Web;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * Created by philipp on 11/18/14.
 */
public class TicketViewController {

    private ObjectProperty<Ticket> ticket = new SimpleObjectProperty<Ticket>();

    private String strTokenCookie;

    @FXML
    private WebView webView;
    @FXML
    private Button btnRefund;

    public Ticket getTicket() {
        return ticket.getValue();
    }

    public void setTicket(Ticket aTicket) {
        this.ticket.setValue(aTicket);
    }

    public ObjectProperty<Ticket> ticketProperty() {
        return ticket;
    }

    public void setStrTokenCookie(String strTokenCookie) {
        this.strTokenCookie = strTokenCookie;
    }

    @FXML
    void initialize() {
        //todo change
        btnRefund.setDisable(true);
        
        ticket.addListener(new ChangeListener<Ticket>() {
            @Override
            public void changed(ObservableValue<? extends Ticket> observable, Ticket oldTicket, Ticket newTicket) {
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
            }
        });

        webView.getEngine().documentProperty().addListener(new ChangeListener<Document>() {
            @Override
            public void changed(ObservableValue<? extends Document> observable, Document oldValue, Document newDoc) {
                Web.autoFillCredentials(newDoc, webView.getEngine());
            }
        });

        webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (newValue == Worker.State.SUCCEEDED) {
                    String strScript =
                            "var div='<div id=\"TmAmount1\" style=\"display:none\">' +\n" +
                            "'<b> Сумма, причитающаяся к возврату: $summa$ руб. </b><br/> Сумма будет переведена на " +
                                    "банковскую карту, с которой производилась оплата.<br/> Внимание! В зависимости " +
                                    "от времени возврата билета относительно отправления поезда, сумма к возврату " +
                                    "может отличаться от стоимости билета.<br/> За дополнительной информацией по " +
                                    "вопросам возврата денежных средств Вы можете обратиться в службу поддержки " +
                                    "ЗАО \"ВТБ24\" по телефону 8-800-775-24-24 или по электронному адресу: " +
                                    "ticket@bnk.ru. В случае возникновения спорных вопросов при списании или " +
                                    "зачислении денежных средств на банковскую карту необходимо обратиться в банк, " +
                                    "держателем банковской карты которого Вы являетесь.<br/>' + \n" +
                            "'</div>';\n" +
                            "\n" +
                            "$(\"<button>Оформить возврат</button>\").click(function(){\n" +
                            "\t\n" +
                            "\tvar ids=/ORDER_ID=(\\d+)&ticket_id=(\\d+)/.exec($(location).attr('href'));\n" +
                            "\tvar url = \"https://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=5235&layer_id=5421&ORDER_ID=\"+ids[1]+\"&ticket_id=\"+ids[2]+\"&action=PREVIEW\";\n" +
                            "\n" +
                            "\t$.ajax(url).always(function(data){\n" +
                            "\t\tvar resp = $.parseJSON(data.responseText);\n" +
                            "\t\tif (resp.result == \"RID\") {\n" +
                            "\n" +
                            "\t\t\t$.ajax(url+\"&rid=\"+resp.RID)\n" +
                            "\t\t\t.always(function(refund){\n" +
                            "\t\t\t\tvar refundData = $.parseJSON(refund.responseText);\n" +
                            "\t\t\t\t\n" +
                            "\t\t\t\tconsole.log(refundData);\n" +
                            "\n" +
                            "\t\t\t\t$(div.replace(/\\$summa\\$/, refundData.sum)).appendTo(\"body\")" +
                                    ".css(\"display\",\"block\").css(\"left\",\"450px\").css(\"top\",\"10px\")\n" +
                            "\t\t\t\t.css(\"z-index\",\"1000\").css(\"position\",\"absolute\").css(\"background-color\",\"#fff\")\n" +
                            "\t\t\t\t.css(\"border\",\"2px solid orange\").css(\"font\",\"inherit\").css(\"padding\",\"10px\")\n" +
                            "\t\t\t\t.css(\"font\", \"12px/1.4 Verdana,sans-serif\").append('<div id=\"buttons\" align=\"right\"></div>');\n" +
                            "\n" +
                            "\t\t\t\t$(\"<button>OK</button>\").appendTo($(\"#buttons\"));\n" +
                            "\t\t\t\t$(\"<button>Cancel</button>\").click(function(){\n" +
                            "\t\t\t\t\t$(\"#TmAmount1\").fadeOut();\n" +
                            "\t\t\t\t}).appendTo($(\"#buttons\"));\n" +
                            "\n" +
                            "\t\t\t});\n" +
                            "\t\t}\n" +
                            "\t});\n" +
                            "}).insertBefore(\"table.topinfo\");";
                    webView.getEngine().executeScript(strScript);
                }
            }
        });
    }
}

package com.grigorio.rzd;

import com.grigorio.rzd.preferences.PrefsController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainController extends ScrollPane{
    // class which is used to intercept ajax calls
    public class JSAjaxHook {
        public void refund(String strURI) {
            System.out.println("JSApp to send: " + strURI);
            int iOrderId, iTicketId;

            Pattern pattern = Pattern.compile("STRUCTURE_ID=5235&layer_id=5421&ORDER_ID=(\\d+)&ticket_id=(\\d+)&action=REFUND&rid=(\\d+)");
            Matcher matcher = pattern.matcher(strURI);

            if (matcher.find()) {
                System.out.println("Refund URL detected");
                iOrderId = Integer.parseInt(matcher.group(1));
                iTicketId = Integer.parseInt(matcher.group(2));

                System.out.println(String.format("Putting refund(%d, %d) into the queue", iOrderId, iTicketId));
                app.getQueue().add(new Refund(iOrderId, iTicketId, strLtpaToken));
            }
        }
    }
    //TODO add close SearchForm on close of this

    private final String SELFSERVICEURI="https://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=5235&layer_id=5382";
    //TODO replace to a proper one page
    // url of bank confirmation form and success string
    //private final String PAYMENTFORMURI="https://paygate.transcredit.ru/mpirun.jsp?action=mpi";
    private final String PAYMENTFORMURI="file:///home/philipp/projects/rzd/resources/html/bank_response.html";
    private final String PAYMENTSUCCESS="Операция завершена успешно";

    private final String strHook = "(function(XHR) {" +
        "\"use strict\";" +
        " var open = XHR.prototype.open;" +
        "var send = XHR.prototype.send;" +
        "XHR.prototype.open = function(method, url, async, user, pass) {" +
            "this._url = url;" +
            "open.call(this, method, url, async, user, pass);};" +

        "XHR.prototype.send = function(data) {" +
            "if (this._url.search('PREVIEW')>0){" +
            "ajaxHook.refund(this._url);}" +
            "send.call(this, data);}" +
    "})(XMLHttpRequest);";

    @FXML
    private WebView fxWebView;

    private ClientSearchController frmClientSearchController = null;
    private Stage stageClientSearch;
    private WebEngine webEngine;
    private Main app;

    private String strLtpaToken = null;

    public void setApp(Main application) {
        app = application;
    }

    @FXML
    void initialize() {
        webEngine = fxWebView.getEngine();

        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observableValue, Worker.State state, Worker.State state2) {
                if (state2 == Worker.State.SUCCEEDED) {
                    Document doc = webEngine.getDocument();
                    String strURI = doc.getDocumentURI();

                    if (PAYMENTFORMURI.equalsIgnoreCase(strURI)) {
                        //TODO create file to start processing
                        System.out.println("Got bank confirmation page. Parsing...");

                        System.out.println(doc.getElementsByTagName("h2").item(0).getTextContent());
                        if (doc.getElementsByTagName("h2").item(0).getTextContent()
                                .equalsIgnoreCase(PAYMENTSUCCESS)) {
                            System.out.println("Payment was successful! Getting parameters of transaction...");

                            // get form action attribute
                            String strFormAction =
                                    doc.getElementsByTagName("form").item(0)
                                            .getAttributes().getNamedItem("action").getTextContent();
                            // parse action url to get order_id
                            int iOrderNum =
                                    Integer.parseInt(
                                            strFormAction.substring(
                                                    strFormAction.indexOf("ORDER_ID=") + "ORDER_ID=".length()));

                            // put request in a queue if cookie set
                            if (strLtpaToken != null) {
                                app.getQueue().add(new Order(iOrderNum, strLtpaToken));
                                System.out.println("OrderId: " + iOrderNum + " placed in a queue for processing");
                            } else {
                                System.out.println("Cookie is not set, can't send an order for processing");
                            }
                        } else {
                            System.out.println("Payment wasn't successful");
                        }
                    } else if (SELFSERVICEURI.equalsIgnoreCase(doc.getDocumentURI())) {
                        // self service page, need to set up ajax hooks
                        webEngine.executeScript(strHook);
                        JSObject window = (JSObject) webEngine.executeScript("window");
                        window.setMember("ajaxHook", new JSAjaxHook());
                    } else {
                        String strCookie = (String) webEngine.executeScript("document.cookie;");

                        // if required cookie set -> save it
                        if (strCookie.indexOf("LtpaToken2") > 0) {
                            strLtpaToken =
                                    strCookie.substring(strCookie.indexOf("LtpaToken2=") + "LtpaToken2".length() + 1,
                                            strCookie.indexOf(";", strCookie.indexOf("LtpaToken2")));
                            System.out.println("LtpaToken2 = " + strLtpaToken);
                        }
                    }

                }
            }
        });

        webEngine.load("http://www.rzd.ru");
    }

    // fills in Contact data into a current active contact fields
    public void insertContactData(Contact cnt) {
        String strInsert =
            String.format("var passData = $(document.activeElement).parents('.trlist-pass__pass-item-bar');" +
                    "$(passData).find('[name=firstName]').val('%s');" +
                    "$(passData).find('[name=lastName]').val('%s');" +
                    "$(passData).find('[name=midName]').val('%s');" +
                    "$(passData).find('[name=docType]').val(%s);" +
                    "$(passData).find('[name=docNumber]').val('%s');" +
                    "$(passData).find('[name=country]').val(%s);" +
                    "$(passData).find('[name=gender]').val(%s);" +
                    "$(passData).find('[name=birthplace]').val('%s');" +
                    "$(passData).find('[name=birthdate]').val('%s');",
                    cnt.getFirstName(), cnt.getLastName(), cnt.getMiddleName(),
                    cnt.getDocType(), cnt.getDocumentNumber(), cnt.getDocCountry(),
                    cnt.getGender(), cnt.getBirthPlace(), cnt.getBirthDate());
        String strSpcPressed =
                "e = jQuery.Event('keyup',{keyCode:32,which:32});" +
                "$(passData).find('[name=loyalNum]').trigger(e);";
        webEngine.executeScript(strInsert + strSpcPressed);
    }

    @FXML
    protected void onKeyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.F6)
            onF6KeyPressed(e);
        else if (e.getCode() == KeyCode.F8)
            onF8KeyPressed(e);
        else if (e.getCode() == KeyCode.F9)
            onF9KeyPressed(e);
        else if (e.getCode() == KeyCode.F7)
            onF7KeyPressed(e);
    }

    protected void onF6KeyPressed(KeyEvent e) {
        Document doc = webEngine.getDocument();
        if (doc == null) {
            return;
        }

        Element elPassList = doc.getElementById("PassList");
        if (elPassList == null) {
            return;
        }

        if (frmClientSearchController == null) {
            try {
                FXMLLoader loader = new FXMLLoader(ClientSearchController.class.getResource("ClientSearch.fxml"));
                Parent root = (Parent) loader.load();

                frmClientSearchController = loader.getController();
                frmClientSearchController.setApp(this.app);
                frmClientSearchController.showFirstN(10);

                stageClientSearch = new Stage();
                stageClientSearch.setScene(new Scene(root));
                stageClientSearch.show();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else if (!stageClientSearch.isShowing()) {
            stageClientSearch.show();
        }
    }

    protected void onF8KeyPressed(KeyEvent e) {
        String cURL = PAYMENTFORMURI;
        webEngine.load(cURL);
    }

    protected void onF7KeyPressed(KeyEvent e) {
        System.out.println("Emulating refund");
        app.getQueue().add(new Refund(103696202, 112682398, strLtpaToken));
    }

    protected void onF9KeyPressed(KeyEvent e) {
        FXMLLoader loader = new FXMLLoader(PrefsController.class.getResource("Preferences.fxml"));
        try {
            Parent root = (Parent) loader.load();

            Stage stgPrefs = new Stage();
            stgPrefs.setScene(new Scene(root));
            stgPrefs.show();
        } catch (IOException e1) {
            System.err.println("Can't load preferences form");
            e1.printStackTrace();
        }
    }
}

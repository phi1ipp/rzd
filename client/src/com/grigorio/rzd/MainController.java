package com.grigorio.rzd;

import com.grigorio.rzd.preferences.PrefsController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import netscape.javascript.JSObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainController extends ScrollPane{
    @FXML
    TextField tfURL;
    @FXML
    Button btnBack;
    @FXML
    Button btnClients;
    @FXML
    Button btnSettings;
    @FXML
    ImageView imgBack;
    @FXML
    ImageView imgClients;
    @FXML
    ImageView imgSettings;

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

    private final String[] arLogonURI = {
            "https://pass.rzd.ru/ticket/logon/ru",
            "https://pass.rzd.ru/timetable/logon/ru",
            "https://rzd.ru/timetable/logon/ru"
    };

    private final String SELFSERVICELOGONURI = "https://pass.rzd.ru/ticket/logon/ru";
    private final String TIMETABLELOGONURI = "https://pass.rzd.ru/timetable/logon/ru";
    private final String SELFSERVICEURI = "https://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=5235&layer_id=5382";
    //TODO replace to a proper one page
    // url of bank confirmation form and success string
    private final String PAYMENTFORMURI="https://paygate.transcredit.ru/mpirun.jsp?action=mpi";
    //private final String PAYMENTFORMURI="file:///home/philipp/projects/rzd/resources/html/bank_response.html";
    private final String PASSFORMURI="http://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=735&layer_id=5374";
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

    public Stage getStageClientSearch() {
        return stageClientSearch;
    }

    private Stage stageClientSearch;
    private WebEngine webEngine;
    private Main app;

    private String strLtpaToken = null;

    public void setApp(Main application) {
        app = application;
    }


    @FXML
    /***
     * Initializes form
     */
    void initialize() {
        webEngine = fxWebView.getEngine();

        // on address change populate tfURL
        webEngine.documentProperty().addListener(new ChangeListener<Document>() {
            @Override
            public void changed(ObservableValue<? extends Document> observableValue, Document document, Document document2) {
                if (document2 != null)
                    tfURL.setText(document2.getDocumentURI());
            }
        });

        webEngine.documentProperty().addListener(new ChangeListener<Document>() {
            @Override
            public void changed(ObservableValue<? extends Document> observableValue, Document oldDoc, Document newDoc) {
                if (newDoc == null)
                    return;

                if (Arrays.asList(arLogonURI).contains(newDoc.getDocumentURI())) {
                    // if not loaded yet -> return
                    if (newDoc.getElementById("j_username") == null
                            || newDoc.getElementById("j_password") == null) {
                        return;
                    }

                    Preferences prefs = Preferences.userRoot().node("com.grigorio.rzd");
                    boolean bAutoFill = prefs.getBoolean(Main.Preferences.stridSSAutofill, false);
                    if (bAutoFill) {
                        fillInSSCredentials(prefs.get(Main.Preferences.stridSSUser, ""),
                                prefs.get(Main.Preferences.stridSSPassword, ""));
                    }

                }
            }
        });

        // on document load check URL to perform calls to Web-Service
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observableValue, Worker.State state,
                                Worker.State state2) {
                // button clients is disabled by default
                btnClients.disableProperty().setValue(true);
                if (state2 == Worker.State.SUCCEEDED) {
                    Document doc = webEngine.getDocument();
                    String strURI = doc.getDocumentURI();

                    if (PAYMENTFORMURI.equalsIgnoreCase(strURI)) {
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


                    } else if (doc.getDocumentURI().indexOf(PASSFORMURI) > -1) {
                        // form with passengers' data
                        // set focus
                        System.out.println("Pasразsenger data screen detected. Setting focus...");
                        String strSetFocusJS = "$('.j-pass-item.pass-item.trlist-pass__pass-item[data-index=0]')." +
                                "find(\"input[name='lastName']\").focus()";
                        webEngine.executeScript(strSetFocusJS);
                        btnClients.disableProperty().setValue(false);

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

    /**
     * Adds a passenger on a passenger data form
     * and fills in its details
     * @param cnt Contact to add
     */
    public void addContactData(Contact cnt) {
        String strInsert =
                String.format("  var passData = $('.pass-item.inactive')[0];" +
                                "var idx = $(passData).data('index');" +
                                "if (idx > 0 && idx < 4) {" +
                                "   $(passData).find('.pass_IU_PassInfo__addOrDel').click();" +
                                "   $(passData).find('[name=firstName]').val('%s');" +
                                "   $(passData).find('[name=lastName]').val('%s');" +
                                "   $(passData).find('[name=midName]').val('%s');" +
                                "   $(passData).find('[name=docType]').val(%s);" +
                                "   $(passData).find('[name=docNumber]').val('%s');" +
                                "   $(passData).find('[name=country]').val(%s);" +
                                "   $(passData).find('[name=gender]').val(%s);" +
                                "   $(passData).find('[name=birthplace]').val('%s');" +
                                "   $(passData).find('[name=birthdate]').val('%s');" +
                                "} else {" +
                                "    alert('Full set');" +
                                "}",
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
        showClientsForm();
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
        showSettingsForm();
    }

    @FXML
    protected void onBtnClientsClicked(ActionEvent e) {
        showClientsForm();
    }

    @FXML
    protected void onBtnSettingsClicked(ActionEvent e) {
        showSettingsForm();
    }

    @FXML
    protected void onBtnBackClicked(ActionEvent e) {
        webEngine.getHistory().go(-1);
    }

    /***
     * Shows client search form
     */
    private void showClientsForm() {
        Document doc = webEngine.getDocument();
        if (doc == null) {
            return;
        }

        Element elPassList = doc.getElementById("PassList");
        if (elPassList == null) {
            return;
        }

        if (frmClientSearchController == null) {
            fxWebView.getScene().setCursor(Cursor.WAIT);

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

    /***
     * Shows settings form
     */
    private void showSettingsForm() {
        FXMLLoader loader = new FXMLLoader(PrefsController.class.getResource("Preferences.fxml"));
        try {
            Parent root = (Parent) loader.load();

            Stage stgPrefs = new Stage();
            stgPrefs.setScene(new Scene(root));
            stgPrefs.setTitle("Settings");
            stgPrefs.show();
        } catch (IOException e1) {
            System.err.println("Can't load preferences form");
            e1.printStackTrace();
        }
    }

    /**
     * Fills in an auth form for RZD self-service
     * @param strUser
     * @param strPwd
     */
    private void fillInSSCredentials(String strUser, String strPwd) {
        if (strUser == null || strPwd == null) {
            System.err.println("Username or password can't be null");
            return;
        }
        String strJS =
                "$('#j_username').val('%s');" +
                "$('#j_password').val('%s');";
        webEngine.executeScript(String.format(strJS, strUser, strPwd));
    }
}

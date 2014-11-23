package com.grigorio.rzd;

import com.grigorio.rzd.Client.ClientSearchController;
import com.grigorio.rzd.Client.Contact;
import com.grigorio.rzd.OrderProcessor.Order;
import com.grigorio.rzd.OrderProcessor.Refund;
import com.grigorio.rzd.preferences.PrefsController;
import com.grigorio.rzd.search.TicketSearchController;
import com.grigorio.rzd.ticket.Ticket;
import com.grigorio.rzd.utils.Web;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
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
    Button btnSearch;
    @FXML
    ImageView imgBack;
    @FXML
    ImageView imgClients;
    @FXML
    ImageView imgSettings;

    private StringProperty authToken = new SimpleStringProperty();
    private Map<Long,Order> saleOrder = new HashMap<>();

    private ClientSearchController frmClientSearchController;
    private Stage stageClientSearch;
    private WebEngine webEngine;
    private Main app;

    public final String getAuthToken() {
        return authToken.getValue();
    }

    /**
     * Class used to intercept ajax calls from self service screen
     * it gets refund data from the call to initiate refund transaction
     */
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
                app.getQueue().add(new Refund(iOrderId, iTicketId, getAuthToken()));
            }
        }
    }

    /**
     * Class tied to a JS called on a passenger data confirmation screen
     * used to pull sale order data from the site (order and ticket ID/num)
     */
    public class JSAjaxProcessor {
        public void getTicketIDs(JSObject jsoJSON) {
            //clear sale order
            saleOrder.clear();

            JSObject orders = (JSObject) jsoJSON.getMember("orders");
            Integer iLen = (Integer) jsoJSON.getMember("orders_cnt");
            for (int i = 0; i < iLen; i++) {
                try {
                    JSObject order = (JSObject) orders.getSlot(i);
                    System.out.println("Processing order# " + order.getMember("orderId"));

                    JSObject tickets = (JSObject) order.getMember("tickets");
                    int jLen = (int) order.getMember("tickets_cnt");
                    List<Ticket> lstTicket = new ArrayList<>();
                    for (int j=0; j < jLen; j++) {
                        try {
                            JSObject ticket = (JSObject) tickets.getSlot(j);
                            System.out.println("Processing ticketId: " + ticket.getMember("ticketId") +
                                " with ticketNum: " + ticket.getMember("ticketNum"));
                            lstTicket.add(new Ticket(Long.parseLong(ticket.getMember("ticketId").toString()),
                                                    Long.parseLong(order.getMember("orderId").toString()),
                                                    new BigInteger(ticket.getMember("ticketNum").toString())));
                        } catch (JSException e) {
                            break;
                        }
                    }
                    saleOrder.put(Long.parseLong(order.getMember("orderId").toString()),
                            new Order(Integer.parseInt(order.getMember("orderId").toString()), lstTicket, authToken.getValue()));
                } catch (JSException e) {
                    break;
                }
            }
            System.out.println("Sale order: " + saleOrder.toString());
        }
    }

    private final String PASSFORMURI = "http://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=735&layer_id=5374";
    private final String PASSCONFIRMURI = "https://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=735&layer_id=5375";
    private final String SELFSERVICEURI = "https://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=5235&layer_id=5382";

    // url of bank confirmation form and success string
    //private final String PAYMENTFORMURI="https://paygate.transcredit.ru/mpirun.jsp?action=mpi";
    private final String PAYMENTFORMURI = "file:///home/philipp/projects/rzd/resources/html/bank_response.html";
    private final String PAYMENTSUCCESS="Операция завершена успешно";

    @FXML
    private WebView fxWebView;

    public Stage getStageClientSearch() {
        return stageClientSearch;
    }

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
        // autofill credentials
        webEngine.documentProperty().addListener(new ChangeListener<Document>() {
            @Override
            public void changed(ObservableValue<? extends Document> observableValue, Document oldDoc, Document newDoc) {
                Web.autoFillCredentials(newDoc, fxWebView.getEngine());

                if (newDoc != null)
                    tfURL.setText(newDoc.getDocumentURI());

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

                    /**
                     * Block for payment proccessing:
                     * Checks URL for order id and compares with the data got from rzd earlier(order/ticket id/num)
                     */
                    if (PAYMENTFORMURI.equalsIgnoreCase(strURI)) {
                        System.out.println("Got bank confirmation page. Parsing...");

                        if (doc.getElementsByTagName("h2").item(0).getTextContent()
                                .equalsIgnoreCase(PAYMENTSUCCESS)) {
                            System.out.println("Payment was successful! Getting parameters of transaction...");

                            // get form action attribute
                            String strFormAction =
                                    doc.getElementsByTagName("form").item(0)
                                            .getAttributes().getNamedItem("action").getTextContent();

                            // parse action url to get order_id
                            long lOrderId =
                                    Long.parseLong(
                                            strFormAction.substring(
                                                    strFormAction.indexOf("ORDER_ID=") + "ORDER_ID=".length()));

                            // put request in a queue if the authCookie set
                            if (getAuthToken() != null) {
                                // check sale order data against the data from the url
                                // and combine them to make a sale transaction call for a web service
                                // print error message otherwise
                                if (!saleOrder.containsKey(lOrderId)) {
                                    System.err.println("OrderID=" + lOrderId +
                                            "from the URL is not found in the sale order" + saleOrder);
                                }
//                                    app.getQueue().add(new Order(iOrderNum, getAuthToken()));
//                                    System.out.println("OrderId: " + iOrderNum + " placed in the queue w/o ticket numbers");

                                app.getQueue().add(
                                        new Order(lOrderId, saleOrder.get((long)lOrderId).getTickets(), getAuthToken()));
                                System.out.println("OrderId: " + lOrderId + " placed in the queue" +
                                        " with tickets=" + saleOrder.get(lOrderId).getTickets());
                            } else {
                                System.out.println("Cookie is not set, can't send an order for processing");
                            }
                        } else {
                            System.out.println("Payment wasn't successful");
                        }


                    } else if (SELFSERVICEURI.equalsIgnoreCase(doc.getDocumentURI())) {
                        // self service page, need to set up ajax send/open hooks
                        //
                        String strHook =
                                "(function(XHR) {" +
                                    "\"use strict\";" +
                                    "var open = XHR.prototype.open;" +
                                    "var send = XHR.prototype.send;" +
                                    "XHR.prototype.open = function(method, url, async, user, pass) {" +
                                        "this._url = url;" +
                                        "open.call(this, method, url, async, user, pass);};" +

                                    "XHR.prototype.send = function(data) {" +
                                    "if (this._url.search('PREVIEW')>0){" +
                                        "ajaxHook.refund(this._url);}" +
                                        "send.call(this, data);}" +
                                    "})(XMLHttpRequest);";

                        webEngine.executeScript(strHook);
                        JSObject window = (JSObject) webEngine.executeScript("window");
                        window.setMember("ajaxHook", new JSAjaxHook());


                    } else if (doc.getDocumentURI().indexOf(PASSFORMURI) > -1) {
                        // form with passengers' data
                        // set focus
                        System.out.println("Passenger data screen detected. Setting focus...");
                        String strSetFocusJS = "$('.j-pass-item.pass-item.trlist-pass__pass-item[data-index=0]')." +
                                "find(\"input[name='lastName']\").focus()";
                        webEngine.executeScript(strSetFocusJS);
                        btnClients.disableProperty().setValue(false);

                    } else if (doc.getDocumentURI().indexOf(PASSCONFIRMURI) > -1) {
                        // page with confirmations where we can get all the IDs for Order and Tickets
                        String strScript =
                                "$(document).ajaxComplete(" +
                                        "function(event, xhr, settings){" +
                                        "   var result={};" +
                                        "   var re=/number='(\\d+)'/;" +
                                        "" +
                                        "   var saleOrder = jQuery.parseJSON(xhr.responseText);" +
                                        "" +
                                            "var orders = saleOrder.orders;" +
                                            "result.orders_cnt = orders.length;" +
                                            "result.orders = [];" +
                                            "for (var i=0; i<orders.length; i++) {" +
                                                "var tickets = orders[i].tickets;" +
                                                "var res_tickets = [];" +
                                                "for (var j=0; j<tickets.length; j++) {" +
                                                    "var arr = re.exec(tickets[j].text);" +
                                                    "res_tickets.push({\"ticketId\" : tickets[j].ticketId, \"ticketNum\" : arr[1]});" +
                                                "}" +
                                                "result.orders.push({\"tickets_cnt\":res_tickets.length, \"tickets\":res_tickets," +
                                                                    "\"orderId\":orders[i].orderId});" +
                                            "}" +
                                        "ajaxProcessor.getTicketIDs(result);" +
                                        "});" +
                                        "" +
                                        "$.ajax({" +
                                        "url     : 'https://pass.rzd.ru/ticket/secure/ru?STRUCTURE_ID=735&layer_id=5378&refererLayerId=5375'," +
                                        "type    : 'POST'," +
                                        "data    : {rid:glbRID}" +
                                        "});";
                        webEngine.executeScript(strScript);
                        JSObject window = (JSObject) webEngine.executeScript("window");
                        window.setMember("ajaxProcessor", new JSAjaxProcessor());

                    } else {
                        String strCookie = (String) webEngine.executeScript("document.cookie;");

                        // if required cookie set -> save it
                        if (strCookie.indexOf("LtpaToken2") > 0) {
                            authToken.setValue(strCookie.substring(strCookie.indexOf("LtpaToken2=") + "LtpaToken2".length() + 1,
                                    strCookie.indexOf(";", strCookie.indexOf("LtpaToken2"))));
                        }
                    }

                }
            }
        });

        webEngine.load("http://www.rzd.ru");
    }

    // fills in Contact data into currently active contact fields
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

        String strInsurance =
                Preferences.userRoot().node("com.grigorio.rzd").getBoolean(Main.Preferences.stridNoInsurance, false) ?
                    "$(passData).find('input[name=\"insCheck\"]').click();" : "";

        webEngine.executeScript(strInsert + strSpcPressed + strInsurance);
    }

    /**
     * Adds a passenger on a passenger data form
     * and fills in its details
     * @param cnt Contact to add
     */
    public void addContactData(Contact cnt) {
        String strPassDataDefine =
                "var passData;" +
                "var passDataPrev = $('.pass-item').not('.inactive').last();" +
                "if ($(passDataPrev).find('input[name=lastName]').val() != '') {" +
                "   passData = $('.pass-item.inactive').first();" +
                "   var idx = $(passData).data('index');" +
                "   if (idx > 0 && idx < 4) {" +
                "       $(passData).find('.pass_IU_PassInfo__addOrDel').click();" +
                "   } else {" +
                "       alert('Full set');" +
                "   }" +
                "} else {" +
                "passData = passDataPrev;" +
                "}";
        String strInsert =
                String.format(  "$(passData).find('[name=firstName]').val('%s');" +
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

        String strInsurance =
                Preferences.userRoot().node("com.grigorio.rzd").getBoolean(Main.Preferences.stridNoInsurance, false) ?
                        "$(passData).find('input[name=\"insCheck\"]').click();" : "";

        webEngine.executeScript(strPassDataDefine + strInsert + strSpcPressed + strInsurance);
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

        saleOrder.clear();
        List<Ticket> lstTickets = new ArrayList<>();
        lstTickets.add (new Ticket(115981079, 107011422, new BigInteger("70050481514552")));
        lstTickets.add (new Ticket(115981080, 107011422, new BigInteger("70050481514563")));
        lstTickets.add (new Ticket(115981081, 107011422, new BigInteger("70050481514574")));
        lstTickets.add (new Ticket(115981082, 107011422, new BigInteger("70050481514585")));
        saleOrder.put(107011422L, new Order(107011422, lstTickets, getAuthToken()));

        webEngine.load(cURL);
    }

    protected void onF7KeyPressed(KeyEvent e) {
        System.out.println("Emulating refund");
        app.getQueue().add(new Refund(103696202, 112682398, getAuthToken()));
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

    @FXML
    protected void onBtnSearchClicked(ActionEvent e) {
        showTicketSearchForm();
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
            try {
                FXMLLoader loader = new FXMLLoader(ClientSearchController.class.getResource("ClientSearch.fxml"));
                Parent root = loader.load();

                frmClientSearchController = loader.getController();
                frmClientSearchController.setApp(this.app);
                frmClientSearchController.showFirstN(10);

                stageClientSearch = new Stage();
                stageClientSearch.setScene(new Scene(root));
                stageClientSearch.setTitle("Client list");
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
            Parent root = loader.load();

            Stage stgPrefs = new Stage();
            stgPrefs.setScene(new Scene(root));
            stgPrefs.setTitle("Settings");
            stgPrefs.show();
        } catch (IOException e1) {
            System.err.println("Can't load preferences form");
            e1.printStackTrace();
        }
    }

    private void showTicketSearchForm() {
        FXMLLoader loader = new FXMLLoader(TicketSearchController.class.getResource("TicketSearch.fxml"));
        try {
            Parent root = loader.load();

            Stage stgTicketSearch = new Stage();
            stgTicketSearch.setScene(new Scene(root));
            stgTicketSearch.setTitle("Поиск проданных билетов");

            stgTicketSearch.show();
        } catch (Exception e) {
            System.err.println("Can't load ticket search form");
            e.printStackTrace();
        }
    }
}

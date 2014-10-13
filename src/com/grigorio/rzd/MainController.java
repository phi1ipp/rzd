package com.grigorio.rzd;

import com.grigorio.rzd.preferences.PrefsController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class MainController extends ScrollPane{
    //TODO add close SearchForm on close of this


    // ltpaToken
    // TODO read token from cookies
    private final String LTPA = "OAGiNMmHc0hXovUBO/rzZUkJabzT7QOSsVT235tKht3lrFBiCMw0p5B0npt5ynRH6qQ/RJ8f4KpAJBww9h7P9apm+t5Ndtv/d9HLnFqB0L3bzJ74m2Et7mDOY1lE/yb0E8VUtklXvIO74I0SDzoJFHOr3CsVhGvLGyREKmiyy/72rLwccWrf62GL9MqNX4TSAPqmlRl6qn4VR6sCR5BqHeoZJK4oIFJfmXvN3JvDe2sBSYodAE9tpAjTE8ZIF72GCIgEbaDYqehgbYO0eD6iLz1astN3dfZSINPuLlDmpD6F/sRqhLCJAz7zO50+3LbQDMFLSF0sYMFbfQE7npeYoA4RJU8CIa+E0mi1koBjBYRZMUUjkV3MSGAybUFMzQ3oiDEBlBs34EMb1UCaBXEWNu+JuEaN+/ny3fHal5+k22/4+iebkasqRGzbATKEQt+d1yRUN5Xn0Au7ScqQWGhjJNODTuIDthUeVWmBUiB3U0rvmzyHcUOQEx20iKWYx1Nij6woBP4gJ3IXmBj3oPI+5cvcw42vS9EhihVv8OYDYZ5jUbgs5143TGTONiCGiulXsEFq8UXoXzyYDGm44bA8NBZ8RmqbWuIOD8oJrbAUyUku1YXt4+852waxIJjkN7jd";

    // url of bank confirmation form and success string
    //private final String PAYMENTFORMURI="https://paygate.transcredit.ru/mpirun.jsp?action=mpi";
    private final String PAYMENTFORMURI="file:///home/philipp/projects/rzd/resources/html/bank_response.html";
    private final String PAYMENTSUCCESS="Операция завершена успешно";

    @FXML
    private WebView fxWebView;

    private ClientSearchController frmClientSearchController = null;
    private Stage stageClientSearch;
    private WebEngine webEngine;
    private Main app;

    private int iPassCount = 0;
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
                    System.out.println("Loading finished");

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
        String cURL = "file:///home/philipp/projects/rzd/resources/html/bank_response.html";
        webEngine.load(cURL);
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

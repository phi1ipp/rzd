package com.grigorio.rzd;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

import java.io.IOException;


public class MainController extends ScrollPane{
    //TODO add close SearchForm on close of this

    // url of bank confirmation form and success string
    private final String PAYMENTFORMURI="https://paygate.transcredit.ru/mpirun.jsp?action=mpi";
    private final String PAYMENTSUCCESS="Операция завершена успешно";

    @FXML
    private WebView fxWebView;

    private ClientSearchController frmClientSearchController = null;
    private Stage stageClientSearch;
    private WebEngine webEngine;
    private Main app;

    private int iPassCount = 0;

    public void setApp(Main application) {
        app = application;
    }

    @FXML
    void initialize() {

        webEngine = fxWebView.getEngine();
        webEngine.documentProperty().addListener(new ChangeListener<Document>() {
            @Override
            public void changed(ObservableValue<? extends Document> observableValue, Document document, Document document2) {
                if (document2 != null) {
                    String strURI = document2.getDocumentURI();

                    if (PAYMENTFORMURI.equalsIgnoreCase(strURI)) {
                        //TODO create file to start processing
                        System.out.println("Got bank confirmation page. Parsing...");
                        if (document2.getElementsByTagName("h2").item(0).getTextContent()
                                .equalsIgnoreCase(PAYMENTSUCCESS)) {
                            System.out.println("Payment was successful! Getting parameters of transaction...");

                            // get form action attribute
                            String strFormAction =
                                    document2.getElementsByTagName("form").item(0)
                                            .getAttributes().getNamedItem("action").getTextContent();
                            // parse action url to get order_id
                            int iOrderNum =
                                    Integer.parseInt(
                                            strFormAction.substring(
                                                    strFormAction.indexOf("ORDER_ID=") + "ORDER_ID=".length()));
                            // put request in a queue
                            app.getQueue().add(new Order(iOrderNum));
                            System.out.println("OrderId: " + iOrderNum + " placed in a queue for processing");
                        } else {
                            System.out.println("Payment wasn't successful");
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
    protected void onF6KeyPressed(KeyEvent e) {
        if (e.getCode() != KeyCode.F6) {
            return;
        }

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

    @FXML
    protected void onF8KeyPressed(KeyEvent e) {
        if (e.getCode() != KeyCode.F8) {
            return;
        }

        webEngine.loadContent("file://");
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

}

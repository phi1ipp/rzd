package com.grigorio.rzd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;


public class MainController extends ScrollPane{
    @FXML
    private WebView fxWebView;
    private WebEngine webEngine;

    @FXML
    void initialize() {
        webEngine = fxWebView.getEngine();
        webEngine.load("http://www.rzd.ru");
    }

    @FXML
    protected void onMouseClick(MouseEvent e) {
        Document doc = webEngine.getDocument();
        if (doc == null) {
            return;
        }

        Element elPassList = doc.getElementById("PassList");
        if (elPassList == null) {
            return;
        }

        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(ClientSearchController.class.getResource("ClientSearch.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}

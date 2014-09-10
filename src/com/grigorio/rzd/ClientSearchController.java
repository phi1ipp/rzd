package com.grigorio.rzd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by Philipp on 9/9/2014.
 */
public class ClientSearchController extends Pane {
    @FXML
    private Button btnExit;
    @FXML
    private TextField txtLastName;

    @FXML
    protected void btnExitClicked(ActionEvent e) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }
}

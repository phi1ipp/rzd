package com.grigorio.rzd;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Created by Philipp on 9/9/2014.
 */
public class ClientSearchController extends Pane {
    private Main app;
    private ObservableList<Contact> lstContacts = FXCollections.observableArrayList();

    @FXML
    private Button btnExit;
    @FXML
    private Button btnInsert;
    @FXML
    private TextField txtLastName;
    @FXML
    private TableView<Contact> tblView;
    @FXML
    private TableColumn<Contact, String> tbcFirstName;
    @FXML
    private TableColumn<Contact, String> tbcLastName;
    @FXML
    private TableColumn<Contact, String> tbcMiddleName;
    @FXML
    private TableColumn<Contact, String> tbcDocNumber;

    @FXML
    void initialize() {
        // init table view control
        tbcFirstName.setCellValueFactory(new PropertyValueFactory<Contact, String>("firstName"));
        tbcLastName.setCellValueFactory(new PropertyValueFactory<Contact, String>("lastName"));
        tbcMiddleName.setCellValueFactory(new PropertyValueFactory<Contact, String>("middleName"));
        tbcDocNumber.setCellValueFactory(new PropertyValueFactory<Contact, String>("documentNumber"));

        tblView.setItems(lstContacts);

        // set a listener for text field changes
        txtLastName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue,
                                String strOldValue, String strNewValue) {
                DBHelper dbHelper = app.getDbHelper();
                if (!dbHelper.isOpened() && !dbHelper.openConnection()) {
                    System.err.println("Can't open connection to DB");
                    return;
                }
                // set the list to a filtered one
                lstContacts.setAll(dbHelper.getPassengerList(txtLastName.getText()));
            }
        });
    }

    @FXML
    protected void btnExitClicked(ActionEvent e) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void btnInsertClicked(ActionEvent e) {
        Contact cnt = tblView.getSelectionModel().getSelectedItem();

        app.getMainScreen().insertContactData(cnt);
    }
    public void setApp(Main application) {
        app = application;
    }
}

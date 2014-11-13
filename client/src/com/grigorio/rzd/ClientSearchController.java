package com.grigorio.rzd;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
    private CheckBox cbAddReplace;

    @FXML
    void initialize() {
        // init table view control
        tbcFirstName.setCellValueFactory(new PropertyValueFactory<Contact, String>("firstName"));
        tbcLastName.setCellValueFactory(new PropertyValueFactory<Contact, String>("lastName"));
        tbcMiddleName.setCellValueFactory(new PropertyValueFactory<Contact, String>("middleName"));
        tbcDocNumber.setCellValueFactory(new PropertyValueFactory<Contact, String>("documentNumber"));

        tblView.setItems(lstContacts);

        tblView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        insertCurrentContactIntoWebview();
                    }
                }
            }
        });

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
        insertCurrentContactIntoWebview();
    }

    public void setApp(Main application) {
        app = application;
    }

    public void showFirstN(int iN) {
        lstContacts.setAll(app.getDbHelper().getFirstN(iN));
    }

    private void insertCurrentContactIntoWebview() {
        Contact cnt = tblView.getSelectionModel().getSelectedItem();

        if (cbAddReplace.isSelected()) {
            app.getMainScreen().addContactData(cnt);
        } else {
            app.getMainScreen().insertContactData(cnt);
        }
    }
}

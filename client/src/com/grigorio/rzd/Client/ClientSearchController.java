package com.grigorio.rzd.Client;

import com.grigorio.rzd.DBHelper;
import com.grigorio.rzd.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.logging.Logger;

/**
 * Client search controller
 */
public class ClientSearchController extends Pane {
    private final Logger logger = Logger.getLogger(ClientSearchController.class.getName());
    private Main app;
    private ObservableList<Contact> lstContacts = FXCollections.observableArrayList();

    @FXML
    private TextField txtLastName;
    @FXML
    private CheckBox cbAddReplace;

    // table section
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
    private TableColumn<Contact, String> tbcCompany;

    @FXML
    void initialize() {
        // init table view control
        tbcFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tbcLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tbcMiddleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        tbcDocNumber.setCellValueFactory(new PropertyValueFactory<>("documentNumber"));
        tbcCompany.setCellValueFactory(new PropertyValueFactory<>("company"));

        tblView.setItems(lstContacts);

        tblView.setOnMouseClicked( mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    insertCurrentContactIntoWebview();
                }
            }
        });

        // set a listener for text field changes
        txtLastName.textProperty().addListener( (observableValue, strOldValue, strNewValue) -> {
            DBHelper dbHelper = app.getDbHelper();
            if (!dbHelper.isOpened() && !dbHelper.openConnection()) {
                logger.severe("Can't open connection to DB");
                return;
            }
            // set the list to a filtered one
            lstContacts.setAll(dbHelper.getPassengerList(txtLastName.getText()));
        });
    }

    @FXML
    protected void btnExitClicked(ActionEvent e) {
        closeForm();
    }

    @FXML
    protected void btnInsertClicked(ActionEvent e) {
        insertCurrentContactIntoWebview();
    }

    @FXML
    protected void onKeyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ESCAPE) {
            closeForm();
        }
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

    private void closeForm() {
        Stage stage = (Stage) tblView.getScene().getWindow();
        stage.close();
    }
}

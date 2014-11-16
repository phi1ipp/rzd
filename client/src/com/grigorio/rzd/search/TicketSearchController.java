package com.grigorio.rzd.search;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by philipp on 11/14/14.
 */
public class TicketSearchController {
    @FXML
    private TextField tfTicketNum;
    @FXML
    private TextField tfCompanyName;
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfFrom;
    @FXML
    private TextField tfTo;
    @FXML
    private DatePicker dpFrom;
    @FXML
    private DatePicker dpTo;

    // table controls
    @FXML
    private TableView<TicketSearchRecord> tvTicketList;
    @FXML
    private TableColumn<TicketSearchRecord, String> tcTicketNum;
    @FXML
    private TableColumn<TicketSearchRecord, String> tcLastName;
    @FXML
    private TableColumn<TicketSearchRecord, String> tcFrom;
    @FXML
    private TableColumn<TicketSearchRecord, String> tcTo;
    @FXML
    private TableColumn<TicketSearchRecord, String> tcTripDate;
    @FXML
    private TableColumn<TicketSearchRecord, String> tcCompany;


    @FXML
    private Button btnSearch;
    @FXML
    private Button btnExit;

    @FXML
    private RadioButton rbAll;

    private ObservableList<TicketSearchRecord> lstTickets = FXCollections.observableArrayList();
    public ObservableList<TicketSearchRecord> getLstTickets() {
        return lstTickets;
    }

    @FXML
    protected void initialize() {
        tcTicketNum.setCellValueFactory(new PropertyValueFactory<TicketSearchRecord, String>("strTicketNum"));
        tcLastName.setCellValueFactory(new PropertyValueFactory<TicketSearchRecord, String>("strLastName"));
        tcFrom.setCellValueFactory(new PropertyValueFactory<TicketSearchRecord, String>("strFrom"));
        tcTo.setCellValueFactory(new PropertyValueFactory<TicketSearchRecord, String>("strTo"));
        tcTripDate.setCellValueFactory(new PropertyValueFactory<TicketSearchRecord, String>("strTripDate"));
        tcCompany.setCellValueFactory(new PropertyValueFactory<TicketSearchRecord, String>("strCompany"));

        dpFrom.setValue(LocalDate.now());

        tfFrom.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String strOld, String strNew) {
                if (strNew != null && strNew.equalsIgnoreCase("XX")) {
                    Popup popup = new Popup();
                    popup.setX(100);
                    popup.setY(200);

                    popup.getContent().addAll(new Label("Popup"));

                    popup.show(btnExit.getScene().getWindow());
                }
            }
        });
        tvTicketList.setItems(lstTickets);
    }

    @FXML
    protected void btnExitClicked(ActionEvent e) {
        btnExit.getScene().getWindow().hide();
    }

    @FXML
    protected void btnSearchClicked(ActionEvent ev) {
        Map<String,Object> mapParams = new HashMap<String,Object>();

        if (tfTicketNum.getText() != null && tfTicketNum.getText().length() > 0)
            mapParams.put(TicketSearchConstants.strTicketNum, tfTicketNum.getText());

        if (tfFrom.getText() != null && tfFrom.getText().length() > 0)
            mapParams.put(TicketSearchConstants.strStationFrom, tfFrom.getText());

        if (tfTo.getText() != null && tfTo.getText().length() > 0)
            mapParams.put(TicketSearchConstants.strStationTo, tfTo.getText());

        if (tfLastName.getText() != null && tfLastName.getText().length() > 0)
            mapParams.put(TicketSearchConstants.strLastName, tfLastName.getText());

        if (tfCompanyName.getText() != null && tfCompanyName.getText().length() > 0)
            mapParams.put(TicketSearchConstants.strCompany, tfCompanyName.getText());

        if (dpFrom.getValue() != null)
            mapParams.put(TicketSearchConstants.strDeptFrom, dpFrom.getValue());

        if (dpTo.getValue() != null)
            mapParams.put(TicketSearchConstants.strDeptTo, dpTo.getValue());

        mapParams.put(TicketSearchConstants.strAllCriteria, rbAll.isSelected());
        System.out.println(mapParams.toString());

        TicketSearchTask task = new TicketSearchTask(mapParams);
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (task.getValue().size() > 0) {
                    lstTickets.setAll(task.getValue());
                } else {
                    Stage stg = new Stage();

                    VBox popup = new VBox();

                    popup.setPrefWidth(200);
                    popup.setPrefHeight(75);
                    popup.setAlignment(Pos.CENTER);
                    popup.setSpacing(5);
                    popup.setPadding(new Insets(10));

                    Button btnOK = new Button("OK");
                    btnOK.setCancelButton(true);
                    btnOK.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            stg.hide();
                        }
                    });
                    popup.getChildren().addAll(new Label("Nothing found"), btnOK);

                    stg.setScene(new Scene(popup));
                    stg.show();
                }
            }
        });
        new Thread(task).start();

        System.out.println(mapParams.toString());
    }
}

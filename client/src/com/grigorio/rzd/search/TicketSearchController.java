package com.grigorio.rzd.search;

import com.grigorio.rzd.ticket.Ticket;
import com.grigorio.rzd.ticket.TicketViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
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
    private Button btnExit;

    @FXML
    private RadioButton rbAll;
    
    private ObservableList<TicketSearchRecord> lstTickets = FXCollections.observableArrayList();
    public ObservableList<TicketSearchRecord> getLstTickets() {
        return lstTickets;
    }

    @FXML
    protected void initialize() {
        tcTicketNum.setCellValueFactory(new PropertyValueFactory<>("strTicketNum"));
        tcLastName.setCellValueFactory(new PropertyValueFactory<>("strLastName"));
        tcFrom.setCellValueFactory(new PropertyValueFactory<>("strFrom"));
        tcTo.setCellValueFactory(new PropertyValueFactory<>("strTo"));
        tcTripDate.setCellValueFactory(new PropertyValueFactory<>("strTripDate"));
        tcCompany.setCellValueFactory(new PropertyValueFactory<>("strCompany"));

        dpFrom.setValue(LocalDate.now());

        tfFrom.textProperty().addListener( (observableValue, strOld, strNew) -> {
            if (strNew != null && strNew.equalsIgnoreCase("XX")) {
                Popup popup = new Popup();
                popup.setX(100);
                popup.setY(200);

                popup.getContent().addAll(new Label("Popup"));

                popup.show(btnExit.getScene().getWindow());
            }
        });

        tvTicketList.setOnMouseClicked( (event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() > 1) {
                TicketSearchRecord ticketRecord = tvTicketList.getSelectionModel().getSelectedItem();

                try {
                    FXMLLoader loader = new FXMLLoader(TicketViewController.class.getResource("TicketView.fxml"));
                    Parent parent = loader.load();

                    TicketViewController controller = loader.getController();
                    controller.setTicket(new Ticket(ticketRecord.getlTicketId(), ticketRecord.getlOrderId()));

                    Scene scene = new Scene(parent);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("Билет - " + ticketRecord.getStrTicketNum());
                    stage.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
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
        Map<String,Object> mapParams = new HashMap<>();

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
        task.setOnSucceeded(event -> {
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
                btnOK.setOnAction(event1 -> stg.hide());
                popup.getChildren().addAll(new Label("Ничего не найдено"), btnOK);

                stg.setScene(new Scene(popup));
                stg.show();
            }
        });
        new Thread(task).start();

        System.out.println(mapParams.toString());
    }
}

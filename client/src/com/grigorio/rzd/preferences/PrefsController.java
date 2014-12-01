package com.grigorio.rzd.preferences;

import com.grigorio.rzd.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

/**
 * Application preferences controller
 */
public class PrefsController {
    @FXML
    private TextField tfDBLoc;
    @FXML
    private TextField tfOutputDir;
    @FXML
    private TextField tfPrivKeyLoc;
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPrivKeyPwd;

    //RZD self-service section
    @FXML
    private CheckBox cbAutoFillSelfServiceCreds;
    @FXML
    private TextField tfSSUser;
    @FXML
    private PasswordField pfSSPassword;
    @FXML
    private CheckBox cbNoInsurance;

    // Logging section
    @FXML
    private TextField tfLogFileName;
    @FXML
    private ChoiceBox<String> chbLogLevel;

    // preferences
    final private Preferences prefs = Preferences.userRoot().node("com.grigorio.rzd");
    final private Logger logger = Logger.getLogger(PrefsController.class.getName());
    private Main app;

    @FXML
    void initialize() {
        tfDBLoc.setText(prefs.get(Main.Preferences.stridDBLoc, "."));
        tfOutputDir.setText(prefs.get(Main.Preferences.stridOutDir,"."));
        tfPrivKeyLoc.setText(prefs.get(Main.Preferences.stridPrivKeyLoc,"."));
        pfPrivKeyPwd.setText(prefs.get(Main.Preferences.stridPrivKeyPwd,""));
        tfUsername.setText(prefs.get(Main.Preferences.stridUsername,""));

        tfSSUser.setText(prefs.get(Main.Preferences.stridSSUser,""));
        pfSSPassword.setText(prefs.get(Main.Preferences.stridSSPassword,""));

        cbNoInsurance.setSelected(prefs.getBoolean(Main.Preferences.stridNoInsurance, false));

        cbAutoFillSelfServiceCreds.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean bOldVal, Boolean bNewVal) {
                tfSSUser.disableProperty().setValue(!bNewVal);
                pfSSPassword.disableProperty().setValue(!bNewVal);
            }
        });

        cbAutoFillSelfServiceCreds.setSelected(prefs.getBoolean(Main.Preferences.stridSSAutofill, false));

        // Log section
        //chbLogLevel.setItems(FXCollections.observableArrayList(app.mapLogLevels.keySet()));
        chbLogLevel.setItems(
                FXCollections.observableArrayList(
                        Level.OFF.toString(), Level.SEVERE.toString(), Level.WARNING.toString(),
                        Level.INFO.toString(), Level.CONFIG.toString(), Level.FINE.toString(),
                        Level.FINER.toString(), Level.FINEST.toString(), Level.ALL.toString()));
        chbLogLevel.getSelectionModel().select(prefs.get(Main.Preferences.strLogLevel, Level.SEVERE.toString()));

        tfLogFileName.setText(prefs.get(Main.Preferences.strLogFileName, "ticket.log"));
    }

    @FXML
    protected void btnSaveClicked(ActionEvent e) {
        // save preferences
        prefs.put(Main.Preferences.stridPrivKeyLoc, tfPrivKeyLoc.getText());
        prefs.put(Main.Preferences.stridPrivKeyPwd, pfPrivKeyPwd.getText());
        prefs.put(Main.Preferences.stridDBLoc, tfDBLoc.getText());
        prefs.put(Main.Preferences.stridOutDir, tfOutputDir.getText());
        prefs.put(Main.Preferences.stridUsername, tfUsername.getText());

        prefs.putBoolean(Main.Preferences.stridSSAutofill, cbAutoFillSelfServiceCreds.isSelected());
        prefs.put(Main.Preferences.stridSSUser, tfSSUser.getText());
        prefs.put(Main.Preferences.stridSSPassword, pfSSPassword.getText());

        prefs.putBoolean(Main.Preferences.stridNoInsurance, cbNoInsurance.isSelected());

        prefs.put(Main.Preferences.strLogLevel, chbLogLevel.getSelectionModel().getSelectedItem());
        prefs.put(Main.Preferences.strLogFileName, tfLogFileName.getText());

        if (app.getLogger().getLevel() !=
                Main.mapLogLevels.get(chbLogLevel.getSelectionModel().getSelectedItem())) {
            app.getLogger().setLevel(Main.mapLogLevels.get(chbLogLevel.getSelectionModel().getSelectedItem()));
            logger.config("Logging level changed to " + chbLogLevel.getSelectionModel().getSelectedItem());
        }

        // close form
        Stage stg = (Stage) tfPrivKeyLoc.getScene().getWindow();
        stg.close();
    }

    @FXML
    protected void btnCancelClicked(ActionEvent e) {
        // close form
        Stage stg = (Stage) tfPrivKeyLoc.getScene().getWindow();
        stg.close();
    }

    @FXML
    protected void btnBrowseDBLocClicked(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Database", "*.db"));
        fileChooser.setTitle("Set DB location");
        File dirName;
        if ((dirName = fileChooser.showOpenDialog(tfDBLoc.getScene().getWindow())) != null) {
            tfDBLoc.setText(dirName.getPath());
        }
    }

    @FXML
    protected void btnBrowseOutDirClicked(ActionEvent e) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Choose output directory");
        File dirName;
        if ((dirName = dirChooser.showDialog(tfOutputDir.getScene().getWindow())) != null) {
            tfOutputDir.setText(dirName.getPath());
        }
    }

    @FXML
    protected void btnBrowsePrivKeyLocClicked(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose private key location");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Private Key (PKCS8)", "*.pk8"));
        File fileName;
        if ((fileName = fileChooser.showOpenDialog(tfPrivKeyLoc.getScene().getWindow())) != null) {
            tfPrivKeyLoc.setText(fileName.getPath());
        }
    }

    @FXML
    protected void bntBrowseLogFileNameClicked(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose log file name");

        File fileName;
        if ((fileName = fileChooser.showOpenDialog(tfLogFileName.getScene().getWindow())) != null) {
            tfLogFileName.setText(fileName.getPath());
        }
    }

    public void setApp(Main anApp) {
        app = anApp;
    }
}

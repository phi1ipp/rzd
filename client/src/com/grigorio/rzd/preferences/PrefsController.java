package com.grigorio.rzd.preferences;

import com.grigorio.rzd.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.prefs.Preferences;

/**
 * Created by philipp on 10/12/14.
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
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnBrowseDBLoc;
    @FXML
    private Button btnBrowseOutDir;
    @FXML
    private Button btnBrowsePrivKeyLoc;

    //RZD self-service section
    @FXML
    private CheckBox cbAutoFillSelfServiceCreds;
    @FXML
    private TextField tfSSUser;
    @FXML
    private PasswordField pfSSPassword;

    // preferences
    final Preferences prefs = Preferences.userRoot().node("com.grigorio.rzd");

    @FXML
    void initialize() {
        tfDBLoc.setText(prefs.get(Main.Preferences.stridDBLoc, "."));
        tfOutputDir.setText(prefs.get(Main.Preferences.stridOutDir,"."));
        tfPrivKeyLoc.setText(prefs.get(Main.Preferences.stridPrivKeyLoc,"."));
        pfPrivKeyPwd.setText(prefs.get(Main.Preferences.stridPrivKeyPwd,""));
        tfUsername.setText(prefs.get(Main.Preferences.stridUsername,""));

        tfSSUser.setText(prefs.get(Main.Preferences.stridSSUser,""));
        pfSSPassword.setText(prefs.get(Main.Preferences.stridSSPassword,""));

        cbAutoFillSelfServiceCreds.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean bOldVal, Boolean bNewVal) {
                tfSSUser.disableProperty().setValue(!bNewVal);
                pfSSPassword.disableProperty().setValue(!bNewVal);
            }
        });

        cbAutoFillSelfServiceCreds.setSelected(prefs.getBoolean(Main.Preferences.stridSSAutofill, false));
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

        // close form
        Stage stg = (Stage) btnSave.getScene().getWindow();
        stg.close();
    }

    @FXML
    protected void btnCancelClicked(ActionEvent e) {
        // close form
        Stage stg = (Stage) btnCancel.getScene().getWindow();
        stg.close();
    }

    @FXML
    protected void btnBrowseDBLocClicked(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Database", "*.db"));
        fileChooser.setTitle("Set DB location");
        File dirName = null;
        if ((dirName = fileChooser.showOpenDialog(btnBrowseDBLoc.getScene().getWindow())) != null) {
            tfDBLoc.setText(dirName.getPath());
        };
    }

    @FXML
    protected void btnBrowseOutDirClicked(ActionEvent e) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Choose output directory");
        File dirName = null;
        if ((dirName = dirChooser.showDialog(btnBrowseOutDir.getScene().getWindow())) != null) {
            tfOutputDir.setText(dirName.getPath());
        };
    }

    @FXML
    protected void btnBrowsePrivKeyLocClicked(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose private key location");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Private Key (PKCS8)", "*.pk8"));
        File fileName = null;
        if ((fileName = fileChooser.showOpenDialog(btnBrowsePrivKeyLoc.getScene().getWindow())) != null) {
            tfPrivKeyLoc.setText(fileName.getPath());
        };
    }
}

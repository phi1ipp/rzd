package com.grigorio.rzd.utils;

import com.grigorio.rzd.Main;
import javafx.scene.web.WebEngine;
import org.w3c.dom.Document;

import java.util.Arrays;
import java.util.prefs.Preferences;

/**
 * Created by philipp on 11/19/14.
 */
public class Web {
    // URLs to use for auto-login
    private static final
    String[] arLogonURI = {
            "https://pass.rzd.ru/ticket/logon/ru",
            "https://pass.rzd.ru/timetable/logon/ru",
            "https://rzd.ru/timetable/logon/ru"
    };

    public static void autoFillCredentials(Document newDoc, WebEngine webEngine) {
        if (newDoc == null)
            return;

        if (Arrays.asList(arLogonURI).contains(newDoc.getDocumentURI())) {
            // if not loaded yet -> return
            if (newDoc.getElementById("j_username") == null
                    || newDoc.getElementById("j_password") == null) {
                return;
            }

            Preferences prefs = Preferences.userRoot().node("com.grigorio.rzd");
            boolean bAutoFill = prefs.getBoolean(Main.Preferences.stridSSAutofill, false);
            if (bAutoFill) {
                fillInSSCredentials(prefs.get(Main.Preferences.stridSSUser, ""),
                        prefs.get(Main.Preferences.stridSSPassword, ""), webEngine);
            }

        }
    }

    /**
     * Fills in an auth form for RZD self-service URL for a given WebEngine
     * @param strUser
     * @param strPwd
     */
    private static void fillInSSCredentials(String strUser, String strPwd, WebEngine webEngine) {
        if (strUser == null || strPwd == null) {
            System.err.println("Username or password can't be null");
            return;
        }
        String strJS =
                "$('#j_username').val('%s');" +
                        "$('#j_password').val('%s');";
        webEngine.executeScript(String.format(strJS, strUser, strPwd));
    }
}

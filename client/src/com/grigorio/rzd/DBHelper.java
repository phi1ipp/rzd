package com.grigorio.rzd;

import com.grigorio.rzd.Client.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

/**
 * Created by Philipp Grigoryev on 9/9/2014.
 */
public class DBHelper {
    private final static String TAG = DBHelper.class.getName();
    private final Logger logger = Logger.getLogger(DBHelper.class.getName());
    private Connection conn = null;
    private PreparedStatement stmtFiltered = null;
    private PreparedStatement stmtCountryName = null;
    private PreparedStatement stmtCountryCode = null;

    private final String strSelect =
            "select *, strftime('%d.%m.%Y',birthdate) as bdate, " +
            "countries.val as cntry_code, customers.name as company " +
            "from contacts inner join countries on countries.country_key = contacts.doccountry_key " +
                    "left outer join customers on contacts.cust_key = customers.cust_key";

    protected void init() throws Exception {
        Class.forName("org.sqlite.JDBC");
    }

    public boolean openConnection() {
        try {
            init();
            String strDBName = Preferences.userRoot().node("com.grigorio.rzd").get(Main.Preferences.stridDBLoc,"");
            conn = DriverManager.getConnection("jdbc:sqlite:" + strDBName);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Can't open connection to DB", e);
            return false;
        }

        try {
            stmtFiltered = conn.prepareStatement(strSelect + " where lastname like ?");
            stmtCountryName = conn.prepareStatement("select name from countries where val=?");
            stmtCountryCode = conn.prepareStatement("select val from countries where name=?");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Can't prepare statements", e);
            return false;
        }

        return true;
    }

    public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error closing DB", e);
        }
    }

    public boolean isOpened() {
        return conn != null;
    }

    public List<Contact> getPassengerList(String strLastNameFilter) {
        logger.entering(TAG, "getPassengerList", strLastNameFilter);
        List lstRes = new ArrayList();

        if (strLastNameFilter == null) {
            logger.warning("filter string is null");
            return lstRes;
        }

        if (stmtFiltered == null) {
            logger.warning("stmtFiltered is null");
            return lstRes;
        }

        try {
            stmtFiltered.setString(1, strLastNameFilter + "%");

            ResultSet rs = stmtFiltered.executeQuery();
            while (rs.next()) {
                Contact cnt = new Contact(rs.getString("firstname"), rs.getString("lastname"),
                        rs.getString("middlename"), rs.getString("docnumber"), rs.getInt("doctype_key"),
                        rs.getString("birthPlace"), rs.getString("bdate"),
                        rs.getString("gender").equalsIgnoreCase("М") ? 2 : 1,
                        rs.getInt("cntry_code"), rs.getString("company"));

                lstRes.add(cnt);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception getting filtered passenger list", e);
        }

        logger.exiting(TAG, "getPassengerList");
        return lstRes;
    }

    public List<Contact> getFirstN(int iN) {
        List lstRes = new ArrayList();

        if (iN == 0) {
            iN = 10;
        }

        if (!isOpened() && !openConnection()) {
            logger.severe("Can't open DB");
            return lstRes;
        }

        try {
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(strSelect + " limit " + iN);
            while (rs.next()) {
                Contact cnt = new Contact(rs.getString("firstname"), rs.getString("lastname"),
                        rs.getString("middlename"), rs.getString("docnumber"), rs.getInt("doctype_key"),
                        rs.getString("birthplace"), rs.getString("bdate"),
                        rs.getString("gender").equalsIgnoreCase("М") ? 2 : 1,
                        rs.getInt("cntry_code"), rs.getString("company"));

                lstRes.add(cnt);
            }
            stmt.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception getting first " + iN + " passengers", e);
        }

        return lstRes;
    }

    // get code by country name
    public Integer getCountryCode(String strCountryName) {
        Integer iRes = 0;

        if (!isOpened()) {
            openConnection();
        }

        if (stmtCountryCode == null) {
            logger.warning("stmtCountryCode is null");
            return iRes;
        }

        try {
            stmtCountryCode.setString(1, strCountryName);
            ResultSet rs = stmtCountryCode.executeQuery();

            // no records -> error message
            if (!rs.next()) {
                logger.warning("Country code not found for name: " + strCountryName);
                return iRes;
            }

            iRes = rs.getInt("val");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Can't get country code", e);
        }

        return iRes;
    }

    // get name by country code
    public String getCountryName(int iCountryCode) {
        String strRes = null;

        if (!isOpened()) {
            openConnection();
        }

        if (stmtCountryName == null) {
            System.err.println("stmtCountryName is null");
            return strRes;
        }

        try {
            stmtCountryName.setInt(1, iCountryCode);
            ResultSet rs = stmtCountryName.executeQuery();

            // no records -> error message
            if (!rs.next()) {
                System.err.println("Country name not found for code: " + iCountryCode);
                return strRes;
            }

            strRes = rs.getString("name");
        } catch (SQLException e) {
            System.err.println("Can't get country name");
            e.printStackTrace();
        }

        return strRes;
    }
}

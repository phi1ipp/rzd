package com.grigorio.rzd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Philipp on 9/9/2014.
 */
public class DBHelper {
    private Connection conn = null;
    private PreparedStatement stmtFiltered = null;
    private PreparedStatement stmtCountryName = null;
    private PreparedStatement stmtCountryCode = null;

    protected void init() throws Exception {
        Class.forName("org.sqlite.JDBC");
    }

    public boolean openConnection() {
        try {
            init();
            String DBNAME = "test.db";
            conn = DriverManager.getConnection("jdbc:sqlite:" + DBNAME);
        } catch (Exception e) {
            System.err.println("Can't open connection to DB");
            return false;
        }

        try {
            stmtFiltered = conn.prepareStatement("select *, strftime('%d.%m.%Y',birthdate) as bdate, " +
                    "countries.val as cntry_code " +
                    "from contacts, countries " +
                    "where countries.country_key = contacts.doccountry_key and lastname like ?");
            stmtCountryName = conn.prepareStatement("select name from countries where val=?");
            stmtCountryCode = conn.prepareStatement("select val from countries where name=?");
        } catch (SQLException e) {
            System.err.println("Can't prepare statements");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isOpened() {
        return conn != null;
    }

    public List<Contact> getPassengerList(String strLastNameFilter) {
        List lstRes = new ArrayList();

        if (strLastNameFilter == null) {
            System.err.println("filter string is null");
            return lstRes;
        }

        if (stmtFiltered == null) {
            System.err.println("stmtFiltered is null");
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
                        rs.getInt("cntry_code"));

                lstRes.add(cnt);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            System.err.println("stmtCountryCode is null");
            return iRes;
        }

        try {
            stmtCountryCode.setString(1, strCountryName);
            ResultSet rs = stmtCountryCode.executeQuery();

            // no records -> error message
            if (!rs.next()) {
                System.err.println("Country code not found for name: " + strCountryName);
                return iRes;
            }

            iRes = rs.getInt("val");
        } catch (SQLException e) {
            System.err.println("Can't get country code");
            e.printStackTrace();
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

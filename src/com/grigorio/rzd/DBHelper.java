package com.grigorio.rzd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Philipp on 9/9/2014.
 */
public class DBHelper {
    private final String DBNAME = "test.db";
    private Connection conn;
    private PreparedStatement stmtFilter;

    protected void init() throws Exception {
        Class.forName("org.sqlite.JDBC");
    }

    public boolean openConnection() {
        try {
            init();
            conn = DriverManager.getConnection("jdbc:sqlite:" + DBNAME);
        } catch (Exception e) {
            System.err.println("Can't open connection to DB");
            return false;
        }

        return true;
    }

    public List<Map<String,String>> getPassengerList(String strLastNameFilter) {
        List lstRes = new ArrayList();

        try {
            stmtFilter = conn.prepareStatement("SELECT * from contact where lastName");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lstRes;
    }
}

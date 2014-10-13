package com.grigorio.rzd;

/**
 * Created by philipp on 10/10/14.
 */
public class Order {
    public int getId() {
        return id;
    }

    private int id;

    public String getStrToken() {
        return strToken;
    }

    public void setStrToken(String strToken) {
        this.strToken = strToken;
    }

    private String strToken;

    Order(int id, String strToken) {
        this.id = id;
        this.strToken = strToken;
    }
}

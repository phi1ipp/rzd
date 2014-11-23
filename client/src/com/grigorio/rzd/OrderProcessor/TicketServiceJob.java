package com.grigorio.rzd.OrderProcessor;

/**
 * Created by philipp on 10/19/14.
 */
public abstract class TicketServiceJob {
    private String type;
    private String token;

    public abstract long getId();

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TicketServiceJob (String aType, String aToken) {
        type = aType;
        token = aToken;
    }
}

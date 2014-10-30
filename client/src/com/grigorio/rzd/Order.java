package com.grigorio.rzd;

/**
 * Created by philipp on 10/10/14.
 */
public class Order extends TicketServiceJob {
    public int getId() {
        return iOrderId;
    }

    private int iOrderId;

    Order(int id, String strToken) {
        super("Order", strToken);
        this.iOrderId = id;
    }
}

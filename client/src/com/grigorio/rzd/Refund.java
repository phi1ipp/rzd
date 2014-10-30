package com.grigorio.rzd;

/**
 * Created by philipp on 10/19/14.
 */
public class Refund extends TicketServiceJob {
    int iOrderId, iTicketId;

    public int getOrderId() {
        return iOrderId;
    }

    public void setOrderId(int iOrderId) {
        this.iOrderId = iOrderId;
    }

    public int getTicketId() {
        return iTicketId;
    }

    public void setTicketId(int iTicketId) {
        this.iTicketId = iTicketId;
    }


    public Refund(int aOrderId, int aTicketId, String strToken) {
        super("Refund", strToken);
        iOrderId = aOrderId;
        iTicketId = aTicketId;
    }

    @Override
    public int getId() {
        return iOrderId;
    }
}

package com.grigorio.rzd.OrderProcessor;

import com.grigorio.rzd.OrderProcessor.TicketServiceJob;

/**
 * Created by philipp on 10/19/14.
 */
public class Refund extends TicketServiceJob {
    long lOrderId, lTicketId;

    public long getOrderId() {
        return lOrderId;
    }

    public void setOrderId(int iOrderId) {
        this.lOrderId = iOrderId;
    }

    public long getTicketId() {
        return lTicketId;
    }

    public void setTicketId(int iTicketId) {
        this.lTicketId = iTicketId;
    }


    public Refund(int aOrderId, int aTicketId, String strToken) {
        super("Refund", strToken);
        lOrderId = aOrderId;
        lTicketId = aTicketId;
    }

    @Override
    public long getId() {
        return lOrderId;
    }
}

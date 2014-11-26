package com.grigorio.rzd.ticket;

/**
 * Created by philipp on 11/18/14.
 */
public class Ticket {
    private long lTicketId;
    private String strTicketNum;
    private long lOrderId;

    public Ticket(long aTicketId, long anOrderId) {
        lTicketId = aTicketId;
        lOrderId = anOrderId;
    }

    public Ticket(long aTicketId, long anOrderId, String aTicketNum) {
        lTicketId = aTicketId;
        lOrderId = anOrderId;
        strTicketNum = aTicketNum;
    }

    public long getlTicketId() {
        return lTicketId;
    }

    public void setlTicketId(long lTicketId) {
        this.lTicketId = lTicketId;
    }

    public String getTicketNum() {
        return strTicketNum;
    }

    public void setTicketNum(String strTicketNum) {
        this.strTicketNum = strTicketNum;
    }

    public long getlOrderId() {
        return lOrderId;
    }

    public void setlOrderId(long lOrderId) {
        this.lOrderId = lOrderId;
    }

    @Override
    public String toString() {
        return "{Ticket ID=" + lTicketId + " Ticket Num=" + strTicketNum + " Order ID=" + lOrderId + "}";
    }
}

package com.grigorio.rzd.ticket;

import java.math.BigInteger;

/**
 * Created by philipp on 11/18/14.
 */
public class Ticket {
    private long lTicketId;
    private BigInteger bintTicketNum;
    private long lOrderId;

    public Ticket(long aTicketId, long anOrderId) {
        lTicketId = aTicketId;
        lOrderId = anOrderId;
    }

    public Ticket(long aTicketId, long anOrderId, BigInteger aTicketNum) {
        lTicketId = aTicketId;
        lOrderId = anOrderId;
        bintTicketNum = aTicketNum;
    }

    public long getlTicketId() {
        return lTicketId;
    }

    public void setlTicketId(long lTicketId) {
        this.lTicketId = lTicketId;
    }

    public BigInteger getBintTicketNum() {
        return bintTicketNum;
    }

    public void setBintTicketNum(BigInteger bintTicketNum) {
        this.bintTicketNum = bintTicketNum;
    }

    public long getlOrderId() {
        return lOrderId;
    }

    public void setlOrderId(long lOrderId) {
        this.lOrderId = lOrderId;
    }

    @Override
    public String toString() {
        return "{Ticket ID=" + lTicketId + " Ticket Num=" + bintTicketNum + " Order ID=" + lOrderId + "}";
    }
}

package com.grigorio.rzd.search;

/**
 * Created by philipp on 11/14/14.
 */
public class TicketSearchRecord {

    private String strTicketNum;
    private String strLastName;
    private String strFrom;
    private String strTo;
    private String strTripDate;
    private String strCompany;
    private long   lTicketId;
    private long   lOrderId;

    public long getlTicketId() {
        return lTicketId;
    }

    public void setlTicketId(long lTicketId) {
        this.lTicketId = lTicketId;
    }

    public long getlOrderId() {
        return lOrderId;
    }

    public void setlOrderId(long lOrderId) {
        this.lOrderId = lOrderId;
    }

    public String getStrTicketNum() {
        return strTicketNum;
    }

    public void setStrTicketNum(String strTicketNum) {
        this.strTicketNum = strTicketNum;
    }

    public String getStrLastName() {
        return strLastName;
    }

    public void setStrLastName(String strLastName) {
        this.strLastName = strLastName;
    }

    public String getStrFrom() {
        return strFrom;
    }

    public void setStrFrom(String strFrom) {
        this.strFrom = strFrom;
    }

    public String getStrTo() {
        return strTo;
    }

    public void setStrTo(String strTo) {
        this.strTo = strTo;
    }

    public String getStrTripDate() {
        return strTripDate;
    }

    public void setStrTripDate(String strTripDate) {
        this.strTripDate = strTripDate;
    }

    public String getStrCompany() {
        return strCompany;
    }

    public void setStrCompany(String strCompany) {
        this.strCompany = strCompany;
    }

    public TicketSearchRecord(String strTicketNum, String strLastName, String strFrom, String strTo,
                              String strTripDate, String strCompany, long lOrderId, long lTicketId) {
        this.strTicketNum = strTicketNum;
        this.strLastName = strLastName;
        this.strFrom = strFrom;
        this.strTo = strTo;
        this.strTripDate = strTripDate;
        this.strCompany = strCompany;
        this.lOrderId = lOrderId;
        this.lTicketId = lTicketId;
    }
}

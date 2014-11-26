
package com.grigorio.rzd.TicketServiceWSProxy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for SearchTicketRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SearchTicketRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="User" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LtpaToken2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nonce" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="createTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="signature" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ticketNum" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="lastName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="stationFrom" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="stationTo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="departureFrom" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="departureTo" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="companyName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="allCriteria" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchTicketRequest", propOrder = {
    "user",
    "ltpaToken2",
    "nonce",
    "createTime",
    "signature",
    "ticketNum",
    "lastName",
    "stationFrom",
    "stationTo",
    "departureFrom",
    "departureTo",
    "companyName",
    "allCriteria"
})
public class SearchTicketRequest {

    @XmlElement(name = "User", required = true)
    protected String user;
    @XmlElement(name = "LtpaToken2", required = true)
    protected String ltpaToken2;
    protected int nonce;
    @XmlElement(required = true)
    protected String createTime;
    @XmlElement(required = true)
    protected String signature;
    @XmlElement(required = true)
    protected String ticketNum;
    @XmlElement(required = true)
    protected String lastName;
    @XmlElement(required = true)
    protected String stationFrom;
    @XmlElement(required = true)
    protected String stationTo;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar departureFrom;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar departureTo;
    @XmlElement(required = true)
    protected String companyName;
    protected boolean allCriteria;

    /**
     * Gets the value of the user property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the value of the user property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUser(String value) {
        this.user = value;
    }

    /**
     * Gets the value of the ltpaToken2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLtpaToken2() {
        return ltpaToken2;
    }

    /**
     * Sets the value of the ltpaToken2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLtpaToken2(String value) {
        this.ltpaToken2 = value;
    }

    /**
     * Gets the value of the nonce property.
     * 
     */
    public int getNonce() {
        return nonce;
    }

    /**
     * Sets the value of the nonce property.
     * 
     */
    public void setNonce(int value) {
        this.nonce = value;
    }

    /**
     * Gets the value of the createTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * Sets the value of the createTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreateTime(String value) {
        this.createTime = value;
    }

    /**
     * Gets the value of the signature property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignature(String value) {
        this.signature = value;
    }

    /**
     * Gets the value of the ticketNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTicketNum() {
        return ticketNum;
    }

    /**
     * Sets the value of the ticketNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTicketNum(String value) {
        this.ticketNum = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the stationFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStationFrom() {
        return stationFrom;
    }

    /**
     * Sets the value of the stationFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStationFrom(String value) {
        this.stationFrom = value;
    }

    /**
     * Gets the value of the stationTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStationTo() {
        return stationTo;
    }

    /**
     * Sets the value of the stationTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStationTo(String value) {
        this.stationTo = value;
    }

    /**
     * Gets the value of the departureFrom property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDepartureFrom() {
        return departureFrom;
    }

    /**
     * Sets the value of the departureFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDepartureFrom(XMLGregorianCalendar value) {
        this.departureFrom = value;
    }

    /**
     * Gets the value of the departureTo property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDepartureTo() {
        return departureTo;
    }

    /**
     * Sets the value of the departureTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDepartureTo(XMLGregorianCalendar value) {
        this.departureTo = value;
    }

    /**
     * Gets the value of the companyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets the value of the companyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyName(String value) {
        this.companyName = value;
    }

    /**
     * Gets the value of the allCriteria property.
     * 
     */
    public boolean isAllCriteria() {
        return allCriteria;
    }

    /**
     * Sets the value of the allCriteria property.
     * 
     */
    public void setAllCriteria(boolean value) {
        this.allCriteria = value;
    }

}

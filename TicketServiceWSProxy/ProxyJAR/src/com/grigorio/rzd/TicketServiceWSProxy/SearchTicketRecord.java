
package com.grigorio.rzd.TicketServiceWSProxy;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for SearchTicketRecord complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SearchTicketRecord">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ticketNum" type="{http://www.w3.org/2001/XMLSchema}unsignedLong"/>
 *         &lt;element name="lastName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="stationFrom" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="stationTo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="departureDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchTicketRecord", propOrder = {
    "ticketNum",
    "lastName",
    "stationFrom",
    "stationTo",
    "departureDate"
})
public class SearchTicketRecord {

    @XmlElement(required = true)
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger ticketNum;
    @XmlElement(required = true)
    protected String lastName;
    @XmlElement(required = true)
    protected String stationFrom;
    @XmlElement(required = true)
    protected String stationTo;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar departureDate;

    /**
     * Gets the value of the ticketNum property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTicketNum() {
        return ticketNum;
    }

    /**
     * Sets the value of the ticketNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTicketNum(BigInteger value) {
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
     * Gets the value of the departureDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDepartureDate() {
        return departureDate;
    }

    /**
     * Sets the value of the departureDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDepartureDate(XMLGregorianCalendar value) {
        this.departureDate = value;
    }

}

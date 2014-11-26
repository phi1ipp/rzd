
package com.grigorio.rzd.TicketServiceWSProxy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TicketMapping complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TicketMapping">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ticketId" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *         &lt;element name="ticketNum" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TicketMapping", propOrder = {
    "ticketId",
    "ticketNum"
})
public class TicketMapping {

    @XmlSchemaType(name = "unsignedInt")
    protected long ticketId;
    @XmlElement(required = true)
    protected String ticketNum;

    /**
     * Gets the value of the ticketId property.
     * 
     */
    public long getTicketId() {
        return ticketId;
    }

    /**
     * Sets the value of the ticketId property.
     * 
     */
    public void setTicketId(long value) {
        this.ticketId = value;
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

}

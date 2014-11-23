
package com.grigorio.rzd.TicketServiceWSProxy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SaleRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SaleRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="orderId" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *         &lt;element name="User" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LtpaToken2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nonce" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="createTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="signature" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ticketMappings" type="{urn:TicketServiceSchema}TicketMappingList"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SaleRequest", propOrder = {
    "orderId",
    "user",
    "ltpaToken2",
    "nonce",
    "createTime",
    "signature",
    "ticketMappings"
})
public class SaleRequest {

    @XmlSchemaType(name = "unsignedInt")
    protected long orderId;
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
    protected TicketMappingList ticketMappings;

    /**
     * Gets the value of the orderId property.
     * 
     */
    public long getOrderId() {
        return orderId;
    }

    /**
     * Sets the value of the orderId property.
     * 
     */
    public void setOrderId(long value) {
        this.orderId = value;
    }

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
     * Gets the value of the ticketMappings property.
     * 
     * @return
     *     possible object is
     *     {@link TicketMappingList }
     *     
     */
    public TicketMappingList getTicketMappings() {
        return ticketMappings;
    }

    /**
     * Sets the value of the ticketMappings property.
     * 
     * @param value
     *     allowed object is
     *     {@link TicketMappingList }
     *     
     */
    public void setTicketMappings(TicketMappingList value) {
        this.ticketMappings = value;
    }

}


package com.grigorio.rzd.TicketServiceWSProxy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TransInfoXMLResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TransInfoXMLResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResponseXMLData" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransInfoXMLResponse", propOrder = {
    "responseXMLData"
})
public class TransInfoXMLResponse {

    @XmlElement(name = "ResponseXMLData", required = true)
    protected String responseXMLData;

    /**
     * Gets the value of the responseXMLData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseXMLData() {
        return responseXMLData;
    }

    /**
     * Sets the value of the responseXMLData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseXMLData(String value) {
        this.responseXMLData = value;
    }

}

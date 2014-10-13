
package com.grigorio.rzd.TicketServiceWSProxy;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for TransInfoResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TransInfoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TransID" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="PrevTransID" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="Lang" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LastRefundTransID" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="STAN" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TStatus" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="RStatus" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="OrderNum" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="CreateTime">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="timeOffset" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="BookingTime">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="timeOffset" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ConfirmTime">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="timeOffset" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ConfirmTimeLimit">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="timeOffset" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="Fee" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="PlaceCount" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="TrainNum" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CarNum" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="CarType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DepartTime">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="timeOffset" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Phone" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="Email" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="ServiceClass" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="StationFrom">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="Code" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="StationTo">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="Code" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="GenderClass" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="ArrivalTime">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="timeOffset" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Carrier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TimeDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="GroupDirection" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="Terminal" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IsTest" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="ExpierSetEr">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="timeOffset" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Domain" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PayTypeID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UfsProfit" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="Blank" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="RetFlag" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                   &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                   &lt;element name="AmountNDS" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                   &lt;element name="TariffType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="TicketNum" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                   &lt;element name="RegTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="RemoteCheckIn" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                   &lt;element name="PrintFlag" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                   &lt;element name="RzhdStatus" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                 &lt;attribute name="PrevID" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Passenger" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="DocType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="DocNum" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="Place" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="PlaceTier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                 &lt;attribute name="BlankID" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransInfoResponse", propOrder = {
    "transID",
    "prevTransID",
    "lang",
    "lastRefundTransID",
    "stan",
    "tStatus",
    "rStatus",
    "orderNum",
    "type",
    "createTime",
    "bookingTime",
    "confirmTime",
    "confirmTimeLimit",
    "amount",
    "fee",
    "placeCount",
    "trainNum",
    "carNum",
    "carType",
    "departTime",
    "phone",
    "email",
    "serviceClass",
    "stationFrom",
    "stationTo",
    "genderClass",
    "arrivalTime",
    "carrier",
    "timeDescription",
    "groupDirection",
    "terminal",
    "isTest",
    "expierSetEr",
    "domain",
    "payTypeID",
    "ufsProfit",
    "blank",
    "passenger"
})
public class TransInfoResponse {

    @XmlElement(name = "TransID", required = true)
    protected BigInteger transID;
    @XmlElement(name = "PrevTransID", required = true)
    protected BigInteger prevTransID;
    @XmlElement(name = "Lang", required = true)
    protected String lang;
    @XmlElement(name = "LastRefundTransID", required = true)
    protected BigInteger lastRefundTransID;
    @XmlElement(name = "STAN", required = true)
    protected String stan;
    @XmlElement(name = "TStatus", required = true)
    protected BigInteger tStatus;
    @XmlElement(name = "RStatus", required = true)
    protected BigInteger rStatus;
    @XmlElement(name = "OrderNum", required = true)
    protected BigInteger orderNum;
    @XmlElement(name = "Type", required = true)
    protected BigInteger type;
    @XmlElement(name = "CreateTime", required = true)
    protected TransInfoResponse.CreateTime createTime;
    @XmlElement(name = "BookingTime", required = true)
    protected TransInfoResponse.BookingTime bookingTime;
    @XmlElement(name = "ConfirmTime", required = true)
    protected TransInfoResponse.ConfirmTime confirmTime;
    @XmlElement(name = "ConfirmTimeLimit", required = true)
    protected TransInfoResponse.ConfirmTimeLimit confirmTimeLimit;
    @XmlElement(name = "Amount")
    protected float amount;
    @XmlElement(name = "Fee")
    protected float fee;
    @XmlElement(name = "PlaceCount", required = true)
    protected BigInteger placeCount;
    @XmlElement(name = "TrainNum", required = true)
    protected String trainNum;
    @XmlElement(name = "CarNum", required = true)
    protected BigInteger carNum;
    @XmlElement(name = "CarType", required = true)
    protected String carType;
    @XmlElement(name = "DepartTime", required = true)
    protected TransInfoResponse.DepartTime departTime;
    @XmlElement(name = "Phone", required = true)
    protected Object phone;
    @XmlElement(name = "Email", required = true)
    protected Object email;
    @XmlElement(name = "ServiceClass", required = true)
    protected String serviceClass;
    @XmlElement(name = "StationFrom", required = true)
    protected TransInfoResponse.StationFrom stationFrom;
    @XmlElement(name = "StationTo", required = true)
    protected TransInfoResponse.StationTo stationTo;
    @XmlElement(name = "GenderClass", required = true)
    protected BigInteger genderClass;
    @XmlElement(name = "ArrivalTime", required = true)
    protected TransInfoResponse.ArrivalTime arrivalTime;
    @XmlElement(name = "Carrier", required = true)
    protected String carrier;
    @XmlElement(name = "TimeDescription", required = true)
    protected String timeDescription;
    @XmlElement(name = "GroupDirection", required = true)
    protected BigInteger groupDirection;
    @XmlElement(name = "Terminal", required = true)
    protected String terminal;
    @XmlElement(name = "IsTest", required = true)
    protected BigInteger isTest;
    @XmlElement(name = "ExpierSetEr", required = true)
    protected TransInfoResponse.ExpierSetEr expierSetEr;
    @XmlElement(name = "Domain", required = true)
    protected String domain;
    @XmlElement(name = "PayTypeID", required = true)
    protected String payTypeID;
    @XmlElement(name = "UfsProfit")
    protected float ufsProfit;
    @XmlElement(name = "Blank", required = true)
    protected List<TransInfoResponse.Blank> blank;
    @XmlElement(name = "Passenger", required = true)
    protected List<TransInfoResponse.Passenger> passenger;

    /**
     * Gets the value of the transID property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTransID() {
        return transID;
    }

    /**
     * Sets the value of the transID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTransID(BigInteger value) {
        this.transID = value;
    }

    /**
     * Gets the value of the prevTransID property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPrevTransID() {
        return prevTransID;
    }

    /**
     * Sets the value of the prevTransID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPrevTransID(BigInteger value) {
        this.prevTransID = value;
    }

    /**
     * Gets the value of the lang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLang() {
        return lang;
    }

    /**
     * Sets the value of the lang property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLang(String value) {
        this.lang = value;
    }

    /**
     * Gets the value of the lastRefundTransID property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLastRefundTransID() {
        return lastRefundTransID;
    }

    /**
     * Sets the value of the lastRefundTransID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLastRefundTransID(BigInteger value) {
        this.lastRefundTransID = value;
    }

    /**
     * Gets the value of the stan property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTAN() {
        return stan;
    }

    /**
     * Sets the value of the stan property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTAN(String value) {
        this.stan = value;
    }

    /**
     * Gets the value of the tStatus property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTStatus() {
        return tStatus;
    }

    /**
     * Sets the value of the tStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTStatus(BigInteger value) {
        this.tStatus = value;
    }

    /**
     * Gets the value of the rStatus property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRStatus() {
        return rStatus;
    }

    /**
     * Sets the value of the rStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRStatus(BigInteger value) {
        this.rStatus = value;
    }

    /**
     * Gets the value of the orderNum property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getOrderNum() {
        return orderNum;
    }

    /**
     * Sets the value of the orderNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setOrderNum(BigInteger value) {
        this.orderNum = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setType(BigInteger value) {
        this.type = value;
    }

    /**
     * Gets the value of the createTime property.
     * 
     * @return
     *     possible object is
     *     {@link TransInfoResponse.CreateTime }
     *     
     */
    public TransInfoResponse.CreateTime getCreateTime() {
        return createTime;
    }

    /**
     * Sets the value of the createTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransInfoResponse.CreateTime }
     *     
     */
    public void setCreateTime(TransInfoResponse.CreateTime value) {
        this.createTime = value;
    }

    /**
     * Gets the value of the bookingTime property.
     * 
     * @return
     *     possible object is
     *     {@link TransInfoResponse.BookingTime }
     *     
     */
    public TransInfoResponse.BookingTime getBookingTime() {
        return bookingTime;
    }

    /**
     * Sets the value of the bookingTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransInfoResponse.BookingTime }
     *     
     */
    public void setBookingTime(TransInfoResponse.BookingTime value) {
        this.bookingTime = value;
    }

    /**
     * Gets the value of the confirmTime property.
     * 
     * @return
     *     possible object is
     *     {@link TransInfoResponse.ConfirmTime }
     *     
     */
    public TransInfoResponse.ConfirmTime getConfirmTime() {
        return confirmTime;
    }

    /**
     * Sets the value of the confirmTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransInfoResponse.ConfirmTime }
     *     
     */
    public void setConfirmTime(TransInfoResponse.ConfirmTime value) {
        this.confirmTime = value;
    }

    /**
     * Gets the value of the confirmTimeLimit property.
     * 
     * @return
     *     possible object is
     *     {@link TransInfoResponse.ConfirmTimeLimit }
     *     
     */
    public TransInfoResponse.ConfirmTimeLimit getConfirmTimeLimit() {
        return confirmTimeLimit;
    }

    /**
     * Sets the value of the confirmTimeLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransInfoResponse.ConfirmTimeLimit }
     *     
     */
    public void setConfirmTimeLimit(TransInfoResponse.ConfirmTimeLimit value) {
        this.confirmTimeLimit = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     */
    public float getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     */
    public void setAmount(float value) {
        this.amount = value;
    }

    /**
     * Gets the value of the fee property.
     * 
     */
    public float getFee() {
        return fee;
    }

    /**
     * Sets the value of the fee property.
     * 
     */
    public void setFee(float value) {
        this.fee = value;
    }

    /**
     * Gets the value of the placeCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPlaceCount() {
        return placeCount;
    }

    /**
     * Sets the value of the placeCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPlaceCount(BigInteger value) {
        this.placeCount = value;
    }

    /**
     * Gets the value of the trainNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrainNum() {
        return trainNum;
    }

    /**
     * Sets the value of the trainNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrainNum(String value) {
        this.trainNum = value;
    }

    /**
     * Gets the value of the carNum property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCarNum() {
        return carNum;
    }

    /**
     * Sets the value of the carNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCarNum(BigInteger value) {
        this.carNum = value;
    }

    /**
     * Gets the value of the carType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarType() {
        return carType;
    }

    /**
     * Sets the value of the carType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarType(String value) {
        this.carType = value;
    }

    /**
     * Gets the value of the departTime property.
     * 
     * @return
     *     possible object is
     *     {@link TransInfoResponse.DepartTime }
     *     
     */
    public TransInfoResponse.DepartTime getDepartTime() {
        return departTime;
    }

    /**
     * Sets the value of the departTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransInfoResponse.DepartTime }
     *     
     */
    public void setDepartTime(TransInfoResponse.DepartTime value) {
        this.departTime = value;
    }

    /**
     * Gets the value of the phone property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getPhone() {
        return phone;
    }

    /**
     * Sets the value of the phone property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setPhone(Object value) {
        this.phone = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setEmail(Object value) {
        this.email = value;
    }

    /**
     * Gets the value of the serviceClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceClass() {
        return serviceClass;
    }

    /**
     * Sets the value of the serviceClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceClass(String value) {
        this.serviceClass = value;
    }

    /**
     * Gets the value of the stationFrom property.
     * 
     * @return
     *     possible object is
     *     {@link TransInfoResponse.StationFrom }
     *     
     */
    public TransInfoResponse.StationFrom getStationFrom() {
        return stationFrom;
    }

    /**
     * Sets the value of the stationFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransInfoResponse.StationFrom }
     *     
     */
    public void setStationFrom(TransInfoResponse.StationFrom value) {
        this.stationFrom = value;
    }

    /**
     * Gets the value of the stationTo property.
     * 
     * @return
     *     possible object is
     *     {@link TransInfoResponse.StationTo }
     *     
     */
    public TransInfoResponse.StationTo getStationTo() {
        return stationTo;
    }

    /**
     * Sets the value of the stationTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransInfoResponse.StationTo }
     *     
     */
    public void setStationTo(TransInfoResponse.StationTo value) {
        this.stationTo = value;
    }

    /**
     * Gets the value of the genderClass property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGenderClass() {
        return genderClass;
    }

    /**
     * Sets the value of the genderClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGenderClass(BigInteger value) {
        this.genderClass = value;
    }

    /**
     * Gets the value of the arrivalTime property.
     * 
     * @return
     *     possible object is
     *     {@link TransInfoResponse.ArrivalTime }
     *     
     */
    public TransInfoResponse.ArrivalTime getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Sets the value of the arrivalTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransInfoResponse.ArrivalTime }
     *     
     */
    public void setArrivalTime(TransInfoResponse.ArrivalTime value) {
        this.arrivalTime = value;
    }

    /**
     * Gets the value of the carrier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarrier() {
        return carrier;
    }

    /**
     * Sets the value of the carrier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarrier(String value) {
        this.carrier = value;
    }

    /**
     * Gets the value of the timeDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeDescription() {
        return timeDescription;
    }

    /**
     * Sets the value of the timeDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeDescription(String value) {
        this.timeDescription = value;
    }

    /**
     * Gets the value of the groupDirection property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGroupDirection() {
        return groupDirection;
    }

    /**
     * Sets the value of the groupDirection property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGroupDirection(BigInteger value) {
        this.groupDirection = value;
    }

    /**
     * Gets the value of the terminal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTerminal() {
        return terminal;
    }

    /**
     * Sets the value of the terminal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTerminal(String value) {
        this.terminal = value;
    }

    /**
     * Gets the value of the isTest property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIsTest() {
        return isTest;
    }

    /**
     * Sets the value of the isTest property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIsTest(BigInteger value) {
        this.isTest = value;
    }

    /**
     * Gets the value of the expierSetEr property.
     * 
     * @return
     *     possible object is
     *     {@link TransInfoResponse.ExpierSetEr }
     *     
     */
    public TransInfoResponse.ExpierSetEr getExpierSetEr() {
        return expierSetEr;
    }

    /**
     * Sets the value of the expierSetEr property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransInfoResponse.ExpierSetEr }
     *     
     */
    public void setExpierSetEr(TransInfoResponse.ExpierSetEr value) {
        this.expierSetEr = value;
    }

    /**
     * Gets the value of the domain property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Sets the value of the domain property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomain(String value) {
        this.domain = value;
    }

    /**
     * Gets the value of the payTypeID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayTypeID() {
        return payTypeID;
    }

    /**
     * Sets the value of the payTypeID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayTypeID(String value) {
        this.payTypeID = value;
    }

    /**
     * Gets the value of the ufsProfit property.
     * 
     */
    public float getUfsProfit() {
        return ufsProfit;
    }

    /**
     * Sets the value of the ufsProfit property.
     * 
     */
    public void setUfsProfit(float value) {
        this.ufsProfit = value;
    }

    /**
     * Gets the value of the blank property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the blank property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBlank().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TransInfoResponse.Blank }
     * 
     * 
     */
    public List<TransInfoResponse.Blank> getBlank() {
        if (blank == null) {
            blank = new ArrayList<TransInfoResponse.Blank>();
        }
        return this.blank;
    }

    /**
     * Gets the value of the passenger property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the passenger property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPassenger().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TransInfoResponse.Passenger }
     * 
     * 
     */
    public List<TransInfoResponse.Passenger> getPassenger() {
        if (passenger == null) {
            passenger = new ArrayList<TransInfoResponse.Passenger>();
        }
        return this.passenger;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="timeOffset" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class ArrivalTime {

        @XmlValue
        protected String value;
        @XmlAttribute
        protected String timeOffset;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the timeOffset property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTimeOffset() {
            return timeOffset;
        }

        /**
         * Sets the value of the timeOffset property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTimeOffset(String value) {
            this.timeOffset = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="RetFlag" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *         &lt;element name="AmountNDS" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *         &lt;element name="TariffType" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="TicketNum" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *         &lt;element name="RegTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="RemoteCheckIn" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *         &lt;element name="PrintFlag" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *         &lt;element name="RzhdStatus" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *       &lt;/sequence>
     *       &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *       &lt;attribute name="PrevID" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "retFlag",
        "amount",
        "amountNDS",
        "tariffType",
        "ticketNum",
        "regTime",
        "remoteCheckIn",
        "printFlag",
        "rzhdStatus"
    })
    public static class Blank {

        @XmlElement(name = "RetFlag", required = true)
        protected BigInteger retFlag;
        @XmlElement(name = "Amount")
        protected float amount;
        @XmlElement(name = "AmountNDS")
        protected float amountNDS;
        @XmlElement(name = "TariffType", required = true)
        protected String tariffType;
        @XmlElement(name = "TicketNum", required = true)
        protected BigInteger ticketNum;
        @XmlElement(name = "RegTime", required = true)
        protected String regTime;
        @XmlElement(name = "RemoteCheckIn", required = true)
        protected BigInteger remoteCheckIn;
        @XmlElement(name = "PrintFlag", required = true)
        protected BigInteger printFlag;
        @XmlElement(name = "RzhdStatus", required = true)
        protected BigInteger rzhdStatus;
        @XmlAttribute(name = "ID")
        protected BigInteger id;
        @XmlAttribute(name = "PrevID")
        protected BigInteger prevID;

        /**
         * Gets the value of the retFlag property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getRetFlag() {
            return retFlag;
        }

        /**
         * Sets the value of the retFlag property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setRetFlag(BigInteger value) {
            this.retFlag = value;
        }

        /**
         * Gets the value of the amount property.
         * 
         */
        public float getAmount() {
            return amount;
        }

        /**
         * Sets the value of the amount property.
         * 
         */
        public void setAmount(float value) {
            this.amount = value;
        }

        /**
         * Gets the value of the amountNDS property.
         * 
         */
        public float getAmountNDS() {
            return amountNDS;
        }

        /**
         * Sets the value of the amountNDS property.
         * 
         */
        public void setAmountNDS(float value) {
            this.amountNDS = value;
        }

        /**
         * Gets the value of the tariffType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTariffType() {
            return tariffType;
        }

        /**
         * Sets the value of the tariffType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTariffType(String value) {
            this.tariffType = value;
        }

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
         * Gets the value of the regTime property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRegTime() {
            return regTime;
        }

        /**
         * Sets the value of the regTime property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRegTime(String value) {
            this.regTime = value;
        }

        /**
         * Gets the value of the remoteCheckIn property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getRemoteCheckIn() {
            return remoteCheckIn;
        }

        /**
         * Sets the value of the remoteCheckIn property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setRemoteCheckIn(BigInteger value) {
            this.remoteCheckIn = value;
        }

        /**
         * Gets the value of the printFlag property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getPrintFlag() {
            return printFlag;
        }

        /**
         * Sets the value of the printFlag property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setPrintFlag(BigInteger value) {
            this.printFlag = value;
        }

        /**
         * Gets the value of the rzhdStatus property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getRzhdStatus() {
            return rzhdStatus;
        }

        /**
         * Sets the value of the rzhdStatus property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setRzhdStatus(BigInteger value) {
            this.rzhdStatus = value;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getID() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setID(BigInteger value) {
            this.id = value;
        }

        /**
         * Gets the value of the prevID property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getPrevID() {
            return prevID;
        }

        /**
         * Sets the value of the prevID property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setPrevID(BigInteger value) {
            this.prevID = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="timeOffset" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class BookingTime {

        @XmlValue
        protected String value;
        @XmlAttribute
        protected String timeOffset;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the timeOffset property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTimeOffset() {
            return timeOffset;
        }

        /**
         * Sets the value of the timeOffset property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTimeOffset(String value) {
            this.timeOffset = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="timeOffset" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class ConfirmTime {

        @XmlValue
        protected String value;
        @XmlAttribute
        protected String timeOffset;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the timeOffset property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTimeOffset() {
            return timeOffset;
        }

        /**
         * Sets the value of the timeOffset property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTimeOffset(String value) {
            this.timeOffset = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="timeOffset" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class ConfirmTimeLimit {

        @XmlValue
        protected String value;
        @XmlAttribute
        protected String timeOffset;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the timeOffset property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTimeOffset() {
            return timeOffset;
        }

        /**
         * Sets the value of the timeOffset property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTimeOffset(String value) {
            this.timeOffset = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="timeOffset" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class CreateTime {

        @XmlValue
        protected String value;
        @XmlAttribute
        protected String timeOffset;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the timeOffset property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTimeOffset() {
            return timeOffset;
        }

        /**
         * Sets the value of the timeOffset property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTimeOffset(String value) {
            this.timeOffset = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="timeOffset" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class DepartTime {

        @XmlValue
        protected String value;
        @XmlAttribute
        protected String timeOffset;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the timeOffset property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTimeOffset() {
            return timeOffset;
        }

        /**
         * Sets the value of the timeOffset property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTimeOffset(String value) {
            this.timeOffset = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="timeOffset" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class ExpierSetEr {

        @XmlValue
        protected String value;
        @XmlAttribute
        protected String timeOffset;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the timeOffset property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTimeOffset() {
            return timeOffset;
        }

        /**
         * Sets the value of the timeOffset property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTimeOffset(String value) {
            this.timeOffset = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="DocType" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="DocNum" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="Place" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="PlaceTier" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *       &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *       &lt;attribute name="BlankID" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "type",
        "docType",
        "docNum",
        "name",
        "place",
        "placeTier"
    })
    public static class Passenger {

        @XmlElement(name = "Type", required = true)
        protected String type;
        @XmlElement(name = "DocType", required = true)
        protected String docType;
        @XmlElement(name = "DocNum", required = true)
        protected String docNum;
        @XmlElement(name = "Name", required = true)
        protected String name;
        @XmlElement(name = "Place", required = true)
        protected String place;
        @XmlElement(name = "PlaceTier", required = true)
        protected String placeTier;
        @XmlAttribute(name = "ID")
        protected BigInteger id;
        @XmlAttribute(name = "BlankID")
        protected BigInteger blankID;

        /**
         * Gets the value of the type property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getType() {
            return type;
        }

        /**
         * Sets the value of the type property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setType(String value) {
            this.type = value;
        }

        /**
         * Gets the value of the docType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDocType() {
            return docType;
        }

        /**
         * Sets the value of the docType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDocType(String value) {
            this.docType = value;
        }

        /**
         * Gets the value of the docNum property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDocNum() {
            return docNum;
        }

        /**
         * Sets the value of the docNum property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDocNum(String value) {
            this.docNum = value;
        }

        /**
         * Gets the value of the name property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Gets the value of the place property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPlace() {
            return place;
        }

        /**
         * Sets the value of the place property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPlace(String value) {
            this.place = value;
        }

        /**
         * Gets the value of the placeTier property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPlaceTier() {
            return placeTier;
        }

        /**
         * Sets the value of the placeTier property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPlaceTier(String value) {
            this.placeTier = value;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getID() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setID(BigInteger value) {
            this.id = value;
        }

        /**
         * Gets the value of the blankID property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getBlankID() {
            return blankID;
        }

        /**
         * Sets the value of the blankID property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setBlankID(BigInteger value) {
            this.blankID = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="Code" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class StationFrom {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "Code")
        protected BigInteger code;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the code property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getCode() {
            return code;
        }

        /**
         * Sets the value of the code property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setCode(BigInteger value) {
            this.code = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="Code" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class StationTo {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "Code")
        protected BigInteger code;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the code property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getCode() {
            return code;
        }

        /**
         * Sets the value of the code property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setCode(BigInteger value) {
            this.code = value;
        }

    }

}

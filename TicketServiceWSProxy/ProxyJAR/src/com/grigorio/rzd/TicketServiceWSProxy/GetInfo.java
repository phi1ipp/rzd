package com.grigorio.rzd.TicketServiceWSProxy;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
// !DO NOT EDIT THIS FILE!
// This source file is generated by Oracle tools
// Contents may be subject to change
// For reporting problems, use the following
// Version = Oracle WebServices (11.1.1.0.0, build 130224.1947.04102)

@WebService(wsdlLocation="http://127.0.0.1/WebService/TicketService.wsdl",
  targetNamespace="urn:TicketService", name="getInfo")
@SOAPBinding(style=Style.RPC)
@XmlSeeAlso(
  { ObjectFactory.class })
public interface GetInfo
{
  @WebMethod(action="urn:TicketService/getTransInfo")
  @Action(output="urn:TicketService/getInfo/getTransInfoResponse", input="urn:TicketService/getTransInfo")
  @WebResult(name="payload", partName="payload")
  public TransInfoResponse getTransInfo(@WebParam(name="payload",
      partName="payload")
    TransInfoRequest payload);

  @WebMethod(action="urn:TicketService/getTransInfo")
  @Action(output="urn:TicketService/getInfo/getTransInfoXMLResponse",
    input="urn:TicketService/getTransInfo")
  @WebResult(name="payload", partName="payload")
  public TransInfoXMLResponse getTransInfoXML(@WebParam(name="payload",
      partName="payload")
    TransInfoRequest payload);

  @WebMethod
  @Action(output="urn:TicketService/getInfo/requestRefundResponse", input="urn:TicketService/getInfo/requestRefundRequest")
  @WebResult(name="payload", partName="payload")
  public RefundXMLResponse requestRefund(@WebParam(name="payload",
      partName="payload")
    RefundRequest payload);

  @WebMethod
  @Action(output="urn:TicketService/getInfo/searchTicketResponse", input="urn:TicketService/getInfo/searchTicketRequest")
  @WebResult(name="payload", partName="payload")
  public SearchTicketResponse searchTicket(@WebParam(name="payload",
      partName="payload")
    SearchTicketRequest payload);

  @WebMethod
  @Action(output="urn:TicketService/getInfo/saleRequestResponse", input="urn:TicketService/getInfo/saleRequestRequest")
  @WebResult(name="payload", partName="payload")
  public TransInfoXMLResponse saleRequest(@WebParam(name="payload",
      partName="payload")
    SaleRequest payload);
}

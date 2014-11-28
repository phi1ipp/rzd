package com.grigorio.rzd.search;

import com.grigorio.rzd.Main;
import com.grigorio.rzd.TicketServiceWSProxy.*;
import com.grigorio.rzd.crypto.Signer;
import javafx.concurrent.Task;

import javax.xml.datatype.DatatypeFactory;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.prefs.Preferences;

/**
 * Created by philipp on 11/16/14.
 */
public class TicketSearchTask extends Task<List<TicketSearchRecord>> {

    private Map<String,Object> mapFilters;

    public TicketSearchTask(Map<String,Object> map) {
        mapFilters = map;
    }

    @Override
    protected List<TicketSearchRecord> call() throws Exception {
        Map<String,Object> mapSignature;
        List<TicketSearchRecord> retList = new ArrayList<>();

        //create request
        SearchTicketRequest req = new SearchTicketRequest();

        //generate signature and fill in fields
        if (mapFilters.get(TicketSearchConstants.strTicketNum) != null) {
            mapSignature = Signer.sign(Long.parseLong(mapFilters.get(TicketSearchConstants.strTicketNum).toString()));
            req.setTicketNum(mapFilters.get(TicketSearchConstants.strTicketNum).toString());
        } else {
            mapSignature = Signer.sign(null);
            req.setTicketNum("0");
        }

        req.setCreateTime(mapSignature.get("time").toString());
        req.setNonce((int) mapSignature.get("nonce"));
        req.setSignature(mapSignature.get("signature").toString());
        req.setUser(Preferences.userRoot().node("com.grigorio.rzd").get(Main.Preferences.stridUsername, ""));

        // set string parameters
        req.setLastName(mapFilters.get(TicketSearchConstants.strLastName) == null ?
                null : mapFilters.get(TicketSearchConstants.strLastName).toString());
        req.setStationFrom(mapFilters.get(TicketSearchConstants.strStationFrom) == null ?
                null : mapFilters.get(TicketSearchConstants.strStationFrom).toString());
        req.setStationTo(mapFilters.get(TicketSearchConstants.strStationTo) == null ?
                null : mapFilters.get(TicketSearchConstants.strStationTo).toString());
        req.setCompanyName(mapFilters.get(TicketSearchConstants.strCompany) == null ?
                null : mapFilters.get(TicketSearchConstants.strCompany).toString());
        req.setAllCriteria((boolean) mapFilters.get(TicketSearchConstants.strAllCriteria));

        //set date parameters
        GregorianCalendar calendar = new GregorianCalendar();

        if (mapFilters.get(TicketSearchConstants.strDeptFrom) != null) {
            Instant instant =
                    ((LocalDate) mapFilters.get(TicketSearchConstants.strDeptFrom))
                            .atStartOfDay(ZoneId.systemDefault()).toInstant();
            calendar.setTime(Date.from(instant));
            req.setDepartureFrom(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
        } else
            req.setDepartureFrom(null);

        if (mapFilters.get(TicketSearchConstants.strDeptTo) != null) {
            Instant instant =
                    ((LocalDate) mapFilters.get(TicketSearchConstants.strDeptTo))
                            .atStartOfDay(ZoneId.systemDefault()).toInstant();
            calendar.setTime(Date.from(instant));
            req.setDepartureTo(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
        } else
            req.setDepartureTo(null);

        TicketService ticketService = new TicketService();
        GetInfo getInfo = ticketService.getPortTicketService();

        SearchTicketResponse resp = getInfo.searchTicket(req);

        for (SearchTicketRecord record : resp.getTicket()) {
            calendar = record.getDepartureDate().toGregorianCalendar();
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            format.setTimeZone(calendar.getTimeZone());

            retList.add(
                    new TicketSearchRecord(record.getTicketNum(), record.getLastName(),
                            record.getStationFrom(), record.getStationTo(), format.format(calendar.getTime()),
                            "", record.getOrderId(), record.getTicketId()));
        }

        return retList;
    }
}

package com.grigorio.rzd.OrderProcessor;

import com.grigorio.rzd.OrderProcessor.TicketServiceJob;
import com.grigorio.rzd.ticket.Ticket;

import java.util.List;

/**
 * Created by philipp on 10/10/14.
 */
public class Order extends TicketServiceJob {
    private long lOrderId;
    private List<Ticket> lstTickets;

    public long getId() {
        return lOrderId;
    }

    public Order(int id, String strToken) {
        super("Order", strToken);
        this.lOrderId = id;
    }

    public Order(long id, List<Ticket> lst, String strToken) {
        super("Order", strToken);
        lOrderId = id;
        lstTickets = lst;
    }

    public final List<Ticket> getTickets() {
        return lstTickets;
    }

    @Override
    public String toString() {
        return "{id=" + lOrderId + " Tickets: " + lstTickets.toString() + "}";
    }
}

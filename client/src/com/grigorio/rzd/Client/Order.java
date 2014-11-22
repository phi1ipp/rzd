package com.grigorio.rzd.Client;

import com.grigorio.rzd.ticket.Ticket;

import java.util.List;

/**
 * Created by philipp on 10/10/14.
 */
public class Order extends TicketServiceJob {
    private int iOrderId;
    private List<Ticket> lstTickets;

    public int getId() {
        return iOrderId;
    }

    public Order(int id, String strToken) {
        super("Order", strToken);
        this.iOrderId = id;
    }

    public Order(int id, List<Ticket> lst, String strToken) {
        super("Order", strToken);
        iOrderId = id;
        lstTickets = lst;
    }

    public final List<Ticket> getTickets() {
        return lstTickets;
    }

    @Override
    public String toString() {
        return "{id=" + iOrderId + " Tickets: " + lstTickets.toString() + "}";
    }
}

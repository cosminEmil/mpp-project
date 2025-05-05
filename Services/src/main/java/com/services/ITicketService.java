package com.services;

import com.model.Ticket;

import java.util.List;

public interface ITicketService {
    void bookTicket(Ticket ticket) throws ServicesException;
    void cancelTicket(Ticket ticket) throws ServicesException;
    Ticket findTicketByCustomerName(String customerName) throws ServicesException;
    /*

     List<Ticket> getTicketsForCustomer(String customerName) throws ServicesException;
     other special methods...

     */
}

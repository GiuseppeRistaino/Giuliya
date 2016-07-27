package com.software.ing.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by asus on 26/07/2016.
 */
public class TicketTest {
    private Ticket ticket;
    @Before
    public void setUp() throws Exception {
        assertNull(ticket);
        ticket = new Ticket();
    }

    @Test
    public void testCostruttoreVuoto(){
        assertEquals("", ticket.getId());
        assertEquals("", ticket.getData());
        assertEquals("", ticket.getPosto());
        assertEquals("", ticket.getTotale());
    }
    @Test
    public void testCostruttoreConId(){
        String id = "id";
        ticket = new Ticket(id);
        assertEquals(id, ticket.getId());
        assertEquals("", ticket.getData());
        assertEquals("", ticket.getPosto());
        assertEquals("", ticket.getTotale());
    }
    @Test
    public void testCostruttoreSenzaId() {
        String data = "nome";
        String posto = "prezzo";
        String totale = "ticket";
        ticket = new Ticket(data, posto, totale);
        assertEquals("", ticket.getId());
        assertEquals(data, ticket.getData());
        assertEquals(posto, ticket.getPosto());
        assertEquals(totale, ticket.getTotale());
    }
    @Test
    public void testSetId() throws Exception {
        String id = "id";
        ticket.setId(id);
        assertEquals(id, ticket.getId());
    }

    @Test
    public void testSetData() throws Exception {
        String data = "12/05/2012";
        ticket.setData(data);
        assertEquals(data, ticket.getData());
    }

    @Test
    public void testSetPosto() throws Exception {
        String posto = "posto";
        ticket.setId(posto);
        assertEquals(posto, ticket.getId());
    }

    @Test
    public void testSetTotale() throws Exception {
        String totale = "totale";
        ticket.setId(totale);
        assertEquals(totale, ticket.getId());
    }
}
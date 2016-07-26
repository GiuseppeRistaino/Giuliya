package com.software.ing.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * I test della classe Prodotto.
 */
public class ProdottoTest {

    private Prodotto prodotto;

    @Before
    public void setUp() throws Exception {
        assertNull(prodotto);
        prodotto = new Prodotto();
    }

    @Test
    public void testCostruttoreVuoto(){
        assertEquals("", prodotto.getId());
        assertEquals("", prodotto.getNome());
        assertEquals("", prodotto.getPrezzo());
        assertEquals("", prodotto.getTicket());
    }
    @Test
    public void testCostruttoreConId(){
        String id = "id";
        prodotto = new Prodotto(id);
        assertEquals(id, prodotto.getId());
        assertEquals("", prodotto.getNome());
        assertEquals("", prodotto.getPrezzo());
        assertEquals("", prodotto.getTicket());
    }
    @Test
    public void testCostruttoreSenzaId(){
        String nome = "nome";
        String prezzo = "prezzo";
        String ticket = "ticket";
        prodotto = new Prodotto(nome, prezzo, ticket);
        assertEquals("", prodotto.getId());
        assertEquals(nome, prodotto.getNome());
        assertEquals(prezzo, prodotto.getPrezzo());
        assertEquals(ticket, prodotto.getTicket());
    }

    @Test
    public void testSetId() throws Exception {
        String id = "id";
        prodotto.setId(id);
        assertEquals(id, prodotto.getId());
    }

    @Test
    public void testSetNome() throws Exception {
        String nome = "nome";
        prodotto.setNome(nome);
        assertEquals(nome, prodotto.getNome());
    }

    @Test
    public void testSetPrezzo() throws Exception {
        String prezzo = "prezzo";
        prodotto.setPrezzo(prezzo);
        assertEquals(prezzo, prodotto.getPrezzo());

    }

    @Test
    public void testSetTicket() throws Exception {
        String ticket = "ticket";
        prodotto.setTicket(ticket);
        assertEquals(ticket, prodotto.getTicket());
    }
}
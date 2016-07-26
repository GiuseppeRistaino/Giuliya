package com.software.ing.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * I test della classe Parola.
 */
public class ParolaTest {
    private Parola parola;

    /**
     * test creazione parola
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        assertNull(parola);
        parola = new Parola();
    }


    @Test
    public void testCostruttore(){

        assertEquals("", new Parola().getParola());
    }
    @Test
    public void testCostruttoreConVariabili(){
        String str = "Giuliya";
        int x = 9;
        int y = 11;
        int w = 12;
        int h = 15;
        parola = new Parola(str, x, y, w, h);
        assertNotNull(parola);
        assertEquals(str, parola.getParola());
        assertEquals(x, parola.getX());
        assertEquals(y, parola.getY());
        assertEquals(w, parola.getW());
        assertEquals(h, parola.getH());
    }
    @Test
    public void testSetParola() throws Exception {
        String str = "Giuliya";
        parola.setParola(str);
        assertEquals(str, parola.getParola());
    }

    @Test
    public void testSetX() throws Exception {
        int newX = 10;
        parola.setX(newX);
        assertEquals(newX, parola.getX());
    }

    @Test
    public void testSetY() throws Exception {
        int newY = 12;
        parola.setY(newY);
        assertEquals(newY, parola.getY());

    }

    @Test
    public void testSetW() throws Exception {
        int newW = 1000;
        parola.setW(newW);
        assertEquals(newW, parola.getW());
    }

    @Test
    public void testSetH() throws Exception {
        int newH = 0;
        parola.setH(newH);
        assertEquals(newH, parola.getH());
    }

    @Test
    public void testGetH() throws Exception {
        assertEquals(parola.h, parola.getH());
    }

    @Test
    public void testGetW() throws Exception {
        assertEquals(parola.w, parola.getW());
    }

    @Test
    public void testGetParola() throws Exception {
        assertEquals(parola.parola, parola.getParola());
    }

    @Test
    public void testGetX() throws Exception {
        assertEquals(parola.x, parola.getX());
    }

    @Test
    public void testGetY() throws Exception {
        assertEquals(parola.y, parola.getY());
    }
}
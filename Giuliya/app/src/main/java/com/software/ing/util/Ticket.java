package com.software.ing.util;

/**
 * Created by Giuseppe on 25/07/2016.
 * La classe dello scontrino fiscale
 */
public class Ticket {
    /**
     * Lo scontrino fiscale.
     * @param id id dello scontrino
     * @param data dello scontrino
     * @param posto il luogo dell'acquisto
     * @param totale il valore totale dell'acquisto
     */

    String id, data, posto, totale;

    /**
     * costruttore vuoto.
     * tutti i valori inizializzati a sctringhe vuote
     */
    public Ticket() {
        id = "";
        data = "";
        posto = "";
        totale = "";
    }

    /**
     * Costruttore
     * @param id id dello scontrino
     */
    public Ticket(String id) {
        this.id = id;
        data = "";
        posto = "";
        totale = "";
    }

    /**
     * Costruttore con tutti i paramentri da assegnare
     * @param id id del prodotto
     * @param data data dell'acquisto
     * @param posto il luogo dell'acquisto
     * @param totale il costo totale
     */

    public Ticket(String id, String data, String posto, String totale) {
        this.id = id;
        this.data = data;
        this.posto = posto;
        this.totale = totale;
    }

    /**
     *Costruttore senza id
     * @param data data dell'acquisto
     * @param posto il luogo dell'acquisto
     * @param totale il costo totale
     */
    public Ticket(String data, String posto, String totale) {
        id = "";
        this.data = data;
        this.posto = posto;
        this.totale = totale;
    }

    /**
     * recupera id scontrino
     * @return id scontrino
     */
    public String getId() {
        return id;
    }

    /**
     * recupera la data
     * @return la data dell'acquisto
     */

    public String getData() {
        return data;
    }

    /**
     * get luogo dell'acquisto
     * @return luogo dell'acquisto
     */
    public String getPosto() {
        return posto;
    }

    /**
     * get totale
     * @return il totale dell'acquisto
     */

    public String getTotale() {
        return totale;
    }

    /**
     * modifica id
     * @param id new id
     */

    public void setId(String id) {
        this.id = id;
    }

    /**
     * modifica dat
     * @param data new data
     */

    public void setData(String data) {
        this.data = data;
    }

    /**
     * modifica luogo
     * @param posto nuovo luogo
     */

    public void setPosto(String posto) {
        this.posto = posto;
    }

    /**
     * modifica totale
     * @param totale new totale
     */

    public void setTotale(String totale) {
        this.totale = totale;
    }
}

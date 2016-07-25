package com.software.ing.util;

/**
 * Created by Giuseppe on 25/07/2016.
 */
public class Ticket {

    String id, data, posto, totale;

    public Ticket() {
        id = "";
        data = "";
        posto = "";
        totale = "";
    }

    public Ticket(String id) {
        this.id = id;
        data = "";
        posto = "";
        totale = "";
    }

    public Ticket(String id, String data, String posto, String totale) {
        this.id = id;
        this.data = data;
        this.posto = posto;
        this.totale = totale;
    }

    public Ticket(String data, String posto, String totale) {
        id = "";
        this.data = data;
        this.posto = posto;
        this.totale = totale;
    }

    public String getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public String getPosto() {
        return posto;
    }

    public String getTotale() {
        return totale;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setPosto(String posto) {
        this.posto = posto;
    }

    public void setTotale(String totale) {
        this.totale = totale;
    }
}

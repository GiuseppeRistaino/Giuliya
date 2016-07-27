package com.software.ing.util;

/**
 * Created by Giuseppe on 25/07/2016.
 */
public class Prodotto {

    String id, nome, prezzo, ticket;

    public Prodotto() {
        this.id = "";
        nome = "";
        prezzo = "";
        ticket = "";
    }

    public Prodotto(String id) {
        this.id = id;
        nome = "";
        prezzo = "";
        ticket = "";
    }

    public Prodotto(String id, String nome, String prezzo, String ticket) {
        this.id = id;
        this.nome = nome;
        this.prezzo = prezzo;
        this.ticket = ticket;
    }

    public Prodotto(String nome, String prezzo, String ticket) {
        id = "";
        this.nome = nome;
        this.prezzo = prezzo;
        this.ticket = ticket;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getPrezzo() {
        return prezzo;
    }

    public String getTicket() {
        return ticket;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPrezzo(String prezzo) {
        this.prezzo = prezzo;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}

package com.software.ing.util;

/**
 * Created by Giuseppe on 25/07/2016.
 * La classe del Prodotto acquistato
 */
public class Prodotto {
    /**
     * La classe prodotto estratto dallo scontrino fiscale.
     * @id id del prodotto
     * @nome nome prodotto
     * @prezzo prodotto
     * @id dello scontrino fiscale a cui appartiene il seguente prodotto
     */

    String id, nome, prezzo, ticket;

    /**
     * costrutto vuoto.
     */
    public Prodotto() {
        this.id = "";
        nome = "";
        prezzo = "";
        ticket = "";
    }

    /**
     * Costruttore.
     * @param id id del prodotto
     */

    public Prodotto(String id) {
        this.id = id;
        nome = "";
        prezzo = "";
        ticket = "";
    }

    /**
     * Costruttore con tutti parametri.
     * @param id id del prodotto
     * @param nome nome del prodotto
     * @param prezzo prezzo del prodotto
     * @param ticket lo scontrino a cui appartiene il prodotto acquistato
     */
    public Prodotto(String id, String nome, String prezzo, String ticket) {
        this.id = id;
        this.nome = nome;
        this.prezzo = prezzo;
        this.ticket = ticket;
    }

    /**
     * Costruttore senza id.
     * @param nome nome del prodotto
     * @param prezzo prezzo del prodotto
     * @param ticket lo scontrino a cui appartiene il prodotto acquistato
     */
    public Prodotto(String nome, String prezzo, String ticket) {
        id = "";
        this.nome = nome;
        this.prezzo = prezzo;
        this.ticket = ticket;
    }

    /**
     * get id prodotto
     * @return id prodotto
     */
    public String getId() {
        return id;
    }

    /**
     * get nome prodotto
     * @return nome prodotto
     */

    public String getNome() {
        return nome;
    }

    /**
     * get prezzo prodotto
     * @return prezzo prodotto
     */

    public String getPrezzo() {
        return prezzo;
    }

    /**
     * get scontrino
     * @return id scontrino
     */

    public String getTicket() {
        return ticket;
    }

    /**
     * set id prodotto
     * @param id nuovo id prodotto
     */

    public void setId(String id) {
        this.id = id;
    }

    /**
     * modifica nome
     * @param nome nuovo nome
     */

    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * modifica prezzo
     * @param prezzo nuovo prezzo
     */

    public void setPrezzo(String prezzo) {
        this.prezzo = prezzo;
    }

    /**
     * modifica scontrino
     * @param ticket nuovo scontrino
     */
    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}

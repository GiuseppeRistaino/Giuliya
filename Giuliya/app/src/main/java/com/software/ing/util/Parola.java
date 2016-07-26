package com.software.ing.util;

/**
 * Created by giuse on 23/07/2016.
 * La parola estratta
 */
public class Parola {
    /**
     * La classe Parola estratta dalla stringa JSon.
     * @x coordinata x della box contenente la parola estratta
     * @y coordinata x della box contenente la parola estratta
     * @w larghezza box contenente la parola estratta
     * @h altezza box contenente la parola estratta
     */
    public String parola;
    public int x, y, w, h;

    /**
     * Costruttore parola vuota.
     */
    public Parola() {
        parola = "";
    }

    /**
     * Costruttore
     * @param parola la stringa contenente la parola.
     * @param x coordinata x della box contenente la parola estratta
     * @param y coordinata x della box contenente la parola estratta
     * @param w larghezza box contenente la parola estratta
     * @param h altezza box contenente la parola estratta
     */
    public Parola(String parola, int x, int y, int w, int h) {
        this.parola = parola;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /**
     *
     * @param parola la stringa a cui viene settato il parametro parola.
     */
    public void setParola(String parola) {
        this.parola = parola;
    }

    /**
     * set ascissa box parola.
     * @param x ascissa box parola
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * set ordinata box parola.
     * @param y ordinata box parola
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * set larghezza box parola.
     * @param w larghezza box
     */

    public void setW(int w) {
        this.w = w;
    }

    /**
     * set altezza box parola.
     * @param h altezza box
     */
    public void setH(int h) {
        this.h = h;
    }

    /**
     * get altezza.
     * @return altezza box
     */
    public int getH() {
        return h;
    }

    /**
     * get larghezza.
     * @return larghezza box
     */

    public int getW() {
        return w;
    }

    /**
     * get parola estratta.
     * @return parola estratta
     */

    public String getParola() {
        return parola;
    }

    /**
     * get ascissa box parola.
     * @return ascissa box parola
     */

    public int getX() {
        return x;
    }

    /**
     * get ordinata box parola.
     * @return ordinata box parola
     */

    public int getY() {
        return y;
    }

    /**
     * genera la stringa con tutti parametri.
     * @return stringa con tutti i i parametri
     */
    @Override
    public String toString() {
        return "Parola{" +
                "parola='" + parola + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", w=" + w +
                ", h=" + h +
                '}';
    }
}

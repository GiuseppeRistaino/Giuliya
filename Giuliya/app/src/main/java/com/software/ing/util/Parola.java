package com.software.ing.util;

/**
 * Created by giuse on 23/07/2016.
 */
public class Parola {

    public String parola;
    public int x, y, w, h;

    public Parola() {
        parola = "";
    }

    public Parola(String parola, int x, int y, int w, int h) {
        this.parola = parola;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getH() {
        return h;
    }

    public int getW() {
        return w;
    }

    public String getParola() {
        return parola;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

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

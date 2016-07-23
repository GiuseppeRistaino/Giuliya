package com.software.ing.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.software.ing.util.Parola;

import java.util.ArrayList;

/**
 * Created by giuse on 23/07/2016.
 */
public class DBTicketManager extends SQLiteOpenHelper {

    private static final String DB_NOME = "DBT";
    private static final int DB_VERSIONE = 1;

    private static final String TABLE_NAME = "ticket";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PAROLA = "parola";
    private static final String COLUMN_X = "x";
    private static final String COLUMN_Y = "y";
    private static final String COLUMN_W = "larghezza";
    private static final String COLUMN_H = "altezza";

    public DBTicketManager(Context context) {
        super(context, DB_NOME, null, DB_VERSIONE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+TABLE_NAME;
        sql += " ( " +COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sql += COLUMN_PAROLA +" TEXT NOT NULL, ";
        sql += COLUMN_X +" INTEGER, ";
        sql += COLUMN_Y +" INTEGER, ";
        sql += COLUMN_W +" INTEGER, ";
        sql += COLUMN_H +" INTEGER);";

        //Eseguiamo la query
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addParola(Parola parola) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_PAROLA, parola.getParola());
        values.put(COLUMN_X, parola.getX());
        values.put(COLUMN_Y, parola.getY());
        values.put(COLUMN_W, parola.getW());
        values.put(COLUMN_H, parola.getH());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Parola trova(String search) {
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_PAROLA + " LIKE \"%" + search + "%\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Parola p = new Parola();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            p.setParola(cursor.getString(1));
            p.setX(Integer.parseInt(cursor.getString(2)));
            p.setY(Integer.parseInt(cursor.getString(3)));
            p.setW(Integer.parseInt(cursor.getString(4)));
            p.setH(Integer.parseInt(cursor.getString(5)));
            cursor.close();
        } else {
            p = null;
        }
        db.close();
        return p;
    }

    public ArrayList<Parola> trovaParoleInLinea(int y) {
        int y1 = y-40;
        int y2 = y+40;
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_Y + " BETWEEN " +y1 +" AND " +y2;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Parola> paroleInLinea = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                Parola p = new Parola(cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                        Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5)));
                paroleInLinea.add(p);
            } while (cursor.moveToNext());
        }
        db.close();
        return paroleInLinea;
    }

    public ArrayList<Parola> trovaParoleInColonna(int x) {
        int x1 = x-40;
        int x2 = x+40;
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_X + " BETWEEN " +x1 +" AND " +x2;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Parola> paroleInLinea = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                Parola p = new Parola(cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                        Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5)));
                Log.d("EURO PRODOTTO", p.toString());
                paroleInLinea.add(p);
            } while (cursor.moveToNext());
        }
        db.close();
        return paroleInLinea;
    }

    public void deleteAll()
    {
        String query = "Select * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCursor = db.rawQuery(query, null);
        if (mCursor.moveToFirst())
        {
            db.delete(TABLE_NAME, null, null);
        }
        db.close();
    }
}

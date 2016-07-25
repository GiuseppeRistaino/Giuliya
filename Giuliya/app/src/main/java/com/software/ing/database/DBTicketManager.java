package com.software.ing.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.software.ing.util.Parola;
import com.software.ing.util.Prodotto;
import com.software.ing.util.Ticket;

import java.util.ArrayList;

/**
 * Created by giuse on 23/07/2016.
 */
public class DBTicketManager extends SQLiteOpenHelper {

    private static final String DB_NOME = "DBT";
    private static final int DB_VERSIONE = 1;

    private static final String TABLE_WORDS = "word";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PAROLA = "parola";
    private static final String COLUMN_X = "x";
    private static final String COLUMN_Y = "y";
    private static final String COLUMN_W = "larghezza";
    private static final String COLUMN_H = "altezza";

    private static final String TABLE_TICKET = "ticket";
    private static final String COLUMN_ID_TICKET = "_id";
    private static final String COLUMN_DATA_TICKET = "data";
    private static final String COLUMN_POSTO_TICKET = "posto";
    private static final String COLUMN_TOTALE_TICKET = "totale";

    private static final String TABLE_PRODOTTI = "prodotto";
    private static final String COLUMN_ID_PRODOTTO = "_id";
    private static final String COLUMN_NOME_PRODOTTO = "nome";
    private static final String COLUMN_PREZZO_PRODOTTO = "prezzo";
    private static final String COLUMN_TICKET_PRODOTTO = "ticket";

    private static final String CREATE_TABLE_WORDS = "CREATE TABLE "+TABLE_WORDS
        + " ( " +COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
        + COLUMN_PAROLA +" TEXT NOT NULL, "
        + COLUMN_X +" INTEGER, "
        + COLUMN_Y +" INTEGER, "
        + COLUMN_W +" INTEGER, "
        + COLUMN_H +" INTEGER);";

    private static final String CREATE_TABLE_TICKET = "CREATE TABLE "+TABLE_TICKET
            + " ( " +COLUMN_ID_TICKET +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DATA_TICKET +" TEXT, "
            + COLUMN_POSTO_TICKET +" TEXT, "
            + COLUMN_TOTALE_TICKET + " TEXT);";

    private static final String CREATE_TABLE_PRODOTTO = "CREATE TABLE "+TABLE_PRODOTTI
            + " ( " +COLUMN_ID_PRODOTTO +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NOME_PRODOTTO +" TEXT, "
            + COLUMN_PREZZO_PRODOTTO +" TEXT,"
            + COLUMN_TICKET_PRODOTTO +" TEXT);";

    public DBTicketManager(Context context) {
        super(context, DB_NOME, null, DB_VERSIONE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Eseguiamo la query
        db.execSQL(CREATE_TABLE_WORDS);
        db.execSQL(CREATE_TABLE_TICKET);
        db.execSQL(CREATE_TABLE_PRODOTTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
        onCreate(db);
    }

    public void addTicket(Ticket ticket) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATA_TICKET, ticket.getData());
        values.put(COLUMN_POSTO_TICKET, ticket.getPosto());
        values.put(COLUMN_TOTALE_TICKET, ticket.getTotale());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_TICKET, null, values);
        db.close();
    }

    public void addProdotto(Prodotto prodotto) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME_PRODOTTO, prodotto.getNome());
        values.put(COLUMN_PREZZO_PRODOTTO, prodotto.getPrezzo());
        values.put(COLUMN_TICKET_PRODOTTO, prodotto.getTicket());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PRODOTTI, null, values);
        db.close();
    }

    public void addParola(Parola parola) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_PAROLA, parola.getParola());
        values.put(COLUMN_X, parola.getX());
        values.put(COLUMN_Y, parola.getY());
        values.put(COLUMN_W, parola.getW());
        values.put(COLUMN_H, parola.getH());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_WORDS, null, values);
        db.close();
    }


    public Parola trovaParola(String search) {
        String query = "Select * FROM " + TABLE_WORDS + " WHERE " + COLUMN_PAROLA + " LIKE \"%" + search + "%\"";

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
        String query = "Select * FROM " + TABLE_WORDS + " WHERE " + COLUMN_Y + " BETWEEN " +y1 +" AND " +y2;

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
        String query = "Select * FROM " + TABLE_WORDS + " WHERE " + COLUMN_X + " BETWEEN " +x1 +" AND " +x2;

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

    public void deleteAllWords()
    {
        String query = "Select * FROM " + TABLE_WORDS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCursor = db.rawQuery(query, null);
        if (mCursor.moveToFirst())
        {
            db.delete(TABLE_WORDS, null, null);
        }
        db.close();
    }

    public Ticket getUltimoTicketInserito()
    {
        String query = "SELECT * FROM " +TABLE_TICKET
                +" ORDER BY " +COLUMN_ID_TICKET +" DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Ticket t = null;
        if (cursor != null) {
            cursor.moveToFirst();
            t = new Ticket(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3));
        }
        Log.d("TICKET", t.getTotale());
        db.close();
        return t;
    }

    public ArrayList<Ticket> getScontrini() {
        String query = "Select * FROM " + TABLE_TICKET;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Ticket> tickets = new ArrayList<>();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            do {
                Ticket p = new Ticket(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3));
                tickets.add(p);
            } while (cursor.moveToNext());
        }
        db.close();
        return tickets;
    }

}

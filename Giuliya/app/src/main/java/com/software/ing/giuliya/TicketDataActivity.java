package com.software.ing.giuliya;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.software.ing.Adapters.AdapterPaginaProdotti;
import com.software.ing.database.DBTicketManager;
import com.software.ing.util.Prodotto;
import com.software.ing.util.Ticket;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class TicketDataActivity extends AppCompatActivity {

    //HashMap che contiene l'ordinata delle parole che sono sulla stessa riga (Key) e un array che contiene le parole che sono sulla stessa riga (value)
    HashMap<String, ArrayList<String>> wordsMapTest = new HashMap<String, ArrayList<String>>();

    TextView textViewTotaleEuro;
    ListView listViewProdotti;
    Button buttonConferma;
    TextView textViewData;
    Button buttonAddProdotto;

    DBTicketManager dbTicketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_data);

        Intent intent = getIntent();

        final String totale = intent.getStringExtra(MainActivity.OCR_RESULT_KEY);
        final ArrayList<String> listaProdotti = intent.getStringArrayListExtra(MainActivity.LISTA_PRODOTTI_KEY);

        textViewTotaleEuro = (TextView) findViewById(R.id.textViewt_totale);
        listViewProdotti = (ListView) findViewById(R.id.listView_Prodotti);
        buttonConferma = (Button) findViewById(R.id.button_conferma);
        textViewData = (TextView) findViewById(R.id.textView_data);
        buttonAddProdotto = (Button) findViewById(R.id.button_add_prodotto);

        textViewTotaleEuro.setText(totale);

        final AdapterPaginaProdotti adapterPaginaProdotti = new AdapterPaginaProdotti(this, R.layout.layout_elemento_lista_prodotti, listaProdotti);

        listViewProdotti.setAdapter(adapterPaginaProdotti);

        //possibilità di modificare il prodotto quando si clicca su un elemento della lista
        listViewProdotti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
            }
        });

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        final String dataString = dateFormat.format(date);
        textViewData.setText(date.toString());

        buttonConferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbTicketManager = new DBTicketManager(TicketDataActivity.this);
                Ticket ticket = new Ticket(dataString, "", textViewTotaleEuro.getText().toString());
                dbTicketManager.addTicket(ticket);
                ticket = dbTicketManager.getUltimoTicketInserito();
                String nomeProdotto = "";
                String prezzoProdotto = "";
                if (ticket != null) {
                    for (String s : listaProdotti) {
                        String[] split = s.split("\\s+");
                        for (int i = 0; i < split.length; i++) {
                            if (split[i].contains(",") || split[i].contains(".")) {
                                prezzoProdotto = split[i];
                            } else
                                nomeProdotto += split[i];
                        }
                        Prodotto prodotto = new Prodotto(nomeProdotto, prezzoProdotto, ticket.getId());
                        nomeProdotto = "";
                        dbTicketManager.addProdotto(prodotto);
                    }
                }
                AlertDialog alertDialog = new AlertDialog.Builder(TicketDataActivity.this).create();
                alertDialog.setTitle("Dati salvati");
                alertDialog.setMessage("I dati dello scontrino sono stati salvati correttamente");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(TicketDataActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.show();

            }
        });


        buttonAddProdotto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(TicketDataActivity.this);
                View promptsView = li.inflate(R.layout.dialog_add_prodotto, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        TicketDataActivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInputNomeProdotto = (EditText) promptsView
                        .findViewById(R.id.editText_nome_prodotto);

                final EditText userInputPrezzoProdotto = (EditText) promptsView
                        .findViewById(R.id.editText_prezzo_prodotto);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        // edit text
                                        String nomeProdotto = userInputNomeProdotto.getText().toString();
                                        String prezzoProdotto = userInputPrezzoProdotto.getText().toString();
                                        if (!nomeProdotto.isEmpty() && !prezzoProdotto.isEmpty()) {
                                            String prodotto = userInputNomeProdotto.getText().toString()
                                                    + " " + userInputPrezzoProdotto.getText().toString();
                                            listaProdotti.add(prodotto);
                                            adapterPaginaProdotti.notifyDataSetChanged();
                                            Double prezzo = Double.parseDouble(userInputPrezzoProdotto.getText().toString());
                                            Double totale = Double.parseDouble(textViewTotaleEuro.getText().toString());
                                            totale += prezzo;
                                            textViewTotaleEuro.setText(Double.toString(totale));
                                        } else
                                            dialog.cancel();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }



}

package com.software.ing.giuliya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.software.ing.Adapters.AdapterPaginaProdotti;
import com.software.ing.database.DBTicketManager;
import com.software.ing.util.Prodotto;
import com.software.ing.util.Ticket;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class TicketDataActivity extends AppCompatActivity {

    /**
     * @param wordsMapTest HashMap che contiene l'ordinata delle parole che sono sulla stessa riga (Key).
     * e un array che contiene le parole che sono sulla stessa riga (value)
     * @param textViewTotaleEuro totale euro
     * @param buttonConferma pulsnte conferma di dati acquisiti
     * @param dbTicketManager database
     */
    HashMap<String, ArrayList<String>> wordsMapTest = new HashMap<String, ArrayList<String>>();

    TextView textViewTotaleEuro;
    ListView listViewProdotti;
    Button buttonConferma;
    TextView textViewData;

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

        textViewTotaleEuro.setText(totale);

        final AdapterPaginaProdotti adapterPaginaProdotti = new AdapterPaginaProdotti(this, R.layout.layout_elemento_lista_prodotti, listaProdotti);

        listViewProdotti.setAdapter(adapterPaginaProdotti);

        listViewProdotti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Intent openPageArmi = new Intent(PaginaListaArmi.this, PaginaArma.class);
                String extra = (String)parent.getItemAtPosition(position);
                openPageArmi.putExtra("arma", extra);
                startActivity(openPageArmi);*/
            }
        });

        Calendar calendar = Calendar.getInstance();
        final Date date = calendar.getTime();
        textViewData.setText(date.toString());

        buttonConferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbTicketManager = new DBTicketManager(TicketDataActivity.this);
                Ticket ticket = new Ticket(date.toString(), "", totale);
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
                            } else {
                                nomeProdotto += split[i];
                            }
                        }
                        Prodotto prodotto = new Prodotto(nomeProdotto, prezzoProdotto, ticket.getId());
                        dbTicketManager.addProdotto(prodotto);
                    }
                }
                Intent intent = new Intent(TicketDataActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }





}

package com.software.ing.giuliya;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.software.ing.Adapters.AdapterPaginaProdottiTicket;
import com.software.ing.Adapters.AdapterPaginaScontrini;
import com.software.ing.database.DBTicketManager;
import com.software.ing.util.Prodotto;

import java.util.ArrayList;

public class ListaProdottiActivity extends AppCompatActivity {

    ListView listViewProdottiTicket;
    DBTicketManager dbTicketManager;
    ArrayList<Prodotto> prodotti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_prodotti);

        Intent intent = getIntent();

        final String idTicket = intent.getStringExtra(ListaTicketsActivity.ID_TICKET_KEY);


        listViewProdottiTicket = (ListView) findViewById(R.id.listView_prodotti_ticket);

        dbTicketManager = new DBTicketManager(this);

        prodotti = dbTicketManager.getProdotti(idTicket);

        if (!prodotti.isEmpty() && prodotti != null) {
            final AdapterPaginaProdottiTicket adapterPaginaProdottiTicket = new AdapterPaginaProdottiTicket(this, R.layout.layout_elemento_lista_prodotti_ticket, prodotti);
            listViewProdottiTicket.setAdapter(adapterPaginaProdottiTicket);
        }
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(ListaProdottiActivity.this).create();
            alertDialog.setTitle("Prodotti non trovati");
            alertDialog.setMessage("Non sono presenti prodotti sullo scontrino selezionato");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ListaProdottiActivity.this, ListaTicketsActivity.class);
                    startActivity(intent);
                }
            });
            alertDialog.show();
        }
    }
}

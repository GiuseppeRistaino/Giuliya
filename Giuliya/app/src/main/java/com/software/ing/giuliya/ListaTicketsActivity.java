package com.software.ing.giuliya;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.software.ing.Adapters.AdapterPaginaProdotti;
import com.software.ing.Adapters.AdapterPaginaScontrini;
import com.software.ing.database.DBTicketManager;
import com.software.ing.util.Ticket;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListaTicketsActivity extends AppCompatActivity {

    ListView listViewScontrini;

    DBTicketManager dbTicketManager;
    ArrayList<Ticket> tickets = new ArrayList<>();

    public static final String ID_TICKET_KEY = "ID_TICKET";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_tickets);

        listViewScontrini = (ListView) findViewById(R.id.listView_scontrini);

        dbTicketManager = new DBTicketManager(this);

        tickets = dbTicketManager.getScontrini();

        if (!tickets.isEmpty() && tickets != null) {
            final AdapterPaginaScontrini adapterPaginaScontrini = new AdapterPaginaScontrini(this, R.layout.layout_elemento_lista_scontrini, tickets);
            listViewScontrini.setAdapter(adapterPaginaScontrini);
            listViewScontrini.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ListaTicketsActivity.this, ListaProdottiActivity.class);
                    Ticket ticket = (Ticket) parent.getItemAtPosition(position);
                    intent.putExtra(ID_TICKET_KEY, ticket.getId());
                    startActivity(intent);
                }
            });
        }
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(ListaTicketsActivity.this).create();
            alertDialog.setTitle("Scontrini non trovati");
            alertDialog.setMessage("Non sono stati ancora registrati scontrini nell'applicazione");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ListaTicketsActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            alertDialog.show();
        }
    }
}

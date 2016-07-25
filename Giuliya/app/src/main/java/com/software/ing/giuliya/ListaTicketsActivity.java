package com.software.ing.giuliya;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_tickets);

        listViewScontrini = (ListView) findViewById(R.id.listView_scontrini);

        dbTicketManager = new DBTicketManager(this);

        tickets = dbTicketManager.getScontrini();

        final AdapterPaginaScontrini adapterPaginaScontrini = new AdapterPaginaScontrini(this, R.layout.layout_elemento_lista_scontrini, tickets);

        listViewScontrini.setAdapter(adapterPaginaScontrini);

    }
}

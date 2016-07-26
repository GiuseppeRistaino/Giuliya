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

/**
 * La scermata di visualizzazione degli scontrini
 *
 */

public class ListaTicketsActivity extends AppCompatActivity {
    /**
     * @param listView_scontrini la list view per visualizzare gli scontrini
     * @param dbTicketManager il database contenente i dati estratti
     * @param tickets la lista degli scontrini salvati
     */

    ListView listViewScontrini;
    DBTicketManager dbTicketManager;
    ArrayList<Ticket> tickets = new ArrayList<>();

    /**
     * Inizializzazione dell'activity e creazione dell'interfaccia dal file activity_lista_tickets
     * @param savedInstanceState
     */
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

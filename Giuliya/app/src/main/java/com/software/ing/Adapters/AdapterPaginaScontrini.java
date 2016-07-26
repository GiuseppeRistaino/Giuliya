package com.software.ing.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.software.ing.giuliya.R;
import com.software.ing.util.Ticket;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter pagina Scontrini.
 */
public class AdapterPaginaScontrini extends ArrayAdapter<Ticket> {

    private int resource;
    private Context context;
    private List<Ticket> values;
    private ArrayList<Ticket> valuesOrigin;

    public AdapterPaginaScontrini(Context context, int resource, List<Ticket> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.values = objects;
        valuesOrigin = new ArrayList<Ticket>();
        valuesOrigin.addAll(values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resource, parent, false);

        Ticket ticket = getItem(position);

        TextView textViewId = (TextView) rowView.findViewById(R.id.textView_layout_elemento_lista_scontrini_id);
        textViewId.setText(ticket.getId());

        TextView textViewTotale = (TextView) rowView.findViewById(R.id.textView_layout_elemento_lista_scontrini_totale);
        textViewTotale.setText(ticket.getTotale());

        TextView textViewData = (TextView) rowView.findViewById(R.id.textView_layout_elemento_lista_scontrini_data);
        textViewData.setText(ticket.getData());

        //String s = values.get(position);
        //Fai qualcosa con la variabile s sempre se ti serve...

        return rowView;
    }

    /*public void filter(String charText) {
        charText = charText.toLowerCase();
        values.clear();
        if (charText.length() == 0) {
            values.addAll(valuesOrigin);
        } else {
            for (String x : valuesOrigin) {
                if (x.toLowerCase().contains(charText)) {
                    values.add(x);
                }
            }
        }
        notifyDataSetChanged();
    }*/


}

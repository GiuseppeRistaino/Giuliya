package com.software.ing.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.software.ing.giuliya.R;
import com.software.ing.util.Prodotto;
import com.software.ing.util.Ticket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giuseppe on 26/07/2016.
 */
public class AdapterPaginaProdottiTicket extends ArrayAdapter<Prodotto> {

    private int resource;
    private Context context;
    private List<Prodotto> values;
    private ArrayList<Prodotto> valuesOrigin;

    public AdapterPaginaProdottiTicket(Context context, int resource, List<Prodotto> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.values = objects;
        valuesOrigin = new ArrayList<Prodotto>();
        valuesOrigin.addAll(values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resource, parent, false);

        Prodotto prodotto = getItem(position);

        TextView textViewId = (TextView) rowView.findViewById(R.id.textView_layout_elemento_lista_prodotto_id);
        textViewId.setText(prodotto.getId());

        TextView textViewTotale = (TextView) rowView.findViewById(R.id.textView_layout_elemento_lista_prodotto_nome);
        textViewTotale.setText(prodotto.getNome());

        TextView textViewData = (TextView) rowView.findViewById(R.id.textView_layout_elemento_lista_prodotto_prezzo);
        textViewData.setText(prodotto.getPrezzo());

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
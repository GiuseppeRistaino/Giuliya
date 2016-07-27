package com.software.ing.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.software.ing.giuliya.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vrexas on 04/02/2016.
 */
public class AdapterPaginaProdotti extends ArrayAdapter<String> {

    private int resource;
    private Context context;
    private List<String> values;
    private ArrayList<String> valuesOrigin;

    public AdapterPaginaProdotti(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.values = objects;
        valuesOrigin = new ArrayList<String>();
        valuesOrigin.addAll(values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resource, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.textView_layout_elemento_lista_prodotti_nome);
        textView.setText(values.get(position));

        String s = values.get(position);
        //Fai qualcosa con la variabile s sempre se ti serve...

        return rowView;
    }

    public void filter(String charText) {
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
    }


}

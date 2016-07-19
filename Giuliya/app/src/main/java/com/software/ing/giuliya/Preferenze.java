package com.software.ing.giuliya;

/**
 * Created by asus on 12/07/2016.
 * Ultima modifica: 19/07/2016
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/** Preferenze è la classe che permette all'utente
 * di inserire i valori di:
 * budget, soglia della notifica, il periodo  di valutazione.*/
public class Preferenze extends Activity {
    /** edittext dove va inserito il valore del budget.*/
    private EditText etb;
    /** edittext dove va inserito il valore della soglia.*/
    private EditText ets;
    /** radiogroup di 4 possibili valori da selezionare.
     * I valori sono: giornaliero, settimanale, mensile, annuale*/
    private RadioGroup radioGroup;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferenze);
    }
    /**
     * Quest metodo inizializza la schermata delle shared preferences
     * con, se esistenti, i valori precedentemente inseriti
     * e salvati dall'utente.
     */
    @Override
    protected void onStart() {
        super.onStart();

        etb = (EditText) findViewById(R.id.editText_budget);
        ets = (EditText) findViewById(R.id.editText_soglia);
        radioGroup = (RadioGroup) findViewById(R.id.shared_radiogroup);
        SharedPreferences settings = getSharedPreferences
                (getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
        etb.setText(settings.getString(getString(R.string.shared_pref_budget), ""));
        ets.setText(settings.getString(getString(R.string.shared_pref_soglia), ""));
        int index = settings.getInt("selected_radio", 2); //di default il mensile
        radioGroup.check(radioGroup.getChildAt(index).getId());
    }

    /**
     * Quest metodo salva nelle SharedPreferences i valori inseriti dall'utente.
     * L'utente clicca sul pulsante "salva"
     * @param view il pulsante salva
     */
    public void salvaBudget(View view) {
        SharedPreferences settings = getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        Context context = getApplicationContext();

        double budget, soglia;
        String budgetparse = etb.getText().toString();
        String sogliaparse = ets.getText().toString();
        System.out.println("budgetparse" + budgetparse);
        if (budgetparse.equals("")) {
            CharSequence text = "i valori del budget e della soglia verranno trascurati";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            editor.putString(getString(R.string.shared_pref_budget), budgetparse);
            editor.putString(getString(R.string.shared_pref_soglia), sogliaparse);
            editor.commit();
            this.finish();
        } else {
            editor.putString(getString(R.string.shared_pref_budget), budgetparse);
            editor.putString(getString(R.string.shared_pref_soglia), sogliaparse);
            budget = Double.parseDouble(budgetparse);
            if (sogliaparse.equals("")) {
                soglia = 0;
            } else {
                soglia = Double.parseDouble(sogliaparse);
            }
            if (soglia >= budget && !budgetparse.equals("")) {
                CharSequence text = "il valore della soglia di notifica deve essere minore del budget";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            } else {
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);
                int idx = radioGroup.indexOfChild(radioButton);
                String visPeriod = (String) radioButton.getText();
                editor.putInt("selected_radio", idx);
                editor.putString("selected_value", visPeriod);
                editor.commit();
                this.finish();
            }
        }
    }
    /**
     * Quest metodo esce dalla scermata di SharedPreferences
     * senza salvare i valori inseriti dall'utente
     * che clicca sul pulsante "annulla".
     * @param view il pulsante annulla.
     */
    public void annulla(View view) {
        this.finish();
    }
}

package com.software.ing.giuliya;

/**
 * Created by asus on 12/07/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import static android.widget.Toast.makeText;


public class Preferenze extends Activity {

    private EditText etb;
    private EditText ets;
    private RadioGroup radioGroup;
    private RadioButton selectedRadio;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferenze);
    }
    @Override
    protected void onStart() {
        super.onStart();

        etb =(EditText) findViewById(R.id.editText_budget);
        ets =(EditText) findViewById(R.id.editText_soglia);
        radioGroup = (RadioGroup)findViewById(R.id.shared_radiogroup);
        SharedPreferences settings=getSharedPreferences(getString(R.string.shared_pref_file_name),Context.MODE_PRIVATE);
        etb.setText(settings.getString(getString(R.string.shared_pref_budget),""));
        ets.setText(settings.getString(getString(R.string.shared_pref_soglia),""));
        int index = settings.getInt("selected_radio", 2); //di default il mensile
        radioGroup.check(radioGroup.getChildAt(index).getId());

    }

    public void salvaBudget(View view) {
        SharedPreferences settings=getSharedPreferences(getString(R.string.shared_pref_file_name),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(getString(R.string.shared_pref_budget), etb.getText().toString());
        double budget, soglia;
        budget = Double.parseDouble(etb.getText().toString());
        editor.putString(getString(R.string.shared_pref_soglia), ets.getText().toString());
        soglia = Double.parseDouble(ets.getText().toString());
        if(soglia>=budget){
            Context context = getApplicationContext();
            CharSequence text = "il valore della soglia di notifica deve essere minore del budget";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else{
            int radioButtonID = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton)radioGroup.findViewById(radioButtonID);
            int idx = radioGroup.indexOfChild(radioButton);
            String visPeriod = (String)radioButton.getText();
            editor.putInt("selected_radio", idx);
            editor.putString("selected_value", visPeriod);

            editor.commit();
            this.finish();
        }

    }
    public void annulla(View view){
        this.finish();
    }

}

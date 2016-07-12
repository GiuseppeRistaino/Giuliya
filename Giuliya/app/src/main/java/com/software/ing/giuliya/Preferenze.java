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
    private RadioButton radioButton;
    final String KEY_SAVED_RADIO_BUTTON_INDEX = "SAVED_RADIO_BUTTON_INDEX";

    //private static boolean debug=false;

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

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
    /*
     * salva le preferenze
     *
     */
    public void salvaBudget(View view) {
        SharedPreferences settings=getSharedPreferences(getString(R.string.shared_pref_file_name),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(getString(R.string.shared_pref_budget), etb.getText().toString());
        editor.putString(getString(R.string.shared_pref_soglia), ets.getText().toString());

        if(Float.parseFloat(etb.getText().toString())==0){
            makeText(this, getString(R.string.toast_notification_budget), Toast.LENGTH_SHORT).show();
        }
        else {
            editor.commit();
            this.finish();
        }
    }
    public void annulla(View view){
        this.finish();
    }

}

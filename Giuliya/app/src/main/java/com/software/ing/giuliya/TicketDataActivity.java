package com.software.ing.giuliya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

public class TicketDataActivity extends AppCompatActivity {

    //HashMap che contiene l'ordinata delle parole che sono sulla stessa riga (Key) e un array che contiene le parole che sono sulla stessa riga (value)
    HashMap<String, ArrayList<String>> wordsMapTest = new HashMap<String, ArrayList<String>>();

    EditText editText;
    TextView textViewTotaleEuro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_data);

        Intent intent = getIntent();

        String result = intent.getStringExtra(MainActivity.OCR_RESULT_KEY);
        //getWordsMap(result);

        //jsonManager = new JsonManager(result);

        //getWords(result);
        editText = (EditText) this.findViewById(R.id.etResponse);
        textViewTotaleEuro = (TextView) findViewById(R.id.textView_totale_euro);
        //editText.setText(stampaLinea());
        //editText.setText(getWords(result));
        //editText.setText(result);

        editText.setText(result);
        textViewTotaleEuro.setText(result);
    }





}

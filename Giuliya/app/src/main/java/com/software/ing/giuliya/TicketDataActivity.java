package com.software.ing.giuliya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
        getWordsMap(result);

        editText = (EditText) this.findViewById(R.id.etResponse);
        editText.setText(stampaLinea());

        textViewTotaleEuro = (TextView) findViewById(R.id.textView_totale_euro);
        textViewTotaleEuro.setText(getTotale());
    }


    //restituisce l'hashMap che contiene tutte le parole che sono contentute nel file JSON creato dall'OCR
    private HashMap<String, String> getWordsMap(String result) {
        HashMap<String, String> wordsMap = new HashMap<String, String>();
        try {
            JSONObject obj = new JSONObject(result);
            JSONArray regions = obj.getJSONArray("regions");
            for (int i = 0; i < regions.length(); i++) {
                JSONArray lines = regions.getJSONObject(i).getJSONArray("lines");
                for (int j = 0; j < lines.length(); j++) {
                    JSONArray words = lines.getJSONObject(j).getJSONArray("words");
                    for (int z = 0; z < words.length(); z++) {
                        //Nell'hashMap la chiave è l'ordinata del rettangolo che contiene la stringa del valore a cui si riferisce
                        //La chiave è presa partizionando la stringa del boundingBox ogni volta che si incontra una "," (virgola)
                        //Nell'array che si viene a formare la seconda posizione è occupata dall'ordinata del boundingBox, la prima posizione è l'ascissa, le restanti sono l'altezza e la larghezza
                        String wordBox = words.getJSONObject(z).getString("boundingBox");
                        String segments[] = wordBox.split(",");

                        Set key = wordsMapTest.entrySet();
                        Iterator iter= key.iterator();
                        boolean aggiunto = false;
                        String parola = words.getJSONObject(z).getString("text") +"-"+segments[0];
                        while (iter.hasNext()) {
                            Map.Entry mentry = (Map.Entry)iter.next();
                            Log.d("MAP:ENTRY", mentry.getKey().toString());
                            int y1 = Integer.parseInt(mentry.getKey().toString());
                            int y2 = Integer.parseInt(segments[1]);
                            if (isOnSameLine(y1, y2)) {
                                ArrayList<String> wordsList = wordsMapTest.get(mentry.getKey().toString());
                                Log.d("PAROLA", wordsList.get(0));
                                wordsList.add(parola);
                                aggiunto = true;
                                break;
                            }
                        }
                        if (!aggiunto) {
                            ArrayList<String> linea = new ArrayList<>();
                            wordsMapTest.put(segments[1], linea);
                            linea.add(parola);
                        }

                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return wordsMap;
    }

    public String stampaLinea() {
        Set set = wordsMapTest.entrySet();
        Iterator i = set.iterator();
        String totalWords = "";
        while (i.hasNext()) {
            Map.Entry mentry = (Map.Entry)i.next();
            ArrayList<String> lines = wordsMapTest.get(mentry.getKey());

            for (String s : lines) {
                totalWords += "---" +s;
            }
            totalWords += "\n";
        }
        return  totalWords;
    }

    //Metodo per verificare se due parole sono sulla stessa riga
    public boolean isOnSameLine (int y1, int y2) {
        boolean isSame = false;
        if (y1-35 <= y2 && y2 <= y1+35) isSame = true;
        return isSame;
    }

    public String getTotale() {
        String totale = "";

        Set key = wordsMapTest.entrySet();
        Iterator iter= key.iterator();
        while (iter.hasNext()) {
            Map.Entry mentry = (Map.Entry)iter.next();
            ArrayList<String> wordsList = wordsMapTest.get(mentry.getKey().toString());
            for (String s : wordsList) {
                if (s.equalsIgnoreCase("TOTALE")) {
                    //PRENDITI SOLO IL NUMERO
                    //Prendi la parola che stà più a destra
                    totale = getWordToRight(wordsList);
                }
            }

        }
        return totale;
    }


    public String getWordToRight(ArrayList<String> words) {
        String wordToRight = "";
        for (String s1 : words) {
            String[] segments1 = s1.split("-");
            int x1 = Integer.parseInt(segments1[1]);
            for (String s2 : words) {
                String[] segments2 = s2.split("-");
                int x2 = Integer.parseInt(segments2[1]);
                if (x2 > x1) wordToRight = segments2[0];
                else wordToRight = segments1[0];
            }
        }
        return wordToRight;
    }


}

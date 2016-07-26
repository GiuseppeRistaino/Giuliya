package com.software.ing.giuliya;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.software.ing.database.DBTicketManager;
import com.software.ing.util.Parola;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * L'activity principale dell'applicazione.
 */

public class MainActivity extends AppCompatActivity {

    /**
     * @param buttonFoto il pulsante per oprire la fotocamera.
     * @param buttonScontrini il pulsante che apre l'activity
     *                        per visualizzare gli scontrini salvati.
     * @param tvTotaleVal TextView contenente il valore totale della spesa
     *                    il valore viene calcolato a partire dal database
     * @param tvBudgetVal il valore specificato dall'utente nelle
     *                    shared preferences
     * @param dbTicketManager il database contenente tutti i dati salvati
     */
    ImageButton buttonFoto;
    ImageButton buttonScontrini;
    TextView tvBudgetVal, tvTotaleVal;
    DBTicketManager dbTicketManager;

    public static final String OCR_RESULT_KEY = "OCR_RESULT";
    public static final String LISTA_PRODOTTI_KEY = "LISTA_PRODOTTI_KEY";

    protected String _path;

    //HashMap che contiene l'ordinata delle parole che sono sulla stessa riga (Key) e un array che contiene le parole che sono sulla stessa riga (value)
    HashMap<String, ArrayList<String>> wordsMapTest = new HashMap<String, ArrayList<String>>();

    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/Giuliya/";
    private static final String TAG = "MainActivity.java";

    /**
     * Inizializzazione dell'applicazione.
     * Vengono settati i valori di budget e del totale.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences settings = getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
        dbTicketManager = new DBTicketManager(this);
        // Inizializzazione EditText per testare la risposta del server
        //etResponse = (EditText) findViewById(R.id.etResponse);
        //Inizializzazione TextView per visualizzare il controllo della connessione
        //tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
        //Inizializzazione del bottone per la fotocamera

        tvBudgetVal = (TextView)   findViewById(R.id.val_budget);
        String valBudget = settings.getString(getString(R.string.shared_pref_budget), "");
        String valtotale = "valtotale"; //calcolare dal database
        tvBudgetVal.setText(valBudget);
        tvTotaleVal = (TextView)   findViewById(R.id.val_totale);
        tvTotaleVal.setText(valtotale);
        buttonFoto = (ImageButton) findViewById(R.id.button_Foto);
        //Apertura della fotocamera
        buttonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCameraActivity();
            }
        });

        buttonScontrini = (ImageButton) findViewById(R.id.button_Scontrini);
        buttonScontrini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListaTicketsActivity.class);
                startActivity(intent);
            }
        });


        //Inizializzazione di path utilizzati per l'immagazzinamento die dati
        String[] paths = new String[] {DATA_PATH, DATA_PATH + "data/"};

        //Verifica della creazione dei path
        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
                    return;
                } else {
                    Log.v(TAG, "Created directory " + path + " on sdcard");
                }
            }
        }

        //Percorso dove si trova l'immagine da utilizzare per l'OCR
        _path = DATA_PATH + "/ocr.jpg";

    }

    /**
     * check se c'è la connessione.
     * @return booleano true se c'è la connessione
     */
    // check network connection
    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Task per la comunicazione con il server di Microsoft per l'OCR
     */
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            File file = new File(_path);
            if (file.exists()) {
                return GetOCR();
            } else {
                return "file don't exist";
            }
        }

        /**
         * onPostExecute displays the results of the AsyncTask.
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            dbTicketManager.deleteAllWords();
            riempiDB(result);
            Parola totale = dbTicketManager.trovaParola("TOTALE");
            if (totale != null) {
                ArrayList<Parola> paroleInLinea = new ArrayList<>();
                ArrayList<Parola> paroleInColonna = new ArrayList<>();
                ArrayList<String> prodotti = new ArrayList<>();
                paroleInLinea = dbTicketManager.trovaParoleInLinea(totale.getY());
                Parola totaleEuro = getParolaDestra(paroleInLinea);
                if (totaleEuro != null) {
                    paroleInColonna = dbTicketManager.trovaParoleInColonna(totaleEuro.getX());
                    paroleInColonna = getEuroProdotti(paroleInColonna);
                    String prodottoLinea = "";
                    for (Parola p : paroleInColonna) {
                        if (totaleEuro.getY() > p.getY()) {
                            paroleInLinea = dbTicketManager.trovaParoleInLinea(p.getY());
                                for (Parola par : paroleInLinea) {
                                    prodottoLinea += par.getParola() + " ";
                            }
                            //if (!prodottoLinea.contains("TOTALE"))
                            prodotti.add(prodottoLinea);
                            prodottoLinea = "";
                        }
                    }
                    Intent intent = new Intent(MainActivity.this, TicketDataActivity.class);
                    intent.putExtra(OCR_RESULT_KEY, totaleEuro.getParola());
                    intent.putStringArrayListExtra(LISTA_PRODOTTI_KEY, prodotti);
                    startActivity(intent);
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Importo totale non trovato");
                    alertDialog.setMessage("Si prega di scattare nuovamente la foto, oppure inserire manualmente i dati dello scontrino.");
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startCameraActivity();
                        }
                    });
                    alertDialog.show();
                }
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Dati non trovati");
                alertDialog.setMessage("Si prega di scattare nuovamente la foto, oppure inserire manualmente i dati dello scontrino.");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startCameraActivity();
                    }
                });
                alertDialog.show();
            }

        }
    }

    /**
     * Metodo per la richiesta di OCR dal server
     * @return la stringa in formato JSON con le parole estratte
     * (o codice d'errore)
     */
    public String GetOCR() {

        HttpClient httpclient = new DefaultHttpClient();
        String result = "";

        try {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.projectoxford.ai")
                    .appendPath("vision")
                    .appendPath("v1.0")
                    .appendPath("ocr")
                    .appendQueryParameter("language", "it")
                    .appendQueryParameter("detectOrientation", "true");

            HttpPost request = new HttpPost(builder.build().toString());
            request.setHeader("Content-Type", "application/octet-stream");
            request.setHeader("Ocp-Apim-Subscription-Key", "56324626a88748adab5e777feb795aae");

            File file = new File(_path);
            InputStreamEntity reqEntity = new InputStreamEntity(
                    new FileInputStream(file), -1);

            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                result = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            result = "Errore";
        }
        return result;
    }

    /**
     * Creazione del menu principale dell'app
     * @param item
     * @return l'item del menu (preferenze)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        Intent myIntent = new Intent(this, Preferenze.class);
        startActivity(myIntent);
        return true;

    }

    /**
     * menu principale
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Metodo per l'attivazione della telecamera
     */
    protected void startCameraActivity() {
        File file = new File(_path);
        Uri outputFileUri = Uri.fromFile(file);

        //Creazione della cartella per la
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(intent, 0);
    }

    /**
     * Gestisci che l'utente non ha scattato la foto
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_CANCELED) {
                //Gestisci che l'utente non ha scattato la foto
            } else {
                //Parte il thread che manda la foto al server
                new HttpAsyncTask().execute();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * inserimento nel database dei ati estratti
     * @param jsonFormat la stringa contenente le parole estratte
     */


    private void riempiDB(String jsonFormat) {
        try {
            JSONObject jsonObject = new JSONObject(jsonFormat);
            JSONArray regions = jsonObject.getJSONArray("regions");
            for (int i = 0; i < regions.length(); i++) {
                JSONArray lines = regions.getJSONObject(i).getJSONArray("lines");
                for (int j = 0; j < lines.length(); j++) {
                    JSONArray words = lines.getJSONObject(j).getJSONArray("words");
                    for (int z = 0; z < words.length(); z++) {
                        String word = words.getJSONObject(z).getString("text");
                        String boundingBox = words.getJSONObject(z).getString("boundingBox");
                        String[] box = boundingBox.split(",");
                        int boxX = Integer.parseInt(box[0]);
                        int boxY = Integer.parseInt(box[1]);
                        int boxW = Integer.parseInt(box[2]);
                        int boxH = Integer.parseInt(box[3]);
                        Parola p = new Parola(word, boxX, boxY, boxW, boxH);
                        dbTicketManager.addParola(p);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * trova la parola più a destra della righa
     * @param parole la righa di parole
     * @return parola più a destra
     */

    public Parola getParolaDestra(ArrayList<Parola> parole) {
        Parola pDestra = null;
        int x = 0;
        for (Parola p : parole) {
            if (x < p.getX()) {
                x = p.getX();
                pDestra = p;
            }
        }
        return pDestra;
    }

    /**
     * recupera il prezzo del prodotto
     * @param parole la righa dello scontrino
     * @return
     */
    public ArrayList<Parola> getEuroProdotti(ArrayList<Parola> parole) {
        ArrayList<Parola> prodotti = new ArrayList<>();
        for (Parola p : parole) {
            if (p.getParola().contains(",") || p.getParola().contains(".")) {
                prodotti.add(p);
            }
        }
        return prodotti;
    }

    /**
     * aggiorna la schermata
     * @param savedInstanceState
     */
    protected void onResume(Bundle savedInstanceState) {
        updateSHPref();
    }
    protected void updateSHPref() {
        SharedPreferences settings = getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);


        tvBudgetVal = (TextView)   findViewById(R.id.val_budget);
        String valBudget = settings.getString(getString(R.string.shared_pref_budget), "");
        String valtotale = "valtotale"; //calcolare dal database
        tvBudgetVal.setText(valBudget);
        tvTotaleVal = (TextView)   findViewById(R.id.val_totale);
        tvTotaleVal.setText(valtotale);
    }

}

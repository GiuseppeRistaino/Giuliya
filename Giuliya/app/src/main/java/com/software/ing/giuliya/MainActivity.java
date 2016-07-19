package com.software.ing.giuliya;

import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    EditText etResponse;
    TextView tvIsConnected;
    Button buttonFoto;

    protected String _path;

    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/Giuliya/";
    private static final String TAG = "MainActivity.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences settings = getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);

        // Inizializzazione EditText per testare la risposta del server
        etResponse = (EditText) findViewById(R.id.etResponse);
        //Inizializzazione TextView per visualizzare il controllo della connessione
        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
        //Inizializzazione del bottone per la fotocamera
        buttonFoto = (Button) findViewById(R.id.button_Foto);
        //Apertura della fotocamera
        buttonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCameraActivity();
            }
        });

        // Controllo per vedere se si è connessi ad internet oppure no
        if(isConnected()){
            tvIsConnected.setBackgroundColor(0xFF00CC00);
            tvIsConnected.setText("You are conncted");
        }
        else{
            tvIsConnected.setText("You are NOT conncted");
        }


        //Inizializzazione di path utilizzati per l'immagazzinamento die dati
        String[] paths = new String[] { DATA_PATH, DATA_PATH + "data/" };

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

    // check network connection
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    //Task per la comunicazione con il server di Microsoft per l'OCR
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            File file = new File(_path);
            if (file.exists())
                return GetOCR();
            else
                return "file don't exist";
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            String totalWord = "";
            String boundingBox = "";
            HashMap<Integer, String> wordsMap = new HashMap<Integer, String>();

            //Tentativo di lavoro con JSON
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
                                Integer yWordBox = Integer.parseInt(segments[1]);
                                wordsMap.put(yWordBox, words.getJSONObject(z).getString("text"));
                            }
                        }
                }
                //Qui si ricavano le parole dall'HashMap
                Set wordsSet = wordsMap.entrySet();
                Iterator iterator = wordsSet.iterator();
                while(iterator.hasNext()) {
                    Map.Entry mentry = (Map.Entry)iterator.next();
                    String box = mentry.getKey().toString();

                    totalWord += "key is: "+ mentry.getKey() + " & Value is: " +mentry.getValue() +"\n";
                }
            } catch (JSONException e) {
                result = "Formato JSON non valido";
            }
            etResponse.setText(totalWord);
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
        }
    }

    //Metodo per la richiesta di OCR dal server
    public String GetOCR()
    {

        HttpClient httpclient = new DefaultHttpClient();
        String result = "";

        try
        {
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
        }
        catch (Exception e)
        {
            result = "Errore";
        }
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml

        Intent myIntent = new Intent(this, Preferenze.class );
        startActivity(myIntent);
        return true;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //Metodo per l'attivazione della telecamera
    protected void startCameraActivity() {
        File file = new File(_path);
        Uri outputFileUri = Uri.fromFile(file);

        //Creazione della cartella per la
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data ) {

        if ( requestCode == 0 ) {

            if ( resultCode == RESULT_CANCELED ) {
                //Gestisci che l'utente non ha scattato la foto
            }
            else {
                //Parte il thread che manda la foto al server
                new HttpAsyncTask().execute();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //Metodo per verificare se due parole sono sulla stessa riga
    public boolean isOnSameLine (int y1, int y2) {
        boolean isSame = false;

        if (y1-5 <= y2 && y2 <= y1+5) isSame = true;

        return isSame;
    }
}

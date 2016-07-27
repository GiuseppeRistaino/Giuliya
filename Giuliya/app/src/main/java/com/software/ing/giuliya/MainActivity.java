package com.software.ing.giuliya;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.software.ing.database.DBTicketManager;
import com.software.ing.util.Parola;
import com.software.ing.util.Ticket;

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
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //EditText etResponse;
    //TextView tvIsConnected;
    ImageButton buttonFoto;
    ImageButton buttonScontrini;
    TextView tvBudgetVal, tvTotaleVal;
    DBTicketManager dbTicketManager;
    TextView textViewRemain;
    ImageButton buttonAdd;

    public static final String OCR_RESULT_KEY = "OCR_RESULT";
    public static final String LISTA_PRODOTTI_KEY = "LISTA_PRODOTTI_KEY";

    protected String _path;

    //HashMap che contiene l'ordinata delle parole che sono sulla stessa riga (Key) e un array che contiene le parole che sono sulla stessa riga (value)
    HashMap<String, ArrayList<String>> wordsMapTest = new HashMap<String, ArrayList<String>>();

    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/Giuliya/";
    private static final String TAG = "MainActivity.java";

    String[] insulti = {"Vai a vivere sotto un ponte",
    "Hai le mani bucate?",
    "Hai vinto la lotteria?",
    "Lo stiamo perdendo! codice rosso!",
    "Errore 404, budget non trovato",
    "Houston! abbiamo un problema!",
    "Prendi le forbici e taglia \n la carta di credito",
    "Hai scoperto come creare \n i soldi dal nulla?",
    "Ti piace perdere facile?",
    "Hai trovato il petrolio?",
    "Il tuo portafogli è come una cipolla.\n Quando lo apri ti viene da piangere",
    "Quast'anno le vacanze con la zappa",
    "La dieta, mai così facile",
    "Datti all'ippica",
    "La matematica non è il tuo mestiere"};

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

        String valtotale = "0"; //calcolare dal database

        String arcoTemporale = settings.getString("selected_value", "");
        String dataPartenza = settings.getString("selected_date", "");

        Date dataOra = new Date();
        Date dataPreferenze = new Date();

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        try {
            dataPreferenze = dateFormat.parse(dataPartenza);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int giorniTrascorsi = (int) giorniTraDueDate(dataPreferenze, dataOra);
        int giorniRestanti = 0;

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(dataOra);

        ArrayList<Ticket> tickets = new ArrayList<>();

        switch (arcoTemporale) {
            case "settimanale":
                giorniRestanti = giorniTrascorsi % 7;
                calendar1.add(Calendar.DATE, -giorniRestanti);
                break;
            case "mensile":
                giorniRestanti = giorniTrascorsi % 30;
                calendar1.add(Calendar.DATE, -giorniRestanti);
                break;
            case "annuale":
                giorniRestanti = giorniTrascorsi % 365;
                calendar1.add(Calendar.DATE, -giorniRestanti);
                break;
        }
        Log.d("GIORNI RESTANTI", Integer.toString(giorniRestanti));
        tickets = dbTicketManager.getScontriniTraDueDate(calendar1.getTime(), dataOra);
        Log.d("DATAORA", dataOra.toString());
        Log.d("DATAPREFERENZA", dataPreferenze.toString());
        double tot = 0;
        if (!tickets.isEmpty()) {
            for (Ticket t : tickets) {
                if (t.getTotale().contains(",")) {
                    t.setTotale(t.getTotale().replace(",","."));
                }
                tot += Double.parseDouble(t.getTotale());
            }
        }
        Log.d("TOT", Double.toString(tot));

        tvBudgetVal = (TextView)   findViewById(R.id.val_budget);

        String valBudget = settings.getString(getString(R.string.shared_pref_budget), "");
        if (valBudget.contains(",")) {
            valBudget = (valBudget.replace(",","."));
        }
        if (valtotale.contains(",")) {
            valtotale = (valtotale.replace(",","."));
        }

        //arrotondo ai due decimali
        Double budget = 0.0;
        if (!valBudget.isEmpty())
            budget = Double.parseDouble(valBudget);
        tot = round(tot, 2);
        budget = round(budget, 2);

        valtotale = Double.toString(tot);
        valBudget = Double.toString(budget);


        //tot =  Double.valueOf(decimalfo.format(tot));

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

        textViewRemain = (TextView) findViewById(R.id.val_remain);
        Double remain = budget - tot;
        remain = round(remain, 2);
        //remain = Double.valueOf(decimalfo.format(remain));

        textViewRemain.setText(Double.toString(remain));
        if (remain <= 0) {
            textViewRemain.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        // Controllo per vedere se si è connessi ad internet oppure no
        /*if(isConnected()){
            tvIsConnected.setBackgroundColor(0xFF00CC00);
            tvIsConnected.setText("You are conncted");
        }
        else{
            tvIsConnected.setText("You are NOT conncted");
        }*/

        buttonAdd = (ImageButton) findViewById(R.id.button_add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TicketDataActivity.class);
                intent.putExtra(OCR_RESULT_KEY, "0.0");
                ArrayList<String> prodotti = new ArrayList<String>();
                intent.putStringArrayListExtra(LISTA_PRODOTTI_KEY, prodotti);
                startActivity(intent);
            }
        });

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

        boolean insultato = settings.getBoolean("selected_insultato", false);

        String soglia = settings.getString("soglia", "");
        Double sogliaDouble;
        if (soglia.isEmpty()) sogliaDouble = 0.0;
        else sogliaDouble = Double.parseDouble(soglia);
        if (sogliaDouble > 0 && tot > sogliaDouble && !insultato) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), MainActivity.class);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            Random random = new Random();
            int insultoselezionato = random.nextInt(14);

            //Build notification
            Notification.Builder notif = null;
            String str_notifica  = insulti[insultoselezionato];

            notif = new Notification.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.visualizza)
                    .setContentTitle("Giuliya")
                    .setContentText(str_notifica);

            notif.setContentIntent(resultPendingIntent);

            NotificationManager notmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //hide the notification after its selection
            int mNotificationId = 001;
            notmanager.notify(mNotificationId, notif.build());
            insultato = true;
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("selected_insultato",insultato);
            editor.commit();
        }

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
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            dbTicketManager.deleteAllWords();
            riempiDB(result);
            Parola totale = dbTicketManager.trovaParola("TOTALE");
            final ArrayList<String> prodotti = new ArrayList<>();
            if (totale != null) {
                ArrayList<Parola> paroleInLinea = new ArrayList<>();
                ArrayList<Parola> paroleInColonna = new ArrayList<>();
                paroleInLinea = dbTicketManager.trovaParoleInLinea(totale.getY());
                final Parola totaleEuro = getParolaDestra(paroleInLinea);
                if (totaleEuro != null && (totaleEuro.getParola().contains(",") || totaleEuro.getParola().contains("."))) {
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
                }
                else {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Importo totale non trovato");
                    alertDialog.setMessage("Si prega di scattare nuovamente la foto, oppure inserire manualmente i dati dello scontrino.");
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startCameraActivity();
                        }
                    });
                    alertDialog.setButton2("DATI", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, TicketDataActivity.class);
                            intent.putExtra(OCR_RESULT_KEY, "0.0");
                            intent.putStringArrayListExtra(LISTA_PRODOTTI_KEY, prodotti);
                            startActivity(intent);
                        }
                    });
                    alertDialog.show();
                }
            }
            else {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Dati non trovati");
                alertDialog.setMessage("Si prega di scattare nuovamente la foto, oppure inserire manualmente i dati dello scontrino.");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startCameraActivity();
                    }
                });
                alertDialog.setButton2("DATI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, TicketDataActivity.class);
                        intent.putExtra(OCR_RESULT_KEY, "0.0");
                        intent.putStringArrayListExtra(LISTA_PRODOTTI_KEY, prodotti);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }

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
        // as you specify a parent activity in AndroidManifest.xml.

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
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
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

    public ArrayList<Parola> getEuroProdotti(ArrayList<Parola> parole) {
        ArrayList<Parola> prodotti = new ArrayList<>();
        for (Parola p : parole) {
            if (p.getParola().contains(",") || p.getParola().contains(".")) {
                prodotti.add(p);
            }
        }
        return prodotti;
    }
    protected void onResume(Bundle savedInstanceState) {
        updateSHPref();
    }
    protected void updateSHPref(){
        SharedPreferences settings = getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);


        tvBudgetVal = (TextView)   findViewById(R.id.val_budget);
        String valBudget = settings.getString(getString(R.string.shared_pref_budget), "");
        String valtotale = "valtotale"; //calcolare dal database
        tvBudgetVal.setText(valBudget);
        tvTotaleVal = (TextView)   findViewById(R.id.val_totale);
        tvTotaleVal.setText(valtotale);
    }

    public long giorniTraDueDate(Date uno, Date due) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(uno);
        c2.setTime(due);
        long giorni = (c2.getTime().getTime() - c1.getTime().getTime()) / (24 * 3600 * 1000);
        return giorni;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}

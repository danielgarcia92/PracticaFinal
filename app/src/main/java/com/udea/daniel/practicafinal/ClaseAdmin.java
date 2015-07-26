package com.udea.daniel.practicafinal;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClaseAdmin extends ActionBarActivity implements OnClickListener{

    // products JSONArray
    JSONArray products = null;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> empresaList;

    private EditText EQUIPO, PJ, DG, PT;
    private Button mEnviar;
    private String eqconsultar,h;

    // Progress Dialog
    private ProgressDialog pDialog;
    private ProgressDialog pDialogConsultar;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //testing on Emulator:
    private static final String REGISTER_URL = "http://practicafinalmoviles.hol.es/prueba2/enviar_modificardatos.php";
    private static String url_all_empresas = "http://practicafinalmoviles.hol.es/prueba2/recibir_obtenerdatos.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_PRODUCTS = "empresas";
    private static final String TAG_EQUIPO = "equipo";
    private static final String TAG_PJ = "pj";
    private static final String TAG_DG = "dg";
    private static final String TAG_PT = "pt";
    String equipo2;
    String pj2;
    String dg2;
    String pt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clase_admin);

        EQUIPO = (EditText)findViewById(R.id.editTextEquipo);
        PJ     = (EditText)findViewById(R.id.editTextPJ);
        DG     = (EditText)findViewById(R.id.editTextDG);
        PT     = (EditText)findViewById(R.id.editTextPT);

        mEnviar = (Button)findViewById(R.id.button1);
        mEnviar.setOnClickListener(this);

        EQUIPO.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus==false){
                    eqconsultar = EQUIPO.getText().toString();
                    //Toast.makeText(ClaseAdmin.this, eqconsultar+"   sdg", Toast.LENGTH_LONG).show();
                    new ConsultarDatos().execute();
                }
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        new CreateUser().execute();
    }

    class CreateUser extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ClaseAdmin.this);
            pDialog.setMessage("Creating User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            String eq = EQUIPO.getText().toString();
            String pj = PJ.getText().toString();
            String dg = DG.getText().toString();
            String pt = PT.getText().toString();

            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("equipo", eq));
                params.add(new BasicNameValuePair("pj", pj));
                params.add(new BasicNameValuePair("dg", dg));
                params.add(new BasicNameValuePair("pt", pt));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        REGISTER_URL, "POST", params);

                // full json response
                Log.d("Registering attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("User Created!", json.toString());
                    finish();
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Registering Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(ClaseAdmin.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }

    class ConsultarDatos extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialogConsultar = new ProgressDialog(ClaseAdmin.this);
            pDialogConsultar.setMessage("Cargando datos...");
            pDialogConsultar.setIndeterminate(false);
            pDialogConsultar.setCancelable(true);
            pDialogConsultar.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // Building Parameters
            List params1 = new ArrayList();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_empresas, "GET", params1);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {

                        JSONObject c = products.getJSONObject(i);

                        h = c.getString(TAG_EQUIPO).toString();

                        if(h.equals(eqconsultar)){
                            // Storing each json item in variable
                            equipo2 = c.getString(TAG_EQUIPO);
                            pj2 = c.getString(TAG_PJ);
                            dg2 = c.getString(TAG_DG);
                            pt2 = c.getString(TAG_PT);

                        }

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialogConsultar.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    PJ.setText(pj2);
                    DG.setText(dg2);
                    PT.setText(pt2);

                }
            });
        }

    }

}
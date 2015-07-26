package com.udea.daniel.practicafinal;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClaseUsuario extends ActionBarActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> empresaList;

    // url to get all products list
    private static String url_all_empresas = "http://practicafinalmoviles.hol.es/prueba2/recibir_obtenerdatos.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "empresas";
    private static final String TAG_EQUIPO = "equipo";
    private static final String TAG_PJ = "pj";
    private static final String TAG_DG = "dg";
    private static final String TAG_PT = "pt";

    // products JSONArray
    JSONArray products = null;

    ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clase_usuario);

        // Hashmap para el ListView
        empresaList = new ArrayList<HashMap<String, String>>();

        // Cargar los productos en el Background Thread
        new LoadAllProducts().execute();
        lista = (ListView) findViewById(R.id.listusuario);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ClaseUsuario.this);
            pDialog.setMessage("Cargando base de datos. Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * obteniendo todos los productos
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List params = new ArrayList();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_empresas, "GET", params);

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
                    //Log.i("ramiro", "produtos.length" + products.length());
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String equipo2 = c.getString(TAG_EQUIPO);
                        String pj2 = c.getString(TAG_PJ);
                        String dg2 = c.getString(TAG_DG);
                        String pt2 = c.getString(TAG_PT);

                        // creating new HashMap
                        HashMap map = new HashMap();

                        // adding each child node to HashMap key => value
                        map.put(TAG_EQUIPO, equipo2);
                        map.put(TAG_PJ, pj2);
                        map.put(TAG_DG, dg2);
                        map.put(TAG_PT, pt2);

                        empresaList.add(map);
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
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            ClaseUsuario.this,
                            empresaList,
                            R.layout.single_post,
                            new String[] {
                                    TAG_EQUIPO,
                                    TAG_PJ,
                                    TAG_DG,
                                    TAG_PT,
                            },
                            new int[] {
                                    R.id.idequipo,
                                    R.id.idpj,
                                    R.id.iddg,
                                    R.id.idpt,
                            });
                    lista.setAdapter(adapter);
                }
            });
        }
    }

}
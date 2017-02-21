package desafiosafet.jeronimo.com.br.desafio_safety;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;


    private static String url = "https://teste-safety.herokuapp.com/api/v1/suggestions";

    ArrayList<HashMap<String, String>> liguagenstList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        liguagenstList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        new GetContacts().execute();
    }


    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();


            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                        String id = jsonObj.getString("id");
                        String name = jsonObj.getString("name");
                        String descricao = jsonObj.getString("description");
                        String imei = jsonObj.getString("imei");
                        String reason_to_learn = jsonObj.getString("reason_to_learn");
                        String username = jsonObj.getString("username");
                        String created_at = jsonObj.getString("created_at");
                        String updated_at = jsonObj.getString("updated_at");


                        HashMap<String, String> language = new HashMap<>();


                        language.put("id", id);
                        language.put("name", name);
                        language.put("description", descricao);
                        language.put("imei", imei);
                        language.put("reason_to_learn", reason_to_learn);
                        language.put("username", username);
                        language.put("created_at", created_at);
                        language.put("updated_at", updated_at);



                        liguagenstList.add(language);

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, liguagenstList,
                    R.layout.list_item, new String[]{"id", "name","description","imei",
                    "reason_to_learn","username","created_at","updated_at"}, new int[]{R.id.id,
                    R.id.name, R.id.description, R.id.imei, R.id.reason_to_learn,
                    R.id.reason_to_learn, R.id.create_at, R.id.update_at});

            lv.setAdapter(adapter);
        }

    }
}

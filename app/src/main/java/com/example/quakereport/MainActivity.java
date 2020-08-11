package com.example.quakereport;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.quakereport.apis.QueryUtils;
import com.example.quakereport.pojo.Earthquake;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2020-01-01&endtime=2020-07-31&limit=100&minmagnitude=1";
    //ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();
    ListView listView;
    //public static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);

        QueryUtils queryUtils = new QueryUtils();
        queryUtils.execute(USGS_REQUEST_URL);
    }

    public void updateUI(ArrayList<Earthquake> earthquakes){
        ArrayAdapterE adapter = new ArrayAdapterE(this, earthquakes);
        listView.setAdapter(adapter);
    }

    public class QueryUtils extends AsyncTask<String, Void, ArrayList<Earthquake>> {

        @Override
        protected ArrayList<Earthquake> doInBackground(String... strings) {
            if (strings.length < 1 || strings[0] == null) {
                return null;
            }
            URL url = createUrl(strings[0]);

            String jsonResponse = null;
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e("MAKEHTTPREQUEST", e.getMessage());
            }

            return extractEarthquakes(jsonResponse);
        }

        @Override
        protected void onPostExecute(ArrayList<Earthquake> earthquakes) {
            if (earthquakes == null) {
                return;
            }
            updateUI(earthquakes);
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            if (url == null) {
                return jsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.d("CODE IS NOT 200", "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e("ERROR CONNECT", e.getMessage());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        public ArrayList<Earthquake> extractEarthquakes(String responseJSON) {
            ArrayList<Earthquake> earthquakes = new ArrayList<>();

            try {
                JSONObject response = new JSONObject(responseJSON);
                JSONArray array = response.getJSONArray("features");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject quake = array.getJSONObject(i);
                    JSONObject properties = quake.getJSONObject("properties");
                    earthquakes.add(new Earthquake(
                            properties.getDouble("mag"),
                            properties.getString("place"),
                            properties.getLong("time"),
                            properties.getString("url")));
                }

            } catch (Exception e) {
                Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
            }
            return earthquakes;
        }

        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                //Log.e(LOG_TAG, "Error with creating URL ", e);
            }
            return url;
        }
    }
}

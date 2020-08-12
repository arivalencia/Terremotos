package com.example.quakereport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.os.Bundle;
import android.widget.ListView;
import com.example.quakereport.pojo.Earthquake;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Earthquake>> {
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2020-01-01&endtime=2020-07-31&limit=100&minmagnitude=1";
    ArrayList<Earthquake> earthquakes = new ArrayList<>();
    ListView listView;
    ArrayAdapterE adapter;
    //public static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapterE(this, earthquakes);
        listView.setAdapter(adapter);
        getSupportLoaderManager().initLoader(1, null, this).forceLoad();
    }

    public void updateUI(ArrayList<Earthquake> earthquakes){
        adapter = new ArrayAdapterE(this, earthquakes);
        listView.setAdapter(adapter);
    }

    @NonNull
    @Override
    public Loader<ArrayList<Earthquake>> onCreateLoader(int id, @Nullable Bundle args) {
        return new EarthquakeLoader(MainActivity.this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Earthquake>> loader, ArrayList<Earthquake> data) {
        updateUI(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Earthquake>> loader) {
        earthquakes = new ArrayList<>();
    }
}

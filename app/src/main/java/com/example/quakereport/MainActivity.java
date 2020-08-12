package com.example.quakereport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.quakereport.pojo.Earthquake;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Earthquake>> {
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2020-01-01&endtime=2020-07-31&limit=100&minmagnitude=1";
    //ArrayList<Earthquake> earthquakes = new ArrayList<>();
    ListView listView;
    TextView viewEmpty;
    ArrayAdapterE adapter;
    RelativeLayout progressBar;
    public static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeVariable();
        if(isConnected()){
            Log.d("CONNECTION", isConnected()+"");
            getSupportLoaderManager().initLoader(1, null, this).forceLoad();
        }else{
            progressBar.setVisibility(View.GONE);
            viewEmpty.setText("Sin conexión a internet.");
            viewEmpty.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }

    public void updateUI(ArrayList<Earthquake> earthquakes){
        if(earthquakes.isEmpty()){
            viewEmpty.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }else{
            adapter = new ArrayAdapterE(this, earthquakes);
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(adapter);
        }
    }

    @NonNull
    @Override
    public Loader<ArrayList<Earthquake>> onCreateLoader(int id, @Nullable Bundle args) {
        return new EarthquakeLoader(MainActivity.this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Earthquake>> loader, ArrayList<Earthquake> data) {
        progressBar.setVisibility(View.GONE);
        updateUI(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Earthquake>> loader) {
        //earthquakes = new ArrayList<>();
    }

    public void initializeVariable(){
        listView = (ListView) findViewById(R.id.list);
        viewEmpty = (TextView) findViewById(R.id.empty);
        progressBar = (RelativeLayout) findViewById(R.id.progressBar);
    }

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}

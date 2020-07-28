package com.example.quakereport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.quakereport.apis.QueryUtils;
import com.example.quakereport.pojo.Earthquake;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();
    ListView listView;
    //public static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapterE adapter = new ArrayAdapterE(
                this, earthquakes);

        listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);
    }
}

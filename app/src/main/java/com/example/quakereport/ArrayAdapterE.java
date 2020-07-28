package com.example.quakereport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.quakereport.pojo.Earthquake;
import android.graphics.drawable.GradientDrawable;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ArrayAdapterE extends ArrayAdapter<Earthquake> implements View.OnClickListener {
    Earthquake earthquake;
    Context context;
    private static final String LOG_TAG = ArrayAdapterE.class.getSimpleName();

    public ArrayAdapterE(Activity context, ArrayList<Earthquake> earthquake) {
        super(context, 0, earthquake);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            context = listItemView.getContext();
        }
        earthquake = getItem(position);
        listItemView.setOnClickListener(this);

        TextView magnitude = (TextView) listItemView.findViewById(R.id.magnitude);
        magnitude.setText(formatMagnitude(earthquake.getMagnitude()));

        TextView near = (TextView) listItemView.findViewById(R.id.near);
        near.setText(earthquake.getNear());

        TextView place = (TextView) listItemView.findViewById(R.id.location);
        place.setText(earthquake.getLocation());

        TextView date = (TextView) listItemView.findViewById(R.id.date);
        date.setText(earthquake.getDate());

        TextView time = (TextView)  listItemView.findViewById(R.id.time);
        time.setText(earthquake.getTime());

        //Agregar color correspondiente a circulo de magnitud
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();
        int magnitudeColor = getMagnitudeColor(earthquake.getMagnitude());
        magnitudeCircle.setColor(listItemView.getContext().getResources().getColor(magnitudeColor));

        return listItemView;
    }

    public String formatMagnitude(double magnitude){
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    public int getMagnitudeColor(double magnitude){
        int mag = (int) magnitude;
        switch (mag){
            case 1:
                ContextCompat.getColor(getContext(), R.color.magnitude1);
            case 2:
                return R.color.magnitude2;
            case 3:
                return R.color.magnitude3;
            case 4:
                return R.color.magnitude4;
            case 5:
                return R.color.magnitude5;
            case 6:
                return R.color.magnitude6;
            case 7:
                return R.color.magnitude7;
            case 8:
                return R.color.magnitude8;
            case 9:
                return R.color.magnitude9;
            case 10:
                return R.color.magnitude10plus;
            default:
                return R.color.magnitudenull;
        }
    }

    @Override
    public void onClick(View v) {
        Uri uri = Uri.parse(earthquake.getURL());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }
}

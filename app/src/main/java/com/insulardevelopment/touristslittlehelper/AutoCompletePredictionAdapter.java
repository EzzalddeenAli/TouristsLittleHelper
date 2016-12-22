package com.insulardevelopment.touristslittlehelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.google.android.gms.location.places.AutocompletePrediction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Маргарита on 22.12.2016.
 */

public class AutoCompletePredictionAdapter extends ArrayAdapter<AutocompletePrediction> {

    List<AutocompletePrediction> predictions, suggestions;

    public class ViewHolder {
        public TextView autoCompletePredictionTextView;

        ViewHolder(View row) {
            autoCompletePredictionTextView = (TextView) row.findViewById(R.id.autocomplete_prediction_text_view);
        }

        public void populateView(AutocompletePrediction prediction) {
            autoCompletePredictionTextView.setText(prediction.getFullText(null));
        }
    }
    public AutoCompletePredictionAdapter(Context context, int resource) {
        super(context, resource);
        predictions = new ArrayList<AutocompletePrediction>();
    }

    public AutoCompletePredictionAdapter(Context context, int resource, List<AutocompletePrediction> objects) {
        super(context, resource, objects);
        predictions = objects;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = vi.inflate(R.layout.autocomplete_prediction_layout, parent, false);
            holder = new ViewHolder(convertView);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.populateView(getItem(position));
        return convertView;
    }
    @Override
    public int getCount() {
        return predictions.size();
    }

    @Override
    public AutocompletePrediction getItem(int position) {
        return predictions.get(position);
    }

    @Override
    public void add(AutocompletePrediction object) {
        predictions.add(object);
        super.add(object);
    }

}





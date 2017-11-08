package com.insulardevelopment.touristslittlehelper.ui.chooseLocation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.google.android.gms.location.places.AutocompletePrediction;
import com.insulardevelopment.touristslittlehelper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Адаптер для выпадающего списка с подсказками
 */

public class AutoCompletePredictionAdapter extends ArrayAdapter<AutocompletePrediction> {

    private List<AutocompletePrediction> predictions, suggestions;
    private int layout;

    public AutoCompletePredictionAdapter(Context context, int resource) {
        super(context, resource);
        predictions = new ArrayList<>();
        suggestions = new ArrayList<>();
        layout = resource;
    }

    public void setData(List<AutocompletePrediction> autocompletePredictions){
        clear();
        addAll(predictions);
        predictions = autocompletePredictions;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.populateView(getItem(position));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((AutocompletePrediction) resultValue).getFullText(null).toString();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    suggestions.clear();
                    for (AutocompletePrediction prediction : predictions) {
                        if (prediction.getFullText(null).toString().startsWith(constraint.toString().toLowerCase())) {
                            suggestions.add(prediction);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    public class ViewHolder {
        public TextView autoCompletePredictionTextView;

        ViewHolder(View row) {
            autoCompletePredictionTextView = row.findViewById(R.id.autocomplete_prediction_text_view);
        }

        public void populateView(AutocompletePrediction prediction) {
            autoCompletePredictionTextView.setText(prediction.getFullText(null));
        }
    }
}





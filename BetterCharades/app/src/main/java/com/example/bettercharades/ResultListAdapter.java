package com.example.bettercharades;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Deb Banerji on 28-May-16.
 */
public class ResultListAdapter extends ArrayAdapter<ResultPair> {

    List<ResultPair> resultPairs;

    public ResultListAdapter(Context context, int resource, List<ResultPair> objects) {
        super(context, resource, objects);
        resultPairs = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.result_item, parent, false);
        }

        ResultPair resultPair = resultPairs.get(position);
        TextView resultText = (TextView) convertView.findViewById(R.id.resultItemText);

        resultText.setText(resultPair.getItem());
        if (!resultPair.isCorrect()) {
            resultText.setTextColor(ContextCompat.getColor(this.getContext(), R.color.colorFailure));
        } else {
            resultText.setTextColor(ContextCompat.getColor(this.getContext(), R.color.colorSuccess));
        }

        return convertView;
    }
}

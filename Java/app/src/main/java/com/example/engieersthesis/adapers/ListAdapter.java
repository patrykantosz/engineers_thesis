package com.example.engieersthesis.adapers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.engieersthesis.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<JSONObject> {

    int vg;

    ArrayList<JSONObject> list;

    Context context;

    public ListAdapter(Context context, int vg, int id, ArrayList<JSONObject> list) {
        super(context, vg, id, list);

        this.context = context;
        this.vg = vg;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(vg, parent, false);

        TextView foodNameTextView = itemView.findViewById(R.id.foodProductNameTextView);
        TextView foodCaloriesTextView = itemView.findViewById(R.id.foodProductCaloriesTextView);

        try {
            String nameToTextView = list.get(position).getString("name") + " (" + list.get(position).getString("brand") + ")";
            foodNameTextView.setText(nameToTextView);
            String caloriesToTextView = list.get(position).getString("energy_value") + " kcal";
            foodCaloriesTextView.setText(caloriesToTextView);
        } catch (JSONException e) {

        }

        return itemView;
    }
}

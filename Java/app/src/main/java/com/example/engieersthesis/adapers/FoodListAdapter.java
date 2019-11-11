package com.example.engieersthesis.adapers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.engieersthesis.R;
import com.example.engieersthesis.utility.Consts;
import com.example.engieersthesis.utility.DoubleRounder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FoodListAdapter extends ArrayAdapter<JSONObject> {

    int vg;
    ArrayList<JSONObject> list;
    Context context;
    private double foodValuesMultiplier;

    public FoodListAdapter(Context context, int vg, int id, ArrayList<JSONObject> list) {
        super(context, vg, id, list);

        System.out.println(list);

        this.context = context;
        this.vg = vg;
        this.list = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(vg, parent, false);

        TextView foodNameTextView = itemView.findViewById(R.id.foodProductNameTextView);
        TextView foodWeightTextView = itemView.findViewById(R.id.foodProductWeightTextView);
        TextView foodCaloriesTextView = itemView.findViewById(R.id.foodProductCaloriesTextView);
        TextView foodProteinsTextView = itemView.findViewById(R.id.foodProductProteinsTextView);
        TextView foodFatsTextView = itemView.findViewById(R.id.foodProductFatsTextView);
        TextView foodCarbohydratesTextView = itemView.findViewById(R.id.foodProductCarbohydratesTextView);

        try {
            foodValuesMultiplier = (Double.parseDouble(list.get(position).getString(Consts.FOOD_PRODUCT_WEIGHT)) / Consts.DEFAULT_MASS_DIV);

            double caloriesValueInDouble = DoubleRounder.roundDouble(list.get(position).getDouble(Consts.FOOD_PRODUCT_ENERGY_VALUE) * foodValuesMultiplier, 0);
            double proteinsValueInDouble = DoubleRounder.roundDouble(list.get(position).getDouble(Consts.FOOD_PRODUCT_PROTEINS) * foodValuesMultiplier, 2);
            double fatsValueInDouble = DoubleRounder.roundDouble(list.get(position).getDouble(Consts.FOOD_PRODUCT_FATS) * foodValuesMultiplier, 2);
            double carbohydratesValueInDouble = DoubleRounder.roundDouble(list.get(position).getDouble(Consts.FOOD_PRODUCT_CARBOHYDRATES) * foodValuesMultiplier, 2);

            String nameToTextView = list.get(position).getString(Consts.FOOD_PRODUCT_NAME) + " (" + list.get(position).getString(Consts.FOOD_PRODUCT_BRAND) + ")";
            String weightToTextView = list.get(position).getString(Consts.FOOD_PRODUCT_WEIGHT) + "g";
            String caloriesToTextView = caloriesValueInDouble + "kcal";
            String proteinsToTextView = proteinsValueInDouble + "g";
            String fatsToTextView = fatsValueInDouble + "g";
            String carbohydratesToTextView = carbohydratesValueInDouble + "g";


            foodNameTextView.setText(nameToTextView);
            foodWeightTextView.setText(weightToTextView);
            foodCaloriesTextView.setText(caloriesToTextView);
            foodProteinsTextView.setText(proteinsToTextView);
            foodFatsTextView.setText(fatsToTextView);
            foodCarbohydratesTextView.setText(carbohydratesToTextView);

        } catch (JSONException e) {

        }

        return itemView;
    }
}

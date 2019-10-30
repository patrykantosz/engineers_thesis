package com.example.engieersthesis;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.engieersthesis.Interfaces.IResult;
import com.example.engieersthesis.requests.VolleyService;
import com.example.engieersthesis.utility.Consts;

import org.json.JSONException;
import org.json.JSONObject;

public class AddFoodProductDetailedActivity extends AppCompatActivity {
    IResult mResultCallback = null;
    private VolleyService volleyService;
    private Button backToPreviousActivityButton;
    private Button addFoodProductToHistoryButton;

    private EditText productWeightEditText;

    private TextView nameAndBrandTextView;
    private TextView energyValueValueTextView;
    private TextView fatsValueTextView;
    private TextView saturatedFatsValueTextView;
    private TextView carbohydratesValueTextView;
    private TextView sugarValueTextView;
    private TextView proteinValueTextView;
    private TextView saltValueTextView;

    private JSONObject foodJson;

    private String foodName;
    private String foodBrand;

    private double foodValuesMultiplier;
    private double energyValue;
    private double fatsValue;
    private double saturatedFatsValue;
    private double carbohydratesValue;
    private double sugarsValue;
    private double proteinsValue;
    private double saltValue;

    private double defaultMassDiv = 100;

    private boolean isWeightEditTextEmpty = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_product_detailed);

        Bundle bundle = getIntent().getExtras();
        convertBundleStringToJsonObject(bundle.getString(Consts.JSON_STRING_FOOD_ID));

        prepareAllControllers();
        getFoodDetails();
        setFoodNameAndBrandTextView();

        productWeightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() >= 1) {
                    foodValuesMultiplier = (Double.parseDouble(charSequence.toString()) / defaultMassDiv);
                    isWeightEditTextEmpty = false;
                    fillTextViews();
                } else {

                    isWeightEditTextEmpty = true;
                    fillTextViews();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        backToPreviousActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void prepareAllControllers() {
        backToPreviousActivityButton = findViewById(R.id.backToPreviousActivityButton);
        addFoodProductToHistoryButton = findViewById(R.id.saveNewProductButton);

        productWeightEditText = findViewById(R.id.productWeightEditText);

        nameAndBrandTextView = findViewById(R.id.nameAndBrandTextView);
        energyValueValueTextView = findViewById(R.id.energyValueValueTextView);
        fatsValueTextView = findViewById(R.id.fatsValueTextView);
        saturatedFatsValueTextView = findViewById(R.id.saturatedFatsValueTextView);
        carbohydratesValueTextView = findViewById(R.id.carbohydratesValueTextView);
        sugarValueTextView = findViewById(R.id.sugarsValueTextView);
        proteinValueTextView = findViewById(R.id.proteinsValueTextView);
        saltValueTextView = findViewById(R.id.saltValueTextView);
    }

    private void convertBundleStringToJsonObject(String bundleString) {
        try {
            foodJson = new JSONObject(bundleString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getFoodDetails() {
        try {
            foodName = foodJson.getString(Consts.FOOD_PRODUCT_NAME);
            foodBrand = foodJson.getString(Consts.FOOD_PRODUCT_BRAND);

            energyValue = foodJson.getDouble(Consts.FOOD_PRODUCT_ENERGY_VALUE);
            fatsValue = foodJson.getDouble(Consts.FOOD_PRODUCT_FATS);
            saturatedFatsValue = foodJson.getDouble(Consts.FOOD_PRODUCT_SATURATED_FATS);
            carbohydratesValue = foodJson.getDouble(Consts.FOOD_PRODUCT_CARBOHYDRATES);
            sugarsValue = foodJson.getDouble(Consts.FOOD_PRODUCT_SUGARS);
            proteinsValue = foodJson.getDouble(Consts.FOOD_PRODUCT_PROTEINS);
            saltValue = foodJson.getDouble(Consts.FOOD_PRODUCT_SALT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillTextViews() {
        if (isWeightEditTextEmpty == false) {
            Log.d("DUPA", "DUPSKO WYWOLANE");
            energyValueValueTextView.setText(Double.toString(energyValue * foodValuesMultiplier));
            fatsValueTextView.setText(Double.toString(fatsValue * foodValuesMultiplier));
            saturatedFatsValueTextView.setText(Double.toString(saturatedFatsValue * foodValuesMultiplier));
            carbohydratesValueTextView.setText(Double.toString(carbohydratesValue * foodValuesMultiplier));
            sugarValueTextView.setText(Double.toString(sugarsValue * foodValuesMultiplier));
            proteinValueTextView.setText(Double.toString(proteinsValue * foodValuesMultiplier));
            saltValueTextView.setText(Double.toString(saltValue * foodValuesMultiplier));
        } else {
            Log.d("DUPA", "DUPSKO WYWOLANE FALSE");
            energyValueValueTextView.setText("");
            fatsValueTextView.setText("");
            saturatedFatsValueTextView.setText("");
            carbohydratesValueTextView.setText("");
            sugarValueTextView.setText("");
            proteinValueTextView.setText("");
            saltValueTextView.setText("");
        }
    }

    private void setFoodNameAndBrandTextView() {
        nameAndBrandTextView.setText(foodName + "(" + foodBrand + ")");
    }
}

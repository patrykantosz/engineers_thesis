package com.example.engieersthesis;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.engieersthesis.Interfaces.IResult;
import com.example.engieersthesis.requests.VolleyService;
import com.example.engieersthesis.utility.Consts;
import com.example.engieersthesis.utility.DoubleRounder;
import com.example.engieersthesis.utility.JSONBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddFoodProductDetailedActivity extends AppCompatActivity {
    IResult mResultCallback = null;
    private VolleyService volleyService;
    private Button backToPreviousActivityButton;
    private Button addFoodProductToHistoryButton;
    private Button deleteFoodProductButton;

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
    private String mealType;
    private String mealDate;
    private String foodDetailsId;
    private String foodWeight;

    private double foodValuesMultiplier;
    private double energyValue;
    private double fatsValue;
    private double saturatedFatsValue;
    private double carbohydratesValue;
    private double sugarsValue;
    private double proteinsValue;
    private double saltValue;

    private boolean isInEditMode = false;
    private boolean isWeightEditTextEmpty = true;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_product_detailed);

        Bundle bundle = getIntent().getExtras();
        convertBundleStringToJsonObject(bundle.getString(Consts.JSON_STRING_FOOD_ID));

        isInEditMode = getIntent().getBooleanExtra(Consts.FOOD_EDITED_INTENT_EXTRA, false);


        prepareAllControllers();
        getFoodDetails();
        setFoodNameAndBrandTextView();

        initVolleyCallback();
        volleyService = new VolleyService(mResultCallback, this);
        mealDate = getIntent().getStringExtra(Consts.MEAL_DATE_INTENT_EXTRA);

        if (!isInEditMode) {
            mealType = getIntent().getStringExtra(Consts.MEAL_TYPE_TO_JSON_REQUEST);
        } else {
            IResult deleteResultCallback = new IResult() {
                @Override
                public void notifySuccess(String requestType, JSONArray response) {
                    Log.d("ResponseArray", response.toString());
                }

                @Override
                public void notifySuccess(String requestType, JSONObject response) {
                    Log.d("ResponseObject", response.toString());
                    Toast.makeText(AddFoodProductDetailedActivity.this, Consts.FOOD_PRODUCT_DELETE_SUCCESS_MSG_ENG, Toast.LENGTH_SHORT).show();
                    volleyService.setmResultCallback(mResultCallback);
                    goBackToUserMainActivity();
                }

                @Override
                public void notifyError(String requestType, VolleyError error) {
                    Log.d("ErrorResponse", error.toString());
                    Toast.makeText(AddFoodProductDetailedActivity.this, Consts.FOOD_PRODUCT_DELETE_FAILURE_MSG_ENG, Toast.LENGTH_SHORT).show();
                    volleyService.setmResultCallback(mResultCallback);
                }
            };
            volleyService.setmResultCallback(deleteResultCallback);
            addFoodWeightToEditText();
            deleteFoodProductButton.setVisibility(View.VISIBLE);
            deleteFoodProductButton.setClickable(true);
            deleteFoodProductButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String urlToDeleteFoodWithQueryParams = Consts.API_DELETE_FOOD_FROM_USER_HISTORY_ENDPOINT + Consts.API_QUERY_PARAM_TO_DETETE_FOOD_FROM_USER_HISTORY + foodDetailsId;
                    volleyService.deleteDataVolleyRequest(Consts.DELETE_METHOD, urlToDeleteFoodWithQueryParams);
                }
            });
        }

        productWeightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() >= 1) {
                    foodValuesMultiplier = (Double.parseDouble(charSequence.toString()) / Consts.DEFAULT_MASS_DIV);
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

        addFoodProductToHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFoodProductToHistoryButton.setEnabled(false);
                if (!isInEditMode)
                    addProductToUserHistory();
                else
                    updateProductInUserHistory();
            }
        });

    }

    private void updateProductInUserHistory() {
        final JSONBuilder jsonBuilder = new JSONBuilder();
        if (checkIfWeightChanged()) {
            jsonBuilder.addNextLine(Consts.FOOD_PRODUCT_WEIGHT, productWeightEditText.getText().toString());
            jsonBuilder.addNextLine(Consts.FOOD_DETAILS_ID, foodDetailsId);

            IResult updateResultCallback = new IResult() {
                @Override
                public void notifySuccess(String requestType, JSONArray response) {
                    Log.d("ResponseArray", response.toString());
                }

                @Override
                public void notifySuccess(String requestType, JSONObject response) {
                    Log.d("PATCHResponseObject", response.toString());
                    Toast.makeText(AddFoodProductDetailedActivity.this, Consts.FOOD_PRODUCT_UPDATE_SUCCESS_MSG_ENG, Toast.LENGTH_SHORT).show();
                    volleyService.setmResultCallback(mResultCallback);
                    goBackToUserMainActivity();
                }

                @Override
                public void notifyError(String requestType, VolleyError error) {
                    Log.d("ErrorResponse", error.toString());
                    Toast.makeText(AddFoodProductDetailedActivity.this, Consts.FOOD_PRODUCT_UPDATE_FAILURE_MSG_ENG, Toast.LENGTH_SHORT).show();
                    volleyService.setmResultCallback(mResultCallback);

                }
            };
            volleyService.setmResultCallback(updateResultCallback);
            volleyService.patchDataVolleyRequest(Consts.PATCH_METHOD, Consts.API_UPDATE_FOOD_WEIGHT_ENDPOINT, jsonBuilder.getJson());
        } else {
            Toast.makeText(AddFoodProductDetailedActivity.this, Consts.FOOD_PRODUCT_UPDATE_SUCCESS_MSG_ENG, Toast.LENGTH_SHORT).show();
            goBackToUserMainActivity();
        }

    }

    private boolean checkIfWeightChanged() {
        return !productWeightEditText.getText().toString().equals(foodWeight);
    }

    void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONArray response) {
                Log.d("ResponseADD", response.toString());
            }

            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.d("ResponseJSONObjectADD", response.toString());
                addFoodProductToHistoryButton.setEnabled(true);
                if(getIntent().getBooleanExtra(Consts.MULTIPLE_ADD, false))
                    finish();
                else
                    goBackToUserMainActivity();
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                addFoodProductToHistoryButton.setEnabled(true);
                Log.d("ResponseADD", error.toString());
            }
        };
    }

    private void prepareAllControllers() {
        backToPreviousActivityButton = findViewById(R.id.backToPreviousActivityButton);
        addFoodProductToHistoryButton = findViewById(R.id.saveNewProductButton);
        deleteFoodProductButton = findViewById(R.id.deleteFoodProductButton);

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
        Log.d("STRING", foodJson.toString());
        try {
            foodName = foodJson.getString(Consts.FOOD_PRODUCT_NAME);
            foodBrand = foodJson.getString(Consts.FOOD_PRODUCT_BRAND);
            if (isInEditMode)
                foodDetailsId = foodJson.getString(Consts.FOOD_DETAILS_ID);

            energyValue = foodJson.getDouble(Consts.FOOD_PRODUCT_ENERGY_VALUE);
            fatsValue = foodJson.getDouble(Consts.FOOD_PRODUCT_FATS);
            saturatedFatsValue = foodJson.getDouble(Consts.FOOD_PRODUCT_SATURATED_FATS);
            carbohydratesValue = foodJson.getDouble(Consts.FOOD_PRODUCT_CARBOHYDRATES);
            sugarsValue = foodJson.getDouble(Consts.FOOD_PRODUCT_SUGARS);
            proteinsValue = foodJson.getDouble(Consts.FOOD_PRODUCT_PROTEINS);
            saltValue = foodJson.getDouble(Consts.FOOD_PRODUCT_SALT);
            if(isInEditMode){
                double multiplier = foodJson.getDouble(Consts.FOOD_PRODUCT_WEIGHT) / 100;
                energyValueValueTextView.setText(Double.toString(DoubleRounder.roundDouble(energyValue * multiplier, 2)));
                fatsValueTextView.setText(Double.toString(DoubleRounder.roundDouble(fatsValue * multiplier, 2)));
                saturatedFatsValueTextView.setText(Double.toString(DoubleRounder.roundDouble(saturatedFatsValue * multiplier, 2)));
                carbohydratesValueTextView.setText(Double.toString(DoubleRounder.roundDouble(carbohydratesValue * multiplier, 2)));
                sugarValueTextView.setText(Double.toString(DoubleRounder.roundDouble(sugarsValue * multiplier, 2)));
                proteinValueTextView.setText(Double.toString(DoubleRounder.roundDouble(proteinsValue * multiplier, 2)));
                saltValueTextView.setText(Double.toString(DoubleRounder.roundDouble(saltValue * multiplier, 2)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillTextViews() {
        if (isWeightEditTextEmpty == false) {
            energyValueValueTextView.setText(Double.toString(DoubleRounder.roundDouble(energyValue * foodValuesMultiplier, 2)));
            fatsValueTextView.setText(Double.toString(DoubleRounder.roundDouble(fatsValue * foodValuesMultiplier, 2)));
            saturatedFatsValueTextView.setText(Double.toString(DoubleRounder.roundDouble(saturatedFatsValue * foodValuesMultiplier, 2)));
            carbohydratesValueTextView.setText(Double.toString(DoubleRounder.roundDouble(carbohydratesValue * foodValuesMultiplier, 2)));
            sugarValueTextView.setText(Double.toString(DoubleRounder.roundDouble(sugarsValue * foodValuesMultiplier, 2)));
            proteinValueTextView.setText(Double.toString(DoubleRounder.roundDouble(proteinsValue * foodValuesMultiplier, 2)));
            saltValueTextView.setText(Double.toString(DoubleRounder.roundDouble(saltValue * foodValuesMultiplier, 2)));
        } else {
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

    private void addProductToUserHistory() {
        JSONBuilder jsonBuilder = new JSONBuilder();
        try {
            jsonBuilder.addNextLine(Consts.FOOD_PRODUCT_ID, foodJson.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonBuilder.addNextLine(Consts.FOOD_PRODUCT_WEIGHT, productWeightEditText.getText().toString());
        jsonBuilder.addNextLine(Consts.MEAL_TYPE, mealType);
        jsonBuilder.addNextLine(Consts.MEAL_DATE, mealDate);

        Log.d("JSON", jsonBuilder.getJson().toString());
        volleyService.postDataVolleyRequest(Consts.POST_METHOD, Consts.API_ADD_NEW_PRODUCT_TO_MEAL_ENDPOINT, jsonBuilder.getJson());
    }

    private void goBackToUserMainActivity() {
        Intent userMainActivityIntent = new Intent(AddFoodProductDetailedActivity.this, UserMainScreenActivity.class);
        userMainActivityIntent.putExtra(Consts.MEAL_DATE_INTENT_EXTRA, mealDate);
        userMainActivityIntent.putExtra("REFRESH", true);
        userMainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(userMainActivityIntent);
    }

    private void addFoodWeightToEditText() {
        try {
            foodWeight = foodJson.getString(Consts.FOOD_PRODUCT_WEIGHT);
            productWeightEditText.setText(Double.toString(DoubleRounder.roundDouble(Double.parseDouble(foodWeight), 2)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

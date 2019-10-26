package com.example.engieersthesis;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.engieersthesis.Interfaces.IResult;
import com.example.engieersthesis.requests.VolleyService;
import com.example.engieersthesis.utility.Consts;
import com.example.engieersthesis.utility.FoodProduct;
import com.example.engieersthesis.utility.JSONBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

public class AddNewFoodProductActivity extends AppCompatActivity {
    IResult mResultCallback = null;
    private VolleyService volleyService;
    private Button backToPreviousActivityButton;
    private Button addNewFoodProductButton;

    private EditText foodProductNameEditText;
    private EditText foodProductBrandEditText;
    private EditText foodProductEnergyValueEditText;
    private EditText foodProductFatsEditText;
    private EditText foodProductSaturatedFatsEditText;
    private EditText foodProductCarbohydratesEditText;
    private EditText foodProductSugarsEditText;
    private EditText foodProductProteinsEditText;
    private EditText foodProductSaltEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food_product);

        prepareAllControllers();
        initVolleyCallback();
        volleyService = new VolleyService(mResultCallback, this);


        backToPreviousActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFoodProductIntent = new Intent(AddNewFoodProductActivity.this, AddFoodProductActivity.class);
                startActivity(addFoodProductIntent);
            }
        });

        addNewFoodProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkIfEditTextsAreEmpty())
                    saveNewProduct();
            }
        });
    }

    private void prepareAllControllers() {
        backToPreviousActivityButton = findViewById(R.id.backToPreviousActivityButton);
        addNewFoodProductButton = findViewById(R.id.saveNewProductButton);

        foodProductNameEditText = findViewById(R.id.foodProductNameEditText);
        foodProductBrandEditText = findViewById(R.id.foodProductBrandEditText);
        foodProductEnergyValueEditText = findViewById(R.id.foodProductEnergyValueEditText);
        foodProductFatsEditText = findViewById(R.id.foodProductFatsEditText);
        foodProductSaturatedFatsEditText = findViewById(R.id.foodProductSaturatedFatsEditText);
        foodProductCarbohydratesEditText = findViewById(R.id.foodProductCarbohydratesEditText);
        foodProductSugarsEditText = findViewById(R.id.foodProductSugarsEditText);
        foodProductProteinsEditText = findViewById(R.id.foodProductProteinsEditText);
        foodProductSaltEditText = findViewById(R.id.foodProductSaltEditText);
    }


    private void saveNewProduct() {
        FoodProduct newProductToAdd = getProductDetailsFromEditTexts();
        JSONObject newFoodProductJSON = prepareNewFoodProductJSON(newProductToAdd);

        volleyService.postDataVolleyRequest(Consts.POST_CALL_REQUEST_TYPE, Consts.API_ADD_NEW_FOOD_PRODUCT, newFoodProductJSON);
    }

    private boolean checkIfEditTextsAreEmpty() {
        boolean isEmpty = false;

        if (foodProductNameEditText.getText().toString().trim().length() == 0) {
            isEmpty = true;
            redBordersAroundEmptyEditText(foodProductNameEditText);
        }
        if (foodProductBrandEditText.getText().toString().trim().length() == 0) {
            isEmpty = true;
            redBordersAroundEmptyEditText(foodProductBrandEditText);
        }
        if (foodProductEnergyValueEditText.getText().toString().trim().length() == 0) {
            isEmpty = true;
            redBordersAroundEmptyEditText(foodProductEnergyValueEditText);
        }
        if (foodProductFatsEditText.getText().toString().trim().length() == 0) {
            isEmpty = true;
            redBordersAroundEmptyEditText(foodProductFatsEditText);
        }
        if (foodProductSaturatedFatsEditText.getText().toString().trim().length() == 0) {
            isEmpty = true;
            redBordersAroundEmptyEditText(foodProductSaturatedFatsEditText);
        }
        if (foodProductCarbohydratesEditText.getText().toString().trim().length() == 0) {
            isEmpty = true;
            redBordersAroundEmptyEditText(foodProductCarbohydratesEditText);
        }
        if (foodProductSugarsEditText.getText().toString().trim().length() == 0) {
            isEmpty = true;
            redBordersAroundEmptyEditText(foodProductSugarsEditText);
        }
        if (foodProductProteinsEditText.getText().toString().trim().length() == 0) {
            isEmpty = true;
            redBordersAroundEmptyEditText(foodProductProteinsEditText);
        }
        if (foodProductSaltEditText.getText().toString().trim().length() == 0) {
            isEmpty = true;
            redBordersAroundEmptyEditText(foodProductSaltEditText);
        }

        return isEmpty;
    }

    private void redBordersAroundEmptyEditText(EditText emptyEditText) {
        emptyEditText.setBackgroundResource(R.drawable.error_edit_text);
        Toast.makeText(AddNewFoodProductActivity.this, Consts.ADD_NEW_FOOD_EMPTY_FIELDS_MSG_PL, Toast.LENGTH_SHORT).show();
    }

    private FoodProduct getProductDetailsFromEditTexts() {
        FoodProduct newFoodProduct = new FoodProduct();

        newFoodProduct.foodName = foodProductNameEditText.getText().toString();
        newFoodProduct.foodBrandName = foodProductBrandEditText.getText().toString();

        newFoodProduct.foodEnergyValue = Float.parseFloat(foodProductEnergyValueEditText.getText().toString());
        newFoodProduct.foodFats = Float.parseFloat(foodProductFatsEditText.getText().toString());
        newFoodProduct.foodSaturatedFats = Float.parseFloat(foodProductSaturatedFatsEditText.getText().toString());
        newFoodProduct.foodCarbohydrates = Float.parseFloat(foodProductCarbohydratesEditText.getText().toString());
        newFoodProduct.foodSugars = Float.parseFloat(foodProductSugarsEditText.getText().toString());
        newFoodProduct.foodProteins = Float.parseFloat(foodProductProteinsEditText.getText().toString());
        newFoodProduct.foodSalt = Float.parseFloat(foodProductSaltEditText.getText().toString());

        return newFoodProduct;
    }

    void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONArray response) {
                Log.d("AddNewFoodResponseArray", response.toString());
            }

            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.d("AddNewFoodResponseObj", response.toString());
                Toast.makeText(AddNewFoodProductActivity.this, Consts.ADD_NEW_FOOD_SUCCES_MSG_PL, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d("AddNewFoodError", error.toString());
                Toast.makeText(AddNewFoodProductActivity.this, Consts.ADD_NEW_FOOD_FAILURE_MSG_PL, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private JSONObject prepareNewFoodProductJSON(FoodProduct newFoodProduct) {
        JSONBuilder jsonBuilder = new JSONBuilder();

        jsonBuilder.addNextLine(Consts.FOOD_PRODUCT_NAME, newFoodProduct.foodName);
        jsonBuilder.addNextLine(Consts.FOOD_PRODUCT_BRAND, newFoodProduct.foodBrandName);
        jsonBuilder.addNextLine(Consts.FOOD_PRODUCT_ENERGY_VALUE, newFoodProduct.foodEnergyValue);
        jsonBuilder.addNextLine(Consts.FOOD_PRODUCT_FATS, newFoodProduct.foodFats);
        jsonBuilder.addNextLine(Consts.FOOD_PRODUCT_SATURATED_FATS, newFoodProduct.foodSaturatedFats);
        jsonBuilder.addNextLine(Consts.FOOD_PRODUCT_CARBOHYDRATES, newFoodProduct.foodCarbohydrates);
        jsonBuilder.addNextLine(Consts.FOOD_PRODUCT_SUGARS, newFoodProduct.foodSugars);
        jsonBuilder.addNextLine(Consts.FOOD_PRODUCT_PROTEINS, newFoodProduct.foodProteins);
        jsonBuilder.addNextLine(Consts.FOOD_PRODUCT_SALT, newFoodProduct.foodSalt);

        return jsonBuilder.getJson();
    }
}

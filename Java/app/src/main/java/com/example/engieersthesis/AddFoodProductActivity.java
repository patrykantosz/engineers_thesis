package com.example.engieersthesis;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.engieersthesis.Interfaces.IResult;
import com.example.engieersthesis.requests.VolleyService;
import com.example.engieersthesis.utility.Consts;
import com.example.engieersthesis.utility.JSONUtilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddFoodProductActivity extends AppCompatActivity {

    IResult mResultCallback = null;
    private TextView mealNameTextView;
    private EditText foodSearchEditText;
    private VolleyService volleyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_product);

        String mealName = getIntent().getStringExtra("mealName");

        mealNameTextView = findViewById(R.id.mealNameTextView);
        foodSearchEditText = findViewById(R.id.foodSearchEditText);

        mealNameTextView.setText(mealName);

        initVolleyCallback();
        volleyService = new VolleyService(mResultCallback, this);

        foodSearchEditText.addTextChangedListener(new TextWatcher() {
            boolean isSeraching = false;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() >= 3) {
                    if (!isSeraching) {
                        isSeraching = true;
                        searchProductInDatabase(charSequence.toString());
                    } else {
                        volleyService.cancelArrayRequest();
                        searchProductInDatabase(charSequence.toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void searchProductInDatabase(String foodToSearch) {
        String urlWithFoodToSearchAsQueryParam = Consts.API_LIST_ALL_FOOD_PRODUCTS_ENDPOINT +
                Consts.API_QUERY_PARAM_TO_SEARCH_FOOD_BY_NAME_ENDPOINT + foodToSearch;
        Log.d("URL: ", urlWithFoodToSearchAsQueryParam);

        volleyService.getDataVolleyRequest("GETCALL", urlWithFoodToSearchAsQueryParam);
    }

    void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONArray response) {
                Log.d("ResponseJSONARRAY:", response.toString());
                ArrayList<JSONObject> listItems = JSONUtilities.getArrayListFromJSONARRAY(response);

                ListView listView = findViewById(R.id.foodProductsListView);

                ListAdapter adapter = new com.example.engieersthesis.adapers.ListAdapter(AddFoodProductActivity.this,
                        R.layout.food_products_list_view, R.id.foodProductNameTextView, listItems);

                listView.setAdapter(adapter);

            }

            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.d("ResponseJSONOBJECT: ", response.toString());
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d("Response:", error.toString());
            }
        };
    }


}

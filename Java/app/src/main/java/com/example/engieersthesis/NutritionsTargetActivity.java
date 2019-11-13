package com.example.engieersthesis;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
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
import com.example.engieersthesis.utility.JSONBuilder;
import com.example.engieersthesis.utility.NumericKeyboardTransformationMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NutritionsTargetActivity extends AppCompatActivity {
    IResult mResultCallback = null;
    private VolleyService volleyService;

    private Button previousAcrtivityButton;
    private Button saveNutritionsTargetButton;
    private TextView energyValueTextView;
    private EditText carboTargetEditView;
    private EditText proteinsTargetEditView;
    private EditText fatsTargetEditView;

    private int userTargetCalories;
    private int userTargetCarbo;
    private int userTargetProteins;
    private int userTargetFats;

    private int calculatedEnergyValue;
    private int energyValueFromFats = 0;
    private int energyValueFromCarbos = 0;
    private int energyValueFromProteins = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutritions_target);

        prepareAllControllers();
        changeEditTextToIntegers();
        initVolleyCallback();
        volleyService = new VolleyService(mResultCallback, this);
        getUserNutritionsTarget();

        previousAcrtivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveNutritionsTargetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkIfEditTextsAreEmpty())
                    saveNutritionsTarget();
            }
        });
    }

    private void prepareAllControllers() {
        previousAcrtivityButton = findViewById(R.id.backToPreviousActivityButton);
        saveNutritionsTargetButton = findViewById(R.id.saveNewParameters);
        energyValueTextView = findViewById(R.id.energyValueTargetValueTextView);
        carboTargetEditView = findViewById(R.id.carboTargetEditView);
        proteinsTargetEditView = findViewById(R.id.proteinsTargetEditText);
        fatsTargetEditView = findViewById(R.id.fatsTargetEditText);
    }

    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONArray response) {
                Log.d("JsonArrayResponse", response.toString());
            }

            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.d("JsonObjectResponse", response.toString());
                extractJsonAndFillControllers(response);
                setEditTextListeners();
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d("ErrorResponse", error.toString());
            }
        };
    }

    private void changeEditTextToIntegers() {
        carboTargetEditView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        carboTargetEditView.setTransformationMethod(new NumericKeyboardTransformationMethod());
        proteinsTargetEditView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        proteinsTargetEditView.setTransformationMethod(new NumericKeyboardTransformationMethod());
        fatsTargetEditView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        fatsTargetEditView.setTransformationMethod(new NumericKeyboardTransformationMethod());
    }

    private void getUserNutritionsTarget() {
        volleyService.getDataVolleyRequest(Consts.GET_METHOD, Consts.API_USER_NUTRITIONS_TARGET_ENDPOINT);
    }

    private void extractJsonAndFillControllers(JSONObject response) {
        try {
            userTargetCalories = response.getInt(Consts.USER_TARGET_CALORIES);
            userTargetCarbo = response.getInt(Consts.USER_TARGET_CARBOHYDRATES);
            userTargetProteins = response.getInt(Consts.USER_TARGET_PROTEINS);
            userTargetFats = response.getInt(Consts.USER_TARGET_FATS);
            fillControllers();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillControllers() {
        energyValueTextView.setText(Integer.toString(userTargetCalories));
        carboTargetEditView.setText(Integer.toString(userTargetCarbo));
        proteinsTargetEditView.setText(Integer.toString(userTargetProteins));
        fatsTargetEditView.setText(Integer.toString(userTargetFats));
    }

    private void setEditTextListeners() {
        fatsTargetEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calculateEnergyValue();
                setEnergyValueToTextView();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        carboTargetEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calculateEnergyValue();
                setEnergyValueToTextView();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        proteinsTargetEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calculateEnergyValue();
                setEnergyValueToTextView();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void calculateEnergyFromFats() {
        energyValueFromFats = Integer.parseInt(fatsTargetEditView.getText().toString()) * Consts.FAT_CALORIES;
    }

    private void calculateEnergyFromCarbos() {
        energyValueFromCarbos = Integer.parseInt(carboTargetEditView.getText().toString()) * Consts.CARBOHYDRATE_CALORIES;
    }

    private void calculateEnergyFromProteins() {
        energyValueFromProteins = Integer.parseInt(proteinsTargetEditView.getText().toString()) * Consts.PROTEIN_CALORIES;
    }

    private void calculateEnergyValue() {
        if (fatsTargetEditView.getText().toString().isEmpty())
            energyValueFromFats = 0;
        else
            calculateEnergyFromFats();
        if (carboTargetEditView.getText().toString().isEmpty())
            energyValueFromCarbos = 0;
        else
            calculateEnergyFromCarbos();
        if (proteinsTargetEditView.getText().toString().isEmpty())
            energyValueFromProteins = 0;
        else
            calculateEnergyFromProteins();
        calculatedEnergyValue = energyValueFromFats + energyValueFromCarbos + energyValueFromProteins;
    }

    private void setEnergyValueToTextView() {
        energyValueTextView.setText(Integer.toString(calculatedEnergyValue));
    }

    private boolean checkIfEditTextsAreEmpty() {
        boolean isEmpty = false;

        if (carboTargetEditView.getText().toString().length() == 0) {
            isEmpty = true;
            redBordersAroundEmptyEditText(carboTargetEditView);
        }
        if (fatsTargetEditView.getText().toString().length() == 0) {
            isEmpty = true;
            redBordersAroundEmptyEditText(fatsTargetEditView);
        }
        if (proteinsTargetEditView.getText().toString().length() == 0) {
            isEmpty = true;
            redBordersAroundEmptyEditText(proteinsTargetEditView);
        }

        return isEmpty;
    }

    private void redBordersAroundEmptyEditText(EditText emptyEditText) {
        emptyEditText.setBackgroundResource(R.drawable.error_edit_text);
        Toast.makeText(NutritionsTargetActivity.this, Consts.ADD_NEW_FOOD_EMPTY_FIELDS_MSG_ENG, Toast.LENGTH_SHORT).show();
    }

    private void saveNutritionsTarget() {
        IResult putCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONArray response) {
                Log.d("JsonArrayResponse", response.toString());
            }

            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.d("JsonObjectResponse", response.toString());
                Toast.makeText(NutritionsTargetActivity.this, Consts.PUT_NUTRITIONS_TARGET_SUCCESS_MSG_ENG, Toast.LENGTH_SHORT).show();
                volleyService.setmResultCallback(mResultCallback);
                Intent userMainScreenIntent = new Intent(NutritionsTargetActivity.this, UserMainScreenActivity.class);
                userMainScreenIntent.putExtra("REFRESH", true);
                userMainScreenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(userMainScreenIntent);
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d("ErrorResponse", error.toString());
                Toast.makeText(NutritionsTargetActivity.this, Consts.PUT_NUTRITUONS_TARGET_SUCCESS_MSG_ENG, Toast.LENGTH_SHORT).show();
            }
        };

        JSONBuilder jsonBuilder = new JSONBuilder();
        jsonBuilder.addNextLine(Consts.USER_TARGET_CALORIES, Integer.parseInt(energyValueTextView.getText().toString()));
        jsonBuilder.addNextLine(Consts.USER_TARGET_CARBOHYDRATES, Integer.parseInt(carboTargetEditView.getText().toString()));
        jsonBuilder.addNextLine(Consts.USER_TARGET_PROTEINS, Integer.parseInt(proteinsTargetEditView.getText().toString()));
        jsonBuilder.addNextLine(Consts.USER_TARGET_FATS, Integer.parseInt(fatsTargetEditView.getText().toString()));

        volleyService.setmResultCallback(putCallback);
        volleyService.putDataVolleyRequest(Consts.PUT_METHOD, Consts.API_USER_NUTRITIONS_TARGET_ENDPOINT, jsonBuilder.getJson());
    }
}

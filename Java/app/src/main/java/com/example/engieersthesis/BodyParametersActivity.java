package com.example.engieersthesis;

import android.content.Intent;
import android.graphics.Color;
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
import com.example.engieersthesis.utility.DoubleRounder;
import com.example.engieersthesis.utility.JSONBuilder;
import com.example.engieersthesis.utility.NumericKeyboardTransformationMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BodyParametersActivity extends AppCompatActivity {
    IResult mResultCallback = null;
    private VolleyService volleyService;

    private Button previousAcrtivityButton;
    private Button saveBodyParametersButton;
    private EditText weightEditText;
    private EditText heightEditText;
    private TextView bmiTextView;

    private int userWeight;
    private int userHeight;
    private double userBmi;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_parameters);

        prepareAllControllers();
        changeEditTextToIntegers();
        initVolleyCallback();
        volleyService = new VolleyService(mResultCallback, this);
        getUserBodyParameters();

        previousAcrtivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveBodyParametersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkIfEditTextsAreEmpty())
                    saveBodyParameters();
            }
        });

        heightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 && weightEditText.getText().toString().length() > 0)
                    calculateUserBmi(Integer.parseInt(weightEditText.getText().toString()), Integer.parseInt(heightEditText.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        weightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 && heightEditText.getText().toString().length() > 0)
                    calculateUserBmi(Integer.parseInt(weightEditText.getText().toString()), Integer.parseInt(heightEditText.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void prepareAllControllers() {
        previousAcrtivityButton = findViewById(R.id.backToPreviousActivityButton);
        saveBodyParametersButton = findViewById(R.id.saveNewParameters);
        weightEditText = findViewById(R.id.bodyWeightEditText);
        heightEditText = findViewById(R.id.bodyHeightEditText);
        bmiTextView = findViewById(R.id.bmiValueTextView);
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
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d("ErrorResponse", error.toString());
            }
        };
    }

    private void changeEditTextToIntegers() {
        heightEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        heightEditText.setTransformationMethod(new NumericKeyboardTransformationMethod());
        weightEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        weightEditText.setTransformationMethod(new NumericKeyboardTransformationMethod());
    }

    private void getUserBodyParameters() {
        volleyService.getDataVolleyRequest(Consts.GET_METHOD, Consts.API_USER_BODY_PARAMETERS_ENDPOINT);
    }

    private void extractJsonAndFillControllers(JSONObject response) {
        extractUserParametersFromJsonObject(response);
    }

    private void extractUserParametersFromJsonObject(JSONObject response) {
        try {
            userWeight = response.getInt(Consts.USER_WEIGHT);
            userHeight = response.getInt(Consts.USER_HEIGHT);
            userBmi = response.getDouble(Consts.USER_BMI);
            fillControllers();
            setColotToBmiTextView(userBmi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillControllers() {
        weightEditText.setText(Integer.toString(userWeight));
        heightEditText.setText(Integer.toString(userHeight));
        bmiTextView.setText(Double.toString(DoubleRounder.roundDouble(userBmi, 2)));
    }

    private void calculateUserBmi(int userWeightToCalcBmi, int userHeightToCalcBmi) {
        if (userHeightToCalcBmi == 0) {
            bmiTextView.setText(Double.toString(0.0));
            return;
        }
        double calcUserBmi = userWeightToCalcBmi / (Math.pow(userHeightToCalcBmi / Consts.DEFAULT_MASS_DIV, 2));
        calcUserBmi = DoubleRounder.roundDouble(calcUserBmi, 2);
        bmiTextView.setText(Double.toString(calcUserBmi));
        setColotToBmiTextView(calcUserBmi);
    }

    private void setColotToBmiTextView(double bmiValue) {
        if (bmiValue < 16.0)
            bmiTextView.setTextColor(Color.parseColor("#0021D6"));
        else if (bmiValue < 17)
            bmiTextView.setTextColor(Color.parseColor("#5470DF"));
        else if (bmiValue < 18.5)
            bmiTextView.setTextColor(Color.parseColor("#8CC78C"));
        else if (bmiValue < 25)
            bmiTextView.setTextColor(Color.parseColor("#08E708"));
        else if (bmiValue < 30)
            bmiTextView.setTextColor(Color.parseColor("#D6BC7F"));
        else if (bmiValue < 35)
            bmiTextView.setTextColor(Color.parseColor("#FFB604"));
        else if (bmiValue < 40)
            bmiTextView.setTextColor(Color.parseColor("#E91609"));
        else
            bmiTextView.setTextColor(Color.parseColor("#440101"));
    }

    private void saveBodyParameters() {
        IResult putCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONArray response) {
                Log.d("JsonArrayResponse", response.toString());
            }

            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.d("JsonObjectResponse", response.toString());
                Toast.makeText(BodyParametersActivity.this, Consts.PUT_BODY_PARAMETERS_SUCCESS_MSG_ENG, Toast.LENGTH_SHORT).show();
                volleyService.setmResultCallback(mResultCallback);
                Intent userMainScreenIntent = new Intent(BodyParametersActivity.this, UserMainScreenActivity.class);
                userMainScreenIntent.putExtra("REFRESH", true);
                userMainScreenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(userMainScreenIntent);
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d("ErrorResponse", error.toString());
                Toast.makeText(BodyParametersActivity.this, Consts.PUT_BODY_PARAMETERS_FAILURE_MSG_ENG, Toast.LENGTH_SHORT).show();
            }
        };

        JSONBuilder jsonBuilder = new JSONBuilder();
        jsonBuilder.addNextLine(Consts.USER_WEIGHT, Integer.parseInt(weightEditText.getText().toString()));
        jsonBuilder.addNextLine(Consts.USER_HEIGHT, Integer.parseInt(heightEditText.getText().toString()));
        jsonBuilder.addNextLine(Consts.USER_BMI, DoubleRounder.roundDouble(Double.parseDouble(bmiTextView.getText().toString()), 2));

        volleyService.setmResultCallback(putCallback);
        volleyService.putDataVolleyRequest(Consts.PUT_METHOD, Consts.API_USER_BODY_PARAMETERS_ENDPOINT, jsonBuilder.getJson());
    }

    private boolean checkIfEditTextsAreEmpty() {
        boolean isEmpty = false;

        if (weightEditText.getText().toString().length() == 0) {
            isEmpty = true;
            redBordersAroundEmptyEditText(weightEditText);
        }
        if (heightEditText.getText().toString().length() == 0) {
            isEmpty = true;
            redBordersAroundEmptyEditText(heightEditText);
        }

        return isEmpty;
    }

    private void redBordersAroundEmptyEditText(EditText emptyEditText) {
        emptyEditText.setBackgroundResource(R.drawable.error_edit_text);
        Toast.makeText(BodyParametersActivity.this, Consts.ADD_NEW_FOOD_EMPTY_FIELDS_MSG_ENG, Toast.LENGTH_SHORT).show();
    }
}

package com.example.engieersthesis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.ClientError;
import com.android.volley.VolleyError;
import com.example.engieersthesis.Interfaces.IResult;
import com.example.engieersthesis.requests.VolleyService;
import com.example.engieersthesis.utility.Consts;
import com.example.engieersthesis.utility.JSONBuilder;
import com.example.engieersthesis.utility.SharedPreferencesSaver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    IResult mResultCallback = null;
    private VolleyService volleyService;
    private Button registerButton;
    private EditText loginEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private ProgressBar progressBar;

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);
        loginEditText.setEnabled(true);
        emailEditText.setEnabled(true);
        passwordEditText.setEnabled(true);
        registerButton.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = findViewById(R.id.registerButton);
        loginEditText = findViewById(R.id.loginEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        progressBar = findViewById(R.id.indeterminateBar);
        progressBar.setVisibility(View.INVISIBLE);

        initVolleyCallback();
        volleyService = new VolleyService(mResultCallback, this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                registerButton.setEnabled(false);
                registerAndGetToken();
            }
        });
    }

    private void registerAndGetToken() {
        JSONBuilder jsonBuilder = new JSONBuilder();
        String login = loginEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        loginEditText.setEnabled(false);
        emailEditText.setEnabled(false);
        passwordEditText.setEnabled(false);

        jsonBuilder.addNextLine("username", login);
        jsonBuilder.addNextLine("email", email);
        jsonBuilder.addNextLine("password", password);

        progressBar.setVisibility(View.VISIBLE);
        volleyService.postDataVolleyRequest("POSTCALL", Consts.API_REGISTER_ENDPOINT, jsonBuilder.getJson());
    }

    void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONArray response) {
                Log.d("Response:", response.toString());
            }

            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.d("ResponseJSONOBJECT: ", response.toString());
                SharedPreferences sharedPreferences = getSharedPreferences(Consts.TOKEN_FILE, MODE_PRIVATE);
                progressBar.setVisibility(View.GONE);
                createIntentForNextActivity();
                registerButton.setEnabled(true);
                try {
                    String token = response.getString(Consts.TOKEN_KEY);
                    SharedPreferencesSaver.saveTokenToSharedPreferences(token, sharedPreferences);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                handleResponseErrors(error); //TODO Check if editexts are empty
                Log.d("Response:", error.toString());
            }
        };
    }

    private void handleResponseErrors(VolleyError error) {
        if (error instanceof ClientError) { //TODO Add support for other types of errors
            progressBar.setVisibility(View.GONE);
            registerButton.setEnabled(true);
            handleClientError();
        }
    }

    private void handleClientError() {
        loginEditText.setBackgroundResource(R.drawable.error_edit_text);
        emailEditText.setBackgroundResource(R.drawable.error_edit_text);
        passwordEditText.setBackgroundResource(R.drawable.error_edit_text);
        loginEditText.setEnabled(true);
        emailEditText.setEnabled(true);
        passwordEditText.setEnabled(true);
        loginEditText.setText("");
        emailEditText.setText("");
        passwordEditText.setText("");
        Toast.makeText(RegisterActivity.this, Consts.WRONG_REGISTER_CREDENTIALS_MSG_ENG, Toast.LENGTH_SHORT).show();
    }

    private void createIntentForNextActivity() {
        Intent userMainScreenIntent = new Intent(RegisterActivity.this, UserMainScreenActivity.class);
        userMainScreenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(userMainScreenIntent);
    }
}

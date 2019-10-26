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
import com.example.engieersthesis.utility.Consts;
import com.example.engieersthesis.utility.JSONBuilder;
import com.example.engieersthesis.utility.SharedPreferencesSaver;
import com.example.engieersthesis.requests.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LogInActivity extends AppCompatActivity {
    IResult mResultCallback = null;
    private VolleyService volleyService;
    private Button logInButton;
    private EditText loginEditText;
    private EditText passwordEditText;
    private ProgressBar progressBar;


    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);
        loginEditText.setEnabled(true);
        passwordEditText.setEnabled(true);
        logInButton.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prepareAllControllers();
        initVolleyCallback();
        volleyService = new VolleyService(mResultCallback, this);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                logInButton.setEnabled(false);
                loginAndGetToken();
            }
        });
    }

    private void prepareAllControllers() {
        logInButton = findViewById(R.id.signInButton);
        loginEditText = findViewById(R.id.loginEditTextLoginActivity);
        passwordEditText = findViewById(R.id.passwordEditTextLoginActivity);

        progressBar = findViewById(R.id.indeterminateBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void loginAndGetToken() {
        JSONBuilder jsonBuilder = new JSONBuilder();
        String login = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        loginEditText.setEnabled(false);
        passwordEditText.setEnabled(false);

        jsonBuilder.addNextLine("username", login);
        jsonBuilder.addNextLine("password", password);

        progressBar.setVisibility(View.VISIBLE);
        volleyService.postDataVolleyRequest(Consts.POST_CALL_REQUEST_TYPE, Consts.API_LOGIN_ENDPOINT, jsonBuilder.getJson());
    }

    void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONArray response) {
                Log.d("Response", response.toString());

            }

            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.d("ResponseJSONOBJECT", response.toString());
                SharedPreferences sharedPreferences = getSharedPreferences(Consts.TOKEN_FILE, MODE_PRIVATE);
                progressBar.setVisibility(View.GONE);
                createIntentForNextActivity();
                logInButton.setEnabled(true);
                try {
                    String token = response.getString(Consts.TOKEN_KEY);
                    SharedPreferencesSaver.saveTokenToSharedPreferences(token, sharedPreferences);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                handleResponseErrors(error);
                Log.d("Response:", error.toString());
            }
        };
    }

    private void createIntentForNextActivity() {
        Intent userMainScreenIntent = new Intent(LogInActivity.this, UserMainScreenActivity.class);
        startActivity(userMainScreenIntent);
    }

    private void handleResponseErrors(VolleyError error) {
        if (error instanceof ClientError) {
            progressBar.setVisibility(View.GONE);
            logInButton.setEnabled(true);
            handleClientError();
        }
    }

    private void handleClientError() {
        loginEditText.setBackgroundResource(R.drawable.error_edit_text);
        passwordEditText.setBackgroundResource(R.drawable.error_edit_text);
        loginEditText.setEnabled(true);
        passwordEditText.setEnabled(true);
        loginEditText.setText("");
        passwordEditText.setText("");
        Toast.makeText(LogInActivity.this, Consts.WRONG_CREDENTIALS_MSG_PL, Toast.LENGTH_SHORT).show();
    }
}

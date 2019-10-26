package com.example.engieersthesis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.example.engieersthesis.Interfaces.IResult;
import com.example.engieersthesis.requests.VolleyService;
import com.example.engieersthesis.utility.Consts;
import com.example.engieersthesis.utility.SharedPreferencesSaver;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    IResult mResultCallback = null;
    private VolleyService volleyService;
    private Button logInButton;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVolleyCallback();
        volleyService = new VolleyService(mResultCallback, this);

        checkIfUserHasValidToken();

        logInButton = findViewById(R.id.logInButton);
        createAccountButton = findViewById(R.id.createAccountButton);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logInIntent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(logInIntent);
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createAccountIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(createAccountIntent);
            }
        });
    }

    private void checkIfUserHasValidToken() {
        SharedPreferences sharedPreferences = getSharedPreferences(Consts.TOKEN_FILE, MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        if (token.length() != 0) {
            volleyService.getDataVolleyRequest(Consts.GET_METHOD, Consts.API_USER_INFO_ENDPOINT);
        }
    }

    void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONArray response) {
                Log.d("ResponseJSONArray", response.toString());
            }

            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.d("ResponseJSONObject", response.toString());
                startUserMainScreenActivity();
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d("Response:", error.toString());
                if (error instanceof AuthFailureError) {
                    Log.d("CheckUserResponse", "User doesn't have active token");
                    checkIfTokenExistAndRemoveIt();
                }
            }
        };
    }

    private void checkIfTokenExistAndRemoveIt() {
        SharedPreferences sharedPreferences = getSharedPreferences(Consts.TOKEN_FILE, MODE_PRIVATE);
        String token = sharedPreferences.getString(Consts.TOKEN_KEY, "");
        if(token.length() != 0) {
            SharedPreferencesSaver.deleteTokenFromSharedPreferences(sharedPreferences);
        }
    }

    private void startUserMainScreenActivity() {
        Intent userMainScreenIntent = new Intent(MainActivity.this, UserMainScreenActivity.class);
        userMainScreenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(userMainScreenIntent);
    }


}

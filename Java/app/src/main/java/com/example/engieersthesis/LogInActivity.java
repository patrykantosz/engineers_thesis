package com.example.engieersthesis;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.engieersthesis.requests.PostRequest;
import com.example.engieersthesis.utility.Consts;
import com.example.engieersthesis.utility.JSONBuilder;
import com.example.engieersthesis.utility.PostRequestType;
import com.example.engieersthesis.utility.SharedPreferencesSaver;

import java.util.concurrent.ExecutionException;

public class LogInActivity extends AppCompatActivity {
    private Button logInButton;
    private EditText loginEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logInButton = findViewById(R.id.signInButton);
        loginEditText = findViewById(R.id.loginEditTextLoginActivity);
        passwordEditText = findViewById(R.id.passwordEditTextLoginActivity);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(Consts.TOKEN_FILE, MODE_PRIVATE);
                SharedPreferencesSaver.saveTokenToSharedPreferences(loginAndGetToken(), sharedPreferences);
            }
        });
    }

    private String loginAndGetToken() {
        JSONBuilder jsonBuilder = new JSONBuilder();
        String login = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        loginEditText.setText("");
        passwordEditText.setText("");

        jsonBuilder.addNextLine("username", login);
        jsonBuilder.addNextLine("password", password);

        PostRequest postRequest = new PostRequest();
        postRequest.setJsonRequest(jsonBuilder.getJson());
        postRequest.setRequestType(PostRequestType.LOGIN);
        try {
            return postRequest.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.example.engieersthesis;

import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity {
    private Button registerButton;
    private EditText loginEditText;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = findViewById(R.id.registerButton);
        loginEditText = findViewById(R.id.loginEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(Consts.TOKEN_FILE, MODE_PRIVATE);
                SharedPreferencesSaver.saveTokenToSharedPreferences(registerAndGetToken(), sharedPreferences);
                Intent userMainScreenIntent = new Intent(RegisterActivity.this, UserMainScreenActivity.class);
                startActivity(userMainScreenIntent);
            }
        });
    }

    private String registerAndGetToken() {
        JSONBuilder jsonBuilder = new JSONBuilder();
        String login = loginEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        loginEditText.setText("");
        emailEditText.setText("");
        passwordEditText.setText("");

        jsonBuilder.addNextLine("username", login);
        jsonBuilder.addNextLine("email", email);
        jsonBuilder.addNextLine("password", password);

        PostRequest postRequest = new PostRequest();
        postRequest.setJsonRequest(jsonBuilder.getJson());
        postRequest.setRequestType(PostRequestType.REGISTER);
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

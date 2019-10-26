package com.example.engieersthesis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.ParseError;
import com.android.volley.VolleyError;
import com.example.engieersthesis.Interfaces.IResult;
import com.example.engieersthesis.requests.VolleyService;
import com.example.engieersthesis.utility.Consts;
import com.example.engieersthesis.utility.JSONBuilder;
import com.example.engieersthesis.utility.SharedPreferencesSaver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserMainScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    IResult mResultCallback = null;
    private VolleyService volleyService;
    private Button breakfastAddButton;
    private Button brunchAddButton;
    private Button dinnerAddButton;
    private Button supperAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        prepareAllControllers();
        initVolleyCallback();
        volleyService = new VolleyService(mResultCallback, this);

        breakfastAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFoodProductIntent = new Intent(UserMainScreenActivity.this, AddFoodProductActivity.class);
                addFoodProductIntent.putExtra("mealName", Consts.BREAKFAST_PL);
                startActivity(addFoodProductIntent);
            }
        });

        brunchAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFoodProductIntent = new Intent(UserMainScreenActivity.this, AddFoodProductActivity.class);
                addFoodProductIntent.putExtra("mealName", Consts.BRUNCH_PL);
                startActivity(addFoodProductIntent);
            }
        });

        dinnerAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFoodProductIntent = new Intent(UserMainScreenActivity.this, AddFoodProductActivity.class);
                addFoodProductIntent.putExtra("mealName", Consts.DINNER_PL);
                startActivity(addFoodProductIntent);
            }
        });

        supperAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFoodProductIntent = new Intent(UserMainScreenActivity.this, AddFoodProductActivity.class);
                addFoodProductIntent.putExtra("mealName", Consts.SUPPER_PL);
                startActivity(addFoodProductIntent);
            }
        });

    }

    private void prepareAllControllers() {
        breakfastAddButton = findViewById(R.id.breakfastAddButton);
        brunchAddButton = findViewById(R.id.brunchAddButton);
        dinnerAddButton = findViewById(R.id.dinnerAddButton);
        supperAddButton = findViewById(R.id.supperAddButton);
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
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d("Response:", error.toString());
                //this is hack, goolge volley is trying to parse all
                //responses, but logout is HTTP 204 No Content, so
                //volley gets an ParseError, which basically means
                //that logout is successful
                if (error instanceof ParseError) {
                    deleteTokenFromSharedPreferences();
                    closeAllActivitiesAndGoToMainScreen();
                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.logout_tools) {
            logOut();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOut() {
        JSONBuilder jsonBuilder = new JSONBuilder();

        volleyService.postDataVolleyRequest(Consts.POST_CALL_REQUEST_TYPE, Consts.API_LOG_OUT_ENDPOINT, jsonBuilder.getJson());
    }

    private void closeAllActivitiesAndGoToMainScreen() {
        Intent mainActivityIntent = new Intent(UserMainScreenActivity.this, MainActivity.class);

        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainActivityIntent);
    }

    private void deleteTokenFromSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences(Consts.TOKEN_FILE, MODE_PRIVATE);
        SharedPreferencesSaver.deleteTokenFromSharedPreferences(sharedPreferences);
    }
}

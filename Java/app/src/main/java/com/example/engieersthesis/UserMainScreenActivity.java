package com.example.engieersthesis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.ParseError;
import com.android.volley.VolleyError;
import com.example.engieersthesis.Interfaces.IResult;
import com.example.engieersthesis.adapers.FoodListAdapter;
import com.example.engieersthesis.requests.VolleyService;
import com.example.engieersthesis.utility.Consts;
import com.example.engieersthesis.utility.JSONBuilder;
import com.example.engieersthesis.utility.JSONUtilities;
import com.example.engieersthesis.utility.SharedPreferencesSaver;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UserMainScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    IResult mResultCallback = null;
    private VolleyService volleyService;
    private Button breakfastAddButton;
    private Button brunchAddButton;
    private Button dinnerAddButton;
    private Button supperAddButton;
    private ImageButton leftArrowButton;
    private ImageButton rightArrowButton;
    private TextView calendarTextView;
    private ListView breakfastListView;
    private ListView brunchListView;
    private ListView dinnerListView;
    private ListView supperListView;

    private JSONObject userFoodHistoryJSON;

    private int daysToGetDate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        prepareAllControllers();
        initVolleyCallback();
        volleyService = new VolleyService(mResultCallback, this);
        getUserFoodHistory();
        daysToGetDate = 0;
        setCalendarTextView();

        breakfastAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFoodProductIntent = new Intent(UserMainScreenActivity.this, AddFoodProductActivity.class);
                addFoodProductIntent.putExtra(Consts.MEAL_TYPE_INTENT_EXTRA, Consts.BREAKFAST_PL);
                addFoodProductIntent.putExtra(Consts.MEAL_DATE_INTENT_EXTRA, calendarTextView.getText());
                startActivity(addFoodProductIntent);
            }
        });

        brunchAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFoodProductIntent = new Intent(UserMainScreenActivity.this, AddFoodProductActivity.class);
                addFoodProductIntent.putExtra(Consts.MEAL_TYPE_INTENT_EXTRA, Consts.BRUNCH_PL);
                addFoodProductIntent.putExtra(Consts.MEAL_DATE_INTENT_EXTRA, calendarTextView.getText());
                startActivity(addFoodProductIntent);
            }
        });

        dinnerAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFoodProductIntent = new Intent(UserMainScreenActivity.this, AddFoodProductActivity.class);
                addFoodProductIntent.putExtra(Consts.MEAL_TYPE_INTENT_EXTRA, Consts.DINNER_PL);
                addFoodProductIntent.putExtra(Consts.MEAL_DATE_INTENT_EXTRA, calendarTextView.getText());
                startActivity(addFoodProductIntent);
            }
        });

        supperAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFoodProductIntent = new Intent(UserMainScreenActivity.this, AddFoodProductActivity.class);
                addFoodProductIntent.putExtra(Consts.MEAL_TYPE_INTENT_EXTRA, Consts.SUPPER_PL);
                addFoodProductIntent.putExtra(Consts.MEAL_DATE_INTENT_EXTRA, calendarTextView.getText());
                startActivity(addFoodProductIntent);
            }
        });

        leftArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daysToGetDate -= 1;
                setCalendarTextView();
                getUserFoodHistory();
            }
        });

        rightArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daysToGetDate += 1;
                setCalendarTextView();
                getUserFoodHistory();
            }
        });

    }

    private void setCalendarTextView() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateFromOtherActivity = getIntent().getStringExtra(Consts.MEAL_DATE_INTENT_EXTRA);
        if (dateFromOtherActivity != null) {
            calendarTextView.setText(dateFromOtherActivity);
            getIntent().removeExtra(Consts.MEAL_DATE_INTENT_EXTRA);
        } else {
            calendarTextView.setText(dateFormat.format(getDayDate()));
        }
    }

    private Date getDayDate() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, +daysToGetDate);
        return calendar.getTime();
    }

    private void getUserFoodHistory() {
        IResult resultCalback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONArray response) {
                Log.d("ResponseArray", response.toString());
                volleyService.setmResultCallback(mResultCallback);
            }

            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.d("ResponseObject", response.toString());
                userFoodHistoryJSON = response;
                try {
                    JSONArray array = userFoodHistoryJSON.getJSONArray("meal");
                    ArrayList<JSONObject> mealArrayList = JSONUtilities.getArrayListFromJSONARRAY(array);
                    fillUserFoodHistoryListViews(mealArrayList);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    volleyService.setmResultCallback(mResultCallback);
                }
                volleyService.setmResultCallback(mResultCallback);
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d("Response", error.toString());
            }
        };

        volleyService.setmResultCallback(resultCalback);
        volleyService.getDataVolleyRequest(Consts.GET_METHOD, Consts.API_USER_FOOD_HISTORY_LIST_ENDPOINT);
    }

    private void fillUserFoodHistoryListViews(ArrayList<JSONObject> mealArrayList) {
        breakfastListView.setAdapter(null);
        brunchListView.setAdapter(null);
        dinnerListView.setAdapter(null);
        supperListView.setAdapter(null);

        for (JSONObject obj : mealArrayList) {
            try {
                String data = obj.getString(Consts.MEAL_DATE);
                String meal_type = obj.getString(Consts.MEAL_TYPE);
                if (calendarTextView.getText().equals(data)) {
                    JSONArray foodarray = obj.getJSONArray("food");
                    final ArrayList<JSONObject> listItems = JSONUtilities.getArrayListFromJSONARRAY(foodarray);

                    FoodListAdapter adapter = new FoodListAdapter(UserMainScreenActivity.this, R.layout.food_product_in_meal_list_view, R.id.foodProductNameTextView, listItems);

                    if (meal_type != null && !meal_type.isEmpty()) {
                        switch (meal_type) {
                            case "BF":
                                breakfastListView.setAdapter(adapter);
                                break;
                            case "BR":
                                brunchListView.setAdapter(adapter);
                                break;
                            case "DN":
                                dinnerListView.setAdapter(adapter);
                                break;
                            case "SU":
                                supperListView.setAdapter(adapter);
                                break;
                        }
                    }
                }
            } catch (JSONException e) {

            }
        }

        breakfastListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int postion, long id) {
                Object listItem = parent.getItemAtPosition(postion);
                if (listItem != null) {
                    startEditFoodActivity(listItem);
                }
            }
        });
        brunchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int postion, long id) {
                Object listItem = parent.getItemAtPosition(postion);
                if (listItem != null) {
                    startEditFoodActivity(listItem);
                }
            }
        });
        dinnerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int postion, long id) {
                Object listItem = parent.getItemAtPosition(postion);
                if (listItem != null) {
                    startEditFoodActivity(listItem);
                }
            }
        });
        supperListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int postion, long id) {
                Object listItem = parent.getItemAtPosition(postion);
                if (listItem != null) {
                    startEditFoodActivity(listItem);
                }
            }
        });


    }

    private void startEditFoodActivity(Object listItem) {
        Intent editFoodProductDetailedIntent = new Intent(UserMainScreenActivity.this, AddFoodProductDetailedActivity.class);
        editFoodProductDetailedIntent.putExtra(Consts.JSON_STRING_FOOD_ID, listItem.toString());
        editFoodProductDetailedIntent.putExtra(Consts.FOOD_EDITED_INTENT_EXTRA, true);
        editFoodProductDetailedIntent.putExtra(Consts.MEAL_DATE_INTENT_EXTRA, calendarTextView.getText());
        startActivity(editFoodProductDetailedIntent);
    }

    private void prepareAllControllers() {
        breakfastAddButton = findViewById(R.id.breakfastAddButton);
        brunchAddButton = findViewById(R.id.brunchAddButton);
        dinnerAddButton = findViewById(R.id.dinnerAddButton);
        supperAddButton = findViewById(R.id.supperAddButton);
        leftArrowButton = findViewById(R.id.leftArrowButton);
        rightArrowButton = findViewById(R.id.rightArrowButton);
        calendarTextView = findViewById(R.id.calendarTextView);
        breakfastListView = findViewById(R.id.breakfastFoodProductsListView);
        brunchListView = findViewById(R.id.brunchFoodProductsListView);
        dinnerListView = findViewById(R.id.dinnerFoodProductsListView);
        supperListView = findViewById(R.id.supperFoodProductsListView);
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

    private void deleteTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(Consts.TOKEN_FILE, MODE_PRIVATE);
        SharedPreferencesSaver.deleteTokenFromSharedPreferences(sharedPreferences);
    }
}

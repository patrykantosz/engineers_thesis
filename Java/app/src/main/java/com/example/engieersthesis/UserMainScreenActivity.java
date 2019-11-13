package com.example.engieersthesis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.example.engieersthesis.utility.DoubleRounder;
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
    private TextView targetCaloriesTextView;
    private TextView targetCarbosTextView;
    private TextView targetFatsTextView;
    private TextView targetProteinsTextView;
    private TextView dailyCaloriesTextView;
    private TextView dailyCarbosTextView;
    private TextView dailyFatsTextView;
    private TextView dailyProteinsTextView;
    private ProgressBar caloriesProgressBar;
    private ProgressBar carbosProgessBar;
    private ProgressBar fatsProgressBar;
    private ProgressBar proteinsProgressBar;

    private boolean refreshIsNeeded = false;

    private double dailyCaloriesValue;
    private double dailyCarbosValue;
    private double dailyFatsValue;
    private double dailyProteinsValue;
    private int targetCaloriesValue;
    private int targetCarbosValue;
    private int targetFatsValue;
    private int targetProteinsValue;

    private JSONObject userFoodHistoryJSON;

    private int daysToGetDate = 0;

    @Override
    protected void onResume() {
        super.onResume();
        if (volleyService != null && refreshIsNeeded) {
            getUserFoodHistory();
        }
    }

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
        daysToGetDate = 0;
        setCalendarTextView();
        fillTexViewsWithDailySummary();
        getUserFoodHistory();

        breakfastAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFoodProductIntent = new Intent(UserMainScreenActivity.this, AddFoodProductActivity.class);
                addFoodProductIntent.putExtra(Consts.MEAL_TYPE_INTENT_EXTRA, Consts.BREAKFAST_EN);
                addFoodProductIntent.putExtra(Consts.MEAL_DATE_INTENT_EXTRA, calendarTextView.getText());
                startActivity(addFoodProductIntent);
            }
        });

        brunchAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFoodProductIntent = new Intent(UserMainScreenActivity.this, AddFoodProductActivity.class);
                addFoodProductIntent.putExtra(Consts.MEAL_TYPE_INTENT_EXTRA, Consts.BRUNCH_EN);
                addFoodProductIntent.putExtra(Consts.MEAL_DATE_INTENT_EXTRA, calendarTextView.getText());
                startActivity(addFoodProductIntent);
            }
        });

        dinnerAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFoodProductIntent = new Intent(UserMainScreenActivity.this, AddFoodProductActivity.class);
                addFoodProductIntent.putExtra(Consts.MEAL_TYPE_INTENT_EXTRA, Consts.DINNER_EN);
                addFoodProductIntent.putExtra(Consts.MEAL_DATE_INTENT_EXTRA, calendarTextView.getText());
                startActivity(addFoodProductIntent);
            }
        });

        supperAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFoodProductIntent = new Intent(UserMainScreenActivity.this, AddFoodProductActivity.class);
                addFoodProductIntent.putExtra(Consts.MEAL_TYPE_INTENT_EXTRA, Consts.SUPPER_EN);
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
        refreshIsNeeded = getIntent().getBooleanExtra("REFRESH", false);
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
                }
                volleyService.setmResultCallback(mResultCallback);
                fillTextViewsWithUserNutritionsTargets();
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
        setAdaptersToNull();
        setDailyFiledsToZero();

        for (JSONObject obj : mealArrayList) {
            try {
                String data = obj.getString(Consts.MEAL_DATE);
                String meal_type = obj.getString(Consts.MEAL_TYPE);
                if (calendarTextView.getText().equals(data)) {
                    JSONArray foodarray = obj.getJSONArray("food");
                    final ArrayList<JSONObject> listItems = JSONUtilities.getArrayListFromJSONARRAY(foodarray);
                    addFoodToDailySummary(listItems);

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

        fillTexViewsWithDailySummary();
        setProgressToProgressBars();

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

    private void setAdaptersToNull() {
        breakfastListView.setAdapter(null);
        brunchListView.setAdapter(null);
        dinnerListView.setAdapter(null);
        supperListView.setAdapter(null);
    }

    private void setDailyFiledsToZero() {
        dailyCaloriesValue = 0;
        dailyCarbosValue = 0;
        dailyFatsValue = 0;
        dailyProteinsValue = 0;

    }

    private void addFoodToDailySummary(ArrayList<JSONObject> foodArrayJson) {
        for (JSONObject foodJson : foodArrayJson) {
            try {
                double foodWeightMultiplier = DoubleRounder.roundDouble(foodJson.getDouble(Consts.FOOD_PRODUCT_WEIGHT) / Consts.DEFAULT_MASS_DIV, 2);
                dailyCaloriesValue += DoubleRounder.roundDouble(foodJson.getInt(Consts.FOOD_PRODUCT_ENERGY_VALUE) * foodWeightMultiplier, 0);
                dailyCarbosValue += DoubleRounder.roundDouble(foodJson.getDouble(Consts.FOOD_PRODUCT_CARBOHYDRATES) * foodWeightMultiplier, 2);
                dailyProteinsValue += DoubleRounder.roundDouble(foodJson.getDouble(Consts.FOOD_PRODUCT_PROTEINS) * foodWeightMultiplier, 2);
                dailyFatsValue += DoubleRounder.roundDouble(foodJson.getDouble(Consts.FOOD_PRODUCT_FATS) * foodWeightMultiplier, 2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void fillTexViewsWithDailySummary() {
        dailyCaloriesTextView.setText(Double.toString(dailyCaloriesValue));
        dailyCarbosTextView.setText(Double.toString(DoubleRounder.roundDouble(dailyCarbosValue, 2)));
        dailyFatsTextView.setText(Double.toString(DoubleRounder.roundDouble(dailyFatsValue, 2)));
        dailyProteinsTextView.setText(Double.toString(DoubleRounder.roundDouble(dailyProteinsValue, 2)));
    }

    private void fillTextViewsWithUserNutritionsTargets() {
        IResult getNutritionsTargetCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONArray response) {
                Log.d("JsonArrayResponse1", response.toString());
            }

            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.d("JsonObjectTarget", response.toString());
                parseJsonInfoToTextViews(response);
                volleyService.setmResultCallback(mResultCallback);
                refreshIsNeeded = false;
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d("ErrorResponse1", error.toString());
            }
        };

        volleyService.setmResultCallback(getNutritionsTargetCallback);
        volleyService.getDataVolleyRequest(Consts.GET_METHOD, Consts.API_USER_NUTRITIONS_TARGET_ENDPOINT);
    }

    private void parseJsonInfoToTextViews(JSONObject response) {
        try {
            targetCaloriesTextView.setText("/" + response.getInt(Consts.USER_TARGET_CALORIES));
            targetCarbosTextView.setText("/" + response.getInt(Consts.USER_TARGET_CARBOHYDRATES));
            targetFatsTextView.setText("/" + response.get(Consts.USER_TARGET_FATS));
            targetProteinsTextView.setText("/" + response.getInt(Consts.USER_TARGET_PROTEINS));
            parseJsonInfoToDoubles(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseJsonInfoToDoubles(JSONObject response) {
        try {
            targetCaloriesValue = response.getInt(Consts.USER_TARGET_CALORIES);
            targetCarbosValue = response.getInt(Consts.USER_TARGET_CARBOHYDRATES);
            targetFatsValue = response.getInt(Consts.USER_TARGET_FATS);
            targetProteinsValue = response.getInt(Consts.USER_TARGET_PROTEINS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setProgressToProgressBars();
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
        caloriesProgressBar = findViewById(R.id.caloriesProgressBar);
        carbosProgessBar = findViewById(R.id.carbosProgressBar);
        proteinsProgressBar = findViewById(R.id.proteinsProgressBar);
        fatsProgressBar = findViewById(R.id.fatsProgressBar);
        targetCaloriesTextView = findViewById(R.id.targetCaloriesTextView);
        targetCarbosTextView = findViewById(R.id.targetCarbosTextView);
        targetFatsTextView = findViewById(R.id.fatsTargetTextView);
        targetProteinsTextView = findViewById(R.id.proteinsTargetTextView);
        dailyCaloriesTextView = findViewById(R.id.dailyCaloriesTextView);
        dailyCarbosTextView = findViewById(R.id.dailyCarbosTextView);
        dailyFatsTextView = findViewById(R.id.dailyFatsTextView);
        dailyProteinsTextView = findViewById(R.id.dailyProteinsTextView);
    }

    void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONArray response) {
                Log.d("Response", response.toString());

            }

            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.d("ResponseJSONOBJECT1", response.toString());
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

        if (id == R.id.parameters_tools) {
            startBodyParameterActivity();
        } else if (id == R.id.target_tools) {
            startNutritionsTargetActivity();
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

    private void startBodyParameterActivity() {
        Intent bodyParameterIntent = new Intent(UserMainScreenActivity.this, BodyParametersActivity.class);
        startActivity(bodyParameterIntent);
    }

    private void startNutritionsTargetActivity() {
        Intent nutritionsTargetIntent = new Intent(UserMainScreenActivity.this, NutritionsTargetActivity.class);
        startActivity(nutritionsTargetIntent);
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

    private void setProgressToProgressBars() {
        int caloriesProgress = (int) Math.round((Double.parseDouble(dailyCaloriesTextView.getText().toString()) * 100) / targetCaloriesValue);
        int carbosProgress = (int) Math.round((Double.parseDouble(dailyCarbosTextView.getText().toString()) * 100) / targetCarbosValue);
        int fatsProgress = (int) Math.round((Double.parseDouble(dailyFatsTextView.getText().toString()) * 100) / targetFatsValue);
        int proteinsProgress = (int) Math.round((Double.parseDouble(dailyProteinsTextView.getText().toString()) * 100) / targetProteinsValue);

        Log.d("Calories", caloriesProgress + "%");
        Log.d("Carbos", carbosProgress + "%");
        Log.d("Fats", fatsProgress + "%");
        Log.d("Proteins", proteinsProgress + "%");

        caloriesProgressBar.setProgress(caloriesProgress);
        carbosProgessBar.setProgress(carbosProgress);
        fatsProgressBar.setProgress(fatsProgress);
        proteinsProgressBar.setProgress(proteinsProgress);

        progressBarPercentColor(caloriesProgressBar);
        progressBarPercentColor(carbosProgessBar);
        progressBarPercentColor(fatsProgressBar);
        progressBarPercentColor(proteinsProgressBar);
    }

    private void progressBarPercentColor(ProgressBar progressBarToChangeColor) {
        int progressPercent = progressBarToChangeColor.getProgress();
        Drawable progressBarDrawable = progressBarToChangeColor.getProgressDrawable().mutate();
        if (progressPercent < 85) {
            progressBarDrawable.setColorFilter(Color.parseColor("#1EC000"), PorterDuff.Mode.SRC_IN);
        } else if (progressPercent < 100) {
            progressBarDrawable.setColorFilter(Color.parseColor("#FFC107"), PorterDuff.Mode.SRC_IN);
        } else {
            progressBarDrawable.setColorFilter(Color.parseColor("#FF1100"), PorterDuff.Mode.SRC_IN);
        }
        progressBarToChangeColor.setProgressDrawable(progressBarDrawable);
    }
}

package com.example.engieersthesis;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.android.volley.VolleyError;
import com.example.engieersthesis.Interfaces.IResult;
import com.example.engieersthesis.requests.VolleyService;
import com.example.engieersthesis.utility.Consts;
import com.example.engieersthesis.utility.JSONUtilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddFoodProductActivity extends AppCompatActivity {

    IResult mResultCallback = null;
    private TextView mealNameTextView;
    private EditText foodSearchEditText;
    private VolleyService volleyService;
    private ImageButton photoFoodSearchButton;
    private Button addNewFoodProductButton;
    private ListView listView;
    private String mealName;
    private String mealDate;
    private String currentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1996;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_product);

        mealName = getIntent().getStringExtra(Consts.MEAL_TYPE_INTENT_EXTRA);
        mealDate = getIntent().getStringExtra(Consts.MEAL_DATE_INTENT_EXTRA);

        mealNameTextView = findViewById(R.id.mealNameTextView);
        foodSearchEditText = findViewById(R.id.foodSearchEditText);
        addNewFoodProductButton = findViewById(R.id.addNewFoodProductButton);
        listView = findViewById(R.id.foodProductsListView);
        photoFoodSearchButton = findViewById(R.id.photoFoodSearchButton);

        mealNameTextView.setText(mealName);

        initVolleyCallback();
        volleyService = new VolleyService(mResultCallback, this);

        foodSearchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() >= 3) {
                    searchProductInDatabase(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        addNewFoodProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNewFoodProductIntent = new Intent(AddFoodProductActivity.this, AddNewFoodProductActivity.class);
                startActivity(addNewFoodProductIntent);
            }
        });

        photoFoodSearchButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                //dispatchTakePictureIntent();
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    dispatchTakePictureIntent();
                }
            }
        });

    }



    private void searchProductInDatabase(String foodToSearch) {
        String urlWithFoodToSearchAsQueryParam = Consts.API_LIST_ALL_FOOD_PRODUCTS_ENDPOINT +
                Consts.API_QUERY_PARAM_TO_SEARCH_FOOD_BY_NAME_ENDPOINT + foodToSearch;
        Log.d("URL: ", urlWithFoodToSearchAsQueryParam);

        volleyService.getDataVolleyRequest(Consts.GET_METHOD, urlWithFoodToSearchAsQueryParam);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImagefile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.example.android.fileproviderforthesis",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            File imageFile = new File(currentPhotoPath);
            if(imageFile.exists()) {
                Intent intent = new Intent(AddFoodProductActivity.this, SearchFoodByPhotoActivity.class);
                intent.putExtra(Consts.PHOTO_PATH, currentPhotoPath);
                intent.putExtra(Consts.MEAL_TYPE_INTENT_EXTRA, mealName);
                intent.putExtra(Consts.MEAL_DATE_INTENT_EXTRA, mealDate);
                startActivity(intent);
            }
        }
    }

    private void runGoogleFirebaseLabeling(Bitmap imageBitmap) {

    }

    private File createImagefile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONArray response) {
                Log.d("ResponseJSONARRAY:", response.toString());
                final ArrayList<JSONObject> listItems = JSONUtilities.getArrayListFromJSONARRAY(response);

                ListAdapter adapter = new com.example.engieersthesis.adapers.ListAdapter(AddFoodProductActivity.this,
                        R.layout.food_products_list_view, R.id.foodProductNameTextView, listItems);

                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int postion, long id) {
                        Object listItem = parent.getItemAtPosition(postion);
                        if (listItem != null) {
                            Intent addFoodProductDetailedIntent = new Intent(AddFoodProductActivity.this, AddFoodProductDetailedActivity.class);
                            addFoodProductDetailedIntent.putExtra(Consts.JSON_STRING_FOOD_ID, listItem.toString());
                            addFoodProductDetailedIntent.putExtra(Consts.MEAL_TYPE_TO_JSON_REQUEST, getMealType());
                            addFoodProductDetailedIntent.putExtra(Consts.MEAL_DATE_INTENT_EXTRA, mealDate);
                            startActivity(addFoodProductDetailedIntent);
                        }
                    }
                });
            }

            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.d("ResponseJSONOBJECT: ", response.toString());
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d("Response:", error.toString());
            }
        };
    }

    private String getMealType() {
        switch (mealName) {
            case Consts.BREAKFAST_EN:
                return Consts.BREAKFAST_EN;
            case Consts.BRUNCH_EN:
                return Consts.BRUNCH_EN;
            case Consts.DINNER_EN:
                return Consts.DINNER_EN;
            case Consts.SUPPER_EN:
                return Consts.SUPPER_EN;
        }

        return "";
    }


}

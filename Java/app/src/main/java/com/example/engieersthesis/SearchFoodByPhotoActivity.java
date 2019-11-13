package com.example.engieersthesis;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.engieersthesis.Interfaces.IResult;
import com.example.engieersthesis.requests.VolleyService;
import com.example.engieersthesis.utility.Consts;
import com.example.engieersthesis.utility.JSONUtilities;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchFoodByPhotoActivity extends AppCompatActivity {
    IResult mResultCallback = null;
    private String currentPhotoPath;
    private String[] labelsFromFirebaseArray;
    private List<String> labelsList;
    private Button goBackToMainActivityButton;
    private ListView labelsListView;
    private ListView resultsListView;
    private String mealName;
    private String mealDate;
    private VolleyService volleyService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food_by_photo);
        currentPhotoPath = getIntent().getStringExtra(Consts.PHOTO_PATH);
        mealName = getIntent().getStringExtra(Consts.MEAL_TYPE_INTENT_EXTRA);
        mealDate = getIntent().getStringExtra(Consts.MEAL_DATE_INTENT_EXTRA);

        goBackToMainActivityButton = findViewById(R.id.backToPreviousActivityButton);
        labelsListView = findViewById(R.id.labelsListView);
        resultsListView = findViewById(R.id.resultsListView);
        initVolleyCallback();
        volleyService = new VolleyService(mResultCallback, this);

        if (!currentPhotoPath.isEmpty())
            labelImageWithGoogleFirebase();
        //fillLabelListView();

        goBackToMainActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToUserMainActivity();
            }
        });
    }

    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONArray response) {
                Log.d("ResponseJSONARRAY:", response.toString());
                final ArrayList<JSONObject> listItems = JSONUtilities.getArrayListFromJSONARRAY(response);

                ListAdapter adapter = new com.example.engieersthesis.adapers.ListAdapter(SearchFoodByPhotoActivity.this,
                        R.layout.food_products_list_view, R.id.foodProductNameTextView, listItems);

                resultsListView.setAdapter(adapter);

                resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int postion, long id) {
                        Object listItem = parent.getItemAtPosition(postion);
                        if (listItem != null) {
                            Intent addFoodProductDetailedIntent = new Intent(SearchFoodByPhotoActivity.this, AddFoodProductDetailedActivity.class);
                            addFoodProductDetailedIntent.putExtra(Consts.JSON_STRING_FOOD_ID, listItem.toString());
                            addFoodProductDetailedIntent.putExtra(Consts.MEAL_TYPE_TO_JSON_REQUEST, getMealType());
                            addFoodProductDetailedIntent.putExtra(Consts.MEAL_DATE_INTENT_EXTRA, mealDate);
                            addFoodProductDetailedIntent.putExtra(Consts.MULTIPLE_ADD, true);

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

    private void labelImageWithGoogleFirebase() {
        File imageFile = new File(currentPhotoPath);
        if (imageFile.exists()) {
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
            FirebaseVisionCloudImageLabelerOptions options =
                    new FirebaseVisionCloudImageLabelerOptions.Builder()
                            .setConfidenceThreshold(0.4f)
                            .build();


            FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getCloudImageLabeler(options);
            labeler.processImage(firebaseVisionImage)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionImageLabel> firebaseVisionImageLabels) {
                            labelsList = new ArrayList<>();
                            for (FirebaseVisionImageLabel label : firebaseVisionImageLabels) {
                                String text = label.getText();
                                String entityId = label.getEntityId();
                                float confidence = label.getConfidence();
                                Log.d("LABEL", text + " " + confidence);
                                if (Arrays.asList(Consts.FRIUTS_AND_VEGIES_ENG).contains(text) || Arrays.asList(Consts.FRIUTS_AND_VEGIES_FIRST_UPPER_ENG).contains(text)) {
                                    labelsList.add(text);
                                }
                            }
                            if (labelsList.size() > 0) {
                                labelsFromFirebaseArray = new String[labelsList.size()];
                                labelsFromFirebaseArray = labelsList.toArray(labelsFromFirebaseArray);
                                fillLabelListView();
                            } else {
                                Toast.makeText(SearchFoodByPhotoActivity.this, Consts.NO_OBJECTS_RECOGNIZED_MS_ENG + " else kurde", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("FirebaseFailure", e.getMessage());
                            Toast.makeText(SearchFoodByPhotoActivity.this, Consts.NO_OBJECTS_RECOGNIZED_MS_ENG, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
        } else {
            Toast.makeText(this, Consts.PHOTO_ERROR_MSG_ENG, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void fillLabelListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, labelsFromFirebaseArray);
        labelsListView.setAdapter(adapter);

        labelsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Object listitem = adapterView.getItemAtPosition(position);
                if (listitem != null) {
                    searchProductInDatabase(listitem.toString());
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

    private void goBackToUserMainActivity() {
        Intent userMainActivityIntent = new Intent(SearchFoodByPhotoActivity.this, UserMainScreenActivity.class);
        userMainActivityIntent.putExtra(Consts.MEAL_DATE_INTENT_EXTRA, mealDate);
        userMainActivityIntent.putExtra("REFRESH", true);
        userMainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(userMainActivityIntent);
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

    public boolean containsCaseInsensitive(String labelToCheck) {
        for (String string : Consts.FRIUTS_AND_VEGIES_ENG) {
            if (string.equalsIgnoreCase(labelToCheck)) {
                return true;
            }
        }
        return false;
    }
}

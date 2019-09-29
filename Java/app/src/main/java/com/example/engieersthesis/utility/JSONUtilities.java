package com.example.engieersthesis.utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONUtilities {
    static public ArrayList<JSONObject> getArrayListFromJSONARRAY(JSONArray jsonArray) {

        ArrayList<JSONObject> aList  = new ArrayList<>();

        try {
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    aList.add(jsonArray.getJSONObject(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return aList;
    }
}

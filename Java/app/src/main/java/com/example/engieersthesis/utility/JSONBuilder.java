package com.example.engieersthesis.utility;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONBuilder extends JSONObject {
    public void addNextLine(String key, String value) {
        try {
            this.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addNextLine(String key, double value) {
        try {
            this.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONBuilder getJson() {
        return this;
    }
}

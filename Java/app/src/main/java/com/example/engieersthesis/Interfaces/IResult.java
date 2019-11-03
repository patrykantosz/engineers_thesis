package com.example.engieersthesis.Interfaces;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IResult {
    void notifySuccess(String requestType, JSONArray response);

    void notifySuccess(String requestType, JSONObject response);

    void notifyError(String requestType, VolleyError error);
}

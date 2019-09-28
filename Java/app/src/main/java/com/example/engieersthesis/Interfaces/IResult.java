package com.example.engieersthesis.Interfaces;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IResult {
    public void notifySuccess(String requestType, JSONArray response);
    public void notifySuccess(String requestType, JSONObject response);
    public void notifyError(String requestType, VolleyError error);
}

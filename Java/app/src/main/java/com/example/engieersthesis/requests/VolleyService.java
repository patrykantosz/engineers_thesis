package com.example.engieersthesis.requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.engieersthesis.Interfaces.IResult;

import org.json.JSONArray;
import org.json.JSONObject;

public class VolleyService {

    private IResult mResultCallback;
    private Context mContext;

    public VolleyService(IResult resultCallback, Context context) {
        mResultCallback = resultCallback;
        mContext = context;
    }


    public void postDataVolleyRequest(final String requestType, String urlToRegisterEndpoint, JSONObject jsonRequest) {
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObj = new JsonObjectRequest(urlToRegisterEndpoint, jsonRequest, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (mResultCallback != null)
                        mResultCallback.notifySuccess(requestType, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mResultCallback != null)
                        mResultCallback.notifyError(requestType, error);
                }
            });

            queue.add(jsonObj);

        } catch (Exception e) {

        }
    }

    public void getDataVolleyRequest(final String requestType, String urlToLoginEndpoint) {
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            JsonArrayRequest jsonObj = new JsonArrayRequest(Request.Method.GET, urlToLoginEndpoint, null, new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    if (mResultCallback != null) {
                        try {
                            new Thread().sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mResultCallback.notifySuccess(requestType, response);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mResultCallback != null)
                        mResultCallback.notifyError(requestType, error);
                }
            });
            queue.add(jsonObj);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

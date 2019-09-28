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
    private RequestQueue queue;
    private JsonArrayRequest jsonArray;
    private JsonObjectRequest jsonObj;

    public VolleyService(IResult resultCallback, Context context) {
        mResultCallback = resultCallback;
        mContext = context;
    }


    public void postDataVolleyRequest(final String requestType, String urlToRegisterEndpoint, JSONObject jsonRequest) {
        try {
            queue = Volley.newRequestQueue(mContext);

            jsonObj = new JsonObjectRequest(urlToRegisterEndpoint, jsonRequest, new Response.Listener<JSONObject>() {
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
            queue = Volley.newRequestQueue(mContext);

            jsonArray = new JsonArrayRequest(Request.Method.GET, urlToLoginEndpoint, null, new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
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
            queue.add(jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelArrayRequest() {
        queue.cancelAll(jsonArray);
    }

    public void cancelObjectRequest() {
        queue.cancelAll(jsonObj);
    }
}

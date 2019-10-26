package com.example.engieersthesis.requests;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.engieersthesis.Interfaces.IResult;
import com.example.engieersthesis.utility.Consts;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

            if(urlToRegisterEndpoint.contains(Consts.API_REGISTER_ENDPOINT))
            {
                jsonObj = createLoginOrRegisterJsonObjectRequest(requestType, urlToRegisterEndpoint, jsonRequest);
            } else if(urlToRegisterEndpoint.contains(Consts.API_LOGIN_ENDPOINT)) {
                JsonObjectRequest newJsonLoginRequest = createLoginOrRegisterJsonObjectRequest(requestType, urlToRegisterEndpoint, jsonRequest);
                jsonObj = changeTimeoutForLoginRequest(newJsonLoginRequest);
            } else {
                jsonObj = createJsonObjectRequestWithAuthToken(requestType, urlToRegisterEndpoint, jsonRequest);
            }
            queue.add(jsonObj);
        } catch (Exception e) {

        }
    }

    private JsonObjectRequest createLoginOrRegisterJsonObjectRequest(final String requestType, String urlToRegisterEndpoint, JSONObject jsonRequest) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlToRegisterEndpoint, jsonRequest, new Response.Listener<JSONObject>() {
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

        return jsonObjectRequest;
    }

    private JsonObjectRequest createJsonObjectRequestWithAuthToken(final String requestType, String urlToRegisterEndpoint, JSONObject jsonRequest) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlToRegisterEndpoint, jsonRequest, new Response.Listener<JSONObject>() {
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
        }) {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = mContext.getSharedPreferences(Consts.TOKEN_FILE, Context.MODE_PRIVATE);
                String token = sharedPreferences.getString(Consts.TOKEN_KEY, "");
                Log.d("getHeadersPOST: ", token);
                params.put(Consts.CONTENT_TYPE, Consts.CONTENT_TYPE_APPLICATION_JSON_UTF8);
                params.put("Authorization", "Token " + token);
                return params;
            }
        };

        return jsonObjectRequest;
    }

    private JsonObjectRequest changeTimeoutForLoginRequest(JsonObjectRequest jsonObj) {
        jsonObj.setRetryPolicy(new DefaultRetryPolicy(
                Consts.LOGIN_TIMEOUT_MS,
                Consts.LOGIN_MAX_RETRIES,
                Consts.LOGIN_MAX_MULTIPLIER
        ));
        return jsonObj;
    }

    public void getDataVolleyRequest(final String requestType, String urlToLoginEndpoint) {
        try {
            queue = Volley.newRequestQueue(mContext);

            if (urlToLoginEndpoint.contains(Consts.API_QUERY_PARAM_TO_SEARCH_FOOD_BY_NAME_ENDPOINT) ||
                    urlToLoginEndpoint.contains(Consts.API_LIST_ALL_FOOD_PRODUCTS_ENDPOINT)) {
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
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<String, String>();
                        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Consts.TOKEN_FILE, Context.MODE_PRIVATE);
                        String token = sharedPreferences.getString(Consts.TOKEN_KEY, "");
                        Log.d("getHeadersGET: ", token);
                        params.put(Consts.CONTENT_TYPE, Consts.CONTENT_TYPE_APPLICATION_JSON_UTF8);
                        params.put("Authorization", "Token " + token);
                        return params;
                    }
                };
                queue.add(jsonArray);
            } else {
                jsonObj = new JsonObjectRequest(Request.Method.GET, urlToLoginEndpoint, null, new Response.Listener<JSONObject>() {

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
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<String, String>();
                        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Consts.TOKEN_FILE, Context.MODE_PRIVATE);
                        String token = sharedPreferences.getString(Consts.TOKEN_KEY, "");
                        Log.d("getHeadersGET: ", token);
                        params.put(Consts.CONTENT_TYPE, Consts.CONTENT_TYPE_APPLICATION_JSON_UTF8);
                        params.put("Authorization", "Token " + token);
                        return params;
                    }
                };
                queue.add(jsonObj);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

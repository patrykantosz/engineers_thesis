package com.example.engieersthesis.requests;

import android.os.AsyncTask;
import android.util.JsonReader;

import com.example.engieersthesis.utility.Consts;
import com.example.engieersthesis.utility.PostRequestType;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class PostRequest extends AsyncTask<Void, Void, String> {

    private String jsonRequest;
    private String token;
    private PostRequestType requestType;

    public void setRequestType(PostRequestType requestType) {
        this.requestType = requestType;
    }

    public void setJsonRequest(String jsonRequest) {
        this.jsonRequest = jsonRequest;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            Connections connection = new Connections();
            HttpsURLConnection connectionToEndpoint;
            if (requestType == PostRequestType.LOGIN)
                connectionToEndpoint = connection.makePostRequest(Consts.API_LOGIN_ENDPOINT, jsonRequest);
            else
                connectionToEndpoint = connection.makePostRequest(Consts.API_REGISTER_ENDPOINT, jsonRequest);

            InputStreamReader inputStreamReader = getInputStreamReader(connectionToEndpoint);
            JsonReader jsonResponseReader = new JsonReader(inputStreamReader);
            jsonResponseReader.beginObject();

            token = getValueFromJson(jsonResponseReader, Consts.TOKEN_KEY);

            jsonResponseReader.close();
            connectionToEndpoint.disconnect();
            return token;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        token = result;
    }

    private InputStreamReader getInputStreamReader(HttpsURLConnection connectionToEndpoint) {
        try {
            InputStream responseBody = connectionToEndpoint.getInputStream();
            InputStreamReader responseBodyReader = new InputStreamReader(responseBody, StandardCharsets.UTF_8);

            return responseBodyReader;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getValueFromJson(JsonReader jsonReader, String key) {
        String keyInJson;

        try {
            while (jsonReader.hasNext()) {
                keyInJson = jsonReader.nextName();
                if (keyInJson.equals(key)) {
                    keyInJson = jsonReader.nextString();
                    return keyInJson;
                } else {
                    jsonReader.skipValue();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}

package com.example.engieersthesis.requests;

import com.example.engieersthesis.utility.Consts;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Connections {
    private URL endpointURL;
    private HttpsURLConnection connectionToEndpoint;
    private OutputStreamWriter outputStreamWriter;

    public HttpsURLConnection makePostRequest(String endpointURL, String jsonRequest) {
        setEndpointURL(endpointURL);
        connectToEndpoint();
        setDoInput(true);
        setDoOutput(true);
        setRequestMethod(Consts.POST_METHOD);
        setRequestProperty(Consts.CONTENT_TYPE, Consts.CONTENT_TYPE_APPLICATION_JSON_UTF8);
        sendJsonRequest(jsonRequest);
        return connectionToEndpoint;
    }

    private void setEndpointURL(String endpointURL) {
        try {
            this.endpointURL = new URL(endpointURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void connectToEndpoint(){
        if(endpointURL != null) {
            try {
                this.connectionToEndpoint = (HttpsURLConnection) endpointURL.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setDoOutput(boolean value){
        if(connectionToEndpoint != null)
            connectionToEndpoint.setDoOutput(value);
    }

    private void setDoInput(boolean value) {
        if(connectionToEndpoint != null)
            connectionToEndpoint.setDoInput(value);
    }

    private void setRequestMethod(String requestMethod) {
        if(connectionToEndpoint != null) {
            try {
                connectionToEndpoint.setRequestMethod(requestMethod);
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
        }
    }

    private void setRequestProperty(String key, String value) {
        if(connectionToEndpoint != null)
            connectionToEndpoint.setRequestProperty(key, value);
    }

    private void sendJsonRequest(String jsonRequest) {
        if(connectionToEndpoint != null) {
            try {
                outputStreamWriter = new OutputStreamWriter(connectionToEndpoint.getOutputStream());
                outputStreamWriter.write(jsonRequest);
                outputStreamWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}

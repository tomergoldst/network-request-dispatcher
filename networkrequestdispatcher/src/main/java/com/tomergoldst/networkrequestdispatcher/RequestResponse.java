package com.tomergoldst.networkrequestdispatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by Tomer on 16/04/2017.
 */
public class RequestResponse {
    private int mResponseCode;
    private String mResponseMessage;
    private String mResponseBody;

    RequestResponse(){
        mResponseCode = 0;
        mResponseMessage = null;
        mResponseBody = null;
    }

    RequestResponse(int responseCode, String responseMessage, String responseBody){
        this.mResponseCode = responseCode;
        this.mResponseMessage = responseMessage;
        this.mResponseBody = responseBody;
    }

    public int getResponseCode(){
        return mResponseCode;
    }

    public String getResponseBody(){
        return mResponseBody;
    }

    public String getResponseMessage() {
        return mResponseMessage;
    }

    public boolean isSuccessful(){
        return mResponseCode == HttpURLConnection.HTTP_OK;
    }

    public boolean hasResponse(){
        return mResponseCode > 0;
    }

    @Override
    public String toString() {
        return "RequestResponse{" +
                "responseCode=" + mResponseCode +
                ", responseMessage='" + mResponseMessage + '\'' +
                ", responseBody='" + mResponseBody + '\'' +
                '}';
    }

    public JSONObject toJsonObject() throws JSONException{
        JSONObject jsonObject;
        jsonObject = new JSONObject(mResponseBody);
        return jsonObject;
    }

    public JSONArray toJsonArray() throws JSONException{
        JSONArray jsonArray;
        jsonArray = new JSONArray(mResponseBody);
        return jsonArray;
    }

}

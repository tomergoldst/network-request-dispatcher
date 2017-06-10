package com.tomergoldst.networkrequestdispatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

/**
 * Created by Tomer on 16/04/2017.
 */
public class RequestResponse {

    private static final String CHARSET_NAME = "UTF-8";

    private final int mResponseCode;
    private final String mResponseMessage;
    private final byte[] mResponseByteArray;
    private final Request mRequest;

    RequestResponse(){
        mResponseCode = 0;
        mResponseMessage = null;
        mResponseByteArray = null;
        mRequest = null;
    }

    RequestResponse(final int responseCode,
                    final String responseMessage,
                    final byte[] responseStream,
                    final Request request){
        this.mResponseCode = responseCode;
        this.mResponseMessage = responseMessage;
        this.mResponseByteArray = responseStream;
        this.mRequest = request;
    }

    public int getResponseCode(){
        return mResponseCode;
    }

    public byte[] getResponseByteArray() {
        return mResponseByteArray;
    }

    public String getResponseBody(){
        if (mResponseByteArray != null) {
            try {
                return readIt(mResponseByteArray);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return null;
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

    public Request getRequest(){
        return mRequest;
    }

    @Override
    public String toString() {
        return "RequestResponse{" +
                "responseCode=" + mResponseCode +
                ", responseMessage='" + mResponseMessage + '\'' +
                ", responseBody='" + getResponseBody() + '\'' +
                '}';
    }

    public JSONObject toJsonObject() throws JSONException{
        try {
            String responseBody = readIt(mResponseByteArray);
            JSONObject jsonObject;
            jsonObject = new JSONObject(responseBody);
            return jsonObject;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public JSONArray toJsonArray() throws JSONException{
        try {
            String responseBody = readIt(mResponseByteArray);
            JSONArray jsonArray;
            jsonArray = new JSONArray(responseBody);
            return jsonArray;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Reads an InputStream and converts it to a String.
    private static String readIt(byte[] bytes) throws UnsupportedEncodingException {
        return new String(bytes, CHARSET_NAME);
    }

}

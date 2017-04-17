package com.tomergoldst.networkrequestdispatcher;

import android.support.v4.util.Pair;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomer on 16/04/2017.
 */

public class Request {

    private static final String TAG = Request.class.getSimpleName();

    private String mUrl;
    private String mMethod;
    private List<Pair<String, String>> mHeaders;
    private List<Pair<String, String>> mParams;
    private JSONObject mData;
    private RequestListener mListener;

    public Request(Builder builder){
        mUrl = builder.url;
        mMethod = builder.method;
        mHeaders = builder.headers;
        mParams = builder.params;
        mData = builder.data;
        mListener = builder.listener;
    }

    @Override
    public String toString() {
        return "Request{" +
                "mUrl='" + mUrl + '\'' +
                ", mMethod='" + mMethod + '\'' +
                ", mHeaders=" + mHeaders +
                ", mParams=" + mParams +
                ", mData=" + mData +
                ", mListener=" + mListener +
                '}';
    }

    public String getUrl() {
        return mUrl;
    }

    public String getMethod() {
        return mMethod;
    }

    public List<Pair<String, String>> getHeaders() {
        return mHeaders;
    }

    public List<Pair<String, String>> getParams() {
        return mParams;
    }

    public JSONObject getData() {
        return mData;
    }

    public RequestListener getListener() {
        return mListener;
    }

    String getParamsAsString(){
        StringBuilder sb = new StringBuilder();

        for (Pair<String, String> pair : mParams) {
            sb.append(String.format("%s=%s", pair.first, pair.second));
            sb.append("&");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    public static class Builder {
        private String url;
        private String method;
        private List<Pair<String, String>> headers;
        private List<Pair<String, String>> params;
        private JSONObject data;
        private RequestListener listener;

        public Builder(){
            headers = new ArrayList<>();
            params = new ArrayList<>();
        }

        public Builder url(String url){
            this.url = url;
            return this;
        }

        public Builder method(String method){
            this.method = method;
            return this;
        }

        public Builder addHeader(String key, String value){
            headers.add(Pair.create(key, value));
            return this;
        }

        public Builder addParameter(String key, String value){
            params.add(Pair.create(key, value));
            return this;
        }

        public Builder data(JSONObject data){
            this.data = data;
            return this;
        }

        public Builder listener(RequestListener listener){
            this.listener = listener;
            return this;
        }

        public Request build(){
            if (url == null){
                throw new RuntimeException("Request must include a url");
            }

            if (method == null){
                throw new RuntimeException("Request must include a method");
            }

            return new Request(this);
        }
    }

}

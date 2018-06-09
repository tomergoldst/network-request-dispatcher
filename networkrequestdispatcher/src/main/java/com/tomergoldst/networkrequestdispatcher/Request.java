/*
Copyright 2016 Tomer Goldstein

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.tomergoldst.networkrequestdispatcher;

import android.util.Pair;

import org.json.JSONArray;
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
    private String mData;
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

    public String getData() {
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
        private String data;
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

        public Builder addHeaders(List<Pair<String, String>> headers){
            this.headers.addAll(headers);
            return this;
        }

        public Builder addParameter(String key, String value){
            params.add(Pair.create(key, value));
            return this;
        }

        public Builder addParameters(List<Pair<String, String>> parameters){
            params.addAll(parameters);
            return this;
        }

        public Builder data(JSONObject jsonObject){
            this.data = jsonObject.toString();
            return this;
        }

        public Builder data(JSONArray jsonArray){
            this.data = jsonArray.toString();
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

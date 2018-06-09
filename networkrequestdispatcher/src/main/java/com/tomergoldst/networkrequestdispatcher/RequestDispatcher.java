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

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * RequestDispatcher handle dispatching a single request item according to a retry policy
 *
 * Created by Tomer on 16/04/2017.
 */
public class RequestDispatcher {

    private static final String TAG = RequestDispatcher.class.getSimpleName();

    public static RequestResponse dispatch(Context context, Request request) {
        return dispatch(context, request, new RequestRetryPolicy(), true);
    }

    public static RequestResponse dispatch(Context context, Request request, boolean showLogs) {
        return dispatch(context, request, new RequestRetryPolicy(), showLogs);
    }

    public static RequestResponse dispatch(Context context,
                                           Request request,
                                           RetryPolicy retryPolicy,
                                           boolean showLogs){
        RequestResponse requestResponse = new RequestResponse();

        while (retryPolicy.hasAnotherAttempt() && !requestResponse.hasResponse()){
            try {
                if (showLogs){
                    Log.i(TAG, request.toString() + " " + retryPolicy.toString());
                }

                requestResponse = connect(request, retryPolicy);

                if (showLogs) {
                    Log.i(TAG, requestResponse.toString() + " for " + request.toString());
                }

                retryPolicy.retry();

            } catch (IOException e) {
                Log.w(TAG, "Request failed, " + requestResponse.toString());
                retryPolicy.retry();
            }
        }

        if (request.getListener() != null){
            if (requestResponse.hasResponse()){
                request.getListener().onResponse(context, requestResponse);
            } else {
                request.getListener().onErrorResponse(context, requestResponse);
            }
        }

        return requestResponse;
    }


    @NonNull
    private static RequestResponse connect(Request request,
                                    RetryPolicy retryPolicy) throws IOException {
        InputStream is = null;
        HttpURLConnection conn = null;

        try {
            // Create connection
            URL url = new URL(request.getUrl());
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(retryPolicy.getReadTimeout());
            conn.setConnectTimeout(retryPolicy.getTimeout());
            conn.setRequestMethod(request.getMethod());

            conn.setDoInput(true);
            conn.setUseCaches(false);

            // add request headers
            if (request.getHeaders() != null && !request.getHeaders().isEmpty()){
                List<Pair<String, String>> headers = request.getHeaders();
                for (Pair<String, String> header : headers){
                    conn.setRequestProperty(header.first, header.second);
                }
            }

            String parameters = null;
            if (!TextUtils.isEmpty(request.getData())){
                parameters = request.getData();
            } else if (request.getParams() != null && !request.getParams().isEmpty()){
                parameters = request.getParamsAsString();
            }

            // add request params
            if (!TextUtils.isEmpty(parameters)){
                conn.setRequestProperty("Content-Length", "" +
                        Integer.toString(parameters.getBytes().length));
                conn.setDoOutput(true);
                conn.setChunkedStreamingMode(0);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
                bw.write(parameters);
                bw.flush();
                bw.close();
            } else {
                conn.setDoOutput(false);
            }

            // Starts the query
            conn.connect();

            // Get Response
            int responseCode = conn.getResponseCode();

            if (responseCode >= 400) {
                is = conn.getErrorStream();
            } else {
                is = conn.getInputStream();
            }

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            byte[] byteArray = buffer.toByteArray();

            String responseMessage = conn.getResponseMessage();

            return new RequestResponse(responseCode, responseMessage, byteArray, request);

        } finally {
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
            if (is != null) {
                is.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }


}

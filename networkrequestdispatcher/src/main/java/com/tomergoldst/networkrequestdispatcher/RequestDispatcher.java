package com.tomergoldst.networkrequestdispatcher;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    private static final String PARAMS_ENCODER_DECODER = "UTF-8";

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
                    Log.i(TAG, request.toString());
                    Log.i(TAG, retryPolicy.toString());
                }

                requestResponse = connect(request, retryPolicy);

                if (showLogs) {
                    Log.i(TAG, requestResponse.toString());
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

        try {
            // Create connection
            URL url = new URL(request.getUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
            if (request.getData() != null){
                parameters = request.getData().toString();
            } else if (request.getParams() != null && !request.getParams().isEmpty()){
                parameters = request.getParamsAsString();
            }

            // add request params
            if (!TextUtils.isEmpty(parameters)){
                conn.setRequestProperty("Content-Length", "" +
                        Integer.toString(parameters.getBytes().length));
                conn.setDoOutput(true);
                conn.setChunkedStreamingMode(0);

                // Send request
//                DataOutputStream wr = new DataOutputStream(
//                        conn.getOutputStream());
//                wr.writeBytes(parameters);
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

            // Convert the InputStream into a string
            String contentAsString = readIt(is);

            String responseMessage = conn.getResponseMessage();

            return new RequestResponse(responseCode, responseMessage, contentAsString);

        } finally {
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    private static String readIt(InputStream stream) throws IOException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream,
                PARAMS_ENCODER_DECODER));
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }

        return responseStrBuilder.toString();
    }

}

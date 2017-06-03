package com.tomergoldst.networkrequestdispatcherdemo;

import android.content.Context;
import android.util.Log;

import com.tomergoldst.networkrequestdispatcher.RequestResponse;

/**
 * Created by Tomer on 10/05/2017.
 */

public class DemoRequestListener extends BaseRequestListener{

    private static final String TAG = DemoRequestListener.class.getSimpleName();

    @Override
    public boolean onResponse(Context context, RequestResponse response) {
        if (super.onResponse(context, response)) {
            return true;
        }

        Log.d(TAG, "onResponse");
        return true;
    }

    @Override
    public boolean onErrorResponse(Context context, RequestResponse response) {
        return super.onErrorResponse(context, response);
    }
}

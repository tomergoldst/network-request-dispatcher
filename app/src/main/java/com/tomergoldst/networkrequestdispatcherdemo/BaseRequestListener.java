package com.tomergoldst.networkrequestdispatcherdemo;

import android.content.Context;
import android.util.Log;

import com.tomergoldst.networkrequestdispatcher.RequestListener;
import com.tomergoldst.networkrequestdispatcher.RequestResponse;

/**
 * Created by Tomer on 10/05/2017.
 */

public class BaseRequestListener implements RequestListener{

    private static final String TAG = BaseRequestListener.class.getSimpleName();

    @Override
    public boolean onResponse(Context context, RequestResponse response) {
        Log.d(TAG, "onResponse");
        return true;
    }

    @Override
    public boolean onErrorResponse(Context context, RequestResponse response) {
        Log.d(TAG, "onErrorResponse");
        return false;
    }
}

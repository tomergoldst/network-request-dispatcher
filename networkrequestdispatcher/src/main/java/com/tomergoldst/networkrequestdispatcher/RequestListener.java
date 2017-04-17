package com.tomergoldst.networkrequestdispatcher;

import android.content.Context;

/**
 * Created by Tomer on 16/04/2017.
 */

public interface RequestListener {
    void onResponse(Context context, RequestResponse response);
    void onErrorResponse(Context context, RequestResponse response);
}

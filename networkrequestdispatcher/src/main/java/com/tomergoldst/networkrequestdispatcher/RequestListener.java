package com.tomergoldst.networkrequestdispatcher;

import android.content.Context;

/**
 * Created by Tomer on 16/04/2017.
 */

public interface RequestListener {
    boolean onResponse(Context context, RequestResponse response);
    boolean onErrorResponse(Context context, RequestResponse response);
}

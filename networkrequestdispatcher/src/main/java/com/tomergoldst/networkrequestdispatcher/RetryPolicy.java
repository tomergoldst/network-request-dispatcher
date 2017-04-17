package com.tomergoldst.networkrequestdispatcher;

/**
 * Created by Tomer on 03/02/2017.
 */

public interface RetryPolicy {

    int getTimeout();

    int getReadTimeout();

    boolean hasAnotherAttempt();

    void retry();

}

package com.tomergoldst.networkrequestdispatcher;

/**
 * Created by Tomer on 16/04/2017.
 */
class RequestRetryPolicy implements RetryPolicy {

    private static final int DEFAULT_TIMEOUT_MS = 3000;
    private static final int DEFAULT_READ_TIMEOUT_MS = 10000;
    private static final int DEFAULT_MAX_RETRY_ATTEMPTS = 3;
    private static final int DEFAULT_BACKOFF_FACTOR = 1;

    private int mTimeout;
    private int mMaxRetryAttempts;
    private int mCurrentRetryAttempt;
    private int mBackOffFactor;

    RequestRetryPolicy(){
        mTimeout = DEFAULT_TIMEOUT_MS;
        mMaxRetryAttempts = DEFAULT_MAX_RETRY_ATTEMPTS;
        mBackOffFactor = DEFAULT_BACKOFF_FACTOR;
        mCurrentRetryAttempt = 1;
    }

    @Override
    public int getTimeout() {
        return mTimeout;
    }

    @Override
    public int getReadTimeout() {
        return DEFAULT_READ_TIMEOUT_MS;
    }

    @Override
    public boolean hasAnotherAttempt(){
        return mCurrentRetryAttempt <= mMaxRetryAttempts;
    }

    @Override
    public void retry(){
        mTimeout += (mTimeout * mBackOffFactor);
        mCurrentRetryAttempt++;
    }

    @Override
    public String toString() {
        return "Retry policy: current retry count = " + mCurrentRetryAttempt + ", " +
                " max retry count = " + mMaxRetryAttempts + ", " +
                " timeout = " + mTimeout + ", " +
                " backoff factor = " + mBackOffFactor;
    }
}

package com.github.lassana.releases;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

/**
 * @author lassana
 * @since 1/13/14
 */
public class VolleyAppController extends Application {

    private static final String TAG = "VolleyApplicationController";

    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

    private static VolleyAppController sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
    }

    public static synchronized VolleyAppController getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("Application instance is null. " +
                    "Is VolleyAppController is application class?");
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        addToRequestQueue(request, null);
    }

    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        VolleyLog.d("Adding to request queue: %s", request.getUrl());

        getRequestQueue().add(request);
    }

    public void cancelPendingRequest(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


}

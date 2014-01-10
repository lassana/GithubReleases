package com.github.lassana.releases.github;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * @author lassana
 * @since 1/10/14
 */
public class Repository {

    private final RequestQueue mRequestQueue;
    private final String mOwner;
    private final String mRepository;

    public Repository(RequestQueue requestQueue, String owner, String repository) {
        mRequestQueue = requestQueue;
        mOwner = owner;
        mRepository = repository;
    }

    public void getTags(Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(buildTagsUrl(mOwner, mRepository),
                listener, errorListener);
        mRequestQueue.add(request);
    }

    private String buildTagsUrl(String owner, String repository) {
        return "https://api.github.com/"
                + "repos/"
                + owner + "/"
                + "repository" + "/"
                + "tags/";
    }

}

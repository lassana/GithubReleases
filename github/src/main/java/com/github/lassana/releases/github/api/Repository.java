package com.github.lassana.releases.github.api;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.github.lassana.releases.github.VolleyAppController;

/**
 * @author lassana
 * @since 1/10/14
 */
public class Repository {

    private final String mOwner;
    private final String mRepository;

    public Repository(String owner, String repository) {
        mOwner = owner;
        mRepository = repository;
    }

    public void getTags(Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(buildTagsUrl(mOwner, mRepository),
                listener, errorListener);
        VolleyAppController.getInstance().addToRequestQueue(request);
    }

    private String buildTagsUrl(String owner, String repository) {
        return "https://api.github.com/"
                + "repos/"
                + owner + "/"
                + "repository" + "/"
                + "tags";
    }

}

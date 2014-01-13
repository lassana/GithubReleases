package com.github.lassana.releases.github.api;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.github.lassana.releases.github.VolleyAppController;
import com.github.lassana.releases.github.model.Tag;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

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

    public static List<Tag> getTags(String response) {
        Type listType = new TypeToken<List<Tag>>() {}.getType();
        return new Gson().fromJson(response, listType);
    }

    private String buildTagsUrl(String owner, String repository) {
        return "https://api.github.com/"
                + "repos/"
                + owner + "/"
                + repository + "/"
                + "tags";
    }

}

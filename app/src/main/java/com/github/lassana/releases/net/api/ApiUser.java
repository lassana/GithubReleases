package com.github.lassana.releases.net.api;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.github.lassana.releases.VolleyAppController;
import com.github.lassana.releases.net.model.Repository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author lassana
 * @since 1/14/14
 */
public class ApiUser {

    private final String mUsername;

    public ApiUser(String username) {
        mUsername = username;
    }

    public void getRepositories(Response.Listener<String> listener,
                                Response.ErrorListener errorListener,
                                String tag) {
        StringRequest request = new StringRequest(buildRepositoriesUrl(), listener, errorListener);
        VolleyAppController.getInstance().addToRequestQueue(request, tag);
    }

    public static List<Repository> getRepositories(String response) {
        Type listType = new TypeToken<List<Repository>>() {
        }.getType();
        return new Gson().fromJson(response, listType);
    }

    private String buildRepositoriesUrl() {
        return "https://api.github.com/"
                + "users/"
                + mUsername + "/"
                + "repos";
    }


}

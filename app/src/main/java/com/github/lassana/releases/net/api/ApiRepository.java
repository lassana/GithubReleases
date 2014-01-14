package com.github.lassana.releases.net.api;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.github.lassana.releases.VolleyAppController;
import com.github.lassana.releases.net.model.Tag;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author lassana
 * @since 1/10/14
 */
public class ApiRepository {

    private final String mOwner;
    private final String mRepository;

    public ApiRepository(String owner, String repository) {
        mOwner = owner;
        mRepository = repository;
    }

    /**
     * Creates list tags request.<p>
     * Example
     * <pre>{@code
     * Repository repository = new Repository("lassana", "listview-anim-sorting");
     * Response.Listener<String> listener = new Response.Listener<String>() {
     *      @Override
     *      public void onResponse(String response) {
     *          List<Tag> tags = Repository.getTags(response);
     *          Log.d(TAG, "onResponse: " + tags);
     *      }
     * };
     * repository.getTags(listener, errorListener, tag);
     * }</pre>
     */
    public void getTags(Response.Listener<String> listener,
                        Response.ErrorListener errorListener,
                        String tag) {
        StringRequest request = new StringRequest(buildTagsUrl(), listener, errorListener);
        VolleyAppController.getInstance().addToRequestQueue(request, tag);
    }

    public static List<Tag> getTags(String response) {
        Type listType = new TypeToken<List<Tag>>() {
        }.getType();
        return new Gson().fromJson(response, listType);
    }

    public String buildWebUrl() {
        return "https://github.com/"
                + "repos/"
                + mOwner + "/"
                + mRepository + "/"
                + "tags";
    }

    private String buildTagsUrl() {
        return "https://api.github.com/"
                + "repos/"
                + mOwner + "/"
                + mRepository + "/"
                + "tags";
    }

}

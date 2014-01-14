package com.github.lassana.releases.net.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author lassana
 * @since 1/14/14
 */
public class Repository {

    @SerializedName("name")
    private final String mName;
    @SerializedName("description")
    private final String mDescription;

    public Repository(String name, String description) {
        mName = name;
        mDescription = description;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    @Override
    public String toString() {
        return mName + "\n" + mDescription;
    }
}

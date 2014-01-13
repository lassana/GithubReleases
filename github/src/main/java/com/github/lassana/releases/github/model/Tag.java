package com.github.lassana.releases.github.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author lassana
 * @since 1/10/14
 */
public class Tag {

    @SerializedName("name")
    private final String mName;
    @SerializedName("zipball_url")
    private final String mZipballUrl;
    @SerializedName("tarball_url")
    private final String mTarballUrl;
    @SerializedName("commit")
    private final Commit mCommit;

    public Tag(String name, String zipballUrl, String tarballUrl, Commit commit) {
        mName = name;
        mZipballUrl = zipballUrl;
        mTarballUrl = tarballUrl;
        mCommit = commit;
    }

    public String getName() {
        return mName;
    }

    public String getZipballUrl() {
        return mZipballUrl;
    }

    public String getTarballUrl() {
        return mTarballUrl;
    }

    public Commit getCommit() {
        return mCommit;
    }
}

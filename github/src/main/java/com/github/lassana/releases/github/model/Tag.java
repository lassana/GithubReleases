package com.github.lassana.releases.github.model;

/**
 * @author lassana
 * @since 1/10/14
 */
public class Tag {
    private final String mName;
    private final String mZipballUrl;
    private final String mTarballUrl;

    public Tag(String name, String zipballUrl, String tarballUrl) {
        mName = name;
        mZipballUrl = zipballUrl;
        mTarballUrl = tarballUrl;
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
}

package com.github.lassana.releases.activity;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.github.lassana.releases.R;
import com.github.lassana.releases.fragment.RepositoriesFragment;
import com.github.lassana.releases.fragment.TagOverviewFragment;
import com.github.lassana.releases.fragment.TagsFragment;
import com.github.lassana.releases.storage.model.GithubContract;

public class MainActivity extends ActionBarActivity
        implements RepositoriesFragment.RepositoriesCallback, TagsFragment.TagsCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new RepositoriesFragment())
                    .commit();
        }
    }

    private void insert() {
        ContentValues values = new ContentValues();
        values.put(GithubContract.Repositories.OWNER, "lassana");
        values.put(GithubContract.Repositories.REPOSITORY_NAME, "listview-anim-sorting");
        Uri uri = getContentResolver().insert(GithubContract.Repositories.CONTENT_URI, values);
/*
        values = new ContentValues();
        values.put(GithubContract.Tags.TAG_NAME, "v0.1");
        values.put(GithubContract.Tags.REPOSITORY_ID, Long.parseLong(uri.getLastPathSegment()));
        values.put(GithubContract.Tags.TARBALL_URL, "http://tarball");
        values.put(GithubContract.Tags.ZIPBALL_URL, "http://zipball");
        getContentResolver().insert(GithubContract.Tags.CONTENT_URI, values);*/
    }

    @Override
    public void requestTags(long repositoryId) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, TagsFragment.getInstance(repositoryId))
                .commit();
    }

    @Override
    public void requestTagOverview(long tagId) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, TagOverviewFragment.getInstance(tagId))
                .commit();
    }
}

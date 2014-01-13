package com.github.lassana.releases.activity;

import android.content.ContentValues;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.github.lassana.releases.R;
import com.github.lassana.releases.fragment.RepositoriesFragment;
import com.github.lassana.releases.fragment.TagsFragment;
import com.github.lassana.releases.storage.model.GithubContract;

public class MainActivity extends ActionBarActivity
        implements RepositoriesFragment.RepositoriesCallback {

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

    private void insertNewTag() {
        ContentValues values = new ContentValues();
        values.put(GithubContract.Repositories.USER_NAME, "owner");
        values.put(GithubContract.Repositories.REPOSITORY_NAME, "repository");
        getContentResolver().insert(GithubContract.Repositories.CONTENT_URI, values);

        values = new ContentValues();
        values.put(GithubContract.Tags.TAG_NAME, "v0.1");
        values.put(GithubContract.Tags.REPOSITORY_ID, 1L);
        values.put(GithubContract.Tags.TARBALL_URL, "http://tarball");
        values.put(GithubContract.Tags.ZIPBALL_URL, "http://zipball");
        getContentResolver().insert(GithubContract.Tags.CONTENT_URI, values);
    }

    @Override
    public void requestTags(long repositoryId) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, TagsFragment.getInstance(repositoryId))
                .commit();
    }
}

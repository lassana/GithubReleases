package com.github.lassana.releases.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.github.lassana.releases.R;
import com.github.lassana.releases.fragment.RepositoriesFragment;
import com.github.lassana.releases.fragment.TagsFragment;

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

    @Override
    public void requestTags(long repositoryId) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, TagsFragment.getInstance(repositoryId))
                .commit();
    }
}

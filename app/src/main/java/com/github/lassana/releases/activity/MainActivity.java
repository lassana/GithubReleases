package com.github.lassana.releases.activity;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.github.lassana.releases.R;
import com.github.lassana.releases.fragment.RepositoriesFragment;
import com.github.lassana.releases.fragment.TagOverviewFragment;
import com.github.lassana.releases.fragment.TagsFragment;

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

    @Override
    public void requestTags(long repositoryId) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, TagsFragment.getInstance(repositoryId))
                .commit();
    }

    @Override
    public void requestTagOverview(long tagId) {
        DialogFragment fragment = TagOverviewFragment.getInstance(tagId);
        fragment.show(getSupportFragmentManager(), Long.toString(tagId));
    }
}

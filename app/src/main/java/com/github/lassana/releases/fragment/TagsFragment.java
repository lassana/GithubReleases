package com.github.lassana.releases.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.lassana.releases.R;

/**
 * @author lassana
 * @since 1/13/14
 */
public class TagsFragment extends ListFragment {

    private static final String EXTRA_REPOSITORY_ID = "extra_repository_id";

    private TagsFragment() {
    }

    public static TagsFragment getInstance(long repositoryId) {
        TagsFragment fragment = new TagsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_REPOSITORY_ID, repositoryId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tags, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        long repositoryId = getArguments().getLong(EXTRA_REPOSITORY_ID);
        Toast.makeText(getActivity(), Long.toString(repositoryId), Toast.LENGTH_SHORT).show();
    }
}

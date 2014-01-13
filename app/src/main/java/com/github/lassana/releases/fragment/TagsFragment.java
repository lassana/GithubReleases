package com.github.lassana.releases.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.github.lassana.releases.R;
import com.github.lassana.releases.storage.model.GithubContract;
import com.github.lassana.releases.view.DraggablePanelLayout;

/**
 * @author lassana
 * @since 1/13/14
 */
public class TagsFragment extends ListFragment {

    private static final String EXTRA_REPOSITORY_ID = "extra_repository_id";

    private static final int LOADER_ID = 2;

    private static final String[] PROJECTION = {
            GithubContract.Tags._ID,
            GithubContract.Tags.REPOSITORY_ID,
            GithubContract.Tags.TAG_NAME};

    private CursorAdapter mAdapter;

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new CursorLoader(
                    getActivity(),
                    GithubContract.Tags.CONTENT_URI,
                    PROJECTION,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
            mAdapter.swapCursor(cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {
            mAdapter.swapCursor(null);
        }
    };

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

        mAdapter = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_list_item_1,
                null,
                new String[]{GithubContract.Tags.TAG_NAME},
                new int[]{android.R.id.text1},
                0);
        DraggablePanelLayout.enableInternalScrolling(getListView());
        getListView().setAdapter(mAdapter);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);

    }
}

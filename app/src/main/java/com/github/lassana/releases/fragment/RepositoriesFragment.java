package com.github.lassana.releases.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.github.lassana.releases.R;
import com.github.lassana.releases.storage.model.GithubContract;
import com.github.lassana.releases.view.DraggablePanelLayout;

/**
 * A placeholder fragment containing a simple view.
 */
public class RepositoriesFragment extends ListFragment {

    private static final String TAG = "MainFragment";

    public static interface RepositoriesCallback {
        void requestTags(long repositoryId);
    }

    private static final int LOADER_ID = 1;

    private static final String[] PROJECTION = {
            GithubContract.Repositories._ID,
            GithubContract.Repositories.USER_NAME,
            GithubContract.Repositories.REPOSITORY_NAME};

    private RepositoriesCallback mRepositoriesCallback;

    private CursorAdapter mAdapter;

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new CursorLoader(
                    getActivity(),
                    GithubContract.Repositories.CONTENT_URI,
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

    public RepositoriesFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof RepositoriesCallback)) {
            throw new IllegalStateException("Activity should be instance of RepositoriesCallback$RepositoriesCallback");
        }
        mRepositoriesCallback = (RepositoriesCallback) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repositories, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_list_item_2,
                null,
                new String[]{GithubContract.Repositories.USER_NAME, GithubContract.Repositories.REPOSITORY_NAME},
                new int[]{android.R.id.text1, android.R.id.text2},
                0);
        DraggablePanelLayout.enableInternalScrolling(getListView());
        getListView().setAdapter(mAdapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mRepositoriesCallback.requestTags(id);
            }
        });
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);
    }


}

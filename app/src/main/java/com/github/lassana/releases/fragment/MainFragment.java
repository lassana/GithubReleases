package com.github.lassana.releases.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
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

import com.github.lassana.releases.R;
import com.github.lassana.releases.storage.model.RepositoriesContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends ListFragment {

    private static final String TAG = "MainFragment";

    private static final int LOADER_ID = 1;
    private static final String[] PROJECTION = {
            RepositoriesContract.Repositories._ID,
            RepositoriesContract.Repositories.USER_NAME,
            RepositoriesContract.Repositories.REPOSITORY_NAME};

    private CursorAdapter mAdapter;

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new CursorLoader(
                    getActivity(),
                    RepositoriesContract.Repositories.CONTENT_URI,
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

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(android.R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues repository = new ContentValues();
                repository.put(RepositoriesContract.Repositories.USER_NAME, "lassana");
                repository.put(RepositoriesContract.Repositories.REPOSITORY_NAME, "listview-anim-sorting");
                Uri uri = getActivity().getApplicationContext().getContentResolver().insert(RepositoriesContract.Repositories.CONTENT_URI, repository);
                Log.d(TAG, uri.toString());
            }
        });
        mAdapter = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_list_item_2,
                null,
                new String[]{RepositoriesContract.Repositories.USER_NAME, RepositoriesContract.Repositories.REPOSITORY_NAME},
                new int[]{android.R.id.text1, android.R.id.text2},
                0);
        getListView().setAdapter(mAdapter);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);
    }


}

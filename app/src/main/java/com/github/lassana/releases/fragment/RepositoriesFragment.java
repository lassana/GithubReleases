package com.github.lassana.releases.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.lassana.releases.R;
import com.github.lassana.releases.adapter.RepositoriesAdapter;
import com.github.lassana.releases.storage.model.GithubContract;
import com.github.lassana.releases.view.DraggablePanelHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class RepositoriesFragment extends ListFragment
        implements RepositoriesAdapter.RepositoriesAdapterCallback {

    private static final String TAG = "MainFragment";

    public static interface RepositoriesCallback {
        void onRepositoryTagsRequested(long repositoryId);
        void onNewRepositoryRequested();
    }

    private static final int LOADER_ID = 1;

    private static final String[] PROJECTION = {
            GithubContract.Repositories._ID,
            GithubContract.Repositories.OWNER,
            GithubContract.Repositories.REPOSITORY_NAME};

    private RepositoriesCallback mRepositoriesCallback;

    private RepositoriesAdapter mAdapter;

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

        view.findViewById(android.R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestNewRepository();
            }
        });

        mAdapter = new RepositoriesAdapter(getActivity(), this);
        DraggablePanelHelper.enableInternalScrolling(getListView());
        getListView().setAdapter(mAdapter);
        getListView().setOnItemClickListener(mAdapter);
        getListView().setOnItemLongClickListener(mAdapter);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);
    }

    @Override
    public void requestOpenRepository(long id) {
        mRepositoriesCallback.onRepositoryTagsRequested(id);
    }

    @Override
    public void requestDeleteRepository(long id) {
        DeleteRepositoryFragment fragment = DeleteRepositoryFragment.getInstance(id);
        fragment.show(getFragmentManager(), "delete_repository");
    }

    private void requestNewRepository() {
        mRepositoriesCallback.onNewRepositoryRequested();
        AddRepositoryFragment fragment = new AddRepositoryFragment();
        fragment.show(getFragmentManager(), "add_repository");
    }


}

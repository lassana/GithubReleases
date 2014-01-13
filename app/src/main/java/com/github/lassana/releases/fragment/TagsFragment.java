package com.github.lassana.releases.fragment;

import android.content.ContentUris;
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
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.lassana.releases.R;
import com.github.lassana.releases.github.api.Repository;
import com.github.lassana.releases.github.model.Tag;
import com.github.lassana.releases.storage.model.GithubContract;
import com.github.lassana.releases.view.DraggablePanelLayout;

import java.util.List;

/**
 * @author lassana
 * @since 1/13/14
 */
public class TagsFragment extends ListFragment {

    private static final String EXTRA_REPOSITORY_ID = "extra_repository_id";

    private static final int LIST_LOADER_ID = 2;
    private static final int REPOSITORY_LOADER_ID = 3;

    private static final String[] TAGS_PROJECTION = {
            GithubContract.Tags._ID,
            GithubContract.Tags.REPOSITORY_ID,
            GithubContract.Tags.TAG_NAME};

    private static final String[] REPOSITORY_PROJECTION = {
            GithubContract.Repositories._ID,
            GithubContract.Repositories.USER_NAME,
            GithubContract.Repositories.REPOSITORY_NAME};

    private CursorAdapter mAdapter;
    private ViewSwitcher mViewSwitcher;

    private LoaderManager.LoaderCallbacks<Cursor> listLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            mViewSwitcher.setDisplayedChild(0);
            return new CursorLoader(
                    getActivity(),
                    GithubContract.Tags.CONTENT_URI,
                    TAGS_PROJECTION,
                    GithubContract.Tags.REPOSITORY_ID + " = ?",
                    new String[]{Long.toString(mRepositoryId)},
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
            mAdapter.swapCursor(cursor);
            mViewSwitcher.setDisplayedChild(1);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {
            mAdapter.swapCursor(null);
        }
    };

    private long mRepositoryId;

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
        mRepositoryId = getArguments().getLong(EXTRA_REPOSITORY_ID);

        mViewSwitcher = (ViewSwitcher) view.findViewById(android.R.id.primary);

        mAdapter = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_list_item_1,
                null,
                new String[]{GithubContract.Tags.TAG_NAME},
                new int[]{android.R.id.text1},
                0);
        DraggablePanelLayout.enableInternalScrolling(getListView());
        getListView().setAdapter(mAdapter);
        getActivity().getSupportLoaderManager().initLoader(LIST_LOADER_ID, null, listLoaderCallbacks);
        loadTags();
    }

    private void loadTags() {
        CursorLoader loader = new CursorLoader(
                getActivity(),
                GithubContract.Repositories.CONTENT_URI,
                REPOSITORY_PROJECTION,
                GithubContract.Repositories._ID + " = ?",
                new String[]{Long.toString(mRepositoryId)},
                null);
        Cursor cursor = loader.loadInBackground();
        cursor.moveToFirst();
        int ownerIndex = cursor.getColumnIndex(GithubContract.Repositories.USER_NAME);
        int repositoryIndex = cursor.getColumnIndex(GithubContract.Repositories.REPOSITORY_NAME);
        String owner = cursor.getString(ownerIndex);
        String repositoryStr = cursor.getString(repositoryIndex);

        Repository repository = new Repository(owner, repositoryStr);
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<Tag> list = Repository.getTags(response);
                for (Tag tag : list) {
                    getActivity().getContentResolver().delete(
                            GithubContract.Tags.CONTENT_URI,
                            GithubContract.Tags.REPOSITORY_ID + " = ?",
                            new String[]{Long.toString(mRepositoryId)});
                    // TODO: put tags into db
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), R.string.toast_bad_repository, Toast.LENGTH_LONG).show();
            }
        };
        repository.getTags(listener, errorListener);
    }
}

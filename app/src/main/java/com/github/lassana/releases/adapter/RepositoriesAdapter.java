package com.github.lassana.releases.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.github.lassana.releases.R;
import com.github.lassana.releases.storage.model.GithubContract;

/**
 * @author Nikolai Doronin
 * @since 2/7/14
 */
public class RepositoriesAdapter extends SimpleCursorAdapter
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener {

    public static interface RepositoriesAdapterCallback {
        void requestDeleteRepository(long id);
        void requestOpenRepository(long id);
    }

    private View mLastExpandedRootView;
    private RepositoriesAdapterCallback mCallback;

    public RepositoriesAdapter(Context context, RepositoriesAdapterCallback callback) {
        super(context, R.layout.list_item_repository, null,
                new String[]{GithubContract.Repositories.REPOSITORY_NAME, GithubContract.Repositories.OWNER},
                new int[]{android.R.id.text1, android.R.id.text2}, 0);
        mCallback = callback;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        Button delete = (Button) view.findViewById(android.R.id.button1);
        Button info = (Button) view.findViewById(android.R.id.button2);
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(GithubContract.Repositories._ID));
        delete.setTag(id);
        info.setTag(id);

        delete.setOnClickListener(this);
        info.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        toggleContentView(view);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        toggleContentView(view);
        return true;
    }

    private void toggleContentView(View itemRoot) {
        if ( mLastExpandedRootView != null) {
            View previous = mLastExpandedRootView;
            mLastExpandedRootView = null;
            toggleContentView(previous);
        }
        View toExpand = itemRoot.findViewById(android.R.id.content);
        View divider = itemRoot.findViewById(android.R.id.empty);
        if (toExpand.getVisibility() == View.VISIBLE) {
            toExpand.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
            mLastExpandedRootView = null;
        } else {
            toExpand.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
            mLastExpandedRootView = itemRoot;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case android.R.id.button1:
                mCallback.requestDeleteRepository((Long) v.getTag());
                break;
            case android.R.id.button2:
                mCallback.requestOpenRepository((Long) v.getTag());
                break;
            default:
                break;
        }
    }
}
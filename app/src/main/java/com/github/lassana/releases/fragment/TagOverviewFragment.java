package com.github.lassana.releases.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lassana.releases.R;
import com.github.lassana.releases.github.api.Repository;
import com.github.lassana.releases.storage.model.GithubContract;

/**
 * @author lassana
 * @since 1/13/14
 */
public class TagOverviewFragment extends DialogFragment {

    private static final String EXTRA_TAG_ID = "extra_tag_id";
    private long mTagId;

    public static TagOverviewFragment getInstance(long repositoryId) {
        TagOverviewFragment fragment = new TagOverviewFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_TAG_ID, repositoryId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tag_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle(R.string.title_tag_overview);
        mTagId = getArguments().getLong(EXTRA_TAG_ID);
        CursorLoader loader = new CursorLoader(
                getActivity(),
                GithubContract.Tags.CONTENT_URI,
                null,
                GithubContract.Tags._ID + " = ?",
                new String[]{Long.toString(mTagId)},
                null);
        Cursor cursor = loader.loadInBackground();

        cursor.moveToFirst();
        int repositoryIdIndex = cursor.getColumnIndex(GithubContract.Tags.REPOSITORY_ID);
        int nameIndex = cursor.getColumnIndex(GithubContract.Tags.TAG_NAME);
        int tarballUrlIndex = cursor.getColumnIndex(GithubContract.Tags.TARBALL_URL);
        int zipballUrlIndex = cursor.getColumnIndex(GithubContract.Tags.ZIPBALL_URL);
        int commitShaIndex = cursor.getColumnIndex(GithubContract.Tags.COMMIT_SHA);
        int commitUrlIndex = cursor.getColumnIndex(GithubContract.Tags.COMMIT_URL);

        final long repositoryId = cursor.getLong(repositoryIdIndex);
        final String name = cursor.getString(nameIndex);
        final String tarballUrl = cursor.getString(tarballUrlIndex);
        final String zipballUrl = cursor.getString(zipballUrlIndex);
        final String commitSha = cursor.getString(commitShaIndex);
        final String commitUrl = cursor.getString(commitUrlIndex);

        cursor.close();

        loader = new CursorLoader(
                getActivity(),
                GithubContract.Repositories.CONTENT_URI,
                null,
                GithubContract.Repositories._ID + " = ?",
                new String[]{Long.toString(repositoryId)},
                null);
        cursor = loader.loadInBackground();
        cursor.moveToFirst();

        cursor.moveToFirst();
        int ownerIndex = cursor.getColumnIndex(GithubContract.Repositories.OWNER);
        int repositoryNameIndex = cursor.getColumnIndex(GithubContract.Repositories.REPOSITORY_NAME);

        final String owner = cursor.getString(ownerIndex);
        final String repositoryName = cursor.getString(repositoryNameIndex);

        ((TextView) view.findViewById(android.R.id.text1)).setText(repositoryName);
        ((TextView) view.findViewById(android.R.id.text2)).setText(owner);
        ((TextView) view.findViewById(R.id.tv_tag)).setText(name);
        ((TextView) view.findViewById(R.id.tv_tarball)).setText(tarballUrl);
        ((TextView) view.findViewById(R.id.tv_zipball)).setText(zipballUrl);
        TextView textView = (TextView) view.findViewById(R.id.tv_commit);
        textView.setText(commitSha);
        textView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(commitUrl));
                startActivity(intent);
            }
        });
        view.findViewById(R.id.layout_repository).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(new Repository(owner, repositoryName).buildWebUrl()));
                startActivity(intent);
            }
        });
    }
}

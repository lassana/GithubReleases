package com.github.lassana.releases.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.github.lassana.releases.R;
import com.github.lassana.releases.storage.model.GithubContract;

/**
 * @author lassana
 * @since 1/14/14
 */
public class DeleteRepositoryFragment extends DialogFragment {

    private static final String EXTRA_REPOSITORY_ID = "extra_repository_id";
    private long mRepositoryId;

    public static DeleteRepositoryFragment getInstance(long repositoryId) {
        DeleteRepositoryFragment fragment = new DeleteRepositoryFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_REPOSITORY_ID, repositoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = savedInstanceState == null ? getArguments() : savedInstanceState;
        mRepositoryId = args.getLong(EXTRA_REPOSITORY_ID);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_warning);
        builder.setMessage(R.string.message_ask_delete);
        builder.setCancelable(true);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] selectionArgs = {Long.toString(mRepositoryId)};
                getActivity().getContentResolver().delete(
                        GithubContract.Repositories.CONTENT_URI,
                        GithubContract.Repositories._ID + " = ?",
                        selectionArgs);
                getActivity().getContentResolver().delete(
                        GithubContract.Tags.CONTENT_URI,
                        GithubContract.Tags.REPOSITORY_ID + " = ?",
                        selectionArgs);
            }
        });
        builder.setNegativeButton(android.R.string.no, null);

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(EXTRA_REPOSITORY_ID, mRepositoryId);
    }
}

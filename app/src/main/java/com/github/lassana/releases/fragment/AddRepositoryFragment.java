package com.github.lassana.releases.fragment;

import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.SimpleAdapter;

import com.android.volley.Response;
import com.github.lassana.releases.R;
import com.github.lassana.releases.VolleyAppController;
import com.github.lassana.releases.net.api.ApiUser;
import com.github.lassana.releases.net.model.Repository;
import com.github.lassana.releases.storage.model.GithubContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lassana
 * @since 1/14/14
 */
public class AddRepositoryFragment extends DialogFragment {

    private static final String TAG_LOAD_REPOSITORIES = "tag_load_repositories";

    private boolean mIsUsernameEmpty = true;
    private boolean mIsRepositoryEmpty = true;
    private EditText mUsernameEditText;
    private AutoCompleteTextView mRepositoryEditText;
    private View mAddButton;

    public AddRepositoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_repository, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Dialog dialog = getDialog();
        dialog.setTitle(R.string.title_add_repository);

        mUsernameEditText = (EditText) view.findViewById(android.R.id.text1);
        mRepositoryEditText = (AutoCompleteTextView) view.findViewById(android.R.id.text2);
        mAddButton = view.findViewById(android.R.id.button1);

        assert mUsernameEditText != null && mRepositoryEditText != null && mAddButton != null;

        invalidateAddButton();

        mUsernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mIsUsernameEmpty = TextUtils.isEmpty(s.toString());
                invalidateAddButton();
            }
        });

        mRepositoryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mIsRepositoryEmpty = TextUtils.isEmpty(s.toString());
                invalidateAddButton();
            }
        });

        mRepositoryEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String username = mUsernameEditText.getText().toString();
                    if (!TextUtils.isEmpty(username)) {
                        loadRepositories(username);
                    }
                } else {
                    VolleyAppController.getInstance().cancelPendingRequest(TAG_LOAD_REPOSITORIES);
                }
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRepository();
            }
        });
    }

    private void invalidateAddButton() {
        mAddButton.setEnabled(!(mIsUsernameEmpty || mIsRepositoryEmpty));
    }

    private void addRepository() {
        String owner = mUsernameEditText.getText().toString();
        String repository = mRepositoryEditText.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put(GithubContract.Repositories.OWNER, owner);
        cv.put(GithubContract.Repositories.REPOSITORY_NAME, repository);
        getActivity().getContentResolver().insert(GithubContract.Repositories.CONTENT_URI, cv);
        getDialog().cancel();
    }

    private void loadRepositories(String username) {
        ApiUser user = new ApiUser(username);
        user.getRepositories(new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                List<Repository> list = ApiUser.getRepositories(s);
                List<Map<String, String>> data = new ArrayList<Map<String, String>>(list.size());
                for (Repository repository : list) {
                    Map<String, String> datum = new HashMap<String, String>(2);
                    datum.put("first", repository.getName());
                    datum.put("second", repository.getDescription());
                    data.add(datum);
                }
                SimpleAdapter adapter = new SimpleAdapter(getActivity(), data,
                        android.R.layout.simple_list_item_2,
                        new String[]{"first", "second"},
                        new int[]{android.R.id.text1, android.R.id.text2});
                mRepositoryEditText.setAdapter(adapter);
            }
        }, null, TAG_LOAD_REPOSITORIES);
    }

}

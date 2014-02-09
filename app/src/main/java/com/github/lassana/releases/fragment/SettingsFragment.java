package com.github.lassana.releases.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.github.lassana.releases.R;

/**
 * @author lassana
 * @since 2/9/14
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}

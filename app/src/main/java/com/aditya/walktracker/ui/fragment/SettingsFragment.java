package com.aditya.walktracker.ui.fragment;

import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.aditya.walktracker.R;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    public SettingsFragment() {}

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.pref_walk);

        Preference preference = findPreference(getString(R.string.weight));
        preference.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        Toast error = Toast.makeText(getContext(), R.string.weight_msg, Toast.LENGTH_LONG);

        if(preference.getKey().equals(getString(R.string.weight))){
            String s = (String) o;
            try {
                float weight= Float.parseFloat(s);
                if(weight<1 || weight>=200){
                    error.show();
                    return false;
                }
            }
            catch (Exception e){
                error.show();
                return false;
            }
        }
        return true;
    }
}
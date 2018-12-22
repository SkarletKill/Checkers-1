package com.gmail.lidteam.checkers.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.gmail.lidteam.checkers.R;
import com.gmail.lidteam.checkers.controllers.UserController;
import com.gmail.lidteam.checkers.models.AILevel;
import com.gmail.lidteam.checkers.models.GameType;
import com.gmail.lidteam.checkers.models.PlayerColor;
import com.gmail.lidteam.checkers.models.User;


public class SettingsActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            super.onBackPressed();
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    public static class MyPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        UserController userController;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            userController = UserController.getInstance(this.getActivity().getApplicationContext());
            initListPreferences();
        }

        private void initListPreferences() {
            final ListPreference prefCheckerColor = (ListPreference) findPreference("pref_checker_color");
            final ListPreference prefAiLevel = (ListPreference) findPreference("pref_ai_level");
            final ListPreference prefGameType = (ListPreference) findPreference("pref_game_type");

            User user = userController.getUser();
            prefCheckerColor.setValueIndex(user.getPreferredColor().ordinal());
            prefAiLevel.setValueIndex(user.getPreferredAiLevel().ordinal());
            prefGameType.setValueIndex(user.getPreferredType().ordinal());

            prefCheckerColor.setSummary(toProperCase(user.getPreferredColor().toString()));
            prefAiLevel.setSummary(toProperCase(user.getPreferredAiLevel().toString()));
            prefGameType.setSummary(toProperCase(user.getPreferredType().toString()));

            final EditTextPreference pref = (EditTextPreference) findPreference("username");
            pref.setText(user.getNickname());
            pref.setSummary(user.getNickname());
            pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    System.out.println("new  \"username\"  " +  newValue.toString());
                    userController.changeNickName(newValue.toString());
                    pref.setSummary(newValue.toString());
                    pref.setText(newValue.toString());
                    return false;
                }
            });
        }

        private String toProperCase(String s) {
            return s.substring(0, 1).toUpperCase() +
                    s.substring(1).toLowerCase();
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            /* get preference */
            Preference preference = findPreference(key);

            /* update summary */
            if (key.equals("pref_checker_color")) {
                String upperCase =  ((ListPreference) preference).getEntry().toString().toUpperCase();
                preference.setSummary(((ListPreference) preference).getEntry());
                userController.setPreferredColor(PlayerColor.valueOf(upperCase));
            }
            if (key.equals("pref_ai_level")) {
//                System.out.println("key.equals(pref_ai_level   " +  ((ListPreference) preference).getEntry());
//                String upperCase =  ((ListPreference) preference).getEntry().toString().toUpperCase();
                String upperCase =  ((ListPreference) preference).getEntry().toString().toUpperCase();
                userController.setPreferredAiLevel(AILevel.valueOf(upperCase));
                preference.setSummary(((ListPreference) preference).getEntry());
            }
            if (key.equals("pref_game_type")) {
//                System.out.println("key.equals(pref_game_type   " +  ((ListPreference) preference).getEntry());
//                String type =  ((ListPreference) preference).getEntry().toString().toUpperCase();
                String upperCase =  ((ListPreference) preference).getEntry().toString().toUpperCase();
                userController.setPreferredType(GameType.valueOf(upperCase));
                preference.setSummary(((ListPreference) preference).getEntry());
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
            super.onPause();
        }

    }
}

package com.gmail.lidteam.checkers.connectors;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.gmail.lidteam.checkers.models.User;

public class SharedPreferencesConnector {
    SharedPreferences preferences;
    private final String firstStart = "firstStart";

    public SharedPreferencesConnector(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean setCurrentUser(User loggedUser){
        return false;
    }

    public boolean unSetCurrentUser(){
        return false;
    }

    public User getCurrentUser(){
        return null;
    }

    public boolean isFirstStart(){
       return preferences.getBoolean(firstStart, false);
    }

    public void setNotFirstStart(){
        //  Make a new preferences editor
        SharedPreferences.Editor e = preferences.edit();

        //  Edit preference to make it false because we don't want this to run again
        e.putBoolean(firstStart, false);

        //  Apply changes
        e.apply();
    }
}

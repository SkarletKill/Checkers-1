package com.gmail.lidteam.checkers.connectors;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.gmail.lidteam.checkers.models.User;
import com.google.gson.Gson;

import java.util.Objects;

public class SharedPreferencesConnector {
    private SharedPreferences preferences;
    private final String firstStart = "firstStart";
    private final String currentUser = "currentUser";

    public SharedPreferencesConnector(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean noUserLogged(){
        return Objects.equals(preferences.getString(currentUser, ""), "");
    }

    public void setCurrentUser(User loggedUser){
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loggedUser);
        prefsEditor.putString(currentUser, json);
        prefsEditor.apply();
    }

    public void unSetCurrentUser(){
        SharedPreferences.Editor prefsEditor = preferences.edit();
        prefsEditor.putString(currentUser, "");
        prefsEditor.apply();
    }

    public User getCurrentUser(){
        Gson gson = new Gson();
        String json = preferences.getString(currentUser, "");
        return gson.fromJson(json, User.class);
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

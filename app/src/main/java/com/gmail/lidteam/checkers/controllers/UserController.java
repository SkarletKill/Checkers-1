package com.gmail.lidteam.checkers.controllers;

import android.content.Context;

import com.gmail.lidteam.checkers.connectors.DBConnectionException;
import com.gmail.lidteam.checkers.connectors.DBConnector;
import com.gmail.lidteam.checkers.connectors.SharedPreferencesConnector;
import com.gmail.lidteam.checkers.models.AILevel;
import com.gmail.lidteam.checkers.models.GameType;
import com.gmail.lidteam.checkers.models.PlayerColor;
import com.gmail.lidteam.checkers.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserController {
    private User user;
    private DBConnector dbConnector;
    private SharedPreferencesConnector sharedPreferencesConnector;
    private static UserController ourInstance;

    public static UserController getInstance(Context context) {
        if(ourInstance == null)   {
            ourInstance =  new UserController(context);
        }
        return ourInstance ;
    }

    private UserController(Context context) {
        sharedPreferencesConnector = new SharedPreferencesConnector(context);
        this.user = sharedPreferencesConnector.getCurrentUser();
    }

    public User getUser() {
        return user;
    }

    public void changePassword(String newPass){
        // user.setNewPassword(newPassword);
        saveAlteredUser();
    }

    public void changeNickName(String newNickName){
        // user.setNewNickName(newNickName);
        user.setNickname(newNickName);
        saveAlteredUser();
    }

    public void clearStatictics(){
        // user.clearStatictics();
        saveAlteredUser();
    }

    private void saveAlteredUser() {
        sharedPreferencesConnector.setCurrentUser(user);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getUid() != null) {
            myRef.child(mAuth.getUid()).child("userHimself").setValue(user);
        }
        sharedPreferencesConnector.setCurrentUser(user);
    }

    private void incFailures(){}

    private void incWins(){}

    private void incDraws(){}

    public void setUserLocally(User user){
        this.user = user;
        sharedPreferencesConnector.setCurrentUser(user);
    }

    public void setPreferredColor(PlayerColor color) {
        user.setPreferredColor(color);
        saveAlteredUser();
    }

    public void setPreferredAiLevel(AILevel level) {
        user.setPreferredAiLevel(level);
        saveAlteredUser();
    }

    public void setPreferredType(GameType type) {
        user.setPreferredType(type);
        saveAlteredUser();
    }

}

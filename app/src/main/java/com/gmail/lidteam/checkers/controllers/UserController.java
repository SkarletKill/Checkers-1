package com.gmail.lidteam.checkers.controllers;

import com.gmail.lidteam.checkers.connectors.DBConnectionException;
import com.gmail.lidteam.checkers.connectors.DBConnector;
import com.gmail.lidteam.checkers.connectors.SharedPreferencesConnector;
import com.gmail.lidteam.checkers.models.User;

public class UserController {
    private User user;
    private DBConnector dbConnector;
    private SharedPreferencesConnector sharedPreferencesConnector;

    public UserController() {
        sharedPreferencesConnector = new SharedPreferencesConnector();
        this.user = sharedPreferencesConnector.getCurrentUser();
    }

    public void changePassword(String newPass){
        // user.setNewPassword(newPassword);
        saveAlteredUser("");

    }

    public void changeNickName(String newNickName){
        // user.setNewNickName(newNickName);
        saveAlteredUser("");
    }

    public void clearStatictics(){
        // user.clearStatictics();
        saveAlteredUser("");
    }

    private void saveAlteredUser(String errorMsg) {
        try {
            DBConnector.getInstance().changeUser(user);
            sharedPreferencesConnector.setCurrentUser(user);
        } catch (DBConnectionException e) {
//            Notifications.showErrorMessage(errorMsg);
        }
    }

    private void incFailures(){}

    private void incWins(){}

    private void incDraws(){}
}

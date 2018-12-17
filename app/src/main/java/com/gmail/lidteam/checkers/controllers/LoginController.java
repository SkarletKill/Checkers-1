package com.gmail.lidteam.checkers.controllers;

import com.gmail.lidteam.checkers.activities.LoginActivity;
import com.gmail.lidteam.checkers.connectors.DBConnector;
import com.gmail.lidteam.checkers.models.User;

public class LoginController {
    private LoginActivity loginActivity;
    private User user;
    private DBConnector dbConnector;

    public LoginController(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    public void processLoginPass(String login, String pass) {
        // validateLoginPass
        // if true => {
        //      user = dbConnector.getUser()
        //        if(user != null) {
        //            sharedPreferencesConnector.setCurrentUser(user);
        //            loginActivity.gotoMainActivity(user);
        //        }
        //      else Notifications.showErrorMessage()
        // }
        // if false => Notifications.showErrorMessage()
    }

    private boolean validateLoginPass(String login, String pass) {
        return false;
    }

}

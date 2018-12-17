package com.gmail.lidteam.checkers.controllers;

import com.gmail.lidteam.checkers.activities.SignupActivity;
import com.gmail.lidteam.checkers.connectors.DBConnector;
import com.gmail.lidteam.checkers.connectors.SharedPreferencesConnector;
import com.gmail.lidteam.checkers.models.User;

public class SignupController {
    private SignupActivity signupActivity;
    private User user;
    private DBConnector dbConnector;
    private SharedPreferencesConnector sharedPreferencesConnector;

    public SignupController(SignupActivity signupActivity) {
        this.signupActivity = signupActivity;
    }

    public void processData(String login, String pass, String pass2, String name) {
        // validateLoginPass
        // if true => {
        //      try{
        //          user = dbConnector.createUser(login, pass, name)
        //          if(user != null) {
        //              sharedPreferencesConnector.setCurrentUser(user);
        //              registerActivity.gotoMainActivity(user);
        //          }
        //          else Notifications.showErrorMessage()
        //      catch(SuchUserAlreadyExistsException e) {
        //          Notifications.showErrorMessage()
        //      }
        // }
        // if false => Notifications.showErrorMessage()
    }

    private boolean validateData(String login, String pass, String pass2, String name) {
        return false;
    }
}

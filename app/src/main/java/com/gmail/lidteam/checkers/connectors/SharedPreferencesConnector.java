package com.gmail.lidteam.checkers.connectors;

import com.gmail.lidteam.checkers.models.User;

public class SharedPreferencesConnector {
    public boolean setCurrentUser(User loggedUser){
        return false;
    }

    public boolean unSetCurrentUser(){
        return false;
    }

    public User getCurrentUser(){
        return null;
    }
}

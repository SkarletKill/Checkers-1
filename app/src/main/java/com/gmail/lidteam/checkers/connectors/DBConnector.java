package com.gmail.lidteam.checkers.connectors;

import com.gmail.lidteam.checkers.models.GameType;
import com.gmail.lidteam.checkers.models.PlayerColor;
import com.gmail.lidteam.checkers.models.User;

import java.util.List;

public class DBConnector {
    private static DBConnector ourInstance = new DBConnector();

    public static DBConnector getInstance() {
        return ourInstance;
    }

    private DBConnector() {
    }

    public User getUser(String login, String pass){
        return null;
    }

    public User createUser(String login, String pass, String name) throws SuchUserAlreadyExistsException {
        return null;
    }

    public void changeUser(User alteredUser) throws DBConnectionException{
    }

    public List<User> getUsersOnline(GameType type, PlayerColor color){
        return null;
    }

}

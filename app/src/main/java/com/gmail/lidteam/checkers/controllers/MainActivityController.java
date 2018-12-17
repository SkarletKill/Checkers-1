package com.gmail.lidteam.checkers.controllers;

import com.gmail.lidteam.checkers.connectors.DBLocalConnector;
import com.gmail.lidteam.checkers.models.OneGame;

import java.util.List;

public class MainActivityController {
    private DBLocalConnector localDB;

    public List<OneGame> getAllGames() {
        return null;
    }

    private OneGame parseGame(String gameStr) {
        return null;
    }

    public void onStartOfflineGameClicked(){}
    public void onStartOnlineGameClicked(){}
    public void onStartBluetoothGameClicked(){
        //
    }
    public void onGameSettingsClicked(){}
    public void onHelpClicked(){}
    public void onWriteUsClicked(){}
}

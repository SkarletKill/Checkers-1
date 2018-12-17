package com.gmail.lidteam.checkers.controllers;

import com.gmail.lidteam.checkers.activities.OneGameActivity;
import com.gmail.lidteam.checkers.connectors.OpponentConnector;
import com.gmail.lidteam.checkers.models.Checker;
import com.gmail.lidteam.checkers.models.OneGame;
import com.gmail.lidteam.checkers.models.User;

public class GameController {
    private User activeUser;
    private UserController userController;
    private OneGame game;
    private OneGameActivity gameActivity;
    private Checker activeChecker;
    private OpponentConnector opponentConnector;

    public GameController(OpponentConnector opponentConnector) {
        this.opponentConnector = opponentConnector;
        // ... init
    }

    public void handleCellClick(String coordinates) {
        if (activeChecker == null) {
            handleFirstClick(coordinates);
        } else {
            handleSecondClick(coordinates);
        }
    }

    private void handleFirstClick(String coordinates){
        Checker checker = game.getCellContents(coordinates);
        // ...
        // true => setCurrentChecker
        // ...
    }

    private void handleSecondClick(String coordinates){

    }

    public void surrender(){}

    public void sendMove(String move){
        opponentConnector.sendUsersMove(move);
    }

    public String getMove(){
        return opponentConnector.getOpponentsMove();
    }

    public boolean isGameOver() {
        return false;
    }

    private void changeUser() {}

    private void unselectChecker(){
        activeChecker = null;
    }

    private void onGameOver(){}

}

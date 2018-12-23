package com.gmail.lidteam.checkers.controllers;

import android.widget.AdapterView;
import android.widget.ImageView;

import com.gmail.lidteam.checkers.R;
import com.gmail.lidteam.checkers.activities.OneGameActivity;
import com.gmail.lidteam.checkers.connectors.OpponentConnector;
import com.gmail.lidteam.checkers.models.Checker;
import com.gmail.lidteam.checkers.models.CheckerColor;
import com.gmail.lidteam.checkers.models.OneGame;
import com.gmail.lidteam.checkers.models.User;

public class GameController {
    private User activeUser;
    private OneGame game;
    private OneGameActivity gameActivity;
    private Checker activeChecker;
    private OpponentConnector opponentConnector;

    public GameController(OneGameActivity activity, OneGame game, OpponentConnector opponentConnector) {
        this.gameActivity = activity;
        this.game = game;
        this.opponentConnector = opponentConnector;
        this.activeUser = this.game.getWhite();
        // ... init
    }

    public boolean handleCellClick(AdapterView<?> parent, ImageView iv, String coordinates, long id) {
        if (activeChecker == null) {
            if (handleFirstClick(coordinates)) {
                iv.setBackgroundResource(R.drawable.lightgreen_square_128);
            }
        } else {
            if (handleSecondClick(coordinates)) {

            } else {
                int pos = converCoord(activeChecker.getPosition().getCoordinates());
                ImageView imageView = (ImageView) parent.getChildAt(pos);
                imageView.setBackgroundResource(R.drawable.black_square_128);
                unselectChecker();
            }
        }
        return false;   // не змінювати гравця
    }

    private boolean handleFirstClick(String coordinates) {
        Checker checker = game.getCellContents(coordinates);
        // ...
        if (checker != null && isCheckerBelongsPlayer(checker)) {
            activeChecker = checker;
            return true;    // checker was chosen
        } else return false;
        // ...
    }

    private boolean handleSecondClick(String coordinates) {

        return false;
    }

    private boolean isCheckerBelongsPlayer(Checker checker) {
        if (checker.getColor().equals(CheckerColor.WHITE) && game.getWhite().equals(activeUser))
            return true;
        else if (checker.getColor().equals(CheckerColor.BLACK) && game.getBlack().equals(activeUser))
            return true;
        return false;
    }

    private int converCoord(String coordinates) {
        return ('h' - coordinates.charAt(0)) * 8 + Integer.parseInt(String.valueOf(coordinates.charAt(1))) - 1;
    }

    public void surrender() {
    }

    public void sendMove(String move) {
        opponentConnector.sendUsersMove(move);
    }

    public String getMove() {
        return opponentConnector.getOpponentsMove();
    }

    public boolean isGameOver() {
        return false;
    }

    private void changeUser() {
    }

    private void unselectChecker() {
        activeChecker = null;
    }

    private void onGameOver() {
    }

}

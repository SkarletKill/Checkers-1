package com.gmail.lidteam.checkers.models;

import android.graphics.Color;

import com.gmail.lidteam.checkers.connectors.SharedPreferencesConnector;

import java.util.ArrayList;
import java.util.List;

public class GameForDB {
    private SharedPreferencesConnector sharedPreferencesConnector = new SharedPreferencesConnector();

    private String gameDateTime;
    private String gameDuration;
    private String gameOpponent;
    private String gameType;
    private String gameWinner;
    private String checkersWinnerLeft;
    private String numberOfMoves;
    private String moves;
    private int bgColor;
    private int textColor;

    public GameForDB(OneGame game) {
        setGameDateTime(game.getStartTime().toString());
        setGameDuration(game.getDuration());
        if(game.getBlack().equals(getSharedPreferencesConnector().getCurrentUser())){
            setGameOpponent(getSharedPreferencesConnector().getCurrentUser().getNickname() + "(b)  VS  " + game.getWhite().getNickname() +  " (w)" );
        }
        else {
            setGameOpponent(getSharedPreferencesConnector().getCurrentUser().getNickname() + "(w)  VS  " + game.getWhite().getNickname() +  " (b)");
        }
        setGameType(game.getGameType().name());
        setGameWinner(game.getWinner().getNickname());
        setCheckersWinnerLeft(String.valueOf(game.getCheckersWinnerLeft()));
        setNumberOfMoves(String.valueOf(game.getMoves().size()));
        CheckerColor color = game.getWinnerColor(getSharedPreferencesConnector().getCurrentUser());
        setBgColor(Color.parseColor(color.getColorCode()));
        setTextColor(Color.parseColor(color.getOppositeColorCode()));
        setMoves(game.getMoves());
    }


    public ArrayList<Move> getMoves() {
        // TODO      ArrayList<Move> parseMoves()
        return new ArrayList<>();
    }

    private void setMoves(List<Move> moves) {
        this.moves = moves.toString();
    }


    public String getGameDateTime() {
        return gameDateTime;
    }

    public String getGameDuration() {
        return gameDuration;
    }

    public String getGameOpponent() {
        return gameOpponent;
    }

    public String getGameType() {
        return gameType;
    }

    public String getGameWinner() {
        return gameWinner;
    }

    public String getCheckersWinnerLeft() {
        return checkersWinnerLeft;
    }

    public String getNumberOfMoves() {
        return numberOfMoves;
    }

    public int getBgColor() {
        return bgColor;
    }

    public int getTextColor() {
        return textColor;
    }

    private SharedPreferencesConnector getSharedPreferencesConnector() {
        return sharedPreferencesConnector;
    }

    public void setGameDateTime(String gameDateTime) {
        this.gameDateTime = gameDateTime;
    }

    public void setGameDuration(String gameDuration) {
        this.gameDuration = gameDuration;
    }

    public void setGameOpponent(String gameOpponent) {
        this.gameOpponent = gameOpponent;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public void setGameWinner(String gameWinner) {
        this.gameWinner = gameWinner;
    }

    public void setCheckersWinnerLeft(String checkersWinnerLeft) {
        this.checkersWinnerLeft = checkersWinnerLeft;
    }

    public void setNumberOfMoves(String numberOfMoves) {
        this.numberOfMoves = numberOfMoves;
    }

    private void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}



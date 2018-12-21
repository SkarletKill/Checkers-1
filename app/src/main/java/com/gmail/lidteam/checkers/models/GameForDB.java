package com.gmail.lidteam.checkers.models;

import android.graphics.Color;

import com.gmail.lidteam.checkers.connectors.SharedPreferencesConnector;

import java.util.ArrayList;
import java.util.List;

public class GameForDB {
    private int id;
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
        SharedPreferencesConnector sharedPreferencesConnector = new SharedPreferencesConnector();

        setGameDateTime(game.getStartTime().toString());
        setGameDuration(game.getDuration());
        if(game.getBlack().equals(sharedPreferencesConnector.getCurrentUser())){
            setGameOpponent(sharedPreferencesConnector.getCurrentUser().getNickname() + "(b)  VS  " + game.getWhite().getNickname() +  " (w)" );
        }
        else {
            setGameOpponent(sharedPreferencesConnector.getCurrentUser().getNickname() + "(w)  VS  " + game.getWhite().getNickname() +  " (b)");
        }
        setGameType(game.getGameType().name());
        setGameWinner(game.getWinner().getNickname());
        setCheckersWinnerLeft(String.valueOf(game.getCheckersWinnerLeft()));
        setNumberOfMoves(String.valueOf(game.getMoves().size()));
        CheckerColor color = game.getWinnerColor(sharedPreferencesConnector.getCurrentUser());
        setBgColor(Color.parseColor(color.getColorCode()));
        setTextColor(Color.parseColor(color.getOppositeColorCode()));
        setMoves(game.getMoves());
    }

    public GameForDB setId(int id) {
        this.id = id;
        return this;
    }

    public GameForDB(String gameDateTime, String gameDuration, String gameOpponent, String gameType, String gameWinner, String checkersWinnerLeft, String numberOfMoves, String moves, int bgColor, int textColor) {
        this.gameDateTime = gameDateTime;
        this.gameDuration = gameDuration;
        this.gameOpponent = gameOpponent;
        this.gameType = gameType;
        this.gameWinner = gameWinner;
        this.checkersWinnerLeft = checkersWinnerLeft;
        this.numberOfMoves = numberOfMoves;
        this.moves = moves;
        this.bgColor = bgColor;
        this.textColor = textColor;
    }

    private void setMoves(List<Move> moves) {
        this.moves = moves.toString();
    }

    public String getMoves() {
        return this.moves;
    }

    public int getId() {
        return id;
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

    @Override
    public String toString() {
        return "GameForDB{" +
                "id=" + id +
                ", gameDateTime='" + gameDateTime + '\'' +
                ", gameDuration='" + gameDuration + '\'' +
                ", gameOpponent='" + gameOpponent + '\'' +
                ", gameType='" + gameType + '\'' +
                ", gameWinner='" + gameWinner + '\'' +
                ", checkersWinnerLeft='" + checkersWinnerLeft + '\'' +
                ", numberOfMoves='" + numberOfMoves + '\'' +
                ", moves='" + moves + '\'' +
                ", bgColor=" + bgColor +
                ", textColor=" + textColor +
                '}';
    }
}



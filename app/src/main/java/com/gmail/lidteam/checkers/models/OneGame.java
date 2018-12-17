package com.gmail.lidteam.checkers.models;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class OneGame {
    private GameType gameType;
    private User white;
    private User black;
    private Date startTime;
    private Date endTame;
    private List<String> moves;
    private Map<String, Cell> board;
    private int whites;
    private int blacks;
    private User winner = null;

    private void initDesk() {
    }

    private void placeCheckers() {
    }

    public Checker getCellContents(String coordinates) {
        return board.get(coordinates).getChecker();
    }

    public boolean isMovePossible(String coordinates) {
        return false;
    }

    public void makeMove(String coordinates){}

    private boolean checkRequiredCombat() {
        return false;
    }

    private boolean checkForFight(Checker selectedChecker) {
        return false;
    }

    private boolean isMovePossibleTo(Checker selectedChecker, Cell cell) {
        return false;
    }

    private boolean isSuperMovePossibleTo(Checker selectedChecker, Cell cell) {
        return false;
    }

    private void addChecker(Cell cell, CheckerColor color, CheckerType type){}

    private void deleteChecker(Cell cell){}

}

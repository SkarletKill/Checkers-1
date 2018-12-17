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
    private List<Move> moves;
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

    public List<Move> getMoves() {
        return moves;
    }

    public GameType getGameType() {
        return gameType;
    }

    public User getWhite() {
        return white;
    }

    public User getBlack() {
        return black;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTame() {
        return endTame;
    }

    public Map<String, Cell> getBoard() {
        return board;
    }

    public int getWhites() {
        return whites;
    }

    public int getBlacks() {
        return blacks;
    }

    public User getWinner() {
        return winner;
    }

    public String getDuration(){
        long diff = endTame.getTime() - startTime.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        return diffHours + ":" + diffMinutes + ":" + diffSeconds;
    }

    public int getCheckersWinnerLeft(){
        if(white.equals(winner)){
            return whites;
        } else if(winner.equals(black))
            return blacks;
        else return 0;
    }

    public CheckerColor getWinnerColor(User user){
        if(user.equals(winner)) return CheckerColor.WHITE;
        else return CheckerColor.BLACK;
    }
}

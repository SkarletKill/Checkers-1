package com.gmail.lidteam.checkers.models;

import java.util.ArrayList;
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

    public OneGame(GameType gameType, User white, User black, Map<String, Cell> board) {
        this.gameType = gameType;
        this.white = white;
        this.black = black;
        this.startTime = new Date();
        this.moves = new ArrayList<>();
        this.board = board;
    }

    public void endGame(){
        endTame = new Date();
    }


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

    List<Move> getMoves() {
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

    Date getStartTime() {
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

    User getWinner() {
        return winner;
    }

    String getDuration(){
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

    CheckerColor getWinnerColor(User user){
        if(user.equals(winner)) return CheckerColor.WHITE;
        else return CheckerColor.BLACK;
    }


}

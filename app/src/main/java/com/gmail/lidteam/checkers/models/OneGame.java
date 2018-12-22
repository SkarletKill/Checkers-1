package com.gmail.lidteam.checkers.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

    public OneGame(GameType gameType, User white, User black) {
        this.gameType = gameType;
        this.white = white;
        this.black = black;
        this.startTime = new Date();
        this.moves = new ArrayList<>();
//        this.board = board;
        this.board = new HashMap<>();
        this.whites = 12;
        this.blacks = 12;

        initDesk();
        placeCheckers();
    }

    public void endGame() {
        endTame = new Date();
    }

    private void initDesk() {
        for (char i = 'a'; i < 'i'; i++) {
            for (int j = 1; j < 9; j++) {
                String key = String.valueOf(i) + j;
                board.put(key, new Cell(key, null));
            }
        }
    }

    private void placeCheckers() {
        String[] userCheckersStartPositions = {"a2", "a4", "a6", "a8",
                "b1", "b3", "b5", "b7",
                "c2", "c4", "c6", "c8"};
        String[] opponentCheckersStartPositions = {"f1", "f3", "f5", "f7",
                "g2", "g4", "g6", "g8",
                "h1", "h3", "h5", "h7"};

        for (String checkerPosition : userCheckersStartPositions) {
            board.get(checkerPosition).setChecker(new Checker(CheckerColor.WHITE, CheckerType.SIMPLE, board.get(checkerPosition)));
        }
        for (String checkerPosition : opponentCheckersStartPositions) {
            board.get(checkerPosition).setChecker(new Checker(CheckerColor.BLACK, CheckerType.SIMPLE, board.get(checkerPosition)));
        }
    }

    public Checker getCellContents(String coordinates) {
        return board.get(coordinates).getChecker();
    }

    public boolean isMovePossible(String coordinates) {
        return false;
    }

    public void makeMove(String coordinates) {
    }

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

    private void addChecker(Cell cell, CheckerColor color, CheckerType type) {
    }

    private void deleteChecker(Cell cell) {
    }

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

    String getDuration() {
        long diff = endTame.getTime() - startTime.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        return diffHours + ":" + diffMinutes + ":" + diffSeconds;
    }

    public int getCheckersWinnerLeft() {
        if (white.equals(winner)) {
            return whites;
        } else if (winner.equals(black))
            return blacks;
        else return 0;
    }

    CheckerColor getWinnerColor(User user) {
        if (user.equals(winner)) return CheckerColor.WHITE;
        else return CheckerColor.BLACK;
    }


}

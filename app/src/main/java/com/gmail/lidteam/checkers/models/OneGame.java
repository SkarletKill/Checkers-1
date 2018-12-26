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
        String[] userCheckersStartPositions = {"b1", "d1", "f1", "h1",
                "a2", "c2", "e2", "g2",
                "b3", "d3", "f3", "h3"};
        String[] opponentCheckersStartPositions = {"a6", "c6", "e6", "g6",
                "b7", "d7", "f7", "h7",
                "a8", "c8", "e8", "g8"};

        for (String checkerPosition : userCheckersStartPositions) {
            board.get(checkerPosition).setChecker(new Checker(CheckerColor.WHITE, CheckerType.SIMPLE, board.get(checkerPosition)));
        }
        for (String checkerPosition : opponentCheckersStartPositions) {
            board.get(checkerPosition).setChecker(new Checker(CheckerColor.BLACK, CheckerType.SIMPLE, board.get(checkerPosition)));
        }
    }

    public void setWinner(boolean white) {
        if (white) {
            this.winner = this.white;
            this.white.setUnbelievableWins(this.white.getUnbelievableWins() + 1);
            this.black.setEpicFails(this.black.getEpicFails() + 1);
        } else {
            this.winner = this.black;
            this.black.setUnbelievableWins(this.black.getUnbelievableWins() + 1);
            this.white.setEpicFails(this.white.getEpicFails() + 1);
        }
        endGame();
    }

    public void deleteChecker(Cell cell, boolean kill) {
        if (kill) {
            if (cell.getChecker().getColor().equals(CheckerColor.WHITE)) whites--;
            if (cell.getChecker().getColor().equals(CheckerColor.BLACK)) blacks--;
        }
        cell.deleteChecker();
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

    public void addMove(Move move) {
        moves.add(move);
    }

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

    public String getDuration() {
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

    public CheckerColor getWinnerColor(User user) {
        if (user.equals(winner)) return CheckerColor.WHITE;
        else return CheckerColor.BLACK;
    }


}

package com.gmail.lidteam.checkers.models;

public class Move {
    private String move;
    private CheckerColor color;

    public Move(String move, CheckerColor color) {
        this.move = move;
        this.color = color;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public CheckerColor getColor() {
        return color;
    }

    public void setColor(CheckerColor color) {
        this.color = color;
    }
}

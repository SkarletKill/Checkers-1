package com.gmail.lidteam.checkers.models;

import android.graphics.Color;
import android.support.annotation.NonNull;


public class Move {
    private Cell from;
    private Cell to;
    private String move;
    private int color;
    private int oppositeColor;

    public Move(Cell from, Cell to, CheckerColor color, boolean fight) {
        this.from = from;
        this.to = to;
        this.color = Color.parseColor(color.getColorCode());
        this.oppositeColor = Color.parseColor(color.getOppositeColorCode());
        this.move = from.getCoordinates() + "-" + to.getCoordinates();
        if (fight) this.move += "*";
    }

    public Cell getFrom() {
        return from;
    }

    public Cell getTo() {
        return to;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public int getColor() {
        return color;
    }

    public int getOppositeColor() {
        return oppositeColor;
    }

    public void setColor(CheckerColor color) {
        this.color = Color.parseColor(color.getColorCode());
        this.oppositeColor = Color.parseColor(color.getOppositeColorCode());
    }

    @NonNull
    @Override
    public String toString() {
        return move + " " + color + " " + oppositeColor;
    }

    public Move(String move) {
//        System.out.println("String move   " + move);
        String[] field = move.trim().split(" ");
//        System.out.println("String field   " + Arrays.toString(field));
        this.move = field[0].trim();
        this.color = Integer.parseInt(field[1].trim());
        this.oppositeColor = Integer.parseInt(field[2].trim());
    }
}

package com.gmail.lidteam.checkers.models;

public class Checker {
    private CheckerColor color;
    private CheckerType type;
    private Cell position;

    public Checker(CheckerColor color, CheckerType type, Cell position) {
        this.color = color;
        this.type = type;
        this.position = position;
    }

    public CheckerColor getColor() {
        return color;
    }

    public void setColor(CheckerColor color) {
        this.color = color;
    }

    public CheckerType getType() {
        return type;
    }

    public void setType(CheckerType type) {
        this.type = type;
    }

    public Cell getPosition() {
        return position;
    }

    public void setPosition(Cell position) {
        this.position = position;
    }
}

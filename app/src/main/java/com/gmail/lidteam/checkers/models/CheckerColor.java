package com.gmail.lidteam.checkers.models;

public enum CheckerColor {
    WHITE("#FFFFFF", "#000000"),
    BLACK("#000000", "#FFFFFF");

    String colorCode;
    String oppositeColorCode;

    CheckerColor(String colorCode, String oppositeColorCode) {
        this.colorCode = colorCode;
        this.oppositeColorCode = oppositeColorCode;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getOppositeColorCode() {
        return oppositeColorCode;
    }
}

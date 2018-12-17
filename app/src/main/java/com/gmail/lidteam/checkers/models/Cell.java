package com.gmail.lidteam.checkers.models;

class Cell {
    private String coordinates;
    private Checker checker;

    public Cell(String coordinates, Checker checker) {
        this.coordinates = coordinates;
        this.checker = checker;
    }

    public Checker getChecker() {
        return checker;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public void setChecker(Checker checker) {
        this.checker = checker;
    }
}

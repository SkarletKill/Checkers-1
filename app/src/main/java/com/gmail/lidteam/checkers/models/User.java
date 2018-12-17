package com.gmail.lidteam.checkers.models;

public class User {
    private String email;
    private String nickname;
    private int unbelievableWins;
    private int epicFails;
    private int draws;

    public User(String email, String nickname, int unbelievableWins, int epicFails, int draws) {
        this.email = email;
        this.nickname = nickname;
        this.unbelievableWins = unbelievableWins;
        this.epicFails = epicFails;
        this.draws = draws;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUnbelievableWins() {
        return unbelievableWins;
    }

    public void setUnbelievableWins(int unbelievableWins) {
        this.unbelievableWins = unbelievableWins;
    }

    public int getEpicFails() {
        return epicFails;
    }

    public void setEpicFails(int epicFails) {
        this.epicFails = epicFails;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }
}

package com.gmail.lidteam.checkers.models;

public class User {
    
    private String email;
    private String nickname;
    private int unbelievableWins;
    private int epicFails;
    private int draws;
    private GameType preferredType;
    private PlayerColor preferredColor;
    private AILevel preferredAiLevel;

    public User() {
    }

    public User(String email, String nickname, int unbelievableWins, int epicFails, int draws) {
        this.email = email;
        this.nickname = nickname;
        this.unbelievableWins = unbelievableWins;
        this.epicFails = epicFails;
        this.draws = draws;
    }

    public User(String email, String nickname, int unbelievableWins, int epicFails, int draws,
                GameType preferredType, PlayerColor preferredColor, AILevel preferredAiLevel) {
        this.email = email;
        this.nickname = nickname;
        this.unbelievableWins = unbelievableWins;
        this.epicFails = epicFails;
        this.draws = draws;
        this.preferredType = preferredType;
        this.preferredColor = preferredColor;
        this.preferredAiLevel = preferredAiLevel;
    }

    public GameType getPreferredType() {
        return preferredType;
    }

    public PlayerColor getPreferredColor() {
        return preferredColor;
    }

    public AILevel getPreferredAiLevel() {
        return preferredAiLevel;
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

    public void setPreferredType(GameType preferredType) {
        this.preferredType = preferredType;
    }

    public void setPreferredColor(PlayerColor preferredColor) {
        this.preferredColor = preferredColor;
    }

    public void setPreferredAiLevel(AILevel preferredAiLevel) {
        this.preferredAiLevel = preferredAiLevel;
    }
}

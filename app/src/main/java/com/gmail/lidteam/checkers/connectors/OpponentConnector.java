package com.gmail.lidteam.checkers.connectors;

public abstract class OpponentConnector {
    public abstract String getOpponentsMove();
    public abstract void sendUsersMove(String move);
}

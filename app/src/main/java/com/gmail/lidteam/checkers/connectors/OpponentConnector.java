package com.gmail.lidteam.checkers.connectors;

import com.gmail.lidteam.checkers.models.Move;

public abstract class OpponentConnector {
    public abstract Move getOpponentsMove();
    public abstract void sendUsersMove(String move);
}

package com.gmail.lidteam.checkers.connectors;

import com.gmail.lidteam.checkers.connectors.OpponentConnector;

public class OfflineOpponentConnector extends OpponentConnector {
    @Override
    public String getOpponentsMove() {
        return null;
    }

    @Override
    public void sendUsersMove(String move) {

    }
}

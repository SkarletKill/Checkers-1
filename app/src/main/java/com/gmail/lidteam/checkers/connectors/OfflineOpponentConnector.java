package com.gmail.lidteam.checkers.connectors;

import android.annotation.TargetApi;
import android.os.Build;

import com.gmail.lidteam.checkers.controllers.GameController;
import com.gmail.lidteam.checkers.models.AILevel;
import com.gmail.lidteam.checkers.models.Cell;
import com.gmail.lidteam.checkers.models.Checker;
import com.gmail.lidteam.checkers.models.CheckerColor;
import com.gmail.lidteam.checkers.models.CheckerType;
import com.gmail.lidteam.checkers.models.Move;
import com.gmail.lidteam.checkers.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class OfflineOpponentConnector extends OpponentConnector {
    private User user;
    private GameController controller;

    public OfflineOpponentConnector(User user, GameController controller) {
        this.user = user;
        this.controller = controller;
    }

    @Override
    public Move getOpponentsMove() {
        if (user.getNickname().equals(AILevel.EASY.name())) return chooseMove();
        if (user.getNickname().equals(AILevel.MIDDLE.name())) return chooseMove();
        if (user.getNickname().equals(AILevel.HARD.name())) return chooseMove();
        return chooseMove();
    }

    @Override
    public void sendUsersMove(String move) {

    }

    private Move chooseMove() {
        List<Move> possibleMoves = getPossibleMoves();
        int index = new Random().nextInt(possibleMoves.size());
        Move move = possibleMoves.get(index);
        return move;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private List<Move> getPossibleMoves() {
        List<Move> moves = new ArrayList<>();
        boolean isWhite = user.equals(controller.getGame().getWhite());
        // select all user's checkers
        ArrayList<Checker> checkers = controller.getGame().getBoard().keySet().stream()
                .map(controller.getGame().getBoard()::get).map(Cell::getChecker).filter(Objects::nonNull)
                .filter(checker -> (isWhite) ? checker.getColor().equals(CheckerColor.WHITE) : checker.getColor().equals(CheckerColor.BLACK))
                .collect(Collectors.toCollection(ArrayList::new));

        if (controller.isRequiredCombat()) {
            for (Checker checker : checkers) {
                moves.addAll(getFightsFor(checker));
            }
        } else {
            for (Checker checker : checkers) {
                moves.addAll(getMovesFor(checker));
            }
        }

        return moves;
    }

    private List<Move> getFightsFor(Checker checker) {
        List<Move> moves = new ArrayList<>();
        boolean fight = true;
        // ...
        String pos;

        if (checker.getType().equals(CheckerType.SIMPLE)) {
            //left-up
            pos = controller.getCoordinatesRelative(checker.getPosition().getCoordinates(), 2, -2);
            if (controller.canSimpleKillOn(checker, pos))
                moves.add(new Move(checker.getPosition(), getCell(pos), checker.getColor(), fight));
            //right-down
            pos = controller.getCoordinatesRelative(checker.getPosition().getCoordinates(), -2, 2);
            if (controller.canSimpleKillOn(checker, pos))
                moves.add(new Move(checker.getPosition(), getCell(pos), checker.getColor(), fight));
            //left-down
            pos = controller.getCoordinatesRelative(checker.getPosition().getCoordinates(), -2, -2);
            if (controller.canSimpleKillOn(checker, pos))
                moves.add(new Move(checker.getPosition(), getCell(pos), checker.getColor(), fight));
            //right-up
            pos = controller.getCoordinatesRelative(checker.getPosition().getCoordinates(), 2, 2);
            if (controller.canSimpleKillOn(checker, pos))
                moves.add(new Move(checker.getPosition(), getCell(pos), checker.getColor(), fight));
        } else if (checker.getType().equals(CheckerType.SUPER)) {
            moves.addAll(getSuperFightsToward(checker, 1, -1));    // left-up
            moves.addAll(getSuperFightsToward(checker, -1, 1));    // right-down
            moves.addAll(getSuperFightsToward(checker, -1, -1));   // left-down
            moves.addAll(getSuperFightsToward(checker, 1, 1));     // right-up
        }
        // ...
        return moves;
    }

    private List<Move> getMovesFor(Checker checker) {
        List<Move> moves = new ArrayList<>();
        boolean fight = false;
        String CheckerCoord = checker.getPosition().getCoordinates();

        String pos;
        if (checker.getType().equals(CheckerType.SIMPLE)) {
            pos = controller.getCoordinatesRelative(CheckerCoord, (checker.getColor().equals(CheckerColor.WHITE)) ? 1 : -1, 1);
            if (controller.checkCollisionFor(pos) && controller.getChecker(pos) == null)
                moves.add(new Move(checker.getPosition(), getCell(pos), checker.getColor(), fight));
            pos = controller.getCoordinatesRelative(pos, 0, -2);
            if (controller.checkCollisionFor(pos) && controller.getChecker(pos) == null)
                moves.add(new Move(checker.getPosition(), getCell(pos), checker.getColor(), fight));
        }
        if (checker.getType().equals(CheckerType.SUPER)) {
            pos = controller.getCoordinatesRelative(CheckerCoord, -1, 1);
            while (controller.checkCollisionFor(pos) && controller.getChecker(pos) == null) {
                moves.add(new Move(checker.getPosition(), getCell(pos), checker.getColor(), fight));
                pos = controller.getCoordinatesRelative(pos, -1, 1);
            }
            pos = controller.getCoordinatesRelative(CheckerCoord, -1, -1);
            while (controller.checkCollisionFor(pos) && controller.getChecker(pos) == null) {
                moves.add(new Move(checker.getPosition(), getCell(pos), checker.getColor(), fight));
                pos = controller.getCoordinatesRelative(pos, -1, 1);
            }

            pos = controller.getCoordinatesRelative(CheckerCoord, 1, 1);
            while (controller.checkCollisionFor(pos) && controller.getChecker(pos) == null) {
                moves.add(new Move(checker.getPosition(), getCell(pos), checker.getColor(), fight));
                pos = controller.getCoordinatesRelative(pos, -1, 1);
            }
            pos = controller.getCoordinatesRelative(CheckerCoord, 1, -1);
            while (controller.checkCollisionFor(pos) && controller.getChecker(pos) == null) {
                moves.add(new Move(checker.getPosition(), getCell(pos), checker.getColor(), fight));
                pos = controller.getCoordinatesRelative(pos, -1, 1);
            }
        }

        return moves;
    }

    private List<Move> getSuperFightsToward(Checker checker, int dy, int dx) {
        String tCoord = controller.getCoordinatesRelative(checker.getPosition().getCoordinates(), dy, dx);
        List<Move> fights = new ArrayList<>();

        while (controller.checkCollisionFor(tCoord)) {
            if (controller.getChecker(tCoord) != null) {
                if (checker.getColor().equals(controller.getChecker(tCoord).getColor())) break;
                else {
                    String tCoord2 = controller.getCoordinatesRelative(tCoord, dy, dx);
                    while (controller.checkCollisionFor(tCoord2) && controller.getChecker(tCoord2) == null) {
                        fights.add(new Move(checker.getPosition(), getCell(tCoord2), checker.getColor(), true));
                        tCoord2 = controller.getCoordinatesRelative(tCoord, dy, dx);
                    }
                    break;
                }
            }
            tCoord = controller.getCoordinatesRelative(tCoord, dy, dx);
        }
        return fights;
    }

    private Cell getCell(String coordinates) {
        return controller.getGame().getBoard().get(coordinates);
    }
}

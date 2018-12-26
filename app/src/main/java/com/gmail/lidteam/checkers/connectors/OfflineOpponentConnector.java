package com.gmail.lidteam.checkers.connectors;

import android.annotation.TargetApi;
import android.os.Build;

import com.gmail.lidteam.checkers.controllers.GameController;
import com.gmail.lidteam.checkers.models.AILevel;
import com.gmail.lidteam.checkers.models.Cell;
import com.gmail.lidteam.checkers.models.Checker;
import com.gmail.lidteam.checkers.models.CheckerColor;
import com.gmail.lidteam.checkers.models.CheckerType;
import com.gmail.lidteam.checkers.models.GameType;
import com.gmail.lidteam.checkers.models.Move;
import com.gmail.lidteam.checkers.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class OfflineOpponentConnector extends OpponentConnector {
    private User user;
    private GameController controller;
    private Map<Move, String> killed;
    private boolean wCombats = false;

    public OfflineOpponentConnector(User user, GameController controller) {
        this.user = user;
        this.controller = controller;
        killed = new HashMap<>();
    }

    @Override
    public Move getOpponentsMove() {
        if (user.getNickname().equals(AILevel.EASY.name())) wCombats = false;
        else if (user.getNickname().equals(AILevel.MIDDLE.name())) wCombats = true;
        else if (user.getNickname().equals(AILevel.HARD.name())) wCombats = true;
        if (controller.getGame().getGameType().equals(GameType.GIVEAWAY)) wCombats = !wCombats;
        return chooseMove();
    }

    @Override
    public void sendUsersMove(String move) {

    }

    private Move chooseMove() {
        List<Move> possibleMoves = getPossibleMoves();
        Move move = null;
        if (wCombats) {
            List<Move> w1 = new ArrayList<>();
            List<Move> w3 = new ArrayList<>();
            for (Move possibleMove : possibleMoves) {
                Map<String, Cell> tempBoard = cloneBoard();
                Cell needDel = tempBoard.get(killed.get(possibleMove));

                if (needDel != null) needDel.deleteChecker();
                Checker checker = tempBoard.get(possibleMove.getFrom().getCoordinates()).getChecker();
                checker.getPosition().deleteChecker();
                tempBoard.get(possibleMove.getTo().getCoordinates()).setChecker(checker);

                boolean moveWhite = user.equals(controller.getGame().getBlack());
                boolean requiredCombat = checkRequiredCombat(tempBoard, moveWhite);

                if (requiredCombat) w1.add(possibleMove);
                else w3.add(possibleMove);
            }

            if (w3.isEmpty()) {
                int index = new Random().nextInt(w1.size());
                move = w1.get(index);
            } else {
                int index = new Random().nextInt(w3.size());
                move = w3.get(index);
            }

            return move;
        } else {
            int index = new Random().nextInt(possibleMoves.size());
            move = possibleMoves.get(index);
        }
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
            if (controller.canSimpleKillOn(checker, pos)) {
                Move move = new Move(checker.getPosition(), getCell(pos), checker.getColor(), fight);
                moves.add(move);
                if (wCombats) {
                    String midCell = controller.getCellBetween(checker.getPosition().getCoordinates(), pos).getCoordinates();
                    killed.put(move, midCell);
                }
            }
            //right-down
            pos = controller.getCoordinatesRelative(checker.getPosition().getCoordinates(), -2, 2);
            if (controller.canSimpleKillOn(checker, pos)) {
                Move move = new Move(checker.getPosition(), getCell(pos), checker.getColor(), fight);
                moves.add(move);
                if (wCombats) {
                    String midCell = controller.getCellBetween(checker.getPosition().getCoordinates(), pos).getCoordinates();
                    killed.put(move, midCell);
                }
            }
            //left-down
            pos = controller.getCoordinatesRelative(checker.getPosition().getCoordinates(), -2, -2);
            if (controller.canSimpleKillOn(checker, pos)) {
                Move move = new Move(checker.getPosition(), getCell(pos), checker.getColor(), fight);
                moves.add(move);
                if (wCombats) {
                    String midCell = controller.getCellBetween(checker.getPosition().getCoordinates(), pos).getCoordinates();
                    killed.put(move, midCell);
                }
            }
            //right-up
            pos = controller.getCoordinatesRelative(checker.getPosition().getCoordinates(), 2, 2);
            if (controller.canSimpleKillOn(checker, pos)) {
                Move move = new Move(checker.getPosition(), getCell(pos), checker.getColor(), fight);
                moves.add(move);
                if (wCombats) {
                    String midCell = controller.getCellBetween(checker.getPosition().getCoordinates(), pos).getCoordinates();
                    killed.put(move, midCell);
                }
            }
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
                pos = controller.getCoordinatesRelative(pos, -1, -1);
            }

            pos = controller.getCoordinatesRelative(CheckerCoord, 1, 1);
            while (controller.checkCollisionFor(pos) && controller.getChecker(pos) == null) {
                moves.add(new Move(checker.getPosition(), getCell(pos), checker.getColor(), fight));
                pos = controller.getCoordinatesRelative(pos, 1, 1);
            }
            pos = controller.getCoordinatesRelative(CheckerCoord, 1, -1);
            while (controller.checkCollisionFor(pos) && controller.getChecker(pos) == null) {
                moves.add(new Move(checker.getPosition(), getCell(pos), checker.getColor(), fight));
                pos = controller.getCoordinatesRelative(pos, 1, -1);
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
                        Move move = new Move(checker.getPosition(), getCell(tCoord2), checker.getColor(), true);
                        fights.add(move);
                        if (wCombats) killed.put(move, tCoord);
                        tCoord2 = controller.getCoordinatesRelative(tCoord2, dy, dx);
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

    @TargetApi(Build.VERSION_CODES.N)
    private Map<String, Cell> cloneBoard() {
        Map<String, Cell> clone = new HashMap<>();
        controller.getGame().getBoard().forEach((k, v) -> {
            clone.put(k, new Cell(k, v.getChecker()));
        });
        clone.forEach((k, v) -> {
            Checker c = v.getChecker();
            if (c != null)
                v.setChecker(new Checker(c.getColor(), c.getType(), clone.get(c.getPosition().getCoordinates())));
        });

        return clone;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private boolean checkRequiredCombat(Map<String, Cell> board, boolean moveWhite) {
        ArrayList<Checker> checkers = board.keySet().stream()
                .map(board::get).map(Cell::getChecker).filter(Objects::nonNull)
                .filter(c -> c.getColor().equals((moveWhite) ? CheckerColor.WHITE : CheckerColor.BLACK))
                .collect(Collectors.toCollection(ArrayList::new));
        for (Checker checker : checkers) {
            if (canFightFor(checker, board)) return true;
        }
        return false;
    }

    public boolean canFightFor(Checker checker, Map<String, Cell> board) {
        String tCoord;

        if (checker == null) {
            System.out.println("A checker wasn't choosen!");
            return false;
        }
        if (checker.getType().equals(CheckerType.SIMPLE)) {     // checking simple checker;
            //left-up
            tCoord = controller.getCoordinatesRelative(checker.getPosition().getCoordinates(), 2, -2);
            if (canSimpleKillOn(checker, tCoord, board)) return true;
            //right-down
            tCoord = controller.getCoordinatesRelative(checker.getPosition().getCoordinates(), -2, 2);
            if (canSimpleKillOn(checker, tCoord, board)) return true;
            //left-down
            tCoord = controller.getCoordinatesRelative(checker.getPosition().getCoordinates(), -2, -2);
            if (canSimpleKillOn(checker, tCoord, board)) return true;
            //right-up
            tCoord = controller.getCoordinatesRelative(checker.getPosition().getCoordinates(), 2, 2);
            if (canSimpleKillOn(checker, tCoord, board)) return true;

            return false;
        }      // end checking simple chacker;
        if (checker.getType().equals(CheckerType.SUPER)) {
            if (canSuperKillToward(checker, 1, -1, board)) return true;    // left-up
            if (canSuperKillToward(checker, -1, 1, board)) return true;    // right-down
            if (canSuperKillToward(checker, -1, -1, board)) return true;   // left-down
            if (canSuperKillToward(checker, 1, 1, board)) return true;     // right-up
            return false;
        }      //end checking super chacker;

        System.out.println("Critical error in getting rank into checkForFight()!");
        return false;
    }

    public boolean canSimpleKillOn(Checker checker, String tCoord, Map<String, Cell> board) {
        if (controller.checkCollisionFor(tCoord) && getChecker(tCoord, board) == null) {
            Checker midChecker = getCellBetween(checker.getPosition().getCoordinates(), tCoord, board).getChecker();
            if (midChecker != null && checker.getColor().getOppositeColorCode().equals(midChecker.getColor().getColorCode()))
                return true;
        }
        return false;
    }

    private boolean canSuperKillToward(Checker checker, int dy, int dx, Map<String, Cell> board) {
        String tCoord = controller.getCoordinatesRelative(checker.getPosition().getCoordinates(), dy, dx);
        while (controller.checkCollisionFor(tCoord)) {
            if (getChecker(tCoord, board) != null) {
                if (checker.getColor().equals(getChecker(tCoord, board).getColor())) break;
                else {
                    String tCoord2 = controller.getCoordinatesRelative(tCoord, dy, dx);
                    if (controller.checkCollisionFor(tCoord2) && getChecker(tCoord2, board) == null)
                        return true;
                    else break;
                }
            }
            tCoord = controller.getCoordinatesRelative(tCoord, dy, dx);
        }
        return false;
    }

    private Cell getCellBetween(String coords1, String coords2, Map<String, Cell> board) {
        char midX = (char) ((controller.getCoordX(coords1) + controller.getCoordX(coords2)) / 2); //???
        char midY = (char) ((controller.getCoordY(coords1) + controller.getCoordY(coords2)) / 2); //???
        String betweenCoordinates = "" + midX + midY;
        return board.get(betweenCoordinates);
    }

    private Checker getChecker(String coordinates, Map<String, Cell> board) {
        return board.get(coordinates).getChecker();
    }
}

package com.gmail.lidteam.checkers.controllers;

import android.annotation.TargetApi;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.gmail.lidteam.checkers.R;
import com.gmail.lidteam.checkers.activities.OneGameActivity;
import com.gmail.lidteam.checkers.connectors.OpponentConnector;
import com.gmail.lidteam.checkers.models.Cell;
import com.gmail.lidteam.checkers.models.Checker;
import com.gmail.lidteam.checkers.models.CheckerColor;
import com.gmail.lidteam.checkers.models.CheckerType;
import com.gmail.lidteam.checkers.models.GameType;
import com.gmail.lidteam.checkers.models.Move;
import com.gmail.lidteam.checkers.models.OneGame;
import com.gmail.lidteam.checkers.models.User;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.gmail.lidteam.checkers.activities.OneGameActivity.setImageFor;

public class GameController {
    private User activeUser;
    private OneGame game;
    private OneGameActivity gameActivity;
    private Checker activeChecker;
//    private OpponentConnector opponentConnector;

    private boolean moveWhite;
    private boolean lastFight;  // last move was lastFight
    private boolean battleOver;  // battle was over
    private boolean requiredCombat;
    private Cell deleteCheckerCell;

    public GameController(OneGameActivity activity, OneGame game) {
        this.gameActivity = activity;
        this.game = game;
        this.activeUser = this.game.getWhite();
        this.moveWhite = true;
        this.lastFight = false;
        this.battleOver = true;
        this.requiredCombat = false;
        this.deleteCheckerCell = null;
        // ... init
    }

    public boolean handleCellClick(AdapterView<?> parent, String position, boolean opponent) {
        Cell cell = Objects.requireNonNull(game.getBoard().get(position));
        if (activeChecker == null) {
            if (handleFirstClick(cell)) {
                setImageFor(parent, 0, R.drawable.lightgreen_square_128, cell);
            }
        } else {
            if (handleSecondClick(cell)) {
                Cell from = activeChecker.getPosition();
                game.addMove(new Move(from, cell, activeUser.equals(game.getWhite())? CheckerColor.WHITE: CheckerColor.BLACK, lastFight));

                game.deleteChecker(activeChecker.getPosition(), false);
                setImageFor(parent, R.drawable.empty_image, R.drawable.black_square_128, activeChecker.getPosition());

                cell.setChecker(activeChecker);
                activeChecker.setPosition(cell);
                int imageId = (moveWhite)
                        ? (activeChecker.getType().equals(CheckerType.SIMPLE)) ? R.drawable.checker_white : R.drawable.white_checker_super
                        : (activeChecker.getType().equals(CheckerType.SIMPLE)) ? R.drawable.checker_black : R.drawable.black_checker_super;
                setImageFor(parent, imageId, 0, cell);

                if (deleteCheckerCell != null) {
                    setImageFor(parent, R.drawable.empty_image, 0, deleteCheckerCell);
                    deleteCheckerCell = null;
                }

                {
                    if (lastFight && canFightFor(activeChecker)) {
                        battleOver = false;
                        requiredCombat = true;
                        setImageFor(parent, 0, R.drawable.lightgreen_square_128, activeChecker.getPosition());
                    } else battleOver = true;

                    if (battleOver) {
//                        d.statistic.clearStrick();
                        changePlayer();
                        requiredCombat = checkRequiredCombat();
                        if (!requiredCombat && !canMoveAtAll()) {
                            if (moveWhite)
                                game.setWinner(!game.getGameType().equals(GameType.CLASSIC));
                            else game.setWinner(game.getGameType().equals(GameType.CLASSIC));
                            //... end game;
                            return true;
                        }
                        activeChecker = null;
                        return false;
                    }
                }

            } else {
                if (battleOver) {
                    setImageFor(parent, 0, R.drawable.black_square_128, activeChecker.getPosition());
                    unselectChecker();
                }
            }
        }
        return false;   // не змінювати гравця
    }

    private boolean handleFirstClick(Cell cell) {
        Checker checker = cell.getChecker();
        // ...
        if (checker != null && isCheckerBelongsPlayer(checker)) {
            activeChecker = checker;
            return true;    // checker was chosen
        } else return false;
        // ...
    }

    private boolean handleSecondClick(Cell cell) {
        // move
        lastFight = false;
        if ((getXYCoord(cell)[0] + getXYCoord(cell)[0]) % 2 == 0) {     // black cell
            if (cell.equals(activeChecker.getPosition())) {
                System.out.println("cancel checker selection");
                return false;
            }
            //check for rank...
            if (canMoveTo(cell)) return true;
        } else {
            System.out.println("It is impossible to make a move to this square");
            return false;
        }

        return false;
        // move
    }

    private boolean isCheckerBelongsPlayer(Checker checker) {
        if (checker.getColor().equals(CheckerColor.WHITE) && game.getWhite().equals(activeUser))
            return true;
        else if (checker.getColor().equals(CheckerColor.BLACK) && game.getBlack().equals(activeUser))
            return true;
        return false;
    }

    public Checker getChecker(String coordinates) {
        return game.getBoard().get(coordinates).getChecker();
    }

    private char getCoordX(Cell cell) {
        return cell.getCoordinates().charAt(0);
    }

    private char getCoordY(Cell cell) {
        return cell.getCoordinates().charAt(1);
    }

    public char getCoordX(String coordinates) {
        return coordinates.charAt(0);
    }

    public char getCoordY(String coordinates) {
        return coordinates.charAt(1);
    }

    private int[] getXYCoord(Cell cell) {
        int[] xy = new int[2];
        xy[0] = 'h' - cell.getCoordinates().charAt(0);    // 0..7
        xy[1] = cell.getCoordinates().charAt(1) - 1;      // 0..7
        return xy;
    }

    private int getXDifference(Cell cell1, Cell cell2) {
        return getCoordX(cell1) - getCoordX(cell2);
    }

    private int getYDifference(Cell cell1, Cell cell2) {
        return getCoordY(cell1) - getCoordY(cell2);
    }

    private boolean checkCollisionFor(int i, int j) {
        if (i > 7 || i < 0) return false;
        if (j > 7 || j < 0) return false;
        return true;
    }

    public boolean checkCollisionFor(String coordinates) {
        if (coordinates.length() > 2) return false;
        if (getCoordX(coordinates) < 'a' || getCoordX(coordinates) > 'h') return false;
        if (Integer.parseInt("" + getCoordY(coordinates)) > 8 || Integer.parseInt("" + getCoordY(coordinates)) < 1)
            return false;
        return true;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private boolean canMoveAtAll() {
        ArrayList<Checker> checkers = game.getBoard().keySet().stream()
                .map(game.getBoard()::get).map(Cell::getChecker).filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
        for (Checker checker : checkers) {
            if (moveWhite && checker.getColor().equals(CheckerColor.WHITE)) {
                if (canMoveFor(checker)) return true;
            } else if (!moveWhite && checker.getColor().equals(CheckerColor.BLACK)) {
                if (canMoveFor(checker)) return true;
            }
        }

        return false;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private boolean checkRequiredCombat() {
        ArrayList<Checker> checkers = game.getBoard().keySet().stream()
                .map(game.getBoard()::get).map(Cell::getChecker).filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
        for (Checker checker : checkers) {
            if (moveWhite && checker.getColor().equals(CheckerColor.WHITE)) {
                if (canFightFor(checker)) return true;
            } else if (!moveWhite && checker.getColor().equals(CheckerColor.BLACK)) {
                if (canFightFor(checker)) return true;
            }
        }
        return false;
    }

    private boolean canMoveFor(Checker checker) {
        String CheckerCoord = checker.getPosition().getCoordinates();

        String pos;
        if (checker.getType().equals(CheckerType.SIMPLE)) {
            pos = getCoordinatesRelative(CheckerCoord, (checker.getColor().equals(CheckerColor.WHITE)) ? 1 : -1, 1);
            if (checkCollisionFor(pos) && getChecker(pos) == null)
                return true;
            pos = getCoordinatesRelative(pos, 0, -2);
            if (checkCollisionFor(pos) && getChecker(pos) == null)
                return true;
        }
        if (checker.getType().equals(CheckerType.SUPER)) {
            pos = getCoordinatesRelative(CheckerCoord, -1, 1);
            if (checkCollisionFor(pos) && getChecker(pos) == null)
                return true;
            pos = getCoordinatesRelative(CheckerCoord, -1, -1);
            if (checkCollisionFor(pos) && getChecker(pos) == null)
                return true;

            pos = getCoordinatesRelative(CheckerCoord, 1, 1);
            if (checkCollisionFor(pos) && getChecker(pos) == null)
                return true;
            pos = getCoordinatesRelative(CheckerCoord, 1, -1);
            if (checkCollisionFor(pos) && getChecker(pos) == null)
                return true;
        }

        return false;
    }

    public boolean canFightFor(Checker checker) {
        String tCoord;

        if (checker == null) {
            System.out.println("A checker wasn't choosen!");
            return false;
        }
        if (checker.getType().equals(CheckerType.SIMPLE)) {     // checking simple checker;
            //left-up
            tCoord = getCoordinatesRelative(checker.getPosition().getCoordinates(), 2, -2);
            if (canSimpleKillOn(checker, tCoord)) return true;
            //right-down
            tCoord = getCoordinatesRelative(checker.getPosition().getCoordinates(), -2, 2);
            if (canSimpleKillOn(checker, tCoord)) return true;
            //left-down
            tCoord = getCoordinatesRelative(checker.getPosition().getCoordinates(), -2, -2);
            if (canSimpleKillOn(checker, tCoord)) return true;
            //right-up
            tCoord = getCoordinatesRelative(checker.getPosition().getCoordinates(), 2, 2);
            if (canSimpleKillOn(checker, tCoord)) return true;

            return false;
        }      // end checking simple chacker;
        if (checker.getType().equals(CheckerType.SUPER)) {
            if (canSuperKillToward(checker, 1, -1)) return true;    // left-up
            if (canSuperKillToward(checker, -1, 1)) return true;    // right-down
            if (canSuperKillToward(checker, -1, -1)) return true;   // left-down
            if (canSuperKillToward(checker, 1, 1)) return true;     // right-up
            return false;
        }      //end checking super chacker;

        System.out.println("Critical error in getting rank into checkForFight()!");
        return false;
    }

    public boolean canSimpleKillOn(Checker checker, String tCoord) {
        if (checkCollisionFor(tCoord) && getChecker(tCoord) == null) {
            Checker midChecker = getCellBetween(checker.getPosition().getCoordinates(), tCoord).getChecker();
            if (midChecker != null && checker.getColor().getOppositeColorCode().equals(midChecker.getColor().getColorCode()))
                return true;
        }
        return false;
    }

    private boolean canSuperKillToward(Checker checker, int dy, int dx) {
        String tCoord = getCoordinatesRelative(checker.getPosition().getCoordinates(), dy, dx);
        while (checkCollisionFor(tCoord)) {
            if (getChecker(tCoord) != null) {
                if (checker.getColor().equals(getChecker(tCoord).getColor())) break;
                else {
                    String tCoord2 = getCoordinatesRelative(tCoord, dy, dx);
                    if (checkCollisionFor(tCoord2) && getChecker(tCoord2) == null)
                        return true;
                    else break;
                }
            }
            tCoord = getCoordinatesRelative(tCoord, dy, dx);
        }
        return false;
    }

    private boolean canMoveTo(Cell cell) {
        if (cell.getChecker() != null) return false;
        boolean moveOpportunity;
        if (activeChecker.getType().equals(CheckerType.SIMPLE)) {
            moveOpportunity = canBasicCheckerMoveTo(cell, activeChecker.getColor());
        } else {
            moveOpportunity = canSuperCheckerMoveTo(cell, activeChecker.getColor());
        }

        if (moveOpportunity) {
            if (activeChecker.getType().equals(CheckerType.SIMPLE)) {
                if ((moveWhite && getCoordY(cell) == '8') || (!moveWhite && getCoordY(cell) == '1')) {
                    activeChecker.setType(CheckerType.SUPER);
                }
            }
            return true;
        }
        return false;
    }

    private boolean canBasicCheckerMoveTo(Cell cell, CheckerColor color) {
        if (!requiredCombat && Math.abs(getXDifference(cell, activeChecker.getPosition())) == 1) {
            if (color.equals(CheckerColor.WHITE) && getYDifference(cell, activeChecker.getPosition()) == 1 ||
                    color.equals(CheckerColor.BLACK) && getYDifference(cell, activeChecker.getPosition()) == -1) {
                lastFight = false;
                return true;
            } else return false;
        }
        if (requiredCombat && Math.abs(getYDifference(cell, activeChecker.getPosition()) *
                getXDifference(cell, activeChecker.getPosition())) == 4) {  //two-way move (lastFight)
            Cell midCell = getCellBetween(cell.getCoordinates(), activeChecker.getPosition().getCoordinates());

            if (midCell.getChecker() == null || color.equals(midCell.getChecker().getColor()))
                return false;
            else {
                game.deleteChecker(midCell, true);
                this.deleteCheckerCell = midCell;
                lastFight = true;
                return true;
            }
        }
        return false;
    }

    private boolean canSuperCheckerMoveTo(Cell cell, CheckerColor color) {
        String[] min_max = setMinMaxCoordinates(getCoordY(cell), getCoordX(cell));
        String min = min_max[0];
        String max = min_max[1];

        if (inMainDiagonally(activeChecker.getPosition(), cell)) {
            return canSuperCheckerMoveToward(color, min, max, 1);
        } else if (inAlternativeDiagonally(activeChecker.getPosition(), cell)) {
            return canSuperCheckerMoveToward(color, min, max, -1);
        }

        return false;
    }

    private boolean canSuperCheckerMoveToward(CheckerColor color, String min, String max, int dx) {
        String mid = getCoordinatesRelative(min, 1, dx);

        // check move for one cell toward
        while (getCoordY(mid) < getCoordY(max)) {
            if (getChecker(mid) != null) {
                if (color.equals(getChecker(mid).getColor())) return false;
                else {
                    if (!requiredCombat) return false;
                    if (deleteCheckerCell == null) {
                        deleteCheckerCell = game.getBoard().get(mid);
                    } else {    // if found 2 enemy checkers on diagonal
                        deleteCheckerCell = null;
                        return false;
                    }
                }
            }
            mid = getCoordinatesRelative(mid, 1, dx);
        }

        // check move by diagonal
        if (deleteCheckerCell != null) {
            lastFight = true;
            game.deleteChecker(deleteCheckerCell, true);
            return true;
        } else return  !requiredCombat;
    }

    private boolean inMainDiagonally(Cell cell1, Cell cell2) {
//        (y1 + x1) == (y2 + x2)
        int shift1 = getXYCoord(cell1)[0] + getXYCoord(cell1)[1];
        int shift2 = getXYCoord(cell2)[0] + getXYCoord(cell2)[1];
        return shift1 == shift2;
    }

    private boolean inAlternativeDiagonally(Cell cell1, Cell cell2) {
//        (y1 - x1) == (y2 - x2)
        int shift1 = getXYCoord(cell1)[0] - getXYCoord(cell1)[1];
        int shift2 = getXYCoord(cell2)[0] - getXYCoord(cell2)[1];
        return shift1 == shift2;
    }

    public String getCoordinatesRelative(String coords, int dy, int dx) {
        String relativeCoordinates = "" + (char) (coords.charAt(0) + dx) + (Integer.parseInt(String.valueOf(coords.charAt(1))) + dy);
        return relativeCoordinates;
    }

    public Cell getCellBetween(String coords1, String coords2) {
        char midX = (char) ((getCoordX(coords1) + getCoordX(coords2)) / 2); //???
        char midY = (char) ((getCoordY(coords1) + getCoordY(coords2)) / 2); //???
        String betweenCoordinates = "" + midX + midY;
        return game.getBoard().get(betweenCoordinates);
    }

    public String getCoordinatesBetween(String coords1, String coords2) {
        char midX = (char) ((getCoordX(coords1) + getCoordX(coords2)) / 2); //???
        char midY = (char) ((getCoordY(coords1) + getCoordY(coords2)) / 2); //???
        String betweenCoordinates = "" + midX + midY;
        return betweenCoordinates;
    }

    private String[] setMinMaxCoordinates(char y, char x) {
        String min, max;
        if (getCoordY(activeChecker.getPosition()) < y) {
            min = "" + getCoordX(activeChecker.getPosition()) + getCoordY(activeChecker.getPosition());
            max = "" + x + y;
        } else {
            min = "" + x + y;
            max = "" + getCoordX(activeChecker.getPosition()) + getCoordY(activeChecker.getPosition());
        }
        return new String[]{min, max};
    }

    public void surrender() {
        game.setWinner(activeUser.equals(game.getBlack()));
    }

//    public void sendMove(String move) {
//        opponentConnector.sendUsersMove(move);
//    }

//    public Move getMove() {
//        return opponentConnector.getOpponentsMove();
//    }

    public boolean isGameOver() {
        return false;
    }

    public void changePlayer() {
        moveWhite = !moveWhite;
        activeUser = (moveWhite) ? game.getWhite() : game.getBlack();
    }

    private void unselectChecker() {
        activeChecker = null;
    }

    private void onGameOver() {
    }


    public OneGame getGame() {
        return game;
    }

    public boolean isRequiredCombat() {
        return requiredCombat;
    }

    public User getActiveUser() {
        return activeUser;
    }

    public boolean isBattleOver() {
        return battleOver;
    }
}
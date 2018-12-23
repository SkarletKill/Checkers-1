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
import com.gmail.lidteam.checkers.models.OneGame;
import com.gmail.lidteam.checkers.models.User;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class GameController {
    private User activeUser;
    private OneGame game;
    private OneGameActivity gameActivity;
    private Checker activeChecker;
    private OpponentConnector opponentConnector;

    private boolean moveWhite;
    private boolean fight;  // last move was fight
    private boolean fightAction = false;
    private boolean requiredCombat = false;

    public GameController(OneGameActivity activity, OneGame game, OpponentConnector opponentConnector) {
        this.gameActivity = activity;
        this.game = game;
        this.opponentConnector = opponentConnector;
        this.activeUser = this.game.getWhite();
        this.moveWhite = true;
        // ... init
    }

    public boolean handleCellClick(AdapterView<?> parent, ImageView iv, int position, long id) {
        String coordinates = parsePosition(position);
        Cell cell = game.getBoard().get(coordinates);
        if (activeChecker == null) {
            if (handleFirstClick(cell)) {
                iv.setBackgroundResource(R.drawable.lightgreen_square_128);
            }
        } else {
            if (handleSecondClick(cell)) {
                activeChecker.getPosition().deleteChecker();
                int pos = convertCoord(activeChecker.getPosition());
                ImageView imageView = (ImageView) parent.getChildAt(pos);
                imageView.setBackgroundResource(R.drawable.black_square_128);
                imageView.setImageResource(R.drawable.empty_image);

                cell.setChecker(activeChecker);
                activeChecker.setPosition(cell);
                iv.setImageResource((moveWhite) ? R.drawable.checker_white : R.drawable.checker_black);

                activeChecker = null;   // if !requiredCombat
                return true;
            } else {
                int pos = convertCoord(activeChecker.getPosition());
                ImageView imageView = (ImageView) parent.getChildAt(pos);
                imageView.setBackgroundResource(R.drawable.black_square_128);
                unselectChecker();
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
        if ((getXYCoord(cell)[0] + getXYCoord(cell)[0]) % 2 == 0) {     // black cell
            if (cell.equals(activeChecker.getPosition())) {
                System.out.println("cancel checker selection");
                return false;
            }
            //check for rank...
            if (checkRankForMove(cell)) return true;
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

    private Checker getChecker(String coordinates) {
        return game.getBoard().get(coordinates).getChecker();
    }

    private char getCoordX(Cell cell) {
        return cell.getCoordinates().charAt(0);
    }

    private char getCoordY(Cell cell) {
        return cell.getCoordinates().charAt(1);
    }

    private char getCoordX(String coordinates) {
        return coordinates.charAt(0);
    }

    private char getCoordY(String coordinates) {
        return coordinates.charAt(1);
    }

    private int[] getXYCoord(Cell cell) {
        int[] xy = new int[2];
        xy[0] = 'h' - cell.getCoordinates().charAt(0);    // 0..7
        xy[1] = cell.getCoordinates().charAt(1) - 1;      // 0..7
        return xy;
    }

    private String parsePosition(int pos) {
        int y = 8 - pos / 8;
        int x = pos % 8;
        return String.valueOf((char) ('a' + x)) + y;
    }

    private int convertCoord(Cell cell) {
        return (cell.getCoordinates().charAt(0) - 'a') + 8 * (8 - Integer.parseInt(String.valueOf(cell.getCoordinates().charAt(1))));
    }

    private boolean checkCollisionFor(int i, int j) {
        if (i > 7 || i < 0) return false;
        if (j > 7 || j < 0) return false;
        return true;
    }

    private boolean checkCollisionFor(String coordinates) {
        if (getCoordX(coordinates) < 'a' || getCoordX(coordinates) > 'h') return false;
        if (Integer.parseInt("" + getCoordY(coordinates)) > 8 || Integer.parseInt("" + getCoordY(coordinates)) < 1)
            return false;
        return true;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private boolean canMoveAtAll(String coordinates) {
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

    // need change x, y to pos (checkForCollision too)
    private boolean canMoveFor(Checker checker) {
        int[] xy = getXYCoord(checker.getPosition());
        int x = xy[0];
        int y = xy[1];

        // ...
        int ty = 0;    //temp var for i-index
        int tx = 0;    //temp var for i-index

//        if (checker.getType() == null) {
//            System.out.println("A checker wasn't choosen!");
//            return false;
//        }
        String pos;
        if (checker.getType().equals(CheckerType.SIMPLE)) {
            ty = (checker.getColor().equals(CheckerColor.WHITE)) ? y - 1 : y + 1;
            tx = x + 1;
            pos = parsePosition(ty * 8 + tx);
            if (checkCollisionFor(ty, tx) && getChecker(pos) == null)
                return true;
            tx = x - 1;
            pos = parsePosition(ty * 8 + tx);
            if (checkCollisionFor(ty, tx) && getChecker(pos) == null)
                return true;
        }
        if (checker.getType().equals(CheckerType.SUPER)) {
            ty = y - 1;
            tx = x + 1;
            pos = parsePosition(ty * 8 + tx);
            if (checkCollisionFor(ty, tx) && getChecker(pos) == null)
                return true;
            tx = x - 1;
            pos = parsePosition(ty * 8 + tx);
            if (checkCollisionFor(ty, tx) && getChecker(pos) == null)
                return true;

            ty = y + 1;
            tx = x + 1;
            pos = parsePosition(ty * 8 + tx);
            if (checkCollisionFor(ty, tx) && getChecker(pos) == null)
                return true;
            tx = x - 1;
            pos = parsePosition(ty * 8 + tx);
            if (checkCollisionFor(ty, tx) && getChecker(pos) == null)
                return true;

        }

        return false;
        // ...
    }

    private boolean canFightFor(Checker checker) {
        int ti = 0;    //temp var for i-indexmo
        int tj = 0;    //temp var for i-index
        String tCoord;

        if (checker == null) {
            System.out.println("A checker wasn't choosen!");
            return false;
        }
        if (checker.getType().equals(CheckerType.SIMPLE)) {     //checking simple chacker;
            //left-up
            tCoord = getCoordinatesRelative(checker.getPosition().getCoordinates(), -2, -2);
            if (checkCollisionFor(tCoord) && getChecker(tCoord) == null) {
                Checker midChecker = getChecker(getCoordinatesBetween(checker.getPosition().getCoordinates(), tCoord));
                if (midChecker != null && checker.getColor().getOppositeColorCode().equals(midChecker.getColor().getColorCode())) return true;
            }
            //right-down
            tCoord = getCoordinatesRelative(checker.getPosition().getCoordinates(), 2, 2);
            if (checkCollisionFor(tCoord) && getChecker(tCoord) == null) {
                Checker midChecker = getChecker(getCoordinatesBetween(checker.getPosition().getCoordinates(), tCoord));
                if (midChecker != null && checker.getColor().getOppositeColorCode().equals(midChecker.getColor().getColorCode())) return true;
            }
            //left-down
            tCoord = getCoordinatesRelative(checker.getPosition().getCoordinates(), 2, -2);
            if (checkCollisionFor(tCoord) && getChecker(tCoord) == null) {
                Checker midChecker = getChecker(getCoordinatesBetween(checker.getPosition().getCoordinates(), tCoord));
                if (midChecker != null && checker.getColor().getOppositeColorCode().equals(midChecker.getColor().getColorCode())) return true;
            }
            //right-up
            tCoord = getCoordinatesRelative(checker.getPosition().getCoordinates(), -2, 2);
            if (checkCollisionFor(tCoord) && getChecker(tCoord) == null) {
                Checker midChecker = getChecker(getCoordinatesBetween(checker.getPosition().getCoordinates(), tCoord));
                if (midChecker != null && checker.getColor().getOppositeColorCode().equals(midChecker.getColor().getColorCode())) return true;
            }

            return false;    //it's important
        }      // end checking simple chacker;
        if (checker.getType().equals(CheckerType.SUPER)) {
            //left-up
            tCoord = getCoordinatesRelative(checker.getPosition().getCoordinates(), -1, -1);
            while (checkCollisionFor(tCoord)) {
                if (getChecker(tCoord) == null) {}
                else if (checker.getColor().equals(getChecker(tCoord).getColor())) break;
                else {
                    String tCoord2 = getCoordinatesRelative(tCoord, -1, -1);
                    if (checkCollisionFor(tCoord2)) {
                        if (getChecker(tCoord2) == null) return true;
                        else break;
                    } else break;
                }
                tCoord = getCoordinatesRelative(tCoord, -1, -1);
            }

            //right-down
            tCoord = getCoordinatesRelative(checker.getPosition().getCoordinates(), 1, 1);
            while (checkCollisionFor(tCoord)) {
                if (getChecker(tCoord) == null) {}
                else if (checker.getColor().equals(getChecker(tCoord).getColor())) break;
                else {
                    String tCoord2 = getCoordinatesRelative(tCoord, 1, 1);
                    if (checkCollisionFor(tCoord2)) {
                        if (getChecker(tCoord2) == null) return true;
                        else break;
                    } else break;
                }
                tCoord = getCoordinatesRelative(tCoord, 1, 1);
            }

            //left-down
            tCoord = getCoordinatesRelative(checker.getPosition().getCoordinates(), 1, -1);
            while (checkCollisionFor(tCoord)) {
                if (getChecker(tCoord) == null) {}
                else if (checker.getColor().equals(getChecker(tCoord).getColor())) break;
                else {
                    String tCoord2 = getCoordinatesRelative(tCoord, 1, -1);
                    if (checkCollisionFor(tCoord2)) {
                        if (getChecker(tCoord2) == null) return true;
                        else break;
                    } else break;
                }
                tCoord = getCoordinatesRelative(tCoord, 1, -1);
            }

            //right-up
            tCoord = getCoordinatesRelative(checker.getPosition().getCoordinates(), -1, 1);
            while (checkCollisionFor(tCoord)) {
                if (getChecker(tCoord) == null) {}
                else if (checker.getColor().equals(getChecker(tCoord).getColor())) break;
                else {
                    String tCoord2 = getCoordinatesRelative(tCoord, -1, 1);
                    if (checkCollisionFor(tCoord2)) {
                        if (getChecker(tCoord2) == null) return true;
                        else break;
                    } else break;
                }
                tCoord = getCoordinatesRelative(tCoord, -1, 1);
            }

            return false;
        }      //end checking super chacker;

        System.out.println("Critical error in getting rank into checkForFight()!");
        return false;
    }

    private boolean canMoveTo(Cell cell) {
        return false;
    }

    private boolean checkRankForMove(Cell cell) {
        if (activeChecker.getType().equals(CheckerType.SIMPLE)) {    // simple white or black
            if (checkPossibilityMoveTo(cell, activeChecker)) {
                if (getCoordY(cell) == '8') {
                    activeChecker.setType(CheckerType.SUPER);
                }
                return true;
            } else {
                if (requiredCombat) System.out.println("Required Combat!");
                System.out.println("It is impossible to make a move simple checker to this cell");
            }
        } else if (activeChecker.getType().equals(CheckerType.SUPER)) {   // Super checker
            if (checkPossibilityMoveTo(cell, activeChecker)) {
//                activeChecker.getPosition().deleteChecker();
//                cell.setChecker(activeChecker);
                return true;
            } else {
                if (requiredCombat) System.out.println("Required Combat!");
                System.out.println("It is impossible to make a move super checker to this cell");
            }
        } else System.out.println("Critical error in getting rank into move()!");
        return false;

    }

    private boolean checkPossibilityMoveTo(Cell cell, Checker checker) {
        if (cell.getChecker() != null)
            return false;       //This cell already has a checker
        if (checker.getType().equals(CheckerType.SIMPLE)) {      //for basic checker
            return checkPossibilityMoveForBasicCheckerTo(cell, checker.getColor());
        } else {        //for super checker
            return checkPossibilityMoveForSuperCheckerTo(cell, checker.getColor());
        }
    }

    private boolean checkPossibilityMoveForBasicCheckerTo(Cell cell, CheckerColor color) {
        if (!requiredCombat && Math.abs(getCoordX(cell) - getCoordX(activeChecker.getPosition())) == 1) {                    //one-way move diagonally
            //fightAction = false;
            if (color.equals(CheckerColor.WHITE) && (getCoordY(cell) - getCoordY(activeChecker.getPosition())) == 1 ||
                    color.equals(CheckerColor.BLACK) && (getCoordY(cell) - getCoordY(activeChecker.getPosition())) == -1)
                return true;
            else return false;
        }
        if (requiredCombat && Math.abs((getCoordY(cell) - getCoordY(activeChecker.getPosition())) *
                (getCoordX(cell) - getCoordX(activeChecker.getPosition()))) == 4) {  //two-way move (fight)
            int midY = (getCoordY(cell) + getCoordY(activeChecker.getPosition())) / 2;
            int midX = (getCoordX(cell) + getCoordX(activeChecker.getPosition())) / 2;
            String midCoords = "" + (char) midY + (char) midX;
            Cell midCell = game.getBoard().get(midCoords);
            if (midCell.getChecker() == null || color.equals(midCell.getChecker().getColor()))
                return false;
            else {
                midCell.deleteChecker();
                fight = true;
//                statistic.updateByKilling(rank);      //sel.rank                      //new element (1)
                return true;
            }
        }
        return false;
    }

    private boolean checkPossibilityMoveForSuperCheckerTo(Cell cell, CheckerColor color) {
        String[] min_max = setMinMaxCoordinates(getCoordY(cell), getCoordX(cell));
        String min = min_max[0];
        String max = min_max[1];

        String[] needDel = new String[1];      //a enemy checker which has been found

        getCoordY(activeChecker.getPosition());
        if (inMainDiagonally(activeChecker.getPosition(), cell)) {              //main diagonally
            String midCoord = getCoordinatesRelative(min, 1, -1);
            return superCheckerPossibilityMoveForMainDiagonally(color, max, midCoord, needDel);
        }

        if (inAlternativeDiagonally(activeChecker.getPosition(), cell)) {              //secondarily diagonally
            String midCoord = getCoordinatesRelative(min, 1, 1);
            return superCheckerPossibilityMoveForSecondarilyDiagonally(color, max, midCoord, needDel);
        }

        return false;
    }

    private boolean superCheckerPossibilityMoveForMainDiagonally(CheckerColor color, String max, String mid, String[] needDel) {

        while (getCoordY(mid) < getCoordY(max)) {
            if (superCheckerNoPossibilityMoveForDiagonalliesForOneCell(color, mid, needDel))
                return false;
            mid = getCoordinatesRelative(mid, 1, -1);
        }

        return checkForCleanlinessSuperCheckerForPossibilityMoveForDiagonallies(needDel);
    }

    private boolean superCheckerPossibilityMoveForSecondarilyDiagonally(CheckerColor color, String max, String mid, String[] needDel) {
        while (getCoordY(mid) < getCoordY(max)) {
            if (superCheckerNoPossibilityMoveForDiagonalliesForOneCell(color, mid, needDel))
                return false;
            mid = getCoordinatesRelative(mid, 1, 1);
        }

        return checkForCleanlinessSuperCheckerForPossibilityMoveForDiagonallies(needDel);
    }

    private boolean superCheckerNoPossibilityMoveForDiagonalliesForOneCell(CheckerColor color, String coord, String[] needDel) {
//        if (cellArr[i][j].value * rank > 0) return true;
        if (color.equals(getChecker(coord).getColor())) return true;
//        if (cellArr[i][j].value * rank < 0) {
        else {
            if (!requiredCombat) return true;
            if (needDel[0].isEmpty()) {
                needDel[0] = coord;
            } else return true;
        }
        return false;
    }

    private boolean checkForCleanlinessSuperCheckerForPossibilityMoveForDiagonallies(String[] needDel) {
        if (!needDel[0].isEmpty()) {
            fight = true;
            game.getBoard().get(needDel[0]).deleteChecker();
//            statistic.updateByKilling(sel.rank);      //sel.rank                      //new element (1)
        } else if (requiredCombat) return false;
        return true;
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

    private String getCoordinatesRelative(String coords, int dy, int dx) {
        String relativeCoordinates = "" + (coords.charAt(1) + dy) + (Integer.parseInt(String.valueOf(coords.charAt(0))) + dx);
        return relativeCoordinates;
    }

    private String getCoordinatesBetween(String coords1, String coords2) {
        char midX = (char) ((getCoordX(coords1) + getCoordX(coords2)) / 2); //???
        char midY = (char) ((getCoordY(coords1) + getCoordY(coords2)) / 2); //???
        String betweenCoordinates = "" + midX + midY;
        return betweenCoordinates;
    }

    private String[] setMinMaxCoordinates(char y, char x) {
        String min, max;
        if (getCoordY(activeChecker.getPosition()) < y) {
            min = "" + getCoordY(activeChecker.getPosition()) + getCoordX(activeChecker.getPosition());
            max = "" + y + x;
        } else {
            min = "" + y + x;
            max = "" + getCoordY(activeChecker.getPosition()) + getCoordX(activeChecker.getPosition());
        }
        return new String[]{min, max};
    }

    public void surrender() {
    }

    public void sendMove(String move) {
        opponentConnector.sendUsersMove(move);
    }

    public String getMove() {
        return opponentConnector.getOpponentsMove();
    }

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

}

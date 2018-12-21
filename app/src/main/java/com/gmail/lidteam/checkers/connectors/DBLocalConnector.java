package com.gmail.lidteam.checkers.connectors;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gmail.lidteam.checkers.models.GameForDB;

import java.util.ArrayList;

public class DBLocalConnector extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MYDB.db";
    private static final String GAMES_TABLE_NAME = "games";
    private static final String GAME_COLUMN_DATE_TIME = "dateTime";
    private static final String GAME_COLUMN_DURATION = "duration";
    private static final String GAME_COLUMN_OPPONENTS = "opponents";
    private static final String GAME_COLUMN_TYPE = "game_type";
    private static final String GAME_COLUMN_WINNER = "winner_name";
    private static final String GAME_COLUMN_WIN_CHECKERS = "checkersOfWinner";
    private static final String GAME_COLUMN_N_OF_MOVES = "numberOfMoves";
    private static final String GAME_COLUMN_MOVES = "moves";
    private static final String GAME_COLUMN_BGCOLOR = "bgColor";
    private static final String GAME_COLUMN_TEXTCOLOR = "textColor";

    public DBLocalConnector(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table games " +
                        "(dateTime text, duration text, opponents text, game_type text, " +
                        "winner_name text, checkersOfWinner text, numberOfMoves text, moves text," +
                        " bgColor int, textColor int)"
        );
    }


    @SuppressWarnings("unused")
    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ GAMES_TABLE_NAME); //delete all rows in a table
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS games");
        onCreate(sqLiteDatabase);
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, GAMES_TABLE_NAME);
    }

    public GameForDB getOneGame(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( "select * from games where rowid="+id+"", null );
        cursor.moveToFirst();
        GameForDB game = parseAGame(cursor).setId(id);
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return game;
    }

    public boolean saveGame(GameForDB game){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GAME_COLUMN_DATE_TIME, game.getGameDateTime());
        contentValues.put(GAME_COLUMN_DURATION, game.getGameDuration());
        contentValues.put(GAME_COLUMN_OPPONENTS, game.getGameOpponent());
        contentValues.put(GAME_COLUMN_TYPE, game.getGameType());
        contentValues.put(GAME_COLUMN_WINNER, game.getGameWinner());
        contentValues.put(GAME_COLUMN_WIN_CHECKERS, game.getCheckersWinnerLeft());
        contentValues.put(GAME_COLUMN_N_OF_MOVES, game.getNumberOfMoves());
        contentValues.put(GAME_COLUMN_MOVES, game.getMoves());
        contentValues.put(GAME_COLUMN_BGCOLOR, game.getBgColor());
        contentValues.put(GAME_COLUMN_TEXTCOLOR, game.getTextColor());

        db.insert(GAMES_TABLE_NAME, null, contentValues);
        return true;
    }

    public ArrayList<GameForDB> getAllGames() {
        ArrayList<GameForDB> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.query( GAMES_TABLE_NAME, new String[] { "rowid", "*" }, null, null, null, null, null );
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (res.moveToFirst()) {
            do {
                int id = res.getInt(res.getColumnIndex("rowid"));
                // получаем значения по номерам столбцов и пишем все в лог
                array_list.add(parseAGame(res).setId(id));
            } while (res.moveToNext());
        } else System.out.println("zero rows!!!");

        if (!res.isClosed()) {
            res.close();
        }

        System.out.println("ALL GAMES    \n  " + array_list);
        return array_list;
    }

    private GameForDB parseAGame(Cursor res){
        String gameDateTime = res.getString(res.getColumnIndex(GAME_COLUMN_DATE_TIME));
        String gameDuration = res.getString(res.getColumnIndex(GAME_COLUMN_DURATION));
        String gameOpponent = res.getString(res.getColumnIndex(GAME_COLUMN_OPPONENTS));
        String gameType = res.getString(res.getColumnIndex(GAME_COLUMN_TYPE));
        String gameWinner = res.getString(res.getColumnIndex(GAME_COLUMN_WINNER));
        String checkersWinnerLeft = res.getString(res.getColumnIndex(GAME_COLUMN_WIN_CHECKERS));
        String numberOfMoves = res.getString(res.getColumnIndex(GAME_COLUMN_N_OF_MOVES));
        String moves = res.getString(res.getColumnIndex(GAME_COLUMN_MOVES));
        int bgColor = res.getInt(res.getColumnIndex(GAME_COLUMN_BGCOLOR));
        int textColor = res.getInt(res.getColumnIndex(GAME_COLUMN_TEXTCOLOR));
        return new GameForDB(gameDateTime, gameDuration, gameOpponent, gameType,
                gameWinner, checkersWinnerLeft, numberOfMoves, moves, bgColor, textColor);
    }
}

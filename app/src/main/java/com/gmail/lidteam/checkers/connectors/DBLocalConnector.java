package com.gmail.lidteam.checkers.connectors;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gmail.lidteam.checkers.models.GameForDB;
import com.gmail.lidteam.checkers.models.OneGame;

import java.util.ArrayList;
import java.util.List;

public class DBLocalConnector extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MYDB.db";
    public static final String GAMES_TABLE_NAME = "games";
    public static final String GAME_COLUMN_DATE_TIME = "dateTime";
    public static final String GAME_COLUMN_DURATION = "duration";
    public static final String CONTACTS_COLUMN_OPPONENTS = "opponents";
    public static final String CONTACTS_COLUMN_TYPE = "game_type";
    public static final String CONTACTS_COLUMN_WINNER = "winner_name";
    public static final String CONTACTS_COLUMN_WIN_CHECKERS = "checkersOfWinner";
    public static final String CONTACTS_COLUMN_N_OF_MOVES = "numberOfMoves";
    public static final String CONTACTS_COLUMN_MOVES = "moves";

    public DBLocalConnector(Context context) {
        super(context, DATABASE_NAME, null, 0);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // TODO Auto-generated method stub
        sqLiteDatabase.execSQL(
                "create table games " +
                        "(dateTime text, duration text, opponents text, game_type text, " +
                        "winner_name text, checkersOfWinner text, numberOfMoves text, moves text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS games");
        onCreate(sqLiteDatabase);

    }

    public List<GameForDB> getAllGames() {
        return null;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, GAMES_TABLE_NAME);
    }

    public Cursor getOneGame(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery( "select * from games where rowid="+id+"", null );
    }

    public boolean saveGame(GameForDB game){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("dateTime", game.getGameDateTime());
        contentValues.put("duration", game.getGameDuration());
        contentValues.put("opponents", game.getGameOpponent());
        contentValues.put("game_type", game.getGameType());
        contentValues.put("winner_name", game.getGameWinner());
        contentValues.put("checkersOfWinner", game.getCheckersWinnerLeft());
        contentValues.put("numberOfMoves", game.getNumberOfMoves());
        contentValues.put("moves", game.getMoves().toString());
        db.insert("contacts", null, contentValues);
        return true;
    }

    public ArrayList<String> getAllGamesDatesString() {
        ArrayList<String> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from games", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(GAME_COLUMN_DATE_TIME)));
            res.moveToNext();
        }

        if (!res.isClosed()) {
            res.close();
        }
        return array_list;
    }


}

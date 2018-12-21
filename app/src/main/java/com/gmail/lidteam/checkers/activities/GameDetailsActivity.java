package com.gmail.lidteam.checkers.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gmail.lidteam.checkers.R;
import com.gmail.lidteam.checkers.connectors.DBLocalConnector;
import com.gmail.lidteam.checkers.models.GameForDB;
import com.gmail.lidteam.checkers.models.Move;

import java.util.ArrayList;

public class GameDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);
        DBLocalConnector localConnector = new DBLocalConnector(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int gameId = extras.getInt("id");

            if (gameId > 0) {
                GameForDB game = localConnector.getOneGame(gameId);
                System.out.println(game);
                View gameInfo = findViewById(R.id.gameInfo);

                // init the main game information (the same as at the main activity list view)
                gameInfo.getRootView().setBackgroundColor(game.getBgColor());
//                System.out.println(game.getBgColor());
//
//                System.out.println(game.getTextColor());

                int textColor = game.getTextColor();

                TextView gameDateTime = gameInfo.findViewById(R.id.gameDateTime);
                TextView gameDuration = gameInfo.findViewById(R.id.gameDuration);
                TextView gameOpponent = gameInfo.findViewById(R.id.gameOpponent);
                TextView gameType = gameInfo.findViewById(R.id.gameType);
                TextView gameWinner = gameInfo.findViewById(R.id.gameWinner);
                TextView checkersWinnerLeft = gameInfo.findViewById(R.id.checkersWinnerLeft);
                TextView numberOfMoves = gameInfo.findViewById(R.id.numberOfMoves);

                gameDateTime.setTextColor(textColor);
                gameDuration.setTextColor(textColor);
                gameOpponent.setTextColor(textColor);
                gameType.setTextColor(textColor);
                gameWinner.setTextColor(textColor);
                checkersWinnerLeft.setTextColor(textColor);
                numberOfMoves.setTextColor(textColor);

                gameDateTime.setText(game.getGameDateTime());
                gameDuration.setText(game.getGameDuration());
                gameOpponent.setText(game.getGameOpponent());
                gameType.setText(game.getGameType());
                String winnerStr = "The winner is : " + game.getGameWinner();
                gameWinner.setText(winnerStr);
                checkersWinnerLeft.setText(game.getCheckersWinnerLeft());
                numberOfMoves.setText(game.getNumberOfMoves());

                if(!parseMoves(game.getMoves()).isEmpty())  {
                    ListView movesListView = findViewById(R.id.movesListView);
                    MoveItemAdapter adapter = new MoveItemAdapter(this, parseMoves(game.getMoves()));
                    movesListView.setAdapter(adapter);
                }
            }
        }

    }

    private ArrayList<Move> parseMoves(String moves) {
        ArrayList<Move> listMoves = new ArrayList<>();
        if(moves.length() > 20) {
            String[] strRepresentations = moves.substring(moves.indexOf('[') + 1, moves.indexOf(']')).trim().split(",");
            for(String str : strRepresentations){
                listMoves.add(new Move(str));
            }
        }
       return listMoves;
    }

    class MoveItemAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        private ArrayList<Move> moves;

        MoveItemAdapter(Context context, ArrayList<Move> moves) {
            this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            this.moves = moves;
        }

        @Override
        public int getCount() {
            return moves.size();
        }

        @Override
        public Object getItem(int i) {
            return moves.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i; //moves.get(i).getId();
        }

        public ArrayList<Move> getMoves() {
            return moves;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Move move = moves.get(i);
            view = layoutInflater.inflate(R.layout.move_item_layout, null);
            view.setBackgroundColor(move.getColor());

            TextView moveText = view.findViewById(R.id.move);
            moveText.setTextColor(move.getOppositeColor());
            moveText.setText(move.getMove());
            return view;
        }
    }
}

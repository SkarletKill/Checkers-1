package com.gmail.lidteam.checkers.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
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
                //means this is the view part not the add contact part.
                Cursor rs = localConnector.getOneGame(gameId);
                rs.moveToFirst();

                //get extra id
                // ініціалізуємо усю інфу про гру, що була на головному екрані (копія із адаптера гри)
                ListView movesListView = findViewById(R.id.movesListView);
                MoveItemAdapter adapter = new MoveItemAdapter(this, new ArrayList<Move>());
                movesListView.setAdapter(adapter);
                
                if (!rs.isClosed()) {
                    rs.close();
                }

            }
        }

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
            return 1; //moves.get(i).getId();
        }

        public ArrayList<Move> getMoves() {
            return moves;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Move move = moves.get(i);
            view = layoutInflater.inflate(R.layout.move_item_layout, null);
            view.setBackgroundColor(Color.parseColor(move.getColor().getColorCode()));

            TextView moveText = view.findViewById(R.id.move);
            moveText.setTextColor(Color.parseColor(move.getColor().getOppositeColorCode()));
            moveText.setText(move.getMove());
            return view;
        }
    }

}

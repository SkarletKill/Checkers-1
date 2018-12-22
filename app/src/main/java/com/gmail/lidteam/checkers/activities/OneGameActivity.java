package com.gmail.lidteam.checkers.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.lidteam.checkers.R;
import com.gmail.lidteam.checkers.adapters.ImageAdapter;
import com.gmail.lidteam.checkers.controllers.GameController;
import com.gmail.lidteam.checkers.controllers.UserController;
import com.gmail.lidteam.checkers.models.CheckerColor;
import com.gmail.lidteam.checkers.models.CheckerType;
import com.gmail.lidteam.checkers.models.GameType;
import com.gmail.lidteam.checkers.models.OneGame;
import com.gmail.lidteam.checkers.models.PlayerColor;
import com.gmail.lidteam.checkers.models.User;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class OneGameActivity extends AppCompatActivity {
    private TextView username;
    private TextView opponent;
    private TextView timer;
    private TextView whiteCheckers;
    private TextView blackCheckers;
    private TextView gameType;
    private GridView board;
    private Button btn_ISurrender;

    private OneGame gameModel;
    private GameController gameController;
    private UserController userController;

    private User userI;
    private User userEnemy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_game);

        timer = (TextView) findViewById(R.id.timer);
        createTimer();

        username = (TextView) findViewById(R.id.username1);
        opponent = (TextView) findViewById(R.id.username2);
        gameType = (TextView) findViewById(R.id.game_type);
        whiteCheckers = (TextView) findViewById(R.id.whites);
        blackCheckers = (TextView) findViewById(R.id.blacks);

        userI = UserController.getInstance(this).getUser();

        createGame();

        board = (GridView) findViewById(R.id.gridView1);
        board.setAdapter(new ImageAdapter(this));
        board.setOnItemClickListener(gridViewOnItemClickListener);

        btn_ISurrender = (Button) findViewById(R.id.btn_surrender);
    }

    private GridView.OnItemClickListener gridViewOnItemClickListener = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {
            // выводим номер позиции
            String coordinates = parsePosition(position);
            gameType.setText(coordinates);     // need change

            // Get the current selected view as a TextView
//            ImageView iv = (ImageView) v;

            // Set the current selected item background color
//            iv.setImageResource(R.drawable.checker_black);
        }
    };

    private String fixTime(String time) {
        return (time.length() < 2) ? "0" + time : time;
    }

    private void createTimer() {
        final int[] seconds = {0};
        final int[] minutes = {0};

        //Declare the timer
        Timer t = new Timer();
        //Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        timer.setText(fixTime(String.valueOf(minutes[0])) + " : " + fixTime(String.valueOf(seconds[0])));
                        seconds[0] += 1;

                        if (seconds[0] == 60) {
                            timer.setText(fixTime(String.valueOf(minutes[0])) + " : " + fixTime(String.valueOf(seconds[0])));

                            seconds[0] = 0;
                            minutes[0] = minutes[0] + 1;
                        }
                    }

                });
            }

        }, 0, 1000);
    }

    private void createGame() {
        GameType gameType = userI.getPreferredType().equals(GameType.GIVEAWAY) ? GameType.GIVEAWAY : GameType.CLASSIC;
        PlayerColor playerColor = userI.getPreferredColor().equals(PlayerColor.BLACK) ? PlayerColor.BLACK : PlayerColor.WHITE;

        if (userI.getPreferredColor().equals(PlayerColor.ANY)) {
            int rand = new Random().nextInt(2);
            playerColor = rand % 2 == 0 ? PlayerColor.BLACK : PlayerColor.WHITE;
        }
        if (userI.getPreferredType().equals(GameType.ANY)) {
            int rand = new Random().nextInt(2);
            gameType = rand % 2 == 0 ? GameType.GIVEAWAY : GameType.CLASSIC;
        }

        username.setText(userI.getNickname());

        gameModel = new OneGame(gameType,
                playerColor.equals(PlayerColor.WHITE) ? userI : userEnemy,
                playerColor.equals(PlayerColor.WHITE) ? userEnemy : userI);
    }

    private String parsePosition(int pos){
        int y = pos / 8;
        int x = 1 + pos % 8;
        return String.valueOf((char)('h' - y)) + x;
    }

    public void onGridItemClick() {
    }

    public void onSurrenderButtonClick() {
    }

    public void repaintDesk() {
    } // ???

    public void addChecker(String coordinates, CheckerColor color, CheckerType type) {
    }

    public void deleteChecker(String coordinates) {
    }

    public void selectCell(String coordinates) {
        // change color
    }

    public void changeBackgroundColor() {
    }


}

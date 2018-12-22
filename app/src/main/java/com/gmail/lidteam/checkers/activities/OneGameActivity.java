package com.gmail.lidteam.checkers.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.gmail.lidteam.checkers.R;
import com.gmail.lidteam.checkers.adapters.ImageAdapter;
import com.gmail.lidteam.checkers.controllers.GameController;
import com.gmail.lidteam.checkers.controllers.UserController;
import com.gmail.lidteam.checkers.models.CheckerColor;
import com.gmail.lidteam.checkers.models.CheckerType;

import java.util.Timer;
import java.util.TimerTask;

public class OneGameActivity extends AppCompatActivity {
    private TextView username;
    private TextView opponent;
    private TextView timer;
    private TextView myCheckers;
    private TextView enemyCheckers;
    private TextView gameType;
    private GridView board;
    private Button btn_ISurrender;

    private GameController gameController;
    private UserController userController;

    private TextView mSelectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_game);

        timer = (TextView) findViewById(R.id.timer);
        createTimer();

//        userController = UserController.getInstance(this).getUser();

        mSelectText = (TextView) findViewById(R.id.game_type);
        board = (GridView) findViewById(R.id.gridView1);
        board.setAdapter(new ImageAdapter(this));
        board.setOnItemClickListener(gridviewOnItemClickListener);

        btn_ISurrender = (Button) findViewById(R.id.btn_surrender);
    }

    private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {
            // TODO Auto-generated method stub
            // выводим номер позиции
            mSelectText.setText(String.valueOf(position));
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

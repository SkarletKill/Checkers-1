package com.gmail.lidteam.checkers.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.gmail.lidteam.checkers.R;
import com.gmail.lidteam.checkers.controllers.GameController;
import com.gmail.lidteam.checkers.models.CheckerColor;
import com.gmail.lidteam.checkers.models.CheckerType;

import java.util.Timer;

public class OneGameActivity extends AppCompatActivity {
    private TextView username;
    private TextView opponent;
    private Timer timer;
    private TextView myCheckers;
    private TextView enemyCheckers;
    private TextView gameType;
    private GridView board;
    private Button btn_ISurrender;

    private GameController gameController;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_game);
    }

    public void onGridItemClick(){}
    public void onSurrenderButtonClick(){}

    public void repaintDesk(){} // ???
    public void addChecker(String coordinates, CheckerColor color, CheckerType type){}
    public void deleteChecker(String coordinates){}
    public void selectCell(String coordinates){
        // change color
    }
    public void changeBackgroundColor(){}

    // test github

}

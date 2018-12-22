package com.gmail.lidteam.checkers.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.gmail.lidteam.checkers.adapters.DataAdapter;
import com.gmail.lidteam.checkers.R;
import com.gmail.lidteam.checkers.adapters.ImageAdapter;
import com.gmail.lidteam.checkers.controllers.GameController;
import com.gmail.lidteam.checkers.models.CheckerColor;
import com.gmail.lidteam.checkers.models.CheckerType;

import java.util.Timer;

public class OneGameActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TextView username;
    private TextView opponent;
    private Timer timer;
    private TextView myCheckers;
    private TextView enemyCheckers;
    private TextView gameType;
    private GridView board;
    private Button btn_ISurrender;

    private GameController gameController;

    private TextView mSelectText;
    private DataAdapter mAdapter;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_game);

        mSelectText = (TextView) findViewById(R.id.game_type);
        GridView gridview = (GridView) findViewById(R.id.gridView1);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(gridviewOnItemClickListener);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position,
                               long id) {
        mSelectText.setText("Выбранный элемент: " + mAdapter.getItem(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        mSelectText.setText("Выбранный элемент: ничего");
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



}

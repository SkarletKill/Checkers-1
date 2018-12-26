package com.gmail.lidteam.checkers.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.lidteam.checkers.R;

public class UserAccountActivity extends AppCompatActivity {
    private ImageView checkersLogo;
    private TextView username;
    private TextView unbelivableWins;
    private TextView epicFails;
    private TextView draws;

    private Button btn_changeNickname;
    private Button btn_changePassword;
    private Button btn_clearStatistics;
    private Button btn_buyCoins;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void changeNickname(){}
    public void changePassword(){}
    public void clearStatistics(){}
    public void buyCoins(){}
}

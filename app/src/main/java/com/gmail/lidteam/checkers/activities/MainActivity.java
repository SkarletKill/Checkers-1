package com.gmail.lidteam.checkers.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gmail.lidteam.checkers.R;
import com.gmail.lidteam.checkers.connectors.DBLocalConnector;
import com.gmail.lidteam.checkers.connectors.SharedPreferencesConnector;
import com.gmail.lidteam.checkers.models.CheckerColor;
import com.gmail.lidteam.checkers.models.OneGame;
import com.gmail.lidteam.checkers.models.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private User user;
    private ListView history;
    private GameItemAdapter adapter;
    private DBLocalConnector dbLocalConnector = new DBLocalConnector();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        history = findViewById(R.id.history) ;
        adapter = new GameItemAdapter(this, new ArrayList<>(dbLocalConnector.getAllGames()));
        history.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_start_offline) {
            // Handle the camera action
        } else if (id == R.id.nav_start_bluetooth) {

        } else if (id == R.id.nav_start_online) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_intro) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    class GameItemAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        private ArrayList<OneGame> games;
        private int size;
        SharedPreferencesConnector sharedPreferencesConnector;

        GameItemAdapter(Context context, ArrayList<OneGame> games) {
            this.size = games.size();
            this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            this.games = games;
        }

        @Override
        public int getCount() {
            return games.size();
        }

        @Override
        public Object getItem(int i) {
            return games.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 1; //games.get(i).getId();
        }

        public ArrayList<OneGame> getGames() {
            return games;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            OneGame game = games.get(size - 1 - i);
            view = layoutInflater.inflate(R.layout.game_item_layout, null);
            CheckerColor bgcolor = game.getWinnerColor(sharedPreferencesConnector.getCurrentUser());
            view.setBackgroundColor(Color.parseColor(bgcolor.getColorCode()));
            int textColor = Color.parseColor(bgcolor.getOppositeColorCode());

            TextView gameDateTime = view.findViewById(R.id.gameDateTime);
            TextView gameDuration = view.findViewById(R.id.gameDuration);
            TextView gameOpponent = view.findViewById(R.id.gameOpponent);
            TextView gameType = view.findViewById(R.id.gameType);
            TextView gameWinner = view.findViewById(R.id.gameWinner);
            TextView checkersWinnerLeft = view.findViewById(R.id.checkersWinnerLeft);
            TextView numberOfMoves = view.findViewById(R.id.numberOfMoves);

            gameDateTime.setTextColor(textColor);
            gameDuration.setTextColor(textColor);
            gameOpponent.setTextColor(textColor);
            gameType.setTextColor(textColor);
            gameWinner.setTextColor(textColor);
            checkersWinnerLeft.setTextColor(textColor);
            numberOfMoves.setTextColor(textColor);

            // TODO нормальні написи із поясненнями
            gameDateTime.setText(game.getStartTime().toString());
            gameDuration.setText(game.getDuration());
            String opponents = game.getBlack().getNickname() + " (b) VS " +
                    game.getWhite().getNickname() + "(W)";
            gameOpponent.setText(opponents);
            gameType.setText(game.getGameType().toString());
            String winnerStr = "The winner is : " + game.getWinner().getNickname();
            gameWinner.setText(winnerStr);
            checkersWinnerLeft.setText(game.getCheckersWinnerLeft());
            numberOfMoves.setText(game.getMoves().size());

            return view;
        }

    }


    public void showErrorMessage(View view, String errorMSG){
        Snackbar.make(view, errorMSG, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}

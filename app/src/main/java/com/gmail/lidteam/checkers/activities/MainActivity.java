package com.gmail.lidteam.checkers.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gmail.lidteam.checkers.R;
import com.gmail.lidteam.checkers.connectors.DBLocalConnector;
import com.gmail.lidteam.checkers.connectors.SharedPreferencesConnector;
import com.gmail.lidteam.checkers.models.CheckerColor;
import com.gmail.lidteam.checkers.models.GameForDB;
import com.gmail.lidteam.checkers.models.OneGame;
import com.gmail.lidteam.checkers.models.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private User user;
    private ListView history;
    private GameItemAdapter adapter;
    private DBLocalConnector dbLocalConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbLocalConnector = new DBLocalConnector(this);
        DBLocalConnector localDB;


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
        if(dbLocalConnector.getAllGames()!= null){
            history = findViewById(R.id.history) ;
            adapter = new GameItemAdapter(this, new ArrayList<>(dbLocalConnector.getAllGames()));
            history.setAdapter(adapter);
            history.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    int id_To_Search = arg2 + 1;

                    Bundle dataBundle = new Bundle();
                    dataBundle.putInt("id", id_To_Search);

                    Intent intent = new Intent(getApplicationContext(),GameDetailsActivity.class);

                    intent.putExtras(dataBundle);
                    startActivity(intent);
                }
            });
        }

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
        private ArrayList<GameForDB> games;
        private int size;

        GameItemAdapter(Context context, ArrayList<GameForDB> games) {
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

        public ArrayList<GameForDB> getGames() {
            return games;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            GameForDB game = games.get(size - 1 - i);
            view = layoutInflater.inflate(R.layout.game_item_layout, null);

            view.setBackgroundColor(game.getBgColor());
            int textColor = game.getTextColor();

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
            gameDateTime.setText(game.getGameDateTime());
            gameDuration.setText(game.getGameDuration());
            gameOpponent.setText(game.getGameOpponent());
            gameType.setText(game.getGameType());
            String winnerStr = "The winner is : " + game.getGameWinner();
            gameWinner.setText(winnerStr);
            checkersWinnerLeft.setText(game.getCheckersWinnerLeft());
            numberOfMoves.setText(game.getNumberOfMoves());

            return view;
        }

    }


    public void showErrorMessage(View view, String errorMSG){
        Snackbar.make(view, errorMSG, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}

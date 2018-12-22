package com.gmail.lidteam.checkers.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gmail.lidteam.checkers.R;
import com.gmail.lidteam.checkers.connectors.DBLocalConnector;
import com.gmail.lidteam.checkers.connectors.SharedPreferencesConnector;
import com.gmail.lidteam.checkers.models.GameForDB;
import com.gmail.lidteam.checkers.models.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private User user;
    private GameItemAdapter adapter;
    private SharedPreferencesConnector sharedPreferencesConnector;
    private DBLocalConnector dbLocalConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferencesConnector = new SharedPreferencesConnector(MainActivity.this);
        staelIntroActivity();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        dbLocalConnector = new DBLocalConnector(this);
//        dbLocalConnector.deleteAll();
        if(sharedPreferencesConnector.noUserLogged() || (mAuth.getCurrentUser() == null))  {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        user = sharedPreferencesConnector.getCurrentUser();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        ArrayList<Move> moves = new ArrayList<>();
//        moves.add(new Move("one", CheckerColor.BLACK));
//        moves.add(new Move("two", CheckerColor.WHITE));
//        moves.add(new Move("three", CheckerColor.BLACK));
//        moves.add(new Move("four", CheckerColor.WHITE));
//        moves.add(new Move("five", CheckerColor.BLACK));
//        moves.add(new Move("six", CheckerColor.WHITE));
//        moves.add(new Move("seven", CheckerColor.BLACK));
//        dbLocalConnector.saveGame(new GameForDB("11/22/63", "2.13", "tiger VS lion", "normal", "lion", "5", "53", moves.toString(), Color.parseColor("#ffffff"), Color.parseColor("#000000")));
//        dbLocalConnector.saveGame(new GameForDB("01/01/2005", "8.25", "second VS first", "not normal", "first", "25", "10", moves.toString(), Color.parseColor("#000000"), Color.parseColor("#ffffff")));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setUserInfo();
        showHistory();
    }

    private void showHistory() {
        if(dbLocalConnector.getAllGames()!= null){
            ListView history = findViewById(R.id.history);
            adapter = new GameItemAdapter(this, new ArrayList<>(dbLocalConnector.getAllGames()));
            history.setAdapter(adapter);
            history.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    //check out whether it finds the right game

                    int id_To_Search = adapter.getGames().get(adapter.getCount() - 1 - arg2).getId();
                    System.out.println("System.out.println(id_To_Search);  "+id_To_Search + " actual id   " + arg2);
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
    protected void onRestart() {
        super.onRestart();
        setUserInfo();
        showHistory();
    }

    private void setUserInfo() {
        user = sharedPreferencesConnector.getCurrentUser();
        NavigationView nav = findViewById(R.id.nav_view);
        TextView userNicknameView = nav.getHeaderView(0).findViewById(R.id.user_nickname);
        TextView userEmailView = nav.getHeaderView(0).findViewById(R.id.user_email);
        userNicknameView.setText(user.getNickname());
        userEmailView.setText(user.getEmail());
    }

    private void staelIntroActivity() {
        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  If the activity has never started before...
                if (sharedPreferencesConnector.isFirstStart()) {
                    //  Launch app intro
                    Intent i = new Intent(MainActivity.this, IntroActivity.class);
                    startActivity(i);
                    sharedPreferencesConnector.setNotFirstStart();
                }
            }
        });
        // Start the thread
        t.start();
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_start_offline) {
            Intent offlineGame = new Intent(this, OneGameActivity.class);
            startActivity(offlineGame);
        } else if (id == R.id.nav_start_bluetooth) {

        } else if (id == R.id.nav_start_online) {

        } else if (id == R.id.nav_settings) {
            Intent settingsActivity = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settingsActivity);
        } else if (id == R.id.nav_intro) {
            Intent i = new Intent(MainActivity.this, IntroActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_send) {
            String OUR_MAIL_ADDRESS = "veggimail6@gmail.com";
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", OUR_MAIL_ADDRESS, null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Trigger Reminders");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        }else if (id == R.id.nav_exit) {
            sharedPreferencesConnector.unSetCurrentUser();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
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
            return  games.get(i).getId();
        }

        ArrayList<GameForDB> getGames() {
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

    @SuppressWarnings("unused")
    public void showErrorMessage(View view, String errorMSG){
        Snackbar.make(view, errorMSG, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}

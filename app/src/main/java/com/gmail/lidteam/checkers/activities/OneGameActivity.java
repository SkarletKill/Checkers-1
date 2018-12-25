package com.gmail.lidteam.checkers.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.lidteam.checkers.R;
import com.gmail.lidteam.checkers.adapters.ImageAdapter;
import com.gmail.lidteam.checkers.connectors.OfflineOpponentConnector;
import com.gmail.lidteam.checkers.connectors.OpponentConnector;
import com.gmail.lidteam.checkers.controllers.GameController;
import com.gmail.lidteam.checkers.controllers.UserController;
import com.gmail.lidteam.checkers.models.CheckerColor;
import com.gmail.lidteam.checkers.models.CheckerType;
import com.gmail.lidteam.checkers.models.GameType;
import com.gmail.lidteam.checkers.models.Move;
import com.gmail.lidteam.checkers.models.OneGame;
import com.gmail.lidteam.checkers.models.PlayerColor;
import com.gmail.lidteam.checkers.models.User;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class OneGameActivity extends AppCompatActivity {
    private TextView playerWhite;
    private TextView playerBlack;
    private TextView timer;
    private TextView whiteCheckers;
    private TextView blackCheckers;
    private TextView gameType;
    private GridView board;
    private Button btn_ISurrender;

    private OneGame gameModel;
    private GameController gameController;
    private OpponentConnector opponentConnector;

    private User userI;
    private User userEnemy;
    private boolean gameOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_game);

        timer = (TextView) findViewById(R.id.timer);
        createTimer();

        playerWhite = (TextView) findViewById(R.id.username1);
        playerBlack = (TextView) findViewById(R.id.username2);
        gameType = (TextView) findViewById(R.id.game_type);
        whiteCheckers = (TextView) findViewById(R.id.whites);
        blackCheckers = (TextView) findViewById(R.id.blacks);

        userI = UserController.getInstance(this).getUser();
        // hardcoded enemy (need to change)
        userEnemy = new User("AI@chekers.game", userI.getPreferredAiLevel().name(), 0, 0, 0);
        createGame();
        gameController = new GameController(this, gameModel);
        opponentConnector = new OfflineOpponentConnector(userEnemy, gameController);
        gameController.setOpponentConnector(opponentConnector);

        board = (GridView) findViewById(R.id.gridView1);
        board.setAdapter(new ImageAdapter(this));
        board.setOnItemClickListener(gridViewOnItemClickListener);

        btn_ISurrender = (Button) findViewById(R.id.btn_surrender);
        initSurrenderButton();
        gameOver = false;
    }

    private GridView.OnItemClickListener gridViewOnItemClickListener = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {

            ImageView iv = (ImageView) v;
            String coordinates = parsePosition(position);
            if (!gameOver && gameController.handleCellClick(parent, coordinates, id))
                gameOver = true;

            if (!gameOver && gameController.getActiveUser().equals(userEnemy)) {
                do {
                    Move move = opponentConnector.getOpponentsMove();
                    gameController.handleCellClick(parent, move.getFrom().getCoordinates(), id);
                    if (gameController.handleCellClick(parent, move.getTo().getCoordinates(), id))
                        gameOver = true;
                } while (!gameController.isBattleOver());
            }

            whiteCheckers.setText(String.valueOf(gameModel.getWhites()));
            blackCheckers.setText(String.valueOf(gameModel.getBlacks()));
            if (gameOver) {
                Toast.makeText(OneGameActivity.this, gameModel.getWinner().getNickname() + " won", Toast.LENGTH_LONG).show();
                // ... goto next Intent
                Intent intent = new Intent(OneGameActivity.this, MainActivity.class);
                startActivity(intent);
            }

        }
    };

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

    private String fixTime(String time) {
        return (time.length() < 2) ? "0" + time : time;
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

        playerWhite.setText(playerColor.equals(PlayerColor.WHITE) ? userI.getNickname() : userEnemy.getNickname());
        playerBlack.setText(playerColor.equals(PlayerColor.BLACK) ? userI.getNickname() : userEnemy.getNickname());
        this.gameType.setText(gameType.name());

        gameModel = new OneGame(gameType,
                playerColor.equals(PlayerColor.WHITE) ? userI : userEnemy,
                playerColor.equals(PlayerColor.WHITE) ? userEnemy : userI);

        whiteCheckers.setText(String.valueOf(gameModel.getWhites()));
        blackCheckers.setText(String.valueOf(gameModel.getBlacks()));
    }

    private void initSurrenderButton() {
        btn_ISurrender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!gameOver) {
                    gameController.surrender();
                    gameOver = true;
                }
            }
        });
    }

    private String parsePosition(int pos) {
        int y = 8 - pos / 8;
        int x = pos % 8;
        return String.valueOf((char) ('a' + x)) + y;
    }

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

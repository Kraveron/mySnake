package com.example.asusmr.snake;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.asusmr.snake.engine.GameEngine;
import com.example.asusmr.snake.enums.Direction;
import com.example.asusmr.snake.enums.Status;
import com.example.asusmr.snake.views.SnakeView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private GameEngine gameEngine;
    private SnakeView snakeView;
    private final Handler handler = new Handler();
    private final long delay = 200;

    private float prevX,prevY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameEngine = new GameEngine();
        gameEngine.loadGame();

        snakeView = (SnakeView)findViewById(R.id.snakeView);
        snakeView.setOnTouchListener(this);
        snakeView.setSnakeViewMap(gameEngine.getMap());

    }

    private void startUpdateHandler(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameEngine.updateGame();

                switch (gameEngine.getCurrentStatus()){

                    case Run:
                        handler.postDelayed(this, delay);
                        break;
                    case GameOver:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setMessage("Your Score is: " + gameEngine.getScore())
                                .setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        gameEngine.restartGame();
                                        snakeView.setSnakeViewMap(gameEngine.getMap());
                                    }
                                })
                                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .create();
                        dialog.setTitle("Game Over!");
                        dialog.show();
                }
                snakeView.setSnakeViewMap(gameEngine.getMap());
                snakeView.invalidate();
            }
        }, delay);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (gameEngine.getCurrentStatus() == Status.Ready) {
            gameEngine.runGame();
            startUpdateHandler();
        } else {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    prevX = event.getX();
                    prevY = event.getY();
                    break;

                case MotionEvent.ACTION_UP:
                    float newX = event.getX();
                    float newY = event.getY();

                    if (Math.abs(newX - prevX) > Math.abs(newY - prevY)) {
                        if (newX > prevX) {
                            gameEngine.updateDirection(Direction.East);
                        } else {
                            gameEngine.updateDirection(Direction.West);
                        }
                    } else {
                        if (newY > prevY) {
                            gameEngine.updateDirection(Direction.South);
                        } else {
                            gameEngine.updateDirection(Direction.North);
                        }
                    }
                    break;
            }
        }
        return true;
    }
}

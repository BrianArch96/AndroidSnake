package com.example.brian.afinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.brian.afinal.engine.GameEngine;
import com.example.brian.afinal.View.SnakeViews;
import com.example.brian.afinal.enums.Direction;
import com.example.brian.afinal.enums.GameState;


public class GameActivity extends AppCompatActivity implements View.OnTouchListener{

    private GameEngine gameEngine;
    private SnakeViews snakeView;
    private final Handler handler = new Handler();
    private int updateDelay;
    private float prevX, prevY;
    public TextView v;
    boolean isPlaying = true;
    public int score;
    private MediaPlayer sound;
    boolean SoundIsPlaying = false, VibrateisRunning = false;
    private int startSpeed = 200;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        v = (TextView) findViewById(R.id.textView5);
        sound = MediaPlayer.create(this, R.raw.itemgrab);
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            //Takes in variables from either the options menu or the play again button on the popup window

            SoundIsPlaying = extra.getBoolean("Sound");
            VibrateisRunning = extra.getBoolean("Vibrate");
            updateDelay = extra.getInt("speed");
            startSpeed = extra.getInt("speed");
            // Set the speed limit of the game so it's not unplayable
            if (updateDelay < 50){
                updateDelay = 50;
            }
        }
        else updateDelay = 200;
        // Makes a new instance of the gameEngine with the variables we took in from the other activities
        gameEngine = new GameEngine(this, sound, SoundIsPlaying, VibrateisRunning, updateDelay);
        //start
        gameEngine.initGame();
        //setup the game world
        snakeView = (SnakeViews) findViewById(R.id.snakeView);
        snakeView.setSnakeMap(gameEngine.GetMap());
        snakeView.setOnTouchListener(this);
        // get the gamespeed variable so that we can change the speed of the game
        updateDelay = gameEngine.getSpeed();
        snakeView.invalidate();
        pauseGame();
        updateHandler();

    }

    @Override
    public void onBackPressed(){
        //If the back button is pressed on the android phone, give an alert message
        handler.removeCallbacksAndMessages(null);
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Closing Activity.").
                setMessage("Are you sure you want to quit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateHandler();
            }
        }).show();
    }

    private void updateHandler(){
        //Sets the game in motion
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameEngine.update();
              if(gameEngine.getCurrentGameState() == GameState.Running){
                  handler.postDelayed(this, updateDelay);
              }

                if (gameEngine.getCurrentGameState() == GameState.Lost){
                    // if the game is finished, ie. player has lost, start the popup window activity
                    onGameLost();
                }
                snakeView.setSnakeMap(gameEngine.GetMap());
                snakeView.invalidate();
                // Update the score of the game using the score variable in the game engine
                v.setText(String.valueOf(gameEngine.getScore()));
                updateDelay = gameEngine.getSpeed();
                score = gameEngine.getScore();
            }
        }, updateDelay);
    }


    private void onGameLost(){
        // if game is lost, call popup window activity and carry the speed and the two booleans over to it.
        Intent i = new Intent("com.example.brian.afinal.PopWindow");
        i.putExtra("speed", startSpeed);
        i.putExtra("v",VibrateisRunning);
        i.putExtra("s", SoundIsPlaying);
        i.putExtra("score", score);
        startActivity(i);
    }

    public void pauseGame(){
        ImageView b = (ImageView) findViewById(R.id.imageView2);
        b.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v) {
                        if (isPlaying == true){
                            //pauses the game
                            handler.removeCallbacksAndMessages(null);
                            isPlaying = false;
                        }
                        else if(isPlaying==false){
                            updateHandler();
                            isPlaying=true;
                        }
                    }
                }
        );
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                // gets coordinate when user presses down on screen
                prevX = event.getX();
                prevY = event.getY();

                break;
            case MotionEvent.ACTION_UP:
                // gets coordinate when user releases finger from screen
                float newX = event.getX();
                float newY = event.getY();
                //checks to see where the press-down is in comparison with the lifting of the finger
                if (Math.abs(newX-prevX) > Math.abs(newY-prevY)){
                    //Left or Right
                    if (newX > prevX){
                        //LEFT
                        gameEngine.UpdateDirection(Direction.East);
                    }
                    else
                        //RIGHT
                    gameEngine.UpdateDirection(Direction.West);
                }
                else {
                    //UP DOWN
                    if (newY > prevY){
                        gameEngine.UpdateDirection(Direction.South);
                        //DOWN
                    }
                    else{
                        gameEngine.UpdateDirection(Direction.North);
                        //UP
                    }
                }


        }
        return true;
    }

}

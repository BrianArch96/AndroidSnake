package com.example.brian.afinal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.os.Vibrator;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brian.afinal.engine.GameEngine;
import com.example.brian.afinal.View.SnakeViews;
import com.example.brian.afinal.enums.Direction;
import com.example.brian.afinal.enums.GameState;

import org.w3c.dom.Text;


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
    private int startSpeed;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        v = (TextView) findViewById(R.id.textView5);
        sound = MediaPlayer.create(this, R.raw.itemgrab);
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            SoundIsPlaying = extra.getBoolean("Sound");
            VibrateisRunning = extra.getBoolean("Vibrate");
            updateDelay = extra.getInt("speed");
            startSpeed = extra.getInt("speed");
            if (updateDelay < 50){
                updateDelay = 50;
            }
        }
        else updateDelay = 120;
        gameEngine = new GameEngine(this, sound, SoundIsPlaying, VibrateisRunning, updateDelay);
        gameEngine.initGame();
        snakeView = (SnakeViews) findViewById(R.id.snakeView);
        snakeView.setSnakeMap(gameEngine.GetMap());
        snakeView.setOnTouchListener(this);
        updateDelay = gameEngine.getSpeed();
        snakeView.invalidate();
        pauseGame();
        updateHandler();

    }

    @Override
    public void onBackPressed(){
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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameEngine.update();
              if(gameEngine.getCurrentGameState() == GameState.Running){
                  handler.postDelayed(this, updateDelay);
              }

                if (gameEngine.getCurrentGameState() == GameState.Lost){
                    onGameLost();
                }
                snakeView.setSnakeMap(gameEngine.GetMap());
                snakeView.invalidate();
                v.setText(String.valueOf(gameEngine.getScore()));
                updateDelay = gameEngine.getSpeed();
                score = gameEngine.getScore();
            }
        }, updateDelay);
    }


    private void onGameLost(){
        Intent i = new Intent("com.example.brian.afinal.PopWindow");
        i.putExtra("speed", startSpeed);
        i.putExtra("v",VibrateisRunning);
        i.putExtra("s", SoundIsPlaying);
        i.putExtra("score", score);
        startActivity(i);
        updateDelay = 120;
    }

    public void pauseGame(){
        ImageView b = (ImageView) findViewById(R.id.imageView2);
        b.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v) {
                        if (isPlaying == true){
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
                prevX = event.getX();
                prevY = event.getY();

                break;
            case MotionEvent.ACTION_UP:
                float newX = event.getX();
                float newY = event.getY();
                if (Math.abs(newX-prevX) > Math.abs(newY-prevY)){
                    //Left oor Right
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

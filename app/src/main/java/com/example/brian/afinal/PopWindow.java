package com.example.brian.afinal;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.brian.afinal.Database.DatabaseHelper;
import com.example.brian.afinal.engine.GameEngine;


import org.w3c.dom.Text;

public class PopWindow extends AppCompatActivity {
    private TextView v;
    private int speed, finalScore;
    boolean SoundIsRunning, VibrateIsRunning;
    private DatabaseHelper db;
    // if player enters nothing as a username but submita their score, they will have a default username of Player
    private String SnakeID = "Player";
    private EditText name;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private MediaPlayer sound;
    float volume = (float) 0.2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pop_window);
        db = new DatabaseHelper(this);
        name = (EditText) findViewById(R.id.Username);
        sound = MediaPlayer.create(this, R.raw.button);
        sound.setVolume(volume, volume);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.8));
        Bundle extra = getIntent().getExtras();
        // sets the variables as those taken in from the gamewindow
        finalScore = extra.getInt("score");
         VibrateIsRunning = extra.getBoolean("v");
         SoundIsRunning = extra.getBoolean("s");
         speed = extra.getInt("speed");

        changeTexts();
        playAgain();
        returnToMenu();
        addData();
        TextView v = (TextView) findViewById(R.id.finalScore);
        v.setText(String.valueOf(finalScore));
    }

    public PopWindow(){

    }

    @Override
    //If the back button is pressed, give an alert window notifying the user they are about to leave the activity
    public void onBackPressed(){
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Closing Activity.").
                setMessage("You're sure you don't want to join the ranks? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PopWindow.this, MainActivity.class);
                        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        overridePendingTransition(R.animator.animation1, R.animator.animation2);

                    }
                })
                .setNegativeButton("No",null).show();
    }


    //Changes the text font
    public void changeTexts(){
        Typeface myTypeFace3 = Typeface.createFromAsset(getAssets(),"ARCADECLASSIC.TTF");
        TextView v1 = (TextView) findViewById(R.id.textView4);
        v1.setTypeface(myTypeFace3);

        Typeface myTypeFace2 = Typeface.createFromAsset(getAssets(),"ARCADECLASSIC.TTF");
        TextView v2 = (TextView) findViewById(R.id.yourscore);
        v2.setTypeface(myTypeFace2);

        Typeface myTypeFace1 = Typeface.createFromAsset(getAssets(),"ARCADECLASSIC.TTF");
        TextView v3 = (TextView) findViewById(R.id.finalScore);
        v3.setTypeface(myTypeFace1);

    }


    //If the user press the player again button, use the same speed and sound, vibrate booleans that you used before and put them into the new
    // game activity
    public void playAgain(){
        final Button b = (Button) findViewById(R.id.playAgain);
        b.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v) {
                        sound.start();
                        v.startAnimation(buttonClick);
                        Intent i = new Intent("com.example.brian.afinal.GameActivity");
                        i.putExtra("speed",speed);
                        i.putExtra("Sound", SoundIsRunning);
                        i.putExtra("Vibrate", VibrateIsRunning);
                        startActivity(i);
                        overridePendingTransition(R.animator.animation1, R.animator.animation2);
                    }
                }
        );
    }

    //Returns the player to the menu
    public void returnToMenu(){
        Button b1 = (Button) findViewById(R.id.returnMenu);
        b1.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v) {
                        sound.start();
                        v.startAnimation(buttonClick);
                        Intent intent = new Intent(PopWindow.this, MainActivity.class);
                        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        //Uses a custom transition from one window to the next, in this case a swiping motion
                        overridePendingTransition(R.animator.animation1, R.animator.animation2);


                    }
                }
        );
    }

    //If the user wishes to add his score to the game, it's added to the leaderboard database
    public void addData(){
        Button b2 = (Button) findViewById(R.id.Leaderboards);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.start();
                v.startAnimation(buttonClick);
                // if user enters a name
                String input =  name.getText().toString();
                if ((input.trim().length() != 0 )){
                    SnakeID = input;
                }
                // if user enters a score, boolean activates a toast message thanking them
              boolean isInserted =  db.insertData(SnakeID, Integer.toString(finalScore));
                if (isInserted){
                    Toast.makeText(PopWindow.this, "Thank you for submitting your score!",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

}

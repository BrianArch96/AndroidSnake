package com.example.brian.afinal;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brian.afinal.Database.DatabaseHelper;
import com.example.brian.afinal.Services.MusicService;
import com.example.brian.afinal.classes.LeaderboardInput;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Serializable  {

    DatabaseHelper db;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private MediaPlayer sound;
    private ArrayList<LeaderboardInput> inputArray = new ArrayList<LeaderboardInput>();
    float volume = (float) 0.2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeTexts();
        onClickButtonListner();
        db = new DatabaseHelper(this);
        sound = MediaPlayer.create(this, R.raw.button);
        sound.setVolume(volume,volume);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(this, MusicService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MusicService.class));
    }

    @Override
    public void onBackPressed(){
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    public void changeTexts(){
        Typeface myTypeFace = Typeface.createFromAsset(getAssets(),"strasua.ttf");
        TextView v =(TextView) findViewById(R.id.textView);
        v.setTypeface(myTypeFace);

        Typeface myTypeFace2 = Typeface.createFromAsset(getAssets(),"ARCADECLASSIC.TTF");
        TextView v1 = (TextView) findViewById(R.id.textView2);
        v1.setTypeface(myTypeFace2);
    }

    public void onClickButtonListner(){
        Button b = (Button) findViewById(R.id.button4);
        Button b1 = (Button) findViewById(R.id.button5);
        Button b2 = (Button) findViewById(R.id.button6);

        b.setOnClickListener(
             new View.OnClickListener(){
                 public void onClick(View v) {
                     sound.start();
                     v.startAnimation(buttonClick);
                     Intent i = new Intent("com.example.brian.afinal.GameActivity");
                     startActivity(i);
                     overridePendingTransition(R.animator.animation1, R.animator.animation2);
                 }
             }
        );
        b1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sound.start();
                        v.startAnimation(buttonClick);
                        Intent i = new Intent("com.example.brian.afinal.OptionActivity");
                        startActivity(i);
                        overridePendingTransition(R.animator.animation1, R.animator.animation2);
                    }
                }
        );
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.start();
                v.startAnimation(buttonClick);
               Cursor res =  db.getAllData();
                if (res.getCount() == 0){
                   // Toast.makeText(MainActivity.this, "No data to display", Toast.LENGTH_LONG).show();
                    showMessage("Error", "No data to show");
                    return;
                }
                while (res.moveToNext()) {
                    LeaderboardInput input = new LeaderboardInput();
                    input.setName(res.getString(1));
                    input.setScore(Integer.parseInt(res.getString(2)));
                    input.setRank(Integer.parseInt(res.getString(0)));


                    inputArray.add(input);
                }
               inputArray = Bubblesort(inputArray);
                String i = "";
                for (int j = 0; j < inputArray.size();j++){
                    i += "Rank:  " + inputArray.get(j).getRank() + "\n";
                    i += "Username: " + inputArray.get(j).getName() + "\n";
                    i += "Score: " + inputArray.get(j).getScore() + "\n\n";
                }
                //show all data
                showMessage("Leaderboards",i);
            }
        });
    }

    public ArrayList<LeaderboardInput> Bubblesort(ArrayList<LeaderboardInput> h){
        String tempName; int tempScore;
        for(int i = 0; i < h.size();i++){
            for(int j = 1; j < h.size(); j++){
                if (h.get(j).getScore() > h.get(j-1).getScore()){
                    tempName = h.get(j-1).getName();
                    tempScore = h.get(j-1).getScore();
                    h.get(j-1).setName(h.get(j).getName());
                    h.get(j-1).setScore(h.get(j).getScore());
                    h.get(j).setName(tempName);
                    h.get(j).setScore(tempScore);
                }
            }
        }
        return h;
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


}

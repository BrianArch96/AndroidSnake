package com.example.brian.afinal;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.content.Intent;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;

import com.example.brian.afinal.Services.MusicService;

import static android.media.MediaPlayer.*;
import static com.example.brian.afinal.R.raw.royalty;

public class OptionActivity extends AppCompatActivity {

    private SeekBar s;
    private CheckBox check1;
    private CheckBox check2;
    private Button play;
    private MediaPlayer sound;
    private boolean SoundisPlaying = false;
    private boolean VibrateIsRunning = false;
    private int speed = 120;
    private Intent i;
    private MediaPlayer mySound;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private Vibrator v;
    float volume = (float) 0.2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        sound = MediaPlayer.create(this, R.raw.button);
        sound.setVolume(volume, volume);
        mySound = MediaPlayer.create(this, R.raw.royalty);
        changeTexts();
        i = new Intent("com.example.brian.afinal.GameActivity");
        Options();
    }

    private void changeTexts(){
        Typeface myTypeFace = Typeface.createFromAsset(getAssets(),"ARCADECLASSIC.TTF");
        TextView v =(TextView) findViewById(R.id.options);
        v.setTypeface(myTypeFace);

        s = (SeekBar) findViewById(R.id.seekBar);
        s.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        s.getThumb().setColorFilter(Color.WHITE,PorterDuff.Mode.SRC_IN);

    }

    public void StartService(){
        Intent i = new Intent(this, MusicService.class);
        startService(i);
    }

    public void StopService(){
        Intent intent = new Intent(this,MusicService.class);
        stopService(intent);

    }

    private void Options(){
        check1 = (CheckBox) findViewById(R.id.checkBox);
        check2 = (CheckBox) findViewById(R.id.checkBox1);
        play = (Button) findViewById(R.id.play);

        s.setMax(120);
        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speed = 160 - progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        check1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check1.isChecked()){
                    StartService();
                    SoundisPlaying = true;
                    i.putExtra("Sound",SoundisPlaying);
                }
                else if (!check1.isChecked()){
                    SoundisPlaying = false;
                    i.putExtra("Sound",SoundisPlaying);
                    StopService();
                }

            }
        });

        check2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check2.isChecked()){
                    v.vibrate(400);
                    VibrateIsRunning = true;
                    i.putExtra("Vibrate",VibrateIsRunning);
                }
                else if (!check1.isChecked()) i.putExtra("Vibrate",VibrateIsRunning);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.start();
                v.startAnimation(buttonClick);
                i.putExtra("speed",speed);
                startActivity(i);
                overridePendingTransition(R.animator.animation1, R.animator.animation2);
            }
        });
    }
}

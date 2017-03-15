package com.example.brian.afinal.Services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.brian.afinal.R;

import static com.example.brian.afinal.R.raw.button;
import static com.example.brian.afinal.R.raw.royalty;

/**
 * Created by Brian on 13/03/2017.
 */

public class MusicService extends Service {


   private MediaPlayer mySound;
    @Override
    public void onCreate() {
        super.onCreate();
        mySound = MediaPlayer.create(this, R.raw.royalty);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"Sound enabled", Toast.LENGTH_SHORT).show();
        mySound.start();
        mySound.setLooping(true);
        float volume = (float) 0.1;
        mySound.setVolume(volume,volume);
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
       Toast.makeText(this, "Sound disabled", Toast.LENGTH_SHORT).show();
        mySound.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

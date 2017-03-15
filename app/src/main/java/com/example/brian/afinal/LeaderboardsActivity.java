package com.example.brian.afinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.brian.afinal.Database.DatabaseHelper;

public class LeaderboardsActivity extends AppCompatActivity {
    DatabaseHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboards);
        myDB = new DatabaseHelper(this);
    }
}

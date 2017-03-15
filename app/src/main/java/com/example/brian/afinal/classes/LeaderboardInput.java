package com.example.brian.afinal.classes;

/**
 * Created by Brian on 09/03/2017.
 */

public class LeaderboardInput {
    private String name;
    private int score;
    private int rank;

    public LeaderboardInput(){

    }
    
    public LeaderboardInput(String name, int score, int rank){
        this.name = name;
        this.score = score;
        this.rank = rank;
    }

    public int getRank(){
        return rank;
    }

    public void setRank(int rank){
        this.rank = rank;
    }



    public String getName(){
        return name;
    }

    public int getScore(){
        return score;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setScore(int score){
        this.score = score;
    }

}

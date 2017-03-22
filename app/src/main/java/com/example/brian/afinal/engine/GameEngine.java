package com.example.brian.afinal.engine;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;
import com.example.brian.afinal.classes.Coordinate;
import com.example.brian.afinal.enums.GameState;
import com.example.brian.afinal.enums.TileType;
import com.example.brian.afinal.enums.Direction;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brian on 25/02/2017.
 */

public class GameEngine {
    public static final int gameWidth =28;
    public static final int gameHeight =42;
    public static final int actualWidth = 27;
    public static final int actualHeight = 41;
    private List<Coordinate> walls = new ArrayList<>();
    private List<Coordinate> snake = new ArrayList<>();
    private Direction currentDirection = Direction.East;
    private GameState currentGameState = GameState.Running;
    private List<Coordinate> apple = new ArrayList<>();
    private int score = 0;
    private Vibrator v;
    private MediaPlayer sound;
    private int speed = 210;
    boolean SoundIsPlaying, VibrateIsRunning;

    public GameEngine(Context context, MediaPlayer sound, boolean S, boolean V, int changedSpeed) {
        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        this.sound = sound;
        SoundIsPlaying = S;
        VibrateIsRunning = V;
        speed = changedSpeed;
    }



    private void Vibrations(){
        v.vibrate(400);
    }

    public int getScore(){
        return score;
    }

    public int getSpeed(){
        return speed;
    }

    public void update(){
        // Check the Snake
        switch(currentDirection){
            case North:
                updateSnake(0,-1);
                break;
            case East:
                updateSnake(1,0);
                break;
            case South:
                updateSnake(0,1);
                break;
            case West:
                updateSnake(-1,0);
                break;
        }
        //Check wall collision
        for (Coordinate w : walls){
            if (snake.get(0).equals(w)){
                currentGameState = GameState.Lost;
            }
        }

        for(Coordinate w: walls){
            if(apple.get(0).equals(w)){
                AddApple();
            }
        }

        for(int i = 1; i < snake.size();i++){
            if(snake.get(0).equals(snake.get(i))){
               currentGameState = GameState.Lost;
            }
        }

        for (Coordinate a: apple){
            if (snake.get(0).equals(a)){
                if (SoundIsPlaying){
                    sound.setVolume((float)0.2,(float)0.2);
                    sound.start();
                }
                AddApple();
                score = score +1;
                speed = speed -2;
                if (speed < 10) speed = 10;
                if (VibrateIsRunning){
                    Vibrations();
                }
                if(currentDirection ==Direction.South){
                    snake.add(new Coordinate(((snake.get(snake.size()-1).getX())),snake.get(snake.size()-1).getY()+1));
                }
                else if(currentDirection == Direction.North){
                    snake.add(new Coordinate(((snake.get(snake.size()-1).getX())),snake.get(snake.size()-1).getY()+1));
                }
                else if (currentDirection == Direction.East){
                    snake.add(new Coordinate(((snake.get(snake.size()-1).getX())),snake.get(snake.size()-1).getY()+1));
                }
                else if (currentDirection == Direction.West){
                    snake.add(new Coordinate(((snake.get(snake.size()-1).getX())),snake.get(snake.size()-1).getY()+1));
                }
            }
        }
    }

    private void updateSnake(int x, int y){
        for (int i = snake.size()-1; i >0; i--){
            snake.get(i).setX(snake.get(i-1).getX());
            snake.get(i).setY(snake.get(i-1).getY());
        }
        snake.get(0).setX(snake.get(0).getX() + x);
        snake.get(0).setY(snake.get(0).getY() + y);

    }

    public void initGame(){
        AddSnake();
        AddWalls();
        AddApple();
    }

    public void AddApple(){
        apple.clear();
        apple.add(new Coordinate((int)(Math.random() * actualWidth-1), (int)(Math.random() * actualHeight-1)));
    }

    private void AddSnake() {
        snake.clear();
        snake.add(new Coordinate(10, 35));
        snake.add(new Coordinate(9, 35));
        snake.add(new Coordinate(8, 35));
        snake.add(new Coordinate(7, 35));
        snake.add(new Coordinate(6, 35));
    }

    private void AddWalls(){
        for (int x = 0; x < gameWidth;x++){
            //Making the top wall / Making the bottom wall
            walls.add(new Coordinate(x,0));
            walls.add(new Coordinate(x, gameHeight-1));
        }

        for (int y = 1; y < gameHeight; y++ ){
            //Making left and right walls
            // at 0,0 walls have already been made, so make starting point = 1;
            walls.add( new Coordinate(0,y));
            walls.add(new Coordinate(gameWidth-1, y));
        }

    }

    public void UpdateDirection(Direction newDirection){
        if(Math.abs(newDirection.ordinal()-currentDirection.ordinal()) % 2 ==1){
            currentDirection = newDirection;
        }
    }
    public TileType[][] GetMap(){
    TileType[][] map = new TileType[gameWidth][gameHeight];

        for (int x = 0;x < gameWidth;x++){
           for (int y = 0; y < gameHeight;y++){
                map[x][y] = TileType.Nothing;
            }
        }

        for (Coordinate wall: walls )
            map[wall.getX()][wall.getY()] = TileType.Wall;

        for (Coordinate s : snake){
            map[s.getX()][s.getY()] = TileType.SnakeTail;
        }

        for (Coordinate a: apple){
            map[a.getX()][a.getY()] = TileType.Apple;
        }

       map[snake.get(0).getX()][snake.get(0).getY()] = TileType.SnakeHead;
        return map;
    }

    public GameState getCurrentGameState(){
        return currentGameState;
    }

}

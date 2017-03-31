package com.example.brian.afinal.classes;

/**
 * Created by Brian on 25/02/2017.
 */

public class Coordinate {

    private int x, y;
//Makes an object Coordinate made up of an x and y position
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Coordinate that = (Coordinate) obj;
        if (getX() != that.getX()) return false;
        return getY() == that.getY();
    }
}

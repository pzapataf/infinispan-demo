package com.infinispan.demo;

import java.io.Serializable;

/**
 * Coordinates for a map
 */
public class MapCoordinates implements Serializable {
    private int x;
    private int y;

    public MapCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public MapCoordinates() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapCoordinates coords = (MapCoordinates) o;

        if (x != coords.x) return false;
        if (y != coords.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "MapCoordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

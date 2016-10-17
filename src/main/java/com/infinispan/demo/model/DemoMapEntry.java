package com.infinispan.demo.model;

import java.io.Serializable;

/**
 * Grid map entry.
 */
public class DemoMapEntry implements Serializable {
    private int x;
    private int y;
    private int z;

    public DemoMapEntry(int x, int y, int h) {
        this.x = x;
        this.y = y;
        this.z = h;
    }

    public static String keyOf(int x, int y) {
        return x + "_" + y;
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

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "DemoMapEntry{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}

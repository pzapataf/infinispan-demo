package com.infinispan.demo.model;

import java.io.Serializable;

/**
 * Grid map entry.
 */
public class DemoMapEntry implements Serializable {
    private int id;
    private int x;
    private int y;
    private int z;

    public DemoMapEntry(int id, int x, int y, int h) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = h;
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

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return toJSON();
    }

    public String toJSON() {
        return "{ " +
                "\"id\":" + id + ", " +
                "\"x\":" + x + ", " +
                "\"y\":" + y + ", " +
                "\"z\":" + z + "}";
    }
}

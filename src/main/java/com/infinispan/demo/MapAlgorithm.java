package com.infinispan.demo;

import com.sun.prism.image.Coords;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;

import java.io.IOException;
import java.util.*;

/**
 * Created by pzapataf on 6/2/16.
 */
public class MapAlgorithm {

    private RemoteGridClient client;
    private int width;
    private int height;
    private Set<MapCoordinates> allKeys = new HashSet<>();

    public MapAlgorithm(RemoteGridClient client, int width, int height) {
        this.client = client;
        this.width = width;
        this.height = height;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                allKeys.add(new MapCoordinates(x,y));
            }
        }
    }

    protected void put(int x, int y, int value) {
        RemoteCache<MapCoordinates, Integer> cache = client.getCache();
        cache.put(
                new MapCoordinates(x, y),
                value
        );
    }

    protected Set<MapCoordinates> getKeysSubset() {
        return allKeys;
    }

    RemoteCache<MapCoordinates, Integer> getCache() {
        return client.getCache();
    }

    public void initMap() throws IOException {
        System.out.println("Initializing height map of width: " +width + "and height: " + height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                put(x, y, 0);
            }
            put(y, y, 100);

            System.out.println("... Line " + y);
        }

        Random r = new Random();

        System.out.println("... Adding random noise ");
//        // Add some random noise
//        for( int i = 0; i < 1000; i++) {
//            cache.put(
//                    new MapCoordinates(r.nextInt(width),r.nextInt(height)),
//                    r.nextInt(255)
//            );
//        }
    }

    private int get(Map<MapCoordinates, Integer> data, int x, int y) {

        Integer value = data.get(new MapCoordinates(x,y));
        return value == null ? 0 : value.intValue();
    }

    public void runAlgorithm() throws IOException {

        System.out.println("Running algorithm");
        Set<MapCoordinates> keys = getKeysSubset();

        Map<MapCoordinates, Integer> existingData= getCache().getAll(keys);

        Map<MapCoordinates, Integer> newData= new HashMap<>();

        System.out.println("Moving up");

        // Move up
        for (MapCoordinates c : existingData.keySet()) {
            newData.put(
                    new MapCoordinates(c.getX(),c.getY()-1),
                    existingData.get(c));
        }

        System.out.println("Data average");
        // Average data
        for (MapCoordinates c : newData.keySet()) {
            int x = c.getX();
            int y = c.getY();
            Integer intensity =
                        (get(newData, x-1,y+1) +
                                get(newData, x,y+1) +
                                get(newData, x+1,y+1) +
                                get(newData, x - 1, y) +
                                get(newData, x, y) +
                                get(newData, x + 1, y) +
                                get(newData, x - 1, y - 1) +
                                get(newData, x, y - 1) +
                                get(newData, x+1,y-1)) / 9;

                //cache.putAsync(new Coords(x, y), intensity);
                newData.put(new MapCoordinates(x,y), intensity);
            }

        // Update cache
        System.out.println("Updating cache....");
        getCache().putAllAsync(newData);
    }


    public Map<MapCoordinates, Integer> readHeightMap() {
        return client.getCache().getAll(allKeys);
    }
}


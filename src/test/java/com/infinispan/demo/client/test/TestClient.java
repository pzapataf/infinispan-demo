package com.infinispan.demo.client.test;

import com.infinispan.demo.GlobalConfig;
import com.infinispan.demo.model.DemoMapEntry;
import org.infinispan.client.hotrod.RemoteCache;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.naming.ConfigurationException;
import java.io.IOException;
import java.util.Random;

/**
 * Tests remote client
 */
public class TestClient {

    @Before
    public void init() throws IOException, ConfigurationException {

    }

    @After
    public void close() {
        GlobalConfig.close();
    }

    private int getHeight(int x, int y, int maxx, int maxy, int maxheight) {
        return (int) (1.0 * (maxheight * Math.sin((double) x/maxx) * Math.cos((double) y/maxy) * maxheight + maxheight));
    }

    @Test
    public void testFillCache() throws Exception {

        RemoteCache<String, DemoMapEntry> cache = GlobalConfig.getClient().getCache();

        cache.clear();

        int maxY = 20;
        int maxX  = 20;
        int maxZ  = 20;

        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                cache.put(
                        DemoMapEntry.keyOf(x, y),
                        new DemoMapEntry(x, y, getHeight(x, y, maxX, maxY, maxZ))
                );
            }
        }
    }
}

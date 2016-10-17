package com.infinispan.demo.endpoints;

import com.infinispan.demo.GlobalConfig;
import com.infinispan.demo.model.DemoMapEntry;
import org.infinispan.client.hotrod.RemoteCache;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.Random;

@Path("/rest/generate-map")
public class RESTGenerateRandomMap {
    private Random r = new Random();

    private int getHeight(int x, int y, int maxx, int maxy, int maxheight, DemoMapEntry existing) {

        if (existing != null) {
            return existing.getZ() + 5;
        } else {
            double h = maxheight * Math.sin((double) x / maxx) * Math.cos((double) y / maxy) * maxheight + maxheight;
            return (int) (h);
        }
    }

    @GET
    @Produces("application/json")
    public void get(
            @QueryParam("maxx") int maxX,
            @QueryParam("maxy") int maxY,
            @QueryParam("maxz") int maxZ
    ) throws Exception {

        RemoteCache<Integer, DemoMapEntry> cache = GlobalConfig.getClient().getCache();

        int n = 0;
        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++, n++) {
                DemoMapEntry existingValue = cache.get(n);
                cache.put(
                        n,
                        new DemoMapEntry(n, x, y,
                                getHeight(x, y, maxX, maxY, maxZ, existingValue))
                );

                Thread.sleep(500L);
            }
        }
    }
}
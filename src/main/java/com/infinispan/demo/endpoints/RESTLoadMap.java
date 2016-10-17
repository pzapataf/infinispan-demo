package com.infinispan.demo.endpoints;

import com.infinispan.demo.GlobalConfig;
import com.infinispan.demo.model.DemoMapEntry;
import org.infinispan.client.hotrod.RemoteCache;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;

@Path("/rest/load-map")
public class RESTLoadMap {

    @GET
    @Produces("application/json")
    public List<DemoMapEntry> get() throws Exception {

        RemoteCache<Integer, DemoMapEntry> cache = GlobalConfig.getClient().getCache();

        // Start with a trivial implementation

        List<DemoMapEntry> entries = new ArrayList<>();

        cache.retrieveEntries(null, 1000).forEachRemaining(
                e -> entries.add((DemoMapEntry) e.getValue())
        );

        return entries;
    }
}
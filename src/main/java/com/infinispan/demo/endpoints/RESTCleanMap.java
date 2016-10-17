package com.infinispan.demo.endpoints;

import com.infinispan.demo.GlobalConfig;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/rest/clean-map")
public class RESTCleanMap {

    @GET
    @Produces("application/json")
    public boolean get() throws Exception {

        GlobalConfig.getClient().getCache().clear();

        return true;
    }
}
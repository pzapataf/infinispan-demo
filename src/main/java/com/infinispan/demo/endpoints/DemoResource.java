package com.infinispan.demo.endpoints;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/demo")
public class DemoResource {

    @GET
    @Produces("text/html")
    public java.io.InputStream get() {
        return getClass().getClassLoader().getResourceAsStream("demo1.html");
    }
}

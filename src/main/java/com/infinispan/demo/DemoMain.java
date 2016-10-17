package com.infinispan.demo;

import com.infinispan.demo.client.RemoteGridClient;
import com.infinispan.demo.endpoints.*;
import com.infinispan.demo.model.CRUDOperation;
import com.infinispan.demo.model.DemoMapEntry;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

public class DemoMain {

    public static void main(String[] args) throws Exception {


        Swarm swarm = new Swarm();

        //swarm.fraction(LoggingFraction.createDebugLoggingFraction());

        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class, "infinispan-demo.war");
        deployment.addClass(RESTLoadMap.class);
        deployment.addClass(RESTCleanMap.class);
        deployment.addClass(RESTGenerateRandomMap.class);

        deployment.addClass(DemoResource.class);
        deployment.addClass(RemoteGridClient.class);
        deployment.addClass(DemoMapEntry.class);
        deployment.addClass(CRUDOperation.class);
        deployment.addClass(DemoMain.class);
        deployment.addClass(DemoDefaults.class);
        deployment.addClass(GlobalConfig.class);
        deployment.addClass(WebSocketServer.class);
        deployment.addAsResource("demo1.html");
        deployment.addAllDependencies();

        System.setProperty("swarm.bind.address", DemoDefaults.HTTP_BIND_SERVER);
        System.setProperty("swarm.http.port", String.valueOf(DemoDefaults.HTTP_PORT));

        swarm.start().deploy(deployment);
    }
}
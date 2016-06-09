package com.infinispan.demo;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

import javax.naming.ConfigurationException;
import java.io.IOException;

/**
 * Simple client to data grid
 */
public class RemoteGridClient {
    private String server;
    private String cacheName;
    private int port;


    private RemoteCacheManager remoteCacheManager = null;


    public RemoteGridClient(String server, String cacheName, int port) throws IOException, ConfigurationException {
        this.server = server;
        this.cacheName = cacheName;
        this.port = port;

        initRemoteCacheManager();

        if(getCache() == null) {
            System.out.println("Please, create a cache named '" + cacheName+ "' first.");
            throw new ConfigurationException("Cache " + cacheName + " not found!");
        }
     }


    private void initRemoteCacheManager() throws IOException {
        System.out.println("*********************************************************");
        System.out.println("** ISPN HotRod client. Connecting to " + server + " : " + port);
        System.out.println("*********************************************************");
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addServer()
                .host(server)
                .port(port);

        this.remoteCacheManager = new RemoteCacheManager(builder.build());
    }


    public void close() {
        if( remoteCacheManager != null ) {
            getRemoteCacheManager().stop();
            remoteCacheManager = null;
        }
    }

    public RemoteCacheManager getRemoteCacheManager() {
        return remoteCacheManager;
    }

    public RemoteCache<MapCoordinates, Integer> getCache() {
        return getRemoteCacheManager().getCache(cacheName);
    }
}

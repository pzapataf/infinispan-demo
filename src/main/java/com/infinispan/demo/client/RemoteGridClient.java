package com.infinispan.demo.client;

import com.infinispan.demo.endpoints.WebSocketServer;
import com.infinispan.demo.model.DemoMapEntry;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryCreated;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryModified;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryRemoved;
import org.infinispan.client.hotrod.annotation.ClientListener;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.event.*;

import javax.naming.ConfigurationException;
import java.io.IOException;

/**
 * Simple client to data grid
 */
@ClientListener
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

        if (getCache() == null) {
            System.out.println("Please, create a cache named '" + cacheName + "' first.");
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

        // Register this client as remote cache listener
        getCache().addClientListener(this);
    }


    public void close() {
        if (remoteCacheManager != null) {
            getRemoteCacheManager().stop();
            remoteCacheManager = null;
        }
    }

    @ClientCacheEntryCreated
    @ClientCacheEntryModified
    @ClientCacheEntryRemoved
    public void handleRemoteEvent(ClientEvent event) {

        Integer key = null;

        switch (event.getType()) {
            case CLIENT_CACHE_ENTRY_CREATED:
                key = (Integer) ((ClientCacheEntryCreatedEvent) event).getKey();
                notifyChange(key, getCache().get(key));
                break;
            case CLIENT_CACHE_ENTRY_EXPIRED:
                key = (Integer) ((ClientCacheEntryExpiredEvent) event).getKey();
                notifyRemoval(key);
                break;
            case CLIENT_CACHE_ENTRY_MODIFIED:
                key = (Integer) ((ClientCacheEntryModifiedEvent) event).getKey();
                notifyChange(key, getCache().get(key));
                break;
            case CLIENT_CACHE_ENTRY_REMOVED:
                key = (Integer) ((ClientCacheEntryRemovedEvent) event).getKey();
                notifyRemoval(key);
                break;
        }
    }


    protected void notifyChange(Integer key, DemoMapEntry v) {
        WebSocketServer.notifyUpdate(key, v);
    }

    protected void notifyRemoval(Integer key) {
        WebSocketServer.notifyRemoval(key);
    }

    public RemoteCacheManager getRemoteCacheManager() {
        return remoteCacheManager;
    }

    public RemoteCache<Integer, DemoMapEntry> getCache() {
        return getRemoteCacheManager().getCache(cacheName);
    }
}

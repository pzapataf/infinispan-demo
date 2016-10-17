package com.infinispan.demo;

import com.infinispan.demo.client.RemoteGridClient;

import javax.naming.ConfigurationException;
import java.io.IOException;

public class GlobalConfig {
    private static RemoteGridClient client;

    private static void init() throws IOException, ConfigurationException {
        client = new RemoteGridClient(
                DemoDefaults.INFINISPAN_SERVER,
                DemoDefaults.CACHE_NAME,
                DemoDefaults.HOTROD_PORT
        );
    }

    public static RemoteGridClient getClient() throws Exception {
        if( client == null ) {
            init();
        }
        return client;
    }

    public static void close() {
        if( client != null ) {
            client.close();
            client = null;
        }
    }
}

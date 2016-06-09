package com.infinispan.demo;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

import javax.naming.ConfigurationException;
import java.io.*;
import java.util.Map;

public class InfinispanDemoServer {

    public static void main(final String[] args) throws IOException, ConfigurationException {
        // TODO: Parse parameters
        final HttpHandler staticHandler = Handlers.resource(new ClassPathResourceManager(InfinispanDemoServer.class.getClassLoader()));

        int httpPort = 8081;
        String httpBindServer = "localhost";
        String datagridServer = "127.0.0.1";
        String cacheName = "DEMO_HEIGHTMAP";
        int hotRodPort = 11222;

        RemoteGridClient remoteGridClient = new RemoteGridClient(datagridServer, cacheName, hotRodPort );

        MapAlgorithm mapAlgorithm = new MapAlgorithm(
                remoteGridClient,
                100,
                100
        );

        JsonFactory jsonFactory = new JsonFactory();

        mapAlgorithm.initMap();

        start(mapAlgorithm);

        System.out.println("HTTP Server Listening...");
        System.out.println("   Grid Viewer: http://"+httpBindServer+":"+httpPort+ "/grid.html");

        Undertow server = Undertow.builder()
                .addHttpListener(httpPort, httpBindServer)
                .setHandler(
                        exchange -> {
                            if (exchange.getRequestURI().startsWith("/rest/run-step")) {

                                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/json");
                                exchange.getResponseSender().send("{result:true}");
                            } else if (exchange.getRequestURI().startsWith("/rest/grid-status")) {
                                Map<MapCoordinates, Integer> map = mapAlgorithm.readHeightMap();
                                //exchange.startBlocking();
                                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/json");
                                exchange.getResponseSender().send(
                                        writeMap(map, jsonFactory)
                                );
                                exchange.setResponseCode(StatusCodes.OK);
                                exchange.endExchange();
                            } else {
                                // Handle request as static
                                staticHandler.handleRequest(exchange);
                            }
                        }
                )
                .build();

        server.start();
    }

    private static void start(MapAlgorithm algo) {
        new Thread(
                () -> {
                    while (true) {
                        try {
                            algo.runAlgorithm();
                            Thread.currentThread().sleep(2000);
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();
    }

    private static String writeMap(Map<MapCoordinates, Integer> map, JsonFactory jsonFactory) throws IOException
    {
        StringWriter writer = new StringWriter();

        JsonGenerator jg = jsonFactory.createGenerator(writer);

        jg.writeStartArray();
        for (MapCoordinates mapCoord : map.keySet()) {
            int height = map.get(mapCoord);
            if( height > 0) {
                jg.writeStartObject();
                jg.writeNumberField("x", mapCoord.getX());
                jg.writeNumberField("y", mapCoord.getY());
                jg.writeNumberField("h", height);
                jg.writeEndObject();
               // System.out.println(mapCoord.getX() + "," + mapCoord.getY() + "=" + height);
            }
        }
        jg.writeEndArray();
        jg.close();

        System.out.println("WRITER:" + writer.toString());

        return writer.toString();
    }
}
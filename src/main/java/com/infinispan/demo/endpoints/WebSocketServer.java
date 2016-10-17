package com.infinispan.demo.endpoints;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/grid-events")
public class WebSocketServer {

    @OnOpen
    public void onOpen(Session session) {
        System.out.printf("New connection with client: {0}\n", session.getId());
    }

    @OnMessage
    public String onMessage(String message, Session session) {
            System.out.printf("New message from Client [{0}]: {1}",
                  new Object[]{session.getId(), message});

        for (Session s : session.getOpenSessions()) {
            if (s.isOpen()) {
                try {
                    s.getBasicRemote().sendObject("UPDATE STUFF");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (EncodeException e) {
                    e.printStackTrace();
                }
            }
        }
        return "Server received [" + message + "]";
    }

    @OnClose
    public void onClose(Session session) {
        System.out.printf("Close connection for client: {0}",
                 session.getId());
    }

    @OnError
    public void onError(Throwable exception, Session session) {
        System.out.printf("Error for client: {0}", session.getId());
    }
}

package com.infinispan.demo.endpoints;

import com.infinispan.demo.model.CRUDOperation;
import com.infinispan.demo.model.DemoMapEntry;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint(value = "/grid-events")
public class WebSocketServer {

    private static Map<String, Session> openSessions = new HashMap<>();

    public static void notifyUpdate(Integer key, DemoMapEntry demoMapEntry) {
        openSessions.values().forEach(
                (s) -> {
                    if (s.isOpen()) {
                        try {
                            s.getBasicRemote().sendObject(
                                    new CRUDOperation(CRUDOperation.TYPE.UPDATE, key, demoMapEntry).toJSON()
                            );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

   /* @OnMessage
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
    }*/

    public static void notifyRemoval(Integer key) {
        openSessions.values().forEach(
                (s) -> {
                    if (s.isOpen()) {
                        try {
                            s.getBasicRemote().sendObject(
                                    new CRUDOperation(CRUDOperation.TYPE.DELETE, key).toJSON()
                            );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    @OnOpen
    public void onOpen(Session session) {

        openSessions.put(session.getId(), session);

        System.out.println("New connection with client:" + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        openSessions.remove(session.getId());
        System.out.println("Session closed : " + session.getId());
    }

    @OnError
    public void onError(Throwable exception, Session session) {
        System.out.println("Error for session " + session.getId());
        exception.printStackTrace();
    }
}

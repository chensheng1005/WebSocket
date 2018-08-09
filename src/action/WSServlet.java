package action;

import jdk.nashorn.internal.objects.annotations.Optimistic;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.*;
import java.io.IOException;

@ServerEndpoint("/ws/{user}")
public class WSServlet {
    private String currentUser;
    private static Set<Session> sessions = new HashSet<>();
    @OnOpen
    public void OnOpen(@PathParam("user") String user,Session session){
        currentUser = user;
        //System.out.println("Connect..."+session.getId());
        sessions.add(session);
        System.out.println(user);
    }
    @OnMessage
    public String OnMessage(String message,Session session)throws IOException{
        System.out.println(currentUser+""+message);
        //return currentUser+":"+message;
        for (Session s : sessions) {
            if (s.isOpen()) {
                s.getBasicRemote().sendText(currentUser+"说:"+message);
            }
        }
        return null;
    }
    @OnClose
    public void OnClose(Session session,CloseReason closeReason){
        System.out.println(String.format("Session %s closed because %s",session.getId(),closeReason));
    }
    @OnError
    public void OnError(Throwable t){
        t.printStackTrace();
    }
}

package server;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/hello")
public class Test {
	
	@OnMessage
    public String hello(String message) {
        System.out.println("Received : "+ message);
        return message;
    }
 
    @OnOpen
    public void myOnOpen(Session session) {
        System.out.println("WebSocket opened: " + session.getId());
    }
 
    @OnClose
    public void myOnClose(Session session, CloseReason closeReason) {
        System.out.println("Closing a WebSocket due to test: " + session.getId());
    }
}

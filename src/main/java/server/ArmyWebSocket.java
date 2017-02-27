package server;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import entities.Unit;
import enums.WEB_SOCKET_ACTION;

@ApplicationScoped
@ServerEndpoint(value = "/army")
public class ArmyWebSocket {

	@Inject
	private ArmySessionHandler sessionHandler;

	@OnOpen
	public void myOnOpen(Session session) {
		System.out.println("Usuario logueado : " + session.getId());
		sessionHandler.addSession(session);
	}

	@OnMessage
	public void myOnMessage(String message, Session session) {
		System.out.println("entro a onMessage");
		System.out.println("json recibido : " + message);
		try (JsonReader reader = Json.createReader(new StringReader(message))) {
			JsonObject jsonMessage = reader.readObject();
			if (WEB_SOCKET_ACTION.ADD.getCode() == Long.valueOf(jsonMessage.getString("action"))) {
				Unit unit = new Unit();
				unit.setId(sessionHandler.getUnits().size() + 1);
				unit.setName(jsonMessage.getString("name"));
				unit.setClassType(jsonMessage.getString("classType"));
				sessionHandler.addUnit(unit);
			}else if (WEB_SOCKET_ACTION.REMOVE.getCode() == Long.valueOf(jsonMessage.getString("action"))) {
				int id = (int) jsonMessage.getInt("id");
				sessionHandler.removeUnit(id);
			}else if (WEB_SOCKET_ACTION.GET.getCode() == Long.valueOf(jsonMessage.getString("action"))) {
				sessionHandler.getCurrentUnits();
			}else{
				int id = (int) jsonMessage.getInt("id");
				sessionHandler.toggleUnit(id);
			}
		}
	}

	@OnClose
	public void myOnClose(Session session, CloseReason closeReason) {
		System.out.println("Usuario se deslogueo: " + session.getId());
		sessionHandler.removeSession(session);
	}

	public void myOnError(Throwable error) {
		Logger.getLogger(ArmyWebSocket.class.getName()).log(Level.SEVERE, null, error);
	}
}

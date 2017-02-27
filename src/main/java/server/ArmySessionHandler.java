package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;

import entities.Unit;

@ApplicationScoped
public class ArmySessionHandler {
	private int unitId = 0;
	private final Set<Session> sessions = new HashSet<>();
	private final Set<Unit> units = new HashSet<>();

	public void addSession(Session session) {
		/**
		 * Se agrega la sesion
		 */
		sessions.add(session);
		for (Unit unit : units) {
			/**
			 * Se envia la lista de unidades registradas al usuario recien
			 * logueado
			 */
			JsonObject addMessage = createAddMessage(unit);
			sendToSession(session, addMessage);
		}
	}

	public void removeSession(Session session) {
		sessions.remove(session);
	}

	public List<Unit> getUnits() {
		return new ArrayList<>(units);
	}

	public void addUnit(Unit unit) {
		unit.setId(unitId);
		units.add(unit);
		unitId++;
		JsonObject addMessage = createAddMessage(unit);
		sendToAllConnectedSessions(addMessage);
	}

	public void removeUnit(int id) {
		Unit unit = getUnitById(id);
		if (unit != null) {
			units.remove(unit);
			JsonProvider provider = JsonProvider.provider();
			JsonObject removeMessage = provider.createObjectBuilder().add("action", "remove").add("id", id).build();
			sendToAllConnectedSessions(removeMessage);
		}
	}
	
	public void getCurrentUnits(){
		JsonObject getMessage = createGetMessage();
		sendToAllConnectedSessions(getMessage);
	}

	public void toggleUnit(int id) {
		// JsonProvider jsonProvider = JsonProvider.provider();
		// Unit unit = getUnitById(id);
		// if(unit != null){
		// if()
		// }
	}

	private Unit getUnitById(int id) {
		for (Unit unit : units) {
			if (unit.getId() == id) {
				return unit;
			}
		}
		return null;
	}

	private JsonObject createAddMessage(Unit unit) {
		JsonProvider provider = JsonProvider.provider();
		JsonObject data = provider.createObjectBuilder().add("id", unit.getId())
				.add("name", unit.getName()).add("classType", unit.getClassType()).build();
		JsonObject addMessage = provider.createObjectBuilder().add("action", "add").add("data", data).build();
		return addMessage;
	}
	
	private JsonObject createGetMessage(){
		JsonProvider provider = JsonProvider.provider();
		JsonArrayBuilder data = Json.createArrayBuilder();
		for(Unit unit : getUnits()){
			data.add(Json.createObjectBuilder().add("id", unit.getId())
					.add("name", unit.getName()).add("classType", unit.getClassType()).build());
		}
		JsonArray array = data.build();
		JsonObject getMessage = provider.createObjectBuilder().add("action", "get").add("data", array).build();
		return getMessage;
	}

	private void sendToAllConnectedSessions(JsonObject message) {
		for (Session session : sessions) {
			sendToSession(session, message);
		}
	}

	private void sendToSession(Session session, JsonObject message) {
		try {
			session.getBasicRemote().sendText(message.toString());
		} catch (IOException ex) {
			sessions.remove(session);
			Logger.getLogger(ArmySessionHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}

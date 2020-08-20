/**
 * Chatroom.class.
 *
 * @original RedHat/JBoss
 * @extended_by Abin Abraham (DCS02)
 */

package my.test.app.chatter;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.jboss.logging.Logger;

@ServerEndpoint("/websocket/chatroom")
public class Chatroom {

	private static final Logger log = Logger.getLogger(Chatroom.class);

	private static final Set<Chatroom> connections = new CopyOnWriteArraySet<Chatroom>();

	private String username;
	private Session session;

	@OnOpen
	public void chatOnOpen(Session session) {
		this.session = session;
		connections.add(this);
		username = session.getQueryString();
		String message = String.format("* %s %s", username, "joined the chatroom.");
		if (username != null)
			broadcast(message);
		log.info("WebSocket opened with Session id: " + session.getId() + " for " + username);
	}

	@OnMessage
	public void incoming(String message) {
		String filteredMessage = String.format("%s: %s", username, HTMLFilter.filter(message.toString()));
		if (username != null)
			broadcast(filteredMessage);
	}

	@OnClose
	public void chatOnClose(CloseReason reason) {
		connections.remove(this);
		String message = String.format("* %s %s", username, "left the chatroom.");
		if (username != null)
			broadcast(message);
		log.info("WebSocket connection closed with CloseCode: " + reason.getCloseCode());
	}

	@OnError
	public void onError(Throwable t) throws Throwable {
		log.error("Chat Error: " + t.toString(), t);
	}

	private static void broadcast(String msg) {
		for (Chatroom client : connections) {
			try {
				synchronized (client) {
					client.session.getBasicRemote().sendText(msg);
				}
			} catch (IOException e) {
				log.debug("Chat Error: Failed to send message to client", e);
				connections.remove(client);
				try {
					client.session.close();
				} catch (IOException e1) {
					// nothing to catch yet..
				}
				if (client.username != null) {
					String message = String.format("* %s %s", client.username, "left the chatroom.");
					broadcast(message);
				}
			}
		}
	}
}

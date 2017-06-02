package slacky.service;

import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

/**
 * Created by rcrisan on 6/2/17.
 */
@ClientEndpoint
public class WebSocketClientEndpoint {
    Session userSession = null;
    private MessageHandler messageHandler;

    public WebSocketClientEndpoint(URI endpoint) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpoint);
        } catch (DeploymentException | IOException e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("Opening websocket");
        this.userSession = userSession;
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Closing websocket");
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }

    public static interface MessageHandler {
        void handleMessage(String message);
    }

}

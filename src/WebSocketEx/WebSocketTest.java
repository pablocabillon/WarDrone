package WebSocketEx;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket")
public class WebSocketTest {

  @OnMessage
  public void onMessage(String message, Session session) 
    throws IOException, InterruptedException {
  
    // Print the client message for testing purposes
    //System.out.println("Recibido: " + message);
  
    // Send the first message to the client
    session.getBasicRemote().sendText(message);


  }
  
  @OnOpen
  public void onOpen() {
    System.out.println("Cliente conectado");
  }

  @OnClose
  public void onClose() {
    System.out.println("Conexión cerrada");
  }
}
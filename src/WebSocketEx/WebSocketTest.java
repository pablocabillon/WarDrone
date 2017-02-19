package WebSocketEx;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
//
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/websocket")
//es - a la vez- nuestro EJB para programar eventos
public class WebSocketTest {

static final Logger LOGGER = Logger.getLogger(WebSocketTest.class.getName());
    //la lista de conexiones realizadas
private static Set<Session> conexiones = Collections.synchronizedSet(new HashSet<Session>());
 
@OnMessage
public void onMessage(String mensaje, Session sesion) throws IOException {
//    JsonReader jLector = Json.createReader(new StringReader(mensaje));         
//    JsonObject jMensaje = jLector.readObject();         
//    jLector.close();
	JsonElement jelement = new JsonParser().parse(mensaje);
	JsonObject  jobject = jelement.getAsJsonObject();
    synchronized(conexiones){
	      // Recorro los clientes conectados y reenvío el mensaje recibido.
      for(Session client : conexiones){
        if (!client.equals(sesion)){
            client.getBasicRemote().sendText(jobject.toString());
        }
      }
  }

}
 
@OnOpen
public void iniciaSesion(Session session) {
    LOGGER.log(Level.INFO, "Iniciando la conexion de {0}", session.getId());
    conexiones.add(session); //Simplemente, lo agregamos a la lista

}

@OnClose
public void finConexion(Session session) {
    LOGGER.info("Terminando la conexion");
    if (conexiones.contains(session)) { // se averigua si está en la colección
        try {
            LOGGER.log(Level.INFO, "Terminando la conexion de {0}", session.getId());
            session.close(); //se cierra la conexión
            conexiones.remove(session); // se retira de la lista
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
}

}
package es.ubu.lsi.server;
import es.ubu.lsi.common.*;

/**
 * Interfaz del server.
 * 
 * @author Alex Tomé Aguiar
 * @author Pablo Simón Sainz
 * @version 1.0
 */
public interface GameServer {
	
	public boolean startup() {
		return false;
	}
	public void shutdown() {
		
		
	}
	public void broadcastRoom(GameElement element) {
		
	}
	
	public void remove(int id) {
		
		
	}
}
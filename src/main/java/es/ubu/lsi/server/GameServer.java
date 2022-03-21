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
	
	/**
	 * 
	 * @return
	 */
	public boolean startup();
	
	/**
	 * 
	 */
	public void shutdown();
	
	/**
	 * 
	 * @param element
	 */
	public void broadcastRoom(GameElement element);
	
	/**
	 * 
	 * @param id
	 */
	public void remove(int id);
}
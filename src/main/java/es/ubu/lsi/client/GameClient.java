package es.ubu.lsi.client;
import java.io.IOException;

import es.ubu.lsi.common.*;

/**
 * Interfaz del cliente.
 * 
 * @author Alex Tomé Aguiar
 * @author Pablo Simón Sainz
 * @version 1.0
 */
public interface GameClient {
	
	/**
	 * Método que lanza el hilo del cliente.
	 * 
	 * @return
	 * @throws IOException 
	 */
	public boolean start() throws IOException;
	
	/**
	 * Método que envía la jugada al servidor.
	 * 
	 * @param element elemento a enviar o jugada
	 */
	public void sendElement(GameElement element);
	
	/**
	 * Método de desconexión.
	 */
	public void disconnect();
	
}
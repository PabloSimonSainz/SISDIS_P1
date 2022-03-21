package es.ubu.lsi.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import es.ubu.lsi.common.GameElement;


/**
 * Interfaz del server.
 * 
 * @author Alex Tomé Aguiar
 * @author Pablo Simón Sainz
 * @version 1.0
 */
public class GameClientImpl implements GameClient {
	
	/**
	 * Constructor de la clase.
	 * 
	 * @param server servidor.
	 * @param port puerto.
	 * @param username nombre del jugador.
	 */
	public GameClientImpl(String server, int port, String username) {
		
	}
	
	/**
	 * Método start del cliente. Arranca un hilo para 
	 * escuchar al servidor.
	 * 
	 * @return
	 */
	@Override
	public boolean start() {
		return false;
	}
	
	/**
	 * Flujo de salida para el envío de la jugada.
	 * 
	 * @param element jugada.
	 */
	@Override
	public void sendElement(GameElement element){
		
	}
	
	/**
	 * Método que permite desconectar al cliente.
	 */
	@Override
	public void disconnect() {
		
	}
	
	/**
	 * Método main de la clase. Instancia el socket arrancando
	 * el hilo principal.
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public void main(String[] args) throws IOException {
		 MulticastSocket socket = new MulticastSocket(1500);
	     InetAddress address = InetAddress.getByName("localhost");
	     socket.joinGroup(address);

	     DatagramPacket packet;
	     socket.leaveGroup(address);
	 	 socket.close();
	}
	
	/**
	 * Clase interna del cliente.
	 * 
	 * @author alext
	 *
	 */
	public class GameClientListener{
		
		/**
		 * 
		 */
		public void run() {
			
			
		}
	}
}
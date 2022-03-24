package es.ubu.lsi.server;

import es.ubu.lsi.common.GameElement;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Implementación de GameServer.
 * 
 * @author Alex Tomé Aguiar
 * @author Pablo Simón Sainz
 * @version 1.0
 */
public class GameServerImpl implements GameServer{
	// Nota: en su invocación no recibe argumentos.

	/** Por defecto el servidor se ejecuta en el puerto 1500 */
	public static final int PORT = 1500;
	protected ServerSocket socket;
	protected List<Socket> clients;
	private boolean keepGoing;

	public static void main(String[] args){
		new GameServerImpl().startup();
	}

	private void fillRoom(){
		while (this.clients.size() < 2 && keepGoing){
			try{
				clients.add(socket.accept());
			} catch (IOException e){
				System.err.println("ERROR>> No se pudo conectar.");
			}
		}
	}
	
	/**
	 * Implementa el bucle con el servidor de sockets (ServerSocket), 
	 * esperando y aceptado peticiones. Ante cada petición entrante y aceptada, 
	 * se instancia un nuevo ServerThreadForClient y se arranca el hilo correspondiente
	 * para que cada cliente tenga su hilo independiente asociado en el servidor 
	 * (con su socket, flujo de entrada y flujo de salida).
	 */
	@Override
	public void startup() {
		// Nota: Es importante ir guardando un registro de los hilos creados para poder 
		// posteriormente realizar el push de los mensajes y un apagado correcto.
		this.clients = new ArrayList<Socket>();

		try {
			this.socket = new ServerSocket(PORT);
		} catch (SocketException e) {
			return;
		}

		while(keepGoing){
			fillRoom();	
			
		}
	}

	/**
	 * Cierra los flujos de entrada/salida del servidor y el socket correspondiente a cada cliente
	 */
	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Envía el resultado sólo a los clientes de una determinada sala (flujo de salida)
	 */
	@Override
	public void broadcastRoom(GameElement element) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Elimina a un cliente de la lista
	 */
	@Override
	public void remove(int id) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @author Alex Tomé Aguiar
	 * @author Pablo Simón Sainz
	 * @version 1.0
	 */
	public class ServerThreadForCLient extends Thread{

		private int id;
		protected DatagramSocket socket;
		protected ObjectInputStream in;
		protected ObjectOutputStream out;

		public ServerThreadForCLient(int id) {
			
			this.id = id;
			
			try{
				socket = new DatagramSocket(GameServerImpl.PORT);
			} catch(SocketException e) {
				System.err.println("ERROR>> No se pudo establecer el socket.");
			}
		}

		/**
		 * Espera en un bucle a los mensajes recibidos de cada cliente (flujo de
		 * entrada), realizándose la operación correspondiente (a través de los 
		 * métodos de la clase externa GameServer).
		 */
		public void run() {
			if (socket == null)
				return;

			while (true) {
					
				}
			}

			socket.close();
		}
	}
}
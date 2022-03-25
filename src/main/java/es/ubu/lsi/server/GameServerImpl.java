package es.ubu.lsi.server;

import es.ubu.lsi.common.GameElement;
import es.ubu.lsi.common.ElementType;
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
	
	/** Socket del servidor */
	protected ServerSocket socket;
	
	/** Hashtable que contiene un par Id con socket. */
	protected Hashtable<ServerThreadForClient, Socket> clients;
	
	/** Booleano que marca si se quiere mantener el servidor activo. */
	private GameElement lastMove;

	public static void main(String[] args){
		new GameServerImpl().startup();
	}

	/**
	 * Se encarga de hacer el bucle que recibe a los clientes.
	 */
	private void fillRoom(){
		int id = 1;
		
		while (lastMove != null && lastMove.getElement() != ElementType.SHUTDOWN){
			try{
				if (this.clients.size() < 2) {
					ServerThreadForClient thread = new ServerThreadForClient(id);
					clients.put(thread, socket.accept());
					
					thread.run();
					
					id++;
				} else {
					Socket temp = socket.accept();
					PrintWriter out = new PrintWriter(temp.getOutputStream(), true);
					
					out.println(">> Full capacity of the room, leaving server...");
					
					out.close();
					temp.close();
				}
			} catch (IOException e){
				System.err.println(">> ERROR: No se pudo conectar.");
				System.err.println(e.getMessage());
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
		this.clients = new Hashtable<ServerThreadForClient, Socket>();

		try {
			this.socket = new ServerSocket(PORT);
		} catch (IOException e) {
			System.err.println(">> ERROR: No se pudo establecer el servidor.");
			System.err.println(e.getMessage());
			return;
		}
		
		fillRoom();
		
		shutdown();
	}

	/**
	 * Cierra los flujos de entrada/salida del servidor y el socket correspondiente a cada cliente
	 */
	@Override
	public void shutdown() {
		Enumeration<ServerThreadForClient> iter = clients.keys();
		while(iter.hasMoreElements()) {
			
		}
	}

	/**
	 * Envía el resultado sólo a los clientes de una determinada sala (flujo de salida)
	 */
	@Override
	public void broadcastRoom(GameElement element) {
		
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
	public class ServerThreadForClient extends Thread{

		private int idRoom;
		private ServerThreadForClient rival;

		public int getIdRoom() {
			return idRoom;
		}
		
		public ServerThreadForClient(int id){
			this.idRoom = id;
		}

		/**
		 * Espera en un bucle a los mensajes recibidos de cada cliente (flujo de
		 * entrada), realizándose la operación correspondiente (a través de los 
		 * métodos de la clase externa GameServer).
		 */
		public void run() {
	        try (Socket clientSocket = clients.get(this);
		            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);                   
		            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        		) {
	            
	        	String inputLine;
	            while(true) {
	            	try {
	            		out.println("> Introduzca su jugada: ");
	            		inputLine = in.readLine();
	            		
	            		if (lastMove.getElement() == ElementType.SHUTDOWN) break;
	            		
	            		ElementType move = ElementType.valueOf(inputLine);
	            		
	            		if (move == ElementType.LOGOUT) break;
	            		
	            		lastMove = new GameElement(this.idRoom, move);	
	            	} catch(IllegalArgumentException e) {
	            		out.println("(!) Comando no valido (!)");
	            	}
	            }
	            
	            out.close();
	            in.close();
	            clientSocket.close();
	        } catch (IOException e) {
	            System.out.println(">> ERROR: El hilo " + this.idRoom +" no se pudo conectar.");
	            System.out.println(e.getMessage());
	        }
		}
	}
}
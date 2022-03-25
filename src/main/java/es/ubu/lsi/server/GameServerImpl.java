package es.ubu.lsi.server;

import es.ubu.lsi.common.GameElement;
import es.ubu.lsi.common.GameResult;
import es.ubu.lsi.common.ElementType;
import java.io.*;
import java.net.*;
import java.sql.SQLClientInfoException;
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
	
	/** Por defecto el servidor se ejecuta como localhost */
	public static final String hostName = "localhost";
	
	/** Socket del servidor */
	protected ServerSocket socket;
	
	/** Hashtable que contiene un par Id con socket. */
	protected Hashtable<Integer, Socket> clients;
	
	/** Booleano que marca si se quiere mantener el servidor activo. */
	private GameElement lastMove;

	public static void main(String[] args){
		System.out.println("Iniciando Sevidor...");		
		new GameServerImpl().startup();
	}

	/**
	 * Se encarga de hacer el bucle que recibe a los clientes.
	 */
	private void fillRoom(){
		int id = 1;
		
		while (lastMove == null || lastMove.getElement() != ElementType.SHUTDOWN){
			try{
				if (this.clients.size() < 2) {
					ServerThreadForClient thread = new ServerThreadForClient(id);
					clients.put(thread.idRoom, socket.accept());
					
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
				System.out.println(">> ERROR: No se pudo conectar.");
				System.out.println(e.getMessage());
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
		this.clients = new Hashtable<Integer, Socket>();

		try {
			this.socket = new ServerSocket(PORT);
		} catch (IOException e) {
			System.out.println(">> ERROR: No se pudo establecer el servidor.");
			System.out.println(e.getMessage());
			return;
		}
		
		fillRoom();
		
		System.out.println("Cerrando Servidor...");
		shutdown();
	}

	/**
	 * Cierra los flujos de entrada/salida del servidor y el socket correspondiente a cada cliente
	 */
	@Override
	public void shutdown() {
		Enumeration<Integer> iter = clients.keys();
		while(iter.hasMoreElements()) {
			remove(iter.nextElement());
		}
	}

	private GameResult getResult(GameElement move1, GameElement move2) {
		ElementType m1 = move1.getElement();
		ElementType m2 = move1.getElement();
		
		if(m1 == m2)
			return GameResult.DRAW;
		
		if(m1 == null || m2 == null)
			return GameResult.WAITING;
		
		switch(m1) {
		case PIEDRA:
			if (m2 == ElementType.TIJERA)
				return GameResult.WIN;
			if (m2 == ElementType.PAPEL)
				return GameResult.LOSE;
		case PAPEL:
			if (m2 == ElementType.PIEDRA)
				return GameResult.WIN;
			if (m2 == ElementType.TIJERA)
				return GameResult.LOSE;
		case TIJERA:
			if (m2 == ElementType.PAPEL)
				return GameResult.WIN;
			if (m2 == ElementType.PIEDRA)
				return GameResult.LOSE;
		default:
			return null;
		}
	}
	
	private void SendMessageToClient(String message, int id) {
		Enumeration<Integer> iter = clients.keys();
		int pointer;
		
		while (iter.hasMoreElements()) {
			pointer = iter.nextElement();
			
			if (pointer == id) {
				Socket temp = clients.get(pointer);
				
				try {
					PrintWriter out = new PrintWriter(temp.getOutputStream(), true);
					out.println(message);
					return;
				} catch (IOException e) {
					System.out.println("ERROR: No se pudo contactar con <" + pointer + ">.");
				}
			}
		}
		
		System.out.println("ERROR: Id <" + id + "> no encontrada.");
	}
	
	/**
	 * Envía el resultado sólo a los clientes de una determinada sala (flujo de salida)
	 */
	@Override
	public void broadcastRoom(GameElement element) {
		GameResult result = getResult(element, lastMove);
		
		if (result == GameResult.WAITING) {
			lastMove = element;
			return;
		}
			
		
		if (result == null) {
			Enumeration<Integer> iter = clients.keys();
			
			while (iter.hasMoreElements()) {
				int pointer = iter.nextElement();
				
				Socket temp = clients.get(pointer);
					
				try {
					PrintWriter out = new PrintWriter(temp.getOutputStream(), true);
					out.println(">> CERRANDO SERVIDOR");
					return;
				} catch (IOException e) {
					System.out.println("ERROR: No se pudo contactar con <" + pointer + ">.");
				}
			}
		}
		
		
		SendMessageToClient(result.toString(), element.getClientId());
		SendMessageToClient(getResult(lastMove, element).toString(), lastMove.getClientId());
	}

	/**
	 * Elimina a un cliente de la lista
	 */
	@Override
	public void remove(int id) {
		try {
			clients.get(id).close();
		} catch (IOException e) {
			System.out.println("<!> Los elementos de " + clients.get(id) + "ya han sido cerrados.");
		}
		clients.remove(id);
	}
	
	/**
	 * @author Alex Tomé Aguiar
	 * @author Pablo Simón Sainz
	 * @version 1.0
	 */
	public class ServerThreadForClient extends Thread{

		private int idRoom;
		
		private Socket clientSocket;
		
		private PrintWriter out;
		
		private BufferedReader in;

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
	        try {
	        	this.clientSocket = clients.get(this.idRoom);
	            this.out = new PrintWriter(clientSocket.getOutputStream(), true);                   
	            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	            
	        	String inputLine;
	        	
	            while(true) {
	            	if(lastMove.getClientId() != this.idRoom)
		            	try {
		            		if (lastMove.getElement() == ElementType.SHUTDOWN) {
		            			out.println("- Se ha cerrado el servidor.");
		            			break;
		            		}
		            		
		            		out.println("> Introduzca su jugada: ");
		            		inputLine = in.readLine();
		            		
		            		ElementType move = ElementType.valueOf(inputLine);
		            		
		            		if (move == ElementType.LOGOUT) break;
		            		
		            		broadcastRoom(new GameElement(this.idRoom, move));	
		            	} catch(IllegalArgumentException e) {
		            		out.println("(!) Comando no valido (!)");
		            	}
	            }
	            
	            close();
	        } catch (IOException e) {
	            System.out.println(">> ERROR: El hilo " + this.idRoom +" no se pudo conectar.");
	            System.out.println(e.getMessage());
	        }
		}
		
		protected void close() {
			try {
				this.out.close();
				this.in.close();
				this.clientSocket.close();
			} catch (IOException e) {
				System.out.println("<!> Los elementos de " + this.idRoom + "ya han sido cerrados.");
				System.out.println(e.getMessage());
			}
		}
	}
}
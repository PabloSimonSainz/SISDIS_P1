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
	protected DatagramSocket socket;

	public static void main(String[] args){
		
	}

	
	/**
	 * Implementa el bucle con el servidor de sockets (ServerSocket), 
	 * esperando y aceptado peticiones. Ante cada petición entrante y aceptada, 
	 * se instancia un nuevo ServerThreadForClient y se arranca el hilo correspondiente
	 * para que cada cliente tenga su hilo independiente asociado en el servidor 
	 * (con su socket, flujo de entrada y flujo de salida).
	 */
	@Override
	public boolean startup() {
		// Nota: Es importante ir guardando un registro de los hilos creados para poder 
		// posteriormente realizar el push de los mensajes y un apagado correcto.

		try {
			this.socket = new DatagramSocket(PORT);
		} catch (SocketException e) {
			System.err.println(e.getMessage());
		}


		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not Implemented");
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
		protected BufferedReader in;

		protected boolean moreQuotes = true;

		public ServerThreadForCLient(int id) throws IOException {
			
			this.id = id;

			socket = new DatagramSocket(GameServerImpl.PORT);
			
			// ?? Añadir mensaje de bienvenida
			/*
			try {
				in = new BufferedReader(new FileReader("message.txt"));
			} catch (FileNotFoundException e) {
				System.err
						.println("Could not open quote file. Serving time instead.");
			}
			*/
		}

		/**
		 * Espera en un bucle a los mensajes recibidos de cada cliente (flujo de
		 * entrada), realizándose la operación correspondiente (a través de los 
		 * métodos de la clase externa GameServer).
		 */
		public void run() {

			while (moreQuotes) {
				try {
					byte[] buf = new byte[256];

					// receive request
					DatagramPacket packet = new DatagramPacket(buf, buf.length);
					socket.receive(packet);
					
					// figure out response
					String dString = null;
					if (in == null)
						dString = new Date().toString();
					else
						dString = getNextQuote();

					buf = dString.getBytes();

					// send the response to the client at "address" and "port"
					InetAddress address = packet.getAddress();
					int port = packet.getPort();
					packet = new DatagramPacket(buf, buf.length, address, port);
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
					moreQuotes = false;
				}
			}
			socket.close();
		}

		protected String getNextQuote() {
			String returnValue = null;
			try {
				if ((returnValue = in.readLine()) == null) {
					in.close();
					moreQuotes = false;
					returnValue = "No more quotes. Goodbye.";
				}
			} catch (IOException e) {
				returnValue = "IOException occurred in server.";
			}
			return returnValue;
		}
	}
}
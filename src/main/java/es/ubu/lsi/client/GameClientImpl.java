package es.ubu.lsi.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import es.ubu.lsi.common.ElementType;
import es.ubu.lsi.common.GameElement;


/**
 * Interfaz del server.
 * 
 * @author Alex Tomé Aguiar
 * @author Pablo Simón Sainz
 * @version 1.0
 */
public class GameClientImpl implements GameClient {
	
	private String server;
	private String username;
	private int port;
	/**
	 * Constructor de la clase.
	 * 
	 * @param server servidor.
	 * @param port puerto.
	 * @param username nombre del jugador.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public GameClientImpl(String server, int port, String username) throws IOException {
		this.server = server;
		this.port = port;
		this.username = username;
		Socket cliente = new Socket(server, port);
		
		try {
			start();
		}catch (UnknownHostException e) {
            System.err.println("Don't know about host " + server);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + server);
            System.exit(1);
        } 
	}
	
	/**
	 * Método start del cliente. Arranca un hilo para 
	 * escuchar al servidor.
	 * 
	 * @return
	 */
	@Override
	public boolean start() {
		String hostName = server;
		String jugada;
		System.out.println ("Introduce la jugada: ");
		Scanner entradaEscaner = new Scanner (System.in);
        jugada = entradaEscaner.nextLine ();
        try {
			if(jugada == "tijera" || jugada == "TIJERA"){
				GameElement elemento = new GameElement(1,ElementType.TIJERA);
				sendElement(elemento);	
			}else if (jugada == "piedra" || jugada == "PIEDRA") {
				GameElement elemento = new GameElement(1,ElementType.PIEDRA);
				sendElement(elemento);
			}else if(jugada == "papel" || jugada == "PAPEL") {
				GameElement elemento = new GameElement(1,ElementType.PAPEL);
				sendElement(elemento);
			}else if(jugada == "logout" || jugada == "LOGOUT") {
				GameElement elemento = new GameElement(1,ElementType.LOGOUT);
				sendElement(elemento);
			}
        }catch (Exception e) {
            
        }  
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
	 * Clase interna del cliente. Lanza el hilo para comunicarse con el servidor
	 * una vez realice la jugada.
	 * 
	 * @author Alex Tomé Aguiar
	 * @author Pablo Simón Sainz
	 * @version 1.0
	 */
	public class GameClientListener{
		
		/**
		 * 
		 */
		public void run() {
			
			
		}
		
	}
}
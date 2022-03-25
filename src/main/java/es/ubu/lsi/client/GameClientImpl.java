package es.ubu.lsi.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import es.ubu.lsi.common.ElementType;
import es.ubu.lsi.common.GameElement;
import es.ubu.lsi.common.GameResult;
import es.ubu.lsi.server.GameServerImpl;


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
	
	/** Socket del servidor */
	protected Socket cliente;
	private PrintWriter out;
	private BufferedReader in;
	
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
		
		try {
			cliente = new Socket(server, port);
			this.out = new PrintWriter(cliente.getOutputStream(), true);                   
            this.in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
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
	 * @throws IOException 
	 */
	@Override
	public boolean start() throws IOException {
		String jugada;
        new GameClientListener();
        //int id = ServerThreadForClient.getIdRoom();     
        try {
        	ElementType move;
        	jugada = in.readLine();
	        while (jugada != "SHUTDOWN") {
	        	move = ElementType.valueOf(jugada);
	        	GameElement elemento = new GameElement(1, move);
				if(move == ElementType.LOGOUT) {
					disconnect();
				}
				sendElement(elemento);
				
			}return true;
	        
        } catch(IllegalArgumentException e) {
    		out.println("(!) Comando no valido (!)");
    	}
		return false;
		
	}
	
	/**
	 * Flujo de salida para el envío de la jugada.
	 * 
	 * @param element jugada.
	 */
	@Override
	public void sendElement(GameElement element){
		String enviar = element.toString();
		out.write(enviar);
	}
	
	/**
	 * Método que permite desconectar al cliente.
	 */
	@Override
	public void disconnect() {
		try {
			in.close();
			out.close();
			cliente.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Método main de la clase. Instancia el socket arrancando
	 * el hilo principal.
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public void main(String[] args) throws IOException {
		new GameClientImpl(GameServerImpl.hostName, GameServerImpl.PORT, args[0]);
	}
	
	/**
	 * Clase interna del cliente. Lanza el hilo para comunicarse con el servidor
	 * una vez realice la jugada.
	 * 
	 * @author Alex Tomé Aguiar
	 * @author Pablo Simón Sainz
	 * @version 1.0
	 */
	public class GameClientListener implements Runnable{
		
		/**
		 * Método run del cliente. Arranca el hilo que escucha al servidor.
		 */
		@Override
		public void run() {
			try {
				String respuesta =  in.readLine();
				if(respuesta != null) {		
					System.out.println(respuesta);
				}
			}catch (IOException e){
				System.err.println("I/0 Exception");
				e.printStackTrace();
			}
		}
		
	}
}
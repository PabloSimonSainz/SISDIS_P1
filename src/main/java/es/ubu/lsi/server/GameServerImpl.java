package es.ubu.lsi.server;

import es.ubu.lsi.common.GameElement;

/**
 * Interfaz del server.
 * 
 * @author Alex Tomé Aguiar
 * @author Pablo Simón Sainz
 * @version 1.0
 */
public class GameServerImpl implements GameServer{
	
	/**
	 * 
	 */
	@Override
	public boolean startup() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 
	 */
	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	@Override
	public void broadcastRoom(GameElement element) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	@Override
	public void remove(int id) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 
	 * @author alext
	 *
	 */
	public class ServerThreadForCLient{
		
	}
}
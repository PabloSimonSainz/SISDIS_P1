package es.ubu.lsi.common;

/**
 * Game result.
 * 
 * @author Alex Tomé Aguiar
 * @author Pablo Simón Sainz
 * @version 1.0
 */
public enum GameResult {
	WIN("WIN"),
	DRAW("DRAW"),
	LOSE("LOSE"),
	WAITING("WAITING");

	private String message;

	GameResult(String message){
		this.message = message;
	}

	public String toString(){
		return this.message;
	}
}

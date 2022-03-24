package es.ubu.lsi.common;


/**
 * Element type.
 * 
 * @author Alex Tomé Aguiar
 * @author Pablo Simón Sainz
 * @version 1.0
 */
public enum ElementType {
	PIEDRA("PIEDRA"),
	PAPEL("PAPEL"),
	TIJERA("TIJERA"),
	LOGOUT("LOGOUT"), 
	SHUTDOWN("SHUTDOWN");

	private String message;

	ElementType(String message){
		this.message = message;
	}

	public String toString(){
		return this.message;
	}
}

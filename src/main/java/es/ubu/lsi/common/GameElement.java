package es.ubu.lsi.common;


/**
 * Clase GameElement.
 * 
 * @author Alex Tomé Aguiar
 * @author Pablo Simón Sainz
 * @version 1.0
 */
public class GameElement {
	
	/**
	 * 
	 */
	private ElementType element;
	
	/**
	 * 
	 */
	private int clientId;
	
	/**
	 * Constructor de la clase.
	 * 
	 * @param id
	 * @param element
	 */
	public GameElement(int id, ElementType element) {
		
	}

	/**
	 * 
	 * @return
	 */
	public ElementType getElement() {
		return element;
	}

	/**
	 * 
	 * @param element
	 */
	public void setElement(ElementType element) {
		this.element = element;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getClientId() {
		return clientId;
	}
	
	
}

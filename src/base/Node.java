package base;

import java.util.LinkedList;
import java.util.List;

/**
 * Diese Klasse representiert einen generischen Knoten
 * 
 * @param <T>
 */
public class Node<T> {

	private T value;
	private List<Node<T>> connections;

	/**
	 * Erzeugt einen neuen Knoten mit dem gegebenen Wert
	 * 
	 * @param value der Wert des Knotens
	 */
	Node(T value) {
		this.value = value;
		connections = new LinkedList<>();
	}

	/**
	 * Gibt den Wert des Knotens zurück
	 * 
	 * @return der Wert des Knotens
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Adds list of nodes this node is connected to
	 * 
	 * @author Jonathan Geraldi Joewono
	 * @param otherNode
	 */
	public void connectTo(Node<T> otherNode) {
		if (!connections.contains(otherNode)) {
			connections.add(otherNode);
			otherNode.connectTo(this);
		}
	}

	/**
	 * 
	 * @return list of nodes this node has connections to
	 */
	public List<Node<T>> getConnections() {
		return connections;
	}
}

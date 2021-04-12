package base;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Diese Klasse representiert einen generischen Graphen mit einer Liste aus
 * Knoten und Kanten.
 *
 * @param <T> Die zugrundeliegende Datenstruktur, beispielsweise
 *        {@link game.map.Castle}
 */
public class Graph<T> {

	private List<Edge<T>> edges;
	private List<Node<T>> nodes;

	/**
	 * Konstruktor für einen neuen, leeren Graphen
	 */
	public Graph() {
		this.nodes = new ArrayList<>();
		this.edges = new LinkedList<>();
	}

	/**
	 * Einen neuen Knoten zum Graphen hinzufügen
	 * 
	 * @param value Der Wert des Knotens
	 * @return Der erstellte Knoten
	 */
	public Node<T> addNode(T value) {
		Node<T> node = new Node<>(value);
		this.nodes.add(node);
		return node;
	}

	/**
	 * Eine neue Kante zwischen zwei Knoten hinzufügen. Sollte die Kante schon
	 * existieren, wird die vorhandene Kante zurückgegeben.
	 * 
	 * @param nodeA Der erste Knoten
	 * @param nodeB Der zweite Knoten
	 * @return Die erstellte oder bereits vorhandene Kante zwischen beiden gegebenen
	 *         Knoten
	 */
	public Edge<T> addEdge(Node<T> nodeA, Node<T> nodeB) {
		Edge<T> edge = getEdge(nodeA, nodeB);
		if (edge != null) {
			return edge;
		}
		edge = new Edge<>(nodeA, nodeB);
		this.edges.add(edge);
		nodeA.connectTo(nodeB);
		//		nodeB.connectTo(nodeA);
		return edge;
	}

	/**
	 * Gibt die Liste aller Knoten zurück
	 * 
	 * @return die Liste aller Knoten
	 */
	public List<Node<T>> getNodes() {
		return this.nodes;
	}

	/**
	 * Gibt die Liste aller Kanten zurück
	 * 
	 * @return die Liste aller Kanten
	 */
	public List<Edge<T>> getEdges() {
		return this.edges;
	}

	/**
	 * Diese Methode gibt alle Werte der Knoten in einer Liste mittels Streams
	 * zurück.
	 * 
	 * @author Kenneth Kartono
	 * @see java.util.stream.Stream#map(Function)
	 * @see java.util.stream.Stream#collect(Collector)
	 * @return Eine Liste aller Knotenwerte
	 */
	public List<T> getAllValues() {
		// TODO: Graph<T>#getAllValues()
		return getNodes().stream().map(n -> n.getValue()).collect(Collectors.toList());
	}

	/**
	 * Diese Methode gibt alle Kanten eines Knotens als Liste mittels Streams
	 * zurück.
	 * 
	 * @author Kenneth Kartono
	 * @param node Der Knoten für die dazugehörigen Kanten
	 * @see java.util.stream.Stream#filter(Predicate)
	 * @see java.util.stream.Stream#collect(Collector)
	 * @return Die Liste aller zum Knoten zugehörigen Kanten
	 */
	public List<Edge<T>> getEdges(Node<T> node) {
		// TODO: Graph<T>#getEdges(Node<T>)
		return getEdges().stream().filter(n -> n.contains(node)).collect(Collectors.toList());
	}

	/**
	 * Diese Methode sucht eine Kante zwischen beiden angegebenen Knoten und gibt
	 * diese zurück oder null, falls diese Kante nicht existiert
	 * 
	 * @author Kenneth Kartono
	 * @param nodeA Der erste Knoten
	 * @param nodeB Der zweite Knoten
	 * @return Die Kante zwischen beiden Knoten oder null
	 */
	public Edge<T> getEdge(Node<T> nodeA, Node<T> nodeB) {
		return getEdges(nodeA).stream().filter(n -> n.contains(nodeB)).findFirst().orElseGet(() -> null);
	}

	/**
	 * Gibt den ersten Knoten mit dem angegebenen Wert zurück oder null, falls
	 * dieser nicht gefunden wurde
	 * 
	 * @author Kenneth Kartono
	 * @param value Der zu suchende Wert
	 * @return Ein Knoten mit dem angegebenen Wert oder null
	 */
	public Node<T> getNode(T value) {
		// TODO: Graph<T>#getNode(T)
		return getNodes().stream().filter(n -> value.equals(n.getValue())).findFirst().orElseGet(() -> null);
	}

	/**
	 * Überprüft, ob alle Knoten in dem Graphen erreichbar sind.
	 * 
	 * @author Jonathan Geraldi Joewono
	 * @return true, wenn alle Knoten erreichbar sind
	 */
	public boolean allNodesConnected() {
		// TODO: Graph<T>#allNodesConnected()
		return route(getNodes().get(0), new ArrayList<Node<T>>(getNodes().size()),
				new ArrayList<Node<T>>(getNodes().size()));
	}

	/**
	 * Backtracking helper function to check if all nodes are connected.
	 * 
	 * @author Jonathan Geraldi Joewono
	 * @param current current node
	 * @param visited list of visited nodes
	 * @param path    nodes previously went through
	 * @return true when all nodes connected, otherwise false
	 */
	private boolean route(Node<T> current, ArrayList<Node<T>> visited, ArrayList<Node<T>> path) {
		if (visited.size() == getNodes().size()) {
			return true;
		}
		Node<T> now = current;
		List<Node<T>> nextPos = getEdges(current).stream().map(n -> n.getOtherNode(now))
				.filter(n -> !visited.contains(n)).collect(Collectors.toList());
		if (nextPos.isEmpty()) {
			// backtrack
			if (path.isEmpty())
				return false;
			if (!visited.contains(current))
				visited.add(current);
			current = path.get(path.size() - 1);
			path.remove(path.size() - 1);
			return route(current, visited, path);
		} else {
			if (!visited.contains(current))
				visited.add(current);
			path.add(current);
			return route(nextPos.get(0), visited, path);
		}
	}
}
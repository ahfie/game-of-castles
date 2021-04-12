package tests.student;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import base.Graph;

/**
 * Tests for allNodesConnected() function
 * 
 * @author Jonathan Geraldi Joewono
 */
public class GraphConnectionTest {

	Graph<String> graphA;
	Graph<Integer> graphB;

	@Test
	public void testGraphA() {
		graphA = new Graph<String>();
		for (String s : new String[] { "A", "B", "C", "D" }) {
			graphA.addNode(s);
		}
		graphA.addEdge(graphA.getNode("A"), graphA.getNode("B"));
		graphA.addEdge(graphA.getNode("B"), graphA.getNode("C"));
		// only A B C connected
		assertFalse(graphA.allNodesConnected());
		// add connection to D
		graphA.addEdge(graphA.getNode("A"), graphA.getNode("D"));
		assertTrue(graphA.allNodesConnected());
	}

	@Test
	public void testGraphB() {
		graphB = new Graph<Integer>();
		for (int i = 0; i < 10; i++)
			graphB.addNode(i);
		// only 0 to 5 connected
		for (int i = 0; i < 5; i++)
			graphB.addEdge(graphB.getNode(i), graphB.getNode(i + 1));
		assertFalse(graphB.allNodesConnected());
		// connect i to i+5 for i: 0 to 5
		for (int i = 0; i < 5; i++)
			graphB.addEdge(graphB.getNode(i), graphB.getNode(i + 5));
		assertTrue(graphB.allNodesConnected());
	}
}

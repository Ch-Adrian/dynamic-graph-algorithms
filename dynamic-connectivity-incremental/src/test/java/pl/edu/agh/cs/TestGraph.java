package pl.edu.agh.cs;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGraph {

    @Test
    public void testGraph() {
        Graph graph = new Graph();

        graph.addNonDirectedEdge(0, 1);
        assertTrue(graph.isConnected(0, 1));

        graph.addNonDirectedEdge(2, 3);
        assertTrue(graph.isConnected(2, 3));
        assertFalse(graph.isConnected(0,2));

        graph.addNonDirectedEdge(3, 4);
        assertTrue(graph.isConnected(2, 4));
        assertFalse(graph.isConnected(1, 4));

        graph.addNonDirectedEdge(5, 4);
        graph.addNonDirectedEdge(5, 2);

        assertTrue(graph.isConnected(2, 4));
        assertFalse(graph.isConnected(1, 4));

        graph.addNonDirectedEdge(5, 0);
        assertTrue(graph.isConnected(3, 0));

    }
}

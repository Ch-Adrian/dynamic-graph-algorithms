package pl.edu.agh.cs;

import org.junit.jupiter.api.Test;
import pl.edu.agh.cs.common.Edge;
import pl.edu.agh.cs.common.OperatingMode;
import pl.edu.agh.cs.common.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DynamicMSTTest {

    @Test
    public void test(){
        DynamicMST dynamicMST = new DynamicMST(10);
        ArrayList<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, 1));
        edges.add(new Edge(1, 2, 1));
        edges.add(new Edge(0, 3, 1));
        edges.add(new Edge(1, 4, 3));
        edges.add(new Edge(3, 4, 1));
        edges.add(new Edge(4, 5, 1));
        edges.add(new Edge(2, 5, 2));

        for(Edge edge: edges){
            dynamicMST.addEdge(edge.getBegin(), edge.getEnd(), edge.getWeight());
        }

        dynamicMST.changeOperatinMode(OperatingMode.DECREMENTAL);
        dynamicMST.calculateMST();

        assertTrue(dynamicMST.checkIfTreeEdgeExists(0, 1));
        assertTrue(dynamicMST.checkIfTreeEdgeExists(0, 3));
        assertTrue(dynamicMST.checkIfTreeEdgeExists(1, 2));
        assertTrue(dynamicMST.checkIfTreeEdgeExists(3, 4));
        assertTrue(dynamicMST.checkIfTreeEdgeExists(4, 5));

        assertTrue(dynamicMST.isConnected(0, 5));

        assertTrue(dynamicMST.checkIfNonTreeEdgeExists(4, 1));
        assertTrue(dynamicMST.checkIfNonTreeEdgeExists(2, 5));

        dynamicMST.deleteEdge(new Edge(0, 3, null));

        assertTrue(dynamicMST.isConnected(0, 5));

        assertFalse(dynamicMST.checkIfTreeEdgeExists(0, 3));
        assertTrue(dynamicMST.checkIfTreeEdgeExists(2, 5));

        dynamicMST.deleteEdge(new Edge(2, 5, null));

        assertTrue(dynamicMST.isConnected(0, 5));

        assertFalse(dynamicMST.checkIfTreeEdgeExists(2, 5));
        assertTrue(dynamicMST.checkIfTreeEdgeExists(1, 4));

        dynamicMST.deleteEdge(new Edge(1, 4, null));

        assertFalse(dynamicMST.isConnected(0, 5));
    }


    @Test
    public void testBruteForce() {
        Integer amtOfNodes = 100000;
        Integer amtOfEdges = 100000;
        Set<Edge> edges = new HashSet<>();
        DynamicMST dc = new DynamicMST(amtOfNodes * 2);
        for (int i = 0; i < amtOfEdges; i++) {
            Integer a = (int) (Math.random() * amtOfNodes);
            Integer b = (int) (Math.random() * amtOfNodes);
            Integer w = (int) (Math.random() * amtOfNodes);
            edges.add(new Edge(a, b, w));
        }

        for (Edge edge : edges) {
            dc.addEdge(edge.getBegin(), edge.getEnd(), edge.getWeight());
        }
        dc.changeOperatinMode(OperatingMode.DECREMENTAL);
        dc.calculateMST();

        for (Edge edge : edges) {
            dc.deleteEdge(edge);
        }
    }

}

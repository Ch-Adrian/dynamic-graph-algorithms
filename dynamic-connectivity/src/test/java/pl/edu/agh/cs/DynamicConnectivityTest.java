package pl.edu.agh.cs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DynamicConnectivityTest {

    @Test
    public void testDC() {
        DynamicConnectivity dc = new DynamicConnectivity(16);
        try {
            dc.addEdge(0, 1);
            dc.addEdge(1, 2);
            dc.addEdge(2, 3);
            dc.addEdge(2, 4);
            dc.addEdge(4, 6);
            dc.addEdge(6, 5);
            dc.addEdge(6, 7);
            dc.addEdge(4, 8);

            dc.addEdge(1, 3);
            dc.addEdge(0, 5);
            dc.addEdge(3, 8);
            dc.addEdge(8, 7);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        assertEquals(1, dc.getForestForLevel(0).getAmtOfTrees());
        System.out.println("show general tree:");
        dc.getForestForLevel(0).getTree(5).show();

        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(0,1));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(1,2));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(2,3));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(2,4));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(4,6));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(5,6));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(6,7));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(4,8));

        assertTrue(dc.getForestForLevel(0).checkIfNonTreeEdgeExists(1,3));
        assertTrue(dc.getForestForLevel(0).checkIfNonTreeEdgeExists(3,8));
        assertTrue(dc.getForestForLevel(0).checkIfNonTreeEdgeExists(0,5));
        assertTrue(dc.getForestForLevel(0).checkIfNonTreeEdgeExists(7,8));

        assertTrue(dc.isConnected(0, 5));

        try {
            dc.deleteEdge(2, 4);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        assertTrue(dc.isConnected(0, 5));

//        assertEquals(1, dc.getForestForLevel(0).getAmtOfTrees());
//        assertEquals(1, dc.getForestForLevel(1).getAmtOfTrees());

        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(0,5));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(0,1));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(1,2));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(2,3));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(4,6));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(5,6));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(6,7));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(4,8));

        assertTrue(dc.getForestForLevel(0).checkIfNonTreeEdgeExists(1,3));
        assertTrue(dc.getForestForLevel(0).checkIfNonTreeEdgeExists(3,8));

        // Forest lvl 1
        assertTrue(dc.getForestForLevel(1).checkIfTreeEdgeExists(0,1));
        assertTrue(dc.getForestForLevel(1).checkIfTreeEdgeExists(1,2));
        assertTrue(dc.getForestForLevel(1).checkIfTreeEdgeExists(2,3));

        try{
            dc.addEdge(2, 4);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        assertTrue(dc.isConnected(0, 5));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(0,5));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(0,1));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(1,2));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(2,3));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(2,4));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(4,6));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(5,6));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(6,7));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(4,8));

        assertTrue(dc.getForestForLevel(0).checkIfNonTreeEdgeExists(1,3));
        assertTrue(dc.getForestForLevel(0).checkIfNonTreeEdgeExists(3,8));
        assertTrue(dc.getForestForLevel(0).checkIfNonTreeEdgeExists(2,4));

        // Forest lvl 1
        assertTrue(dc.getForestForLevel(1).checkIfTreeEdgeExists(0,1));
        assertTrue(dc.getForestForLevel(1).checkIfTreeEdgeExists(1,2));
        assertTrue(dc.getForestForLevel(1).checkIfTreeEdgeExists(2,3));

        try{
            dc.deleteEdge(0, 5);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        assertTrue(dc.isConnected(0, 5));
        // Forest lvl 0
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(0,1));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(1,2));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(2,3));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(2,4));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(4,6));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(5,6));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(6,7));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(4,8));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(2,4));

        assertTrue(dc.getForestForLevel(0).checkIfNonTreeEdgeExists(1,3));
        assertTrue(dc.getForestForLevel(0).checkIfNonTreeEdgeExists(3,8));

        // Forest lvl 1
        assertTrue(dc.getForestForLevel(1).checkIfTreeEdgeExists(0,1));
        assertTrue(dc.getForestForLevel(1).checkIfTreeEdgeExists(1,2));
        assertTrue(dc.getForestForLevel(1).checkIfTreeEdgeExists(2,3));
    }

    @Test
    public void testDC2() {
        DynamicConnectivity dc = new DynamicConnectivity(3);
        try {
            dc.addEdge(0, 1);
            dc.addEdge(0, 2);
            dc.addEdge(1,2);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        assertTrue(dc.isConnected(1, 2));
        assertFalse(dc.isConnected(1, 10));

        try {
            dc.deleteEdge(0, 1);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        assertTrue(dc.isConnected(1, 0));

        try {
            dc.deleteEdge(0, 2);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        assertFalse(dc.isConnected(1, 0));
    }


}

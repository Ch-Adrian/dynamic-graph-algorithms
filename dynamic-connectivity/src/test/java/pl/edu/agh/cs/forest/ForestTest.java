package pl.edu.agh.cs.forest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.channels.ScatteringByteChannel;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ForestTest {

    @Test
    public void testIsConnected1() {
        Forest forest = new Forest(0, null);

        try {
            forest.addTreeEdge(0, 1);
            forest.addTreeEdge(0, 2);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        assertTrue(forest.isConnected(1, 2));
        assertFalse(forest.isConnected(1, 10));

    }

    @Test
    public void testIsConnected2() {
        Forest forest = new Forest(0, null);

        try {
            forest.addTreeEdge(0, 1);
            forest.addTreeEdge(2, 3);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        assertFalse(forest.isConnected(1, 2));
        assertFalse(forest.isConnected(1, 10));

        try {
            forest.addTreeEdge(0, 2);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        assertTrue(forest.isConnected(1, 3));

    }

    @Test
    @Disabled
    public void testAddNonTreeEdge() {
        Forest forest = new Forest(0, null);

        try {
            forest.addTreeEdge(0, 1);
            forest.addTreeEdge(0, 2);
            forest.addNonTreeEdge(2, 1);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        assertTrue(forest.isConnected(1, 2));
        assertFalse(forest.isConnected(1, 10));


        forest.deleteTreeEdge(0,1);

        assertFalse(forest.isConnected(1, 0));

        try {
            forest.findReplacementEdge(1, 0, 0);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        assertTrue(forest.isConnected(1, 0));

        forest.deleteTreeEdge(0, 2);

        assertFalse(forest.isConnected(1, 0));
    }

}

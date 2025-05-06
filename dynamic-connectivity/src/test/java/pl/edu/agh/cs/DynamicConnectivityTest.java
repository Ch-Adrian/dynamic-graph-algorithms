package pl.edu.agh.cs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

        assertTrue(dc.isConnected(0, 5));
    }

}

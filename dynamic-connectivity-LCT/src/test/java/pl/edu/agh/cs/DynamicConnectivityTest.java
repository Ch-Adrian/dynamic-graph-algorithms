package pl.edu.agh.cs;

import org.junit.jupiter.api.Test;
import pl.edu.agh.cs.common.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DynamicConnectivityTest {

    @Test
    public void test1(){
        DynamicConnectivity dc = new DynamicConnectivity(10);
        dc.addEdge(0, 1);
        dc.addEdge(0, 2);
        dc.addEdge(1, 2);

        assertTrue(dc.isConnected(1,2));

        dc.deleteEdge(0,1);
        assertTrue(dc.isConnected(1,2));

        dc.deleteEdge(2,1);
        assertFalse(dc.isConnected(1,2));
        assertTrue(dc.isConnected(0,2));
    }

    @Test
    public void test2(){
        Integer amtOfNodes = 100000;
        Integer amtOfEdges = 100000;
        Set<Pair<Integer, Integer>> edges = new HashSet<>();
        DynamicConnectivity dc = new DynamicConnectivity(amtOfNodes*2);
        for(int i =0; i<amtOfEdges; i++){
            Integer a = (int)(Math.random()*amtOfNodes);
            Integer b = (int)(Math.random()*amtOfNodes);
            edges.add(new Pair<>(a,b));
        }

        for(Pair<Integer, Integer> edge : edges){
            dc.addEdge(edge.getFirst(), edge.getSecond());
        }

        for(Pair<Integer, Integer> edge : edges){
            dc.deleteEdge(edge.getFirst(), edge.getSecond());
        }
    }

    @Test
    public void test3() {

    }

}

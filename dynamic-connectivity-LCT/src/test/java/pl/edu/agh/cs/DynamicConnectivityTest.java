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
    public void testBruteForce(){
        Integer amtOfNodes = 1000000;
        Integer amtOfEdges = 1000000;
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
        DynamicConnectivity dynamicConnectivity = new DynamicConnectivity(10);
        ArrayList<Pair<Integer, Integer>> treeEdges = new ArrayList<>();
        ArrayList<Pair<Integer, Integer>> nonTreeEdges = new ArrayList<>();

        treeEdges.add(new Pair<>(0,1));
        treeEdges.add(new Pair<>(1,2));
        treeEdges.add(new Pair<>(2,3));
        treeEdges.add(new Pair<>(2,4));
        treeEdges.add(new Pair<>(4,5));
        treeEdges.add(new Pair<>(4,8));
        treeEdges.add(new Pair<>(5,6));
        treeEdges.add(new Pair<>(5,7));

        nonTreeEdges.add(new Pair<>(0,6));
        nonTreeEdges.add(new Pair<>(1,3));
        nonTreeEdges.add(new Pair<>(3,8));
        nonTreeEdges.add(new Pair<>(8,7));

        for(Pair<Integer, Integer> edge : treeEdges){
            dynamicConnectivity.addEdge(edge.getFirst(), edge.getSecond());
        }

        for(Pair<Integer, Integer> edge : nonTreeEdges){
            dynamicConnectivity.addEdge(edge.getFirst(), edge.getSecond());
        }

        assertTrue(dynamicConnectivity.isConnected(1,5));
        dynamicConnectivity.deleteEdge(2,4);
        assertTrue(dynamicConnectivity.isConnected(1,5));
        dynamicConnectivity.deleteEdge(0,6);
        assertTrue(dynamicConnectivity.isConnected(1,5));
        dynamicConnectivity.deleteEdge(3,8);
        assertFalse(dynamicConnectivity.isConnected(1,5));
        assertTrue(dynamicConnectivity.isConnected(1,3));
        dynamicConnectivity.deleteEdge(1,2);
        assertTrue(dynamicConnectivity.isConnected(1,3));
        dynamicConnectivity.deleteEdge(1,3);
        assertFalse(dynamicConnectivity.isConnected(1,3));

    }

}

package pl.edu.agh.cs;

import org.junit.jupiter.api.Test;
import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.EulerTourTree;
import pl.edu.agh.cs.eulerTourTree.splay.Node;
import pl.edu.agh.cs.eulerTourTree.splay.SplayTree;
import pl.edu.agh.cs.forest.Forest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DynamicConnectivityTest {

    @Test
    public void testDC() {
        DynamicConnectivity dc = new DynamicConnectivity(16);
        Forest forest = dc.getForestForLevel(0);
        Map<Integer, Set<Integer>> nonTreeEdges = forest.getNonTreeEdges();
        Map<Pair<Integer, Integer>, Set<Node>> keyToNodes = forest.getKeyToNodes();

        dc.addEdge(0, 1);

        assertTrue(nonTreeEdges.isEmpty());
        assertEquals(4, keyToNodes.size());
        assertEquals(2, keyToNodes.get(new Pair<>(0,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,1)).size());

        dc.addEdge(1, 2);

        assertTrue(nonTreeEdges.isEmpty());
        assertEquals(7, keyToNodes.size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,0)).size());
        assertEquals(3, keyToNodes.get(new Pair<>(1,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,2)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,2)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,1)).size());

        dc.addEdge(2, 3);

        assertTrue(nonTreeEdges.isEmpty());
        assertEquals(10, keyToNodes.size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,0)).size());
        assertEquals(2, keyToNodes.get(new Pair<>(1,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,2)).size());
        assertEquals(3, keyToNodes.get(new Pair<>(2,2)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,3)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(3,3)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(3,2)).size());

        dc.addEdge(2, 4);
        dc.addEdge(4, 6);
        dc.addEdge(6, 5);
        dc.addEdge(6, 7);
        dc.addEdge(4, 8);

        assertTrue(nonTreeEdges.isEmpty());
        assertEquals(25, keyToNodes.size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,0)).size());
        assertEquals(2, keyToNodes.get(new Pair<>(1,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,2)).size());
        assertEquals(3, keyToNodes.get(new Pair<>(2,2)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,3)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(3,3)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(3,2)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,4)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(4,2)).size());
        assertEquals(4, keyToNodes.get(new Pair<>(4,4)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(4,6)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(6,4)).size());
        assertEquals(3, keyToNodes.get(new Pair<>(6,6)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(6,5)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(5,6)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(5,5)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(6,7)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(7,6)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(7,7)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(4,8)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(8,4)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(8,8)).size());
        assertEquals(new Pair<>(2, 4), forest.getRepresentativeTreeNode(0).get().key);

        dc.addEdge(1, 3);

        assertFalse(nonTreeEdges.isEmpty());
        assertEquals(2, nonTreeEdges.size());
        assertEquals(Integer.valueOf(3), nonTreeEdges.get(1).iterator().next());
        assertEquals(Integer.valueOf(1), nonTreeEdges.get(3).iterator().next());

        dc.addEdge(0, 5);
        dc.addEdge(3, 8);
        dc.addEdge(8, 7);

        assertFalse(nonTreeEdges.isEmpty());
        assertEquals(6, nonTreeEdges.size());
        assertEquals(Integer.valueOf(3), nonTreeEdges.get(1).iterator().next());
        assertEquals(Integer.valueOf(1), nonTreeEdges.get(3).iterator().next());
        assertEquals(Integer.valueOf(0), nonTreeEdges.get(5).iterator().next());
        assertEquals(Integer.valueOf(5), nonTreeEdges.get(0).iterator().next());
        //assertEquals(Integer.valueOf(8), nonTreeEdges.get(3).getLast());
        assertEquals(Integer.valueOf(3), nonTreeEdges.get(8).iterator().next());
        assertEquals(Integer.valueOf(8), nonTreeEdges.get(7).iterator().next());
        //assertEquals(Integer.valueOf(7), nonTreeEdges.get(8).getLast());

        assertEquals(25, keyToNodes.size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,0)).size());
        assertEquals(2, keyToNodes.get(new Pair<>(1,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,2)).size());
        assertEquals(3, keyToNodes.get(new Pair<>(2,2)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,3)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(3,3)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(3,2)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,4)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(4,2)).size());
        assertEquals(4, keyToNodes.get(new Pair<>(4,4)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(4,6)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(6,4)).size());
        assertEquals(3, keyToNodes.get(new Pair<>(6,6)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(6,5)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(5,6)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(5,5)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(6,7)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(7,6)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(7,7)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(4,8)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(8,4)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(8,8)).size());
        assertEquals(new Pair<>(2, 4), forest.getRepresentativeTreeNode(0).get().key);

        assertEquals(Integer.valueOf(1), dc.getForestForLevel(0).getAmtOfTrees());

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

        dc.deleteEdge(2, 4);

        assertTrue(dc.isConnected(0, 5));

        assertEquals(Integer.valueOf(1), dc.getForestForLevel(0).getAmtOfTrees());
        assertEquals(Integer.valueOf(1), dc.getForestForLevel(1).getAmtOfTrees());

        assertFalse(dc.getForestForLevel(0).checkIfTreeEdgeExists(2,4));
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

        dc.addEdge(2, 4);

        Forest forestLvl0 = dc.getForestForLevel(0);
        Forest forestLvl1 = dc.getForestForLevel(1);

        assertEquals(Integer.valueOf(1), dc.getForestForLevel(0).getAmtOfTrees());
        assertEquals(Integer.valueOf(1), dc.getForestForLevel(1).getAmtOfTrees());

        assertTrue(dc.isConnected(0, 5));
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
        assertTrue(dc.getForestForLevel(0).checkIfNonTreeEdgeExists(2,4));

        // Forest lvl 1
        assertTrue(dc.getForestForLevel(1).checkIfTreeEdgeExists(0,1));
        assertTrue(dc.getForestForLevel(1).checkIfTreeEdgeExists(1,2));
        assertTrue(dc.getForestForLevel(1).checkIfTreeEdgeExists(2,3));

        dc.deleteEdge(0, 5);

        assertTrue(dc.isConnected(0, 5));
        // Forest lvl 0
        assertFalse(dc.getForestForLevel(0).checkIfTreeEdgeExists(0,5));
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

        // Forest lvl 1
        assertTrue(dc.getForestForLevel(1).checkIfTreeEdgeExists(0,1));
        assertTrue(dc.getForestForLevel(1).checkIfTreeEdgeExists(1,2));
        assertTrue(dc.getForestForLevel(1).checkIfTreeEdgeExists(2,3));
    }

    @Test
    public void testDC2() {
        DynamicConnectivity dc = new DynamicConnectivity(3);
        Forest forest = dc.getForestForLevel(0);
        Map<Integer, Set<Integer>> nonTreeEdges = forest.getNonTreeEdges();
        Map<Pair<Integer, Integer>, Set<Node>> keyToNodes = forest.getKeyToNodes();

        dc.addEdge(0, 1);

        assertTrue(nonTreeEdges.isEmpty());
        assertEquals(4, keyToNodes.size());
        assertEquals(2, keyToNodes.get(new Pair<>(0,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,1)).size());

        dc.addEdge(0, 2);

        assertTrue(nonTreeEdges.isEmpty());
        assertEquals(7, keyToNodes.size());
        assertEquals(3, keyToNodes.get(new Pair<>(0,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,2)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,2)).size());

        dc.addEdge(1,2);

        assertFalse(nonTreeEdges.isEmpty());
        assertEquals(7, keyToNodes.size());
        assertEquals(3, keyToNodes.get(new Pair<>(0,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,2)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,2)).size());

        assertEquals(2, nonTreeEdges.size());
        assertEquals(Integer.valueOf(2), nonTreeEdges.get(1).iterator().next());
        assertEquals(Integer.valueOf(1), nonTreeEdges.get(2).iterator().next());

        assertTrue(dc.isConnected(1, 2));
        assertFalse(dc.isConnected(1, 10));

        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(0,1));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(0,2));

        assertTrue(dc.getForestForLevel(0).checkIfNonTreeEdgeExists(1,2));

        dc.deleteEdge(0, 1);

        assertFalse(dc.getForestForLevel(0).checkIfTreeEdgeExists(0,1));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(0,2));
        assertFalse(dc.getForestForLevel(0).checkIfNonTreeEdgeExists(1,2));
        assertTrue(dc.getForestForLevel(0).checkIfTreeEdgeExists(1,2));

        assertTrue(dc.isConnected(1, 0));

        dc.deleteEdge(0, 2);

        assertFalse(dc.isConnected(1, 0));
    }

    @Test
    public void testDCBruteForce(){
        Integer amtOfVertices = 1000001;
        DynamicConnectivity dc = new DynamicConnectivity(amtOfVertices);
        for(int i =0; i<amtOfVertices; i++){
            dc.addEdge(i, i+1);
        }

        if(dc.getForestForLevel(0).getKeyToNodes().get(new Pair<>(0,0)).iterator().next() != null){
            Node n = dc.getForestForLevel(0).getKeyToNodes().get(new Pair<>(0,0)).iterator().next();
            SplayTree splayTree = new SplayTree();
            Optional<Node> r = splayTree.getRootNode(n);
            if(r.isPresent()){
                System.out.println(r.get());
                System.out.println(r.get().sizeOfTree);
//                EulerTourTree.show(r.get());
            }
        }

        for(int i =0; i<amtOfVertices; i++){
            dc.deleteEdge(i, i+1);
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

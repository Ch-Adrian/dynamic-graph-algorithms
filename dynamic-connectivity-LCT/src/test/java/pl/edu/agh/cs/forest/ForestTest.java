package pl.edu.agh.cs.forest;

import org.junit.jupiter.api.Test;
import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.linkCutTree.LinkCutTree;
import pl.edu.agh.cs.linkCutTree.splay.Node;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ForestTest {

    @Test
    public void getEdgesTest(){
        Forest forest = new Forest(0, null);
        forest.addTreeEdge(0, 1);
        forest.addTreeEdge(1, 2);
        forest.addTreeEdge(1, 3);

        Set<Pair<Integer, Integer>> edges = forest.getEdges(0);
        assertEquals(3, edges.size());
        assertTrue(edges.contains(new Pair<>(0, 1)));
        assertTrue(edges.contains(new Pair<>(1, 2)));
        assertTrue(edges.contains(new Pair<>(1, 3)));
    }

    @Test
    public void getVerticesTest(){
        Forest forest = new Forest(0, null);
        forest.addTreeEdge(0, 1);
        forest.addTreeEdge(1, 2);
        forest.addTreeEdge(1, 3);

        Set<Integer> vertices = forest.getVertices(0);
        assertEquals(4, vertices.size());
        assertTrue(vertices.contains(0));
        assertTrue(vertices.contains(1));
        assertTrue(vertices.contains(2));
        assertTrue(vertices.contains(3));

    }

    @Test
    public void testIsConnected() {
        Forest forest = new Forest(0, null);

        forest.addTreeEdge(0, 1);
        forest.addTreeEdge(0, 2);

        assertTrue(forest.isConnected(1, 2));
        assertTrue(forest.isConnected(1, 1));
        assertFalse(forest.isConnected(1, 10));

    }

    @Test
    public void testAddTreeEdge(){
        ArrayList<Forest> arrayListForests = new ArrayList<>();
        arrayListForests.add(new Forest(0, arrayListForests));
        arrayListForests.add(new Forest(1, arrayListForests));
        arrayListForests.add(new Forest(2, arrayListForests));

        Forest forest0 = arrayListForests.get(0);

        forest0.addTreeEdge(0, 1);

        Map<Integer, Optional<Node>> keyToNode = forest0.getKeyToNodes();
        assertEquals(2, keyToNode.size());

        forest0.addTreeEdge(2, 3);
        assertEquals(4, keyToNode.size());

        forest0.addNonTreeEdge(1, 2);
        assertEquals(4, keyToNode.size());
        assertFalse(forest0.isConnected(0, 2));
        assertTrue(forest0.checkIfTreeEdgeExists(0, 1));
        assertTrue(forest0.checkIfTreeEdgeExists(2, 3));
        assertTrue(forest0.checkIfNonTreeEdgeExists(1, 2));

        forest0.addTreeEdge(0, 3);
        assertEquals(4, keyToNode.size());
        assertTrue(forest0.isConnected(0, 2));
        assertTrue(forest0.checkIfTreeEdgeExists(0, 3));

        forest0.deleteTreeEdge(0,3);
        assertFalse(forest0.isConnected(0, 2));
        assertFalse(forest0.checkIfTreeEdgeExists(0, 3));

        forest0.findReplacementEdge(1,2, 0);
        assertTrue(forest0.isConnected(0, 2));
        assertFalse(forest0.checkIfNonTreeEdgeExists(1,2));
        assertTrue(forest0.checkIfTreeEdgeExists(1,2));
    }

    @Test
    public void testFindReplacementEdge(){
        ArrayList<Forest> arrayListForests = new ArrayList<>();
        arrayListForests.add(new Forest(0, arrayListForests));
        arrayListForests.add(new Forest(1, arrayListForests));
        arrayListForests.add(new Forest(2, arrayListForests));

        Forest forest0 = arrayListForests.get(0);

        forest0.addTreeEdge(0, 1);
        forest0.addTreeEdge(2, 3);
        forest0.addNonTreeEdge(0, 1);
        forest0.addNonTreeEdge(1, 3);

        forest0.findReplacementEdge(0, 3, 0);

        Map<Integer, Optional<Node>> keyToNodes0 = forest0.getKeyToNodes();
        Map<Integer, Optional<Node>> keyToNodes1 = arrayListForests.get(1).getKeyToNodes();
        Map<Integer, Optional<Node>> keyToNodes2 = arrayListForests.get(2).getKeyToNodes();
        Map<Integer, LinkedHashSet<Integer>> nonTreeEdges0 = arrayListForests.get(0).getNonTreeEdges();
        Map<Integer, LinkedHashSet<Integer>> nonTreeEdges1 = arrayListForests.get(1).getNonTreeEdges();
        Map<Integer, LinkedHashSet<Integer>> nonTreeEdges2 = arrayListForests.get(2).getNonTreeEdges();

        assertEquals(4, keyToNodes0.size());
        assertTrue(forest0.checkIfTreeEdgeExists(1, 3));
        assertFalse(forest0.checkIfNonTreeEdgeExists(1, 3));

        assertEquals(2, keyToNodes1.size());

        assertEquals(2, nonTreeEdges1.size());
        assertEquals(1, nonTreeEdges1.get(1).size());
        assertEquals(1, nonTreeEdges1.get(0).size());

        assertEquals(0, nonTreeEdges2.size());
        assertEquals(0, keyToNodes2.size());
    }

    @Test
    public void testFindReplacementEdgeHigherLevel() {
        ArrayList<Forest> arrayListForests = new ArrayList<>();
        arrayListForests.add(new Forest(0, arrayListForests));
        arrayListForests.add(new Forest(1, arrayListForests));
        arrayListForests.add(new Forest(2, arrayListForests));

        Forest forest0 = arrayListForests.get(0);
        Forest forest1 = arrayListForests.get(1);

        Map<Integer, Optional<Node>> keyToNodes0 = forest0.getKeyToNodes();
        Map<Integer, Optional<Node>> keyToNodes1 = arrayListForests.get(1).getKeyToNodes();
        Map<Integer, Optional<Node>> keyToNodes2 = arrayListForests.get(2).getKeyToNodes();
        Map<Integer, LinkedHashSet<Integer>> nonTreeEdges0 = arrayListForests.get(0).getNonTreeEdges();
        Map<Integer, LinkedHashSet<Integer>> nonTreeEdges1 = arrayListForests.get(1).getNonTreeEdges();
        Map<Integer, LinkedHashSet<Integer>> nonTreeEdges2 = arrayListForests.get(2).getNonTreeEdges();

        forest0.addTreeEdge(0, 1);
        forest0.addTreeEdge(1, 2);
        forest0.addTreeEdge(1, 3);
        forest0.addTreeEdge(3, 4);
        forest0.addNonTreeEdge(0, 2);

        forest1.addTreeEdge(0, 1);
        forest1.addTreeEdge(1, 2);
        forest1.addNonTreeEdge(0, 2);


        assertEquals(2, nonTreeEdges0.size());
        assertEquals(1, nonTreeEdges0.get(2).size());
        assertEquals(1, nonTreeEdges0.get(0).size());

        assertEquals(2, nonTreeEdges1.size());
        assertEquals(1, nonTreeEdges1.get(2).size());
        assertEquals(1, nonTreeEdges1.get(0).size());

        assertEquals(0, nonTreeEdges2.size());
        assertEquals(0, keyToNodes2.size());

        forest0.deleteTreeEdge(0,1);
        forest1.deleteTreeEdge(0,1);

        forest0.findReplacementEdge(0, 1, 1);

        assertEquals(2, nonTreeEdges0.size());
        assertEquals(0, nonTreeEdges0.get(2).size());
        assertEquals(0, nonTreeEdges0.get(0).size());

        assertEquals(2, nonTreeEdges1.size());
        assertEquals(0, nonTreeEdges1.get(2).size());
        assertEquals(0, nonTreeEdges1.get(0).size());

        assertEquals(0, nonTreeEdges2.size());
        assertEquals(0, keyToNodes2.size());

    }

}

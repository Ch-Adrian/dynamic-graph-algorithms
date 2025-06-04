package pl.edu.agh.cs.forest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.splay.Node;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ForestTest {

    @Test
    public void testAddTreeEdge(){
        Forest forest = new Forest(0, null);
        Map<Pair<Integer, Integer>, Set<Node>> keyToNodes = forest.getKeyToNodes();

        forest.addTreeEdge(0,0);
        assertEquals(1, keyToNodes.size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,0)).size());

        forest.addTreeEdge(1,2);
        assertEquals(5, keyToNodes.size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,0)).size());
        assertEquals(2, keyToNodes.get(new Pair<>(1,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,2)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,2)).size());

        forest.addTreeEdge(0,1);
        assertEquals(7, keyToNodes.size());
        assertEquals(2, keyToNodes.get(new Pair<>(0,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,1)).size());
        assertEquals(2, keyToNodes.get(new Pair<>(1,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,2)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,2)).size());

        forest.addTreeEdge(2,3);
        assertEquals(10, keyToNodes.size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,1)).size());
        assertEquals(2, keyToNodes.get(new Pair<>(1,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,2)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,1)).size());
        assertEquals(3, keyToNodes.get(new Pair<>(2,2)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,3)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(3,2)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(3,3)).size());

        forest.addTreeEdge(4,3);
        assertEquals(13, keyToNodes.size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,1)).size());
        assertEquals(2, keyToNodes.get(new Pair<>(1,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,2)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,1)).size());
        assertEquals(2, keyToNodes.get(new Pair<>(2,2)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,3)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(3,2)).size());
        assertEquals(3, keyToNodes.get(new Pair<>(3,3)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(4,3)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(3,4)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(4,4)).size());
    }

    @Test
    public void testAddNonTreeEdge(){
        Forest forest = new Forest(0, null);
        Map<Integer, Set<Integer>> nonTreeEdges = forest.getNonTreeEdges();
        forest.addNonTreeEdge(0,1);
        assertEquals(2, nonTreeEdges.size());
        assertEquals(1, nonTreeEdges.get(0).size());
        assertEquals(1, nonTreeEdges.get(1).size());
    }

    @Test
    public void testDeleteNonTreeEdge(){
        Forest forest = new Forest(0, null);
        Map<Integer, Set<Integer>> nonTreeEdges = forest.getNonTreeEdges();

        forest.addNonTreeEdge(0,1);
        assertEquals(2, nonTreeEdges.size());
        assertEquals(1, nonTreeEdges.get(0).size());
        assertEquals(1, nonTreeEdges.get(1).size());

        forest.deleteNonTreeEdge(0, 1);
        assertEquals(2, nonTreeEdges.size());
        assertEquals(0, nonTreeEdges.get(0).size());
        assertEquals(0, nonTreeEdges.get(1).size());
    }

    @Test
    public void testDeleteTreeEdge(){
        Forest forest = new Forest(0, null);
        Map<Pair<Integer, Integer>, Set<Node>> keyToNodes = forest.getKeyToNodes();

        forest.addTreeEdge(1,2);
        assertEquals(4, keyToNodes.size());
        assertEquals(2, keyToNodes.get(new Pair<>(1,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,2)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,2)).size());

        forest.deleteTreeEdge(1,2);
        assertEquals(4, keyToNodes.size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,1)).size());
        assertEquals(0, keyToNodes.get(new Pair<>(2,1)).size());
        assertEquals(0, keyToNodes.get(new Pair<>(1,2)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(2,2)).size());
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

        Map<Pair<Integer, Integer>, Set<Node>> keyToNodes0 = forest0.getKeyToNodes();
        Map<Pair<Integer, Integer>, Set<Node>> keyToNodes1 = arrayListForests.get(1).getKeyToNodes();
        Map<Pair<Integer, Integer>, Set<Node>> keyToNodes2 = arrayListForests.get(2).getKeyToNodes();
        Map<Integer, Set<Integer>> nonTreeEdges0 = arrayListForests.get(0).getNonTreeEdges();
        Map<Integer, Set<Integer>> nonTreeEdges1 = arrayListForests.get(1).getNonTreeEdges();
        Map<Integer, Set<Integer>> nonTreeEdges2 = arrayListForests.get(2).getNonTreeEdges();

        assertEquals(10, keyToNodes0.size());
        assertEquals(1, keyToNodes0.get(new Pair<>(0,0)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(0,1)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(1,0)).size());
        assertEquals(3, keyToNodes0.get(new Pair<>(1,1)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(1,3)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(3,1)).size());
        assertEquals(2, keyToNodes0.get(new Pair<>(3,3)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(2,3)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(3,2)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(2,2)).size());

        assertEquals(3, nonTreeEdges0.size());
        assertEquals(1, nonTreeEdges0.get(1).size());
        assertEquals(1, nonTreeEdges0.get(0).size());
        assertEquals(0, nonTreeEdges0.get(3).size());

        assertEquals(4, keyToNodes1.size());
        assertEquals(2, keyToNodes1.get(new Pair<>(0,0)).size());
        assertEquals(1, keyToNodes1.get(new Pair<>(0,1)).size());
        assertEquals(1, keyToNodes1.get(new Pair<>(1,0)).size());
        assertEquals(1, keyToNodes1.get(new Pair<>(1,1)).size());

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

        Map<Pair<Integer, Integer>, Set<Node>> keyToNodes0 = forest0.getKeyToNodes();
        Map<Pair<Integer, Integer>, Set<Node>> keyToNodes1 = arrayListForests.get(1).getKeyToNodes();
        Map<Pair<Integer, Integer>, Set<Node>> keyToNodes2 = arrayListForests.get(2).getKeyToNodes();
        Map<Integer, Set<Integer>> nonTreeEdges0 = arrayListForests.get(0).getNonTreeEdges();
        Map<Integer, Set<Integer>> nonTreeEdges1 = arrayListForests.get(1).getNonTreeEdges();
        Map<Integer, Set<Integer>> nonTreeEdges2 = arrayListForests.get(2).getNonTreeEdges();

        forest0.addTreeEdge(0, 1);
        forest0.addTreeEdge(1, 2);
        forest0.addTreeEdge(1, 3);
        forest0.addTreeEdge(3, 4);
        forest0.addNonTreeEdge(0, 2);

        forest1.addTreeEdge(0, 1);
        forest1.addTreeEdge(1, 2);
        forest1.addNonTreeEdge(0, 2);

        assertEquals(13, keyToNodes0.size());
        assertEquals(1, keyToNodes0.get(new Pair<>(0,0)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(0,1)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(1,0)).size());
        assertEquals(3, keyToNodes0.get(new Pair<>(1,1)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(1,3)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(3,1)).size());
        assertEquals(3, keyToNodes0.get(new Pair<>(3,3)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(2,1)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(1,2)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(2,2)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(3,4)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(4,3)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(4,4)).size());

        assertEquals(2, nonTreeEdges0.size());
        assertEquals(1, nonTreeEdges0.get(2).size());
        assertEquals(1, nonTreeEdges0.get(0).size());

        assertEquals(7, keyToNodes1.size());
        assertEquals(1, keyToNodes1.get(new Pair<>(0,0)).size());
        assertEquals(1, keyToNodes1.get(new Pair<>(0,1)).size());
        assertEquals(1, keyToNodes1.get(new Pair<>(1,0)).size());
        assertEquals(3, keyToNodes1.get(new Pair<>(1,1)).size());
        assertEquals(1, keyToNodes1.get(new Pair<>(1,2)).size());
        assertEquals(1, keyToNodes1.get(new Pair<>(2,1)).size());
        assertEquals(1, keyToNodes1.get(new Pair<>(2,2)).size());

        assertEquals(2, nonTreeEdges1.size());
        assertEquals(1, nonTreeEdges1.get(2).size());
        assertEquals(1, nonTreeEdges1.get(0).size());

        assertEquals(0, nonTreeEdges2.size());
        assertEquals(0, keyToNodes2.size());

        forest0.deleteTreeEdge(0,1);
        forest1.deleteTreeEdge(0,1);

        forest0.findReplacementEdge(0, 1, 1);

        assertEquals(15, keyToNodes0.size());
        assertEquals(2, keyToNodes0.get(new Pair<>(0,0)).size());
        assertEquals(0, keyToNodes0.get(new Pair<>(0,1)).size());
        assertEquals(0, keyToNodes0.get(new Pair<>(1,0)).size());
        assertEquals(2, keyToNodes0.get(new Pair<>(1,1)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(1,3)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(3,1)).size());
        assertEquals(2, keyToNodes0.get(new Pair<>(3,3)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(2,1)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(1,2)).size());
        assertEquals(2, keyToNodes0.get(new Pair<>(2,2)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(3,4)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(4,3)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(4,4)).size());

        assertEquals(2, nonTreeEdges0.size());
        assertEquals(0, nonTreeEdges0.get(2).size());
        assertEquals(0, nonTreeEdges0.get(0).size());

        assertEquals(9, keyToNodes1.size());
        assertEquals(2, keyToNodes1.get(new Pair<>(0,0)).size());
        assertEquals(0, keyToNodes1.get(new Pair<>(0,1)).size());
        assertEquals(0, keyToNodes1.get(new Pair<>(1,0)).size());
        assertEquals(1, keyToNodes1.get(new Pair<>(0,2)).size());
        assertEquals(1, keyToNodes1.get(new Pair<>(2,0)).size());
        assertEquals(1, keyToNodes1.get(new Pair<>(1,1)).size());
        assertEquals(1, keyToNodes1.get(new Pair<>(1,2)).size());
        assertEquals(1, keyToNodes1.get(new Pair<>(2,1)).size());
        assertEquals(2, keyToNodes1.get(new Pair<>(2,2)).size());

        assertEquals(2, nonTreeEdges1.size());
        assertEquals(0, nonTreeEdges1.get(2).size());
        assertEquals(0, nonTreeEdges1.get(0).size());

        assertEquals(0, nonTreeEdges2.size());
        assertEquals(0, keyToNodes2.size());

    }

    @Test
    public void testFindReplacementEdgeHigherLevel2() {
        ArrayList<Forest> arrayListForests = new ArrayList<>();
        arrayListForests.add(new Forest(0, arrayListForests));
        arrayListForests.add(new Forest(1, arrayListForests));
        arrayListForests.add(new Forest(2, arrayListForests));

        Forest forest0 = arrayListForests.get(0);
        Forest forest1 = arrayListForests.get(1);

        Map<Pair<Integer, Integer>, Set<Node>> keyToNodes0 = forest0.getKeyToNodes();
        Map<Pair<Integer, Integer>, Set<Node>> keyToNodes1 = arrayListForests.get(1).getKeyToNodes();
        Map<Pair<Integer, Integer>, Set<Node>> keyToNodes2 = arrayListForests.get(2).getKeyToNodes();
        Map<Integer, Set<Integer>> nonTreeEdges0 = arrayListForests.get(0).getNonTreeEdges();
        Map<Integer, Set<Integer>> nonTreeEdges1 = arrayListForests.get(1).getNonTreeEdges();
        Map<Integer, Set<Integer>> nonTreeEdges2 = arrayListForests.get(2).getNonTreeEdges();

        forest0.addTreeEdge(0, 1);
        forest0.addTreeEdge(1, 2);
        forest0.addTreeEdge(1, 3);
        forest0.addTreeEdge(3, 4);
        forest0.addNonTreeEdge(0, 2);

        forest1.addTreeEdge(0, 1);
        forest1.addTreeEdge(1, 2);

        assertEquals(13, keyToNodes0.size());
        assertEquals(1, keyToNodes0.get(new Pair<>(0,0)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(0,1)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(1,0)).size());
        assertEquals(3, keyToNodes0.get(new Pair<>(1,1)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(1,3)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(3,1)).size());
        assertEquals(3, keyToNodes0.get(new Pair<>(3,3)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(2,1)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(1,2)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(2,2)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(3,4)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(4,3)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(4,4)).size());

        assertEquals(2, nonTreeEdges0.size());
        assertEquals(1, nonTreeEdges0.get(2).size());
        assertEquals(1, nonTreeEdges0.get(0).size());

        assertEquals(7, keyToNodes1.size());
        assertEquals(1, keyToNodes1.get(new Pair<>(0,0)).size());
        assertEquals(1, keyToNodes1.get(new Pair<>(0,1)).size());
        assertEquals(1, keyToNodes1.get(new Pair<>(1,0)).size());
        assertEquals(3, keyToNodes1.get(new Pair<>(1,1)).size());
        assertEquals(1, keyToNodes1.get(new Pair<>(1,2)).size());
        assertEquals(1, keyToNodes1.get(new Pair<>(2,1)).size());
        assertEquals(1, keyToNodes1.get(new Pair<>(2,2)).size());

        assertEquals(0, nonTreeEdges1.size());

        assertEquals(0, nonTreeEdges2.size());
        assertEquals(0, keyToNodes2.size());

        forest0.deleteTreeEdge(0,1);
        forest1.deleteTreeEdge(0,1);

        forest0.findReplacementEdge(0, 1, 1);

        assertEquals(15, keyToNodes0.size());
        assertEquals(2, keyToNodes0.get(new Pair<>(0,0)).size());
        assertEquals(0, keyToNodes0.get(new Pair<>(0,1)).size());
        assertEquals(0, keyToNodes0.get(new Pair<>(1,0)).size());
        assertEquals(2, keyToNodes0.get(new Pair<>(1,1)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(1,3)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(3,1)).size());
        assertEquals(2, keyToNodes0.get(new Pair<>(3,3)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(2,1)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(1,2)).size());
        assertEquals(2, keyToNodes0.get(new Pair<>(2,2)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(3,4)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(4,3)).size());
        assertEquals(1, keyToNodes0.get(new Pair<>(4,4)).size());

        assertEquals(2, nonTreeEdges0.size());
        assertEquals(0, nonTreeEdges0.get(2).size());
        assertEquals(0, nonTreeEdges0.get(0).size());

        assertEquals(7, keyToNodes1.size());
        assertEquals(1, keyToNodes1.get(new Pair<>(0,0)).size());
        assertEquals(0, keyToNodes1.get(new Pair<>(0,1)).size());
        assertEquals(0, keyToNodes1.get(new Pair<>(1,0)).size());
        assertEquals(2, keyToNodes1.get(new Pair<>(1,1)).size());
        assertEquals(1, keyToNodes1.get(new Pair<>(1,2)).size());
        assertEquals(1, keyToNodes1.get(new Pair<>(2,1)).size());
        assertEquals(1, keyToNodes1.get(new Pair<>(2,2)).size());

        assertEquals(1, nonTreeEdges1.size());// todo

        assertEquals(0, nonTreeEdges2.size());
        assertEquals(0, keyToNodes2.size());

    }

    @Test
    public void testCheckIfNonTreeEdgeExists(){
        Forest forest = new Forest(0, null);
        Map<Integer, Set<Integer>> nonTreeEdges = forest.getNonTreeEdges();
        forest.addNonTreeEdge(0,1);
        assertTrue(forest.checkIfNonTreeEdgeExists(0, 1));
        assertFalse(forest.checkIfNonTreeEdgeExists(0, 2));
        assertFalse(forest.checkIfNonTreeEdgeExists(2, 2));
    }

    @Test
    public void testCheckIfTreeEdgeExists(){
        Forest forest = new Forest(0, null);
        Map<Pair<Integer, Integer>, Set<Node>> keyToNodes = forest.getKeyToNodes();

        assertFalse(forest.checkIfTreeEdgeExists(0, 1));
        forest.addTreeEdge(0,1);
        assertTrue(forest.checkIfTreeEdgeExists(0, 1));
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
    public void testIsConnected1() {
        Forest forest = new Forest(0, null);

        forest.addTreeEdge(0, 1);
        forest.addTreeEdge(2, 3);

        assertFalse(forest.isConnected(1, 2));
        assertFalse(forest.isConnected(1, 10));

        forest.addTreeEdge(0, 2);

        assertTrue(forest.isConnected(1, 3));
    }

    @Test
    public void testGetAmtOfTrees(){
        Forest forest = new Forest(0, null);
        assertEquals(Integer.valueOf(0), forest.getAmtOfTrees());
        forest.addTreeEdge(0,1);
        assertEquals(Integer.valueOf(1), forest.getAmtOfTrees());
        forest.addTreeEdge(3,2);
        assertEquals(Integer.valueOf(2), forest.getAmtOfTrees());
        forest.addTreeEdge(1, 2);
        assertEquals(Integer.valueOf(1), forest.getAmtOfTrees());
    }

    @Test
    public void testAddNonTreeEdge2() {
        ArrayList<Forest> arrayListForests = new ArrayList<>();
        arrayListForests.add(new Forest(0, arrayListForests));
        arrayListForests.add(new Forest(1, arrayListForests));
        arrayListForests.add(new Forest(2, arrayListForests));

        Forest forest = arrayListForests.get(0);

        forest.addTreeEdge(0, 1);
        forest.addTreeEdge(0, 2);
        forest.addNonTreeEdge(2, 1);

        assertTrue(forest.isConnected(1, 2));
        assertFalse(forest.isConnected(1, 10));

        forest.deleteTreeEdge(0,1);
        assertFalse(forest.isConnected(1, 0));

        forest.findReplacementEdge(1, 0, 0);
        assertTrue(forest.isConnected(1, 0));

        forest.deleteTreeEdge(0, 2);
        assertFalse(forest.isConnected(1, 0));
    }

}

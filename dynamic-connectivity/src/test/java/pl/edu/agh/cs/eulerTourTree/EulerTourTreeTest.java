package pl.edu.agh.cs.eulerTourTree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.splay.Node;
import pl.edu.agh.cs.eulerTourTree.splay.SplayTree;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.edu.agh.cs.eulerTourTree.EulerTourTree.dfs;

public class EulerTourTreeTest {

    @BeforeEach
    void setUp() {
        EulerTourTree.resetKeyToNodes();
    }

    @Test
    public void testReroot(){
        EulerTourTree tree = new EulerTourTree();
        Node node = new Node(new Pair<>(0,0));
        node.right = new Node(new Pair<>(0,1));
        node.right.right = new Node(new Pair<>(1,1));
        node.right.right.right = new Node(new Pair<>(1,0));
        node.right.right.right.right = new Node(new Pair<>(0,0));
        node.right.right.right.right.right = new Node(new Pair<>(0,2));
        node.right.right.right.right.right.right = new Node(new Pair<>(2,2));
        node.right.right.right.right.right.right.right = new Node(new Pair<>(2,0));
        node.right.right.right.right.right.right.right.right = new Node(new Pair<>(0,0));

        Node n1 = node;
        Node n2 = node.right;
        while(n2 != null){
            n2.parent = n1;
            n1 = n2;
            n2 = n2.right;
        }

        Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes = new HashMap<>();
        keyToNodes.put(new Pair<>(0,0), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(0,1), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(1,1), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(1,0), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(0,2), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(2,2), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(2,0), new LinkedHashSet<>());

        keyToNodes.get(new Pair<>(0,0)).add(node);
        keyToNodes.get(new Pair<>(0,1)).add(node.right);
        keyToNodes.get(new Pair<>(1,1)).add(node.right.right);
        keyToNodes.get(new Pair<>(1,0)).add(node.right.right.right);
        keyToNodes.get(new Pair<>(0,0)).add(node.right.right.right.right);
        keyToNodes.get(new Pair<>(0,2)).add(node.right.right.right.right.right);
        keyToNodes.get(new Pair<>(2,2)).add(node.right.right.right.right.right.right);
        keyToNodes.get(new Pair<>(2,0)).add(node.right.right.right.right.right.right.right);
        keyToNodes.get(new Pair<>(0,0)).add(node.right.right.right.right.right.right.right.right);

        tree.setKeyToNodes(keyToNodes);
        tree.setSplayRoot(node);

        tree.reRoot(1);
        assertEquals(Integer.valueOf(1), tree.getRoot());

        tree.reRoot(2);
        assertEquals(Integer.valueOf(2), tree.getRoot());
    }

    @Test
    public void testAddEdge(){
        EulerTourTree tree = new EulerTourTree();
        tree.addEdge(0, 1);
        tree.addEdge(0, 2);
        tree.addEdge(1, 3);
        tree.addEdge(1, 4);
        tree.addEdge(2, 5);
        tree.addEdge(2, 6);

        tree.setSplayRoot(tree.getSplayRoot());
        dfs(tree.getSplayRoot());

        assertEquals(Integer.valueOf(2), tree.getRoot());
        assertEquals(new Node(new Pair<>(0,2)), tree.getSplayRoot());
        tree.reRoot(0);

        assertEquals(Integer.valueOf(0), tree.getRoot());

        tree.reRoot(1);
        assertEquals(Integer.valueOf(1), tree.getRoot());
    }

    @Test
    public void testLink(){
        EulerTourTree treeLeft = new EulerTourTree();
        treeLeft.addEdge(0,1);

        EulerTourTree treeRight = new EulerTourTree();
        treeRight.addEdge(2,3);

        treeLeft.link(1, 2, treeRight);

        Node splayRoot = treeLeft.getSplayRoot();
        assertEquals(new Pair<>(2,1), splayRoot.key);
        assertEquals(new Pair<>(1,1), splayRoot.right.key);
        assertEquals(new Pair<>(2,2), splayRoot.left.key);
        assertEquals(new Pair<>(2,2), splayRoot.left.left.right.key);
        assertEquals(new Pair<>(3,2), splayRoot.left.left.right.right.right.right.key);
        assertEquals(new Pair<>(1,0), splayRoot.left.left.left.left.left.left.right.key);
        assertEquals(Integer.valueOf(1), treeLeft.getRoot());

    }

    @Test
    public void testCutTree(){
        EulerTourTree tree = new EulerTourTree();
        tree.addEdge(0, 1);
        tree.addEdge(0, 2);
        tree.addEdge(1, 3);
        tree.addEdge(1, 4);
        tree.addEdge(2, 5);
        tree.addEdge(2, 6);

        Pair<EulerTourTree, EulerTourTree> halves = tree.cut(0,2);

        Node splayRootLeft = SplayTree.getRootNode(halves.getFirst().getSplayRoot());
        Node splayRootRight = SplayTree.getRootNode(halves.getSecond().getSplayRoot());
//        dfs(splayRootRight);
        assertEquals(new Pair<>(0,0), splayRootLeft.key);
        assertEquals(new Pair<>(0,1), splayRootLeft.right.left.key);
        assertEquals(new Pair<>(0,0), splayRootLeft.right.right.right.key);
        assertEquals(new Pair<>(4,1), splayRootLeft.right.left.right.right.right.right.right.right.right.right.key);
        assertEquals(13, splayRootLeft.sizeOfTree);

        assertEquals(new Pair<>(2,2), splayRootRight.key);
        assertEquals(new Pair<>(2,5), splayRootRight.right.key);
        assertEquals(new Pair<>(5,2), splayRootRight.right.right.right.key);
        assertEquals(new Pair<>(2,2), splayRootRight.right.right.right.right.right.right.right.right.key);
        assertEquals(9, splayRootRight.sizeOfTree);

//        dfs(splayRootRight);
    }

    @Test
    public void testCutTree1(){
        EulerTourTree tree = new EulerTourTree();
        tree.addEdge(0, 1);
        Pair<EulerTourTree, EulerTourTree> halves = tree.cut(0,1);
        Node splayRootLeft = SplayTree.getRootNode(halves.getFirst().getSplayRoot());
        Node splayRootRight = SplayTree.getRootNode(halves.getSecond().getSplayRoot());

        assertEquals(new Pair<>(1,1), splayRootLeft.key);
        assertEquals(new Pair<>(0,0), splayRootRight.key);
    }

}

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

public class EulerTourTreeTest {

    @BeforeEach
    void setUp() {

    }

    private void dfsSetParent(Node node){
        if(node.left != null){
            node.left.parent = node;
            dfsSetParent(node.left);
        }
        if(node.right != null){
            node.right.parent = node;
            dfsSetParent(node.right);
        }
    }

    @Test
    public void testReroot(){
        Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes = new HashMap<>();
        Node node = new Node(new Pair<>(0,0));
        node.right = new Node(new Pair<>(0,1));
        node.right.right = new Node(new Pair<>(1,1));
        node.right.right.right = new Node(new Pair<>(1,0));
        node.right.right.right.right = new Node(new Pair<>(0,0));
        node.right.right.right.right.right = new Node(new Pair<>(0,2));
        node.right.right.right.right.right.right = new Node(new Pair<>(2,2));
        node.right.right.right.right.right.right.right = new Node(new Pair<>(2,0));
        node.right.right.right.right.right.right.right.right = new Node(new Pair<>(0,0));

        dfsSetParent(node);

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

        EulerTourTree.reRoot(node, 1, keyToNodes);
        assertEquals(Integer.valueOf(1), EulerTourTree.getEulerTourRoot(node));

        EulerTourTree.reRoot(node, 2, keyToNodes);
        assertEquals(Integer.valueOf(2), EulerTourTree.getEulerTourRoot(node));
    }

    @Test
    public void testReroot2(){
        Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes = new HashMap<>();
        Node node = new Node(new Pair<>(4,6));
        node.right = new Node(new Pair<>(6,6));
        node.right.right = new Node(new Pair<>(6,5));
        node.right.right.right = new Node(new Pair<>(5,5));
        node.right.right.right.right = new Node(new Pair<>(5,6));
        node.right.right.right.right.right = new Node(new Pair<>(6,6));
        node.right.right.right.right.right.right = new Node(new Pair<>(6,7));
        node.right.right.right.right.right.right.right = new Node(new Pair<>(7,7));
        node.right.right.right.right.right.right.right.right = new Node(new Pair<>(7,6));
        node.right.right.right.right.right.right.right.right.right = new Node(new Pair<>(6,6));

        node.left = new Node(new Pair<>(4,4));
        node.left.right = new Node(new Pair<>(2,4));
        node.left.right.right = new Node(new Pair<>(4,4));
        node.left.right.left = new Node(new Pair<>(2,2));
        node.left.right.left.right = new Node(new Pair<>(1,2));
        node.left.right.left.right.right = new Node(new Pair<>(2,2));
        node.left.right.left.right.right.right = new Node(new Pair<>(2,3));
        node.left.right.left.right.right.right.right = new Node(new Pair<>(3,3));
        node.left.right.left.right.right.right.right.right = new Node(new Pair<>(3,2));
        node.left.right.left.right.right.right.right.right.right = new Node(new Pair<>(2,2));
        node.left.right.left.left = new Node(new Pair<>(4,2));
        node.left.right.left.right.left = new Node(new Pair<>(1,1));
        node.left.right.left.right.left.left = new Node(new Pair<>(2,1));
        node.left.right.left.right.left.right = new Node(new Pair<>(0,1));
        node.left.right.left.right.left.right.right = new Node(new Pair<>(1,1));
        node.left.right.left.right.left.right.left = new Node(new Pair<>(0,0));
        node.left.right.left.right.left.right.left.left = new Node(new Pair<>(1,0));

        node.left.left = new Node(new Pair<>(6,6));
        node.left.left.right = new Node(new Pair<>(6,4));

        dfsSetParent(node);

        keyToNodes.put(new Pair<>(0,0), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(0,1), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(1,1), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(1,0), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(1,2), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(2,1), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(2,2), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(2,3), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(3,2), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(3,3), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(2,4), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(4,2), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(4,4), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(4,6), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(6,4), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(6,6), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(6,5), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(5,6), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(5,5), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(6,7), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(7,6), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(7,7), new LinkedHashSet<>());

        keyToNodes.get(new Pair<>(4,6)).add(node);
        keyToNodes.get(new Pair<>(6,6)).add(node.right);
        keyToNodes.get(new Pair<>(6,5)).add(node.right.right);
        keyToNodes.get(new Pair<>(5,5)).add(node.right.right.right);
        keyToNodes.get(new Pair<>(5,6)).add(node.right.right.right.right);
        keyToNodes.get(new Pair<>(6,6)).add(node.right.right.right.right.right);
        keyToNodes.get(new Pair<>(6,7)).add(node.right.right.right.right.right.right);
        keyToNodes.get(new Pair<>(7,7)).add(node.right.right.right.right.right.right.right);
        keyToNodes.get(new Pair<>(7,6)).add(node.right.right.right.right.right.right.right.right);
        keyToNodes.get(new Pair<>(6,6)).add(node.right.right.right.right.right.right.right.right.right);
        keyToNodes.get(new Pair<>(4,4)).add(node.left);
        keyToNodes.get(new Pair<>(2,4)).add(node.left.right);
        keyToNodes.get(new Pair<>(4,4)).add(node.left.right.right);
        keyToNodes.get(new Pair<>(2,2)).add(node.left.right.left);
        keyToNodes.get(new Pair<>(1,2)).add(node.left.right.left.right);
        keyToNodes.get(new Pair<>(2,2)).add(node.left.right.left.right.right);
        keyToNodes.get(new Pair<>(2,3)).add(node.left.right.left.right.right.right);
        keyToNodes.get(new Pair<>(3,3)).add(node.left.right.left.right.right.right.right);
        keyToNodes.get(new Pair<>(3,2)).add(node.left.right.left.right.right.right.right.right);
        keyToNodes.get(new Pair<>(2,2)).add(node.left.right.left.right.right.right.right.right.right);
        keyToNodes.get(new Pair<>(4,2)).add(node.left.right.left.left);
        keyToNodes.get(new Pair<>(1,1)).add(node.left.right.left.right.left);
        keyToNodes.get(new Pair<>(2,1)).add(node.left.right.left.right.left.left);
        keyToNodes.get(new Pair<>(0,1)).add(node.left.right.left.right.left.right);
        keyToNodes.get(new Pair<>(1,1)).add(node.left.right.left.right.left.right.right);
        keyToNodes.get(new Pair<>(0,0)).add(node.left.right.left.right.left.right.left);
        keyToNodes.get(new Pair<>(1,0)).add(node.left.right.left.right.left.right.left.left);
        keyToNodes.get(new Pair<>(6,6)).add(node.left.left);
        keyToNodes.get(new Pair<>(6,4)).add(node.left.left.right);

        EulerTourTree.reRoot(node, 4, keyToNodes);
        assertEquals(Integer.valueOf(4), EulerTourTree.getEulerTourRoot(node));
//        tree.show();

    }

    @Test
    public void testAddEdge(){
        Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes = new HashMap<>();
        EulerTourTree.addEdge(null,0, 1, keyToNodes);
        tree.addEdge(0, 2);
        tree.addEdge(1, 3);
        tree.addEdge(1, 4);
        tree.addEdge(2, 5);
        tree.addEdge(2, 6);

        EulerTourTree.setSplayRoot(tree.getSplayRoot());

//        tree.show();
//
//        for(LinkedHashSet<Node> nodes: keyToNodes.values()) {
//            for(Node n: nodes) {
//                System.out.println(n.key+" reference: "+n);
//                if(n.parent != null) System.out.println("Parent: "+n.parent.key);
//            }
//        }
        assertEquals(Integer.valueOf(2), tree.getEulerTourRoot());
        assertEquals(new Pair<>(0,2), tree.getSplayRoot().key);
        tree.reRoot(0);

        assertEquals(Integer.valueOf(0), tree.getEulerTourRoot());

        tree.reRoot(1);
        assertEquals(Integer.valueOf(1), tree.getEulerTourRoot());
    }

    @Test
    public void testLink(){
        Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes = new HashMap<>();
        EulerTourTree treeLeft = new EulerTourTree(keyToNodes);
        treeLeft.addEdge(0,1);

        EulerTourTree treeRight = new EulerTourTree(keyToNodes);
        treeRight.addEdge(2,3);

        treeLeft.link(1, 2, treeRight);

        Node splayRoot = treeLeft.getSplayRoot();
        assertEquals(new Pair<>(2,1), splayRoot.key);
        assertEquals(new Pair<>(1,1), splayRoot.right.key);
        assertEquals(new Pair<>(2,2), splayRoot.left.key);
        assertEquals(new Pair<>(2,2), splayRoot.left.left.right.key);
        assertEquals(new Pair<>(3,2), splayRoot.left.left.right.right.right.right.key);
        assertEquals(new Pair<>(1,0), splayRoot.left.left.left.left.left.left.right.key);
        assertEquals(Integer.valueOf(1), treeLeft.getEulerTourRoot());

    }

    @Test
    public void testCutTree(){
        Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes = new HashMap<>();
        EulerTourTree tree = new EulerTourTree(keyToNodes);
        tree.addEdge(0, 1);
        tree.addEdge(0, 2);
        tree.addEdge(1, 3);
        tree.addEdge(1, 4);
        tree.addEdge(2, 5);
        tree.addEdge(2, 6);

        EulerTourTree secondTree = tree.cut(0,2);

        Node splayRootRight = SplayTree.getRootNode(tree.getSplayRoot());
        Node splayRootLeft = SplayTree.getRootNode(secondTree.getSplayRoot());

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

    }

    @Test
    public void testCutTree1(){
        Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes = new HashMap<>();
        EulerTourTree tree = new EulerTourTree(keyToNodes);
        tree.addEdge(0, 1);
        EulerTourTree secondTree = tree.cut(0,1);
        Node splayRootLeft = SplayTree.getRootNode(tree.getSplayRoot());
        Node splayRootRight = SplayTree.getRootNode(secondTree.getSplayRoot());

        assertEquals(new Pair<>(1,1), splayRootRight.key);
        assertEquals(new Pair<>(0,0), splayRootLeft.key);
    }

    @Test
    public void testDeleteEdge(){

    }

}

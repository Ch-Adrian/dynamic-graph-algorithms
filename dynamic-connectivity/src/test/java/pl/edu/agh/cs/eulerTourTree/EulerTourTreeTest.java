package pl.edu.agh.cs.eulerTourTree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.splay.Node;
import pl.edu.agh.cs.eulerTourTree.splay.SplayTree;

import java.lang.annotation.ElementType;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class EulerTourTreeTest {

    Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes = new HashMap<>();

    @BeforeEach
    void setUp() {
        keyToNodes = new HashMap<>();
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
    public void testCreateNewEulerTourTreeCheckPreconditions(){
        keyToNodes.put(new Pair<>(0,0), new LinkedHashSet<>());
        keyToNodes.get(new Pair<>(0,0)).add(new Node(new Pair<>(0,0)));
        assertThrows(RuntimeException.class, () -> EulerTourTree.createNewEulerTourTree(0, 1, keyToNodes));

        keyToNodes.put(new Pair<>(1,1), new LinkedHashSet<>());
        keyToNodes.get(new Pair<>(1,1)).add(new Node(new Pair<>(1,1)));
        keyToNodes.get(new Pair<>(0,0)).clear();
        assertThrows(RuntimeException.class, () -> EulerTourTree.createNewEulerTourTree(0, 1, keyToNodes));

        keyToNodes.get(new Pair<>(1,1)).clear();
        assertDoesNotThrow(() -> EulerTourTree.createNewEulerTourTree(0, 1, keyToNodes));

    }

    @Test
    public void testCreateNewEulerTourTree() {
        Node newTree = EulerTourTree.createNewEulerTourTree(0, 1, keyToNodes);
        assertEquals(new Pair<>(0, 0), newTree.key);
        assertEquals(4, keyToNodes.size());
        assertEquals(2, keyToNodes.get(new Pair<>(0,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,1)).size());
    }

    @Test
    public void testAddNode(){
        Node newNode = EulerTourTree.addNode(new Pair<>(0,0), keyToNodes);
        assertEquals(new Pair<>(0,0), newNode.key);
        assertEquals(1, keyToNodes.size());
    }

    @Test public void testReRootPreconditions(){
        assertThrows(RuntimeException.class, () -> EulerTourTree.reRoot(null, 0, keyToNodes));
    }

    @Test public void testReRoot(){
        Node node = new Node(new Pair<>(0,0));
        keyToNodes.put(new Pair<>(0,0), new LinkedHashSet<>());
        keyToNodes.get(new Pair<>(0,0)).add(node);

        Optional<Node> optNodeAfterReroot = EulerTourTree.reRoot(node, 0, keyToNodes);
        assertTrue(optNodeAfterReroot.isPresent());
        assertEquals(node, optNodeAfterReroot.get());
    }

    @Test public void testReRoot1(){
        Node node00 = EulerTourTree.addNode(new Pair<>(0, 0), keyToNodes);
        Node node01 = EulerTourTree.addNode(new Pair<>(0, 1), keyToNodes);
        Node node11 = EulerTourTree.addNode(new Pair<>(1, 1), keyToNodes);
        Node node10 = EulerTourTree.addNode(new Pair<>(1, 0), keyToNodes);
        Node node002 = EulerTourTree.addNode(new Pair<>(0, 0), keyToNodes);

        node00.right = node01;
        node01.right = node11;
        node11.right = node10;
        node10.right = node002;
        dfsSetParent(node00);

        assertEquals(Integer.valueOf(0), EulerTourTree.getEulerTourRoot(node00));
        Optional<Node> optNodeAfterReRoot = EulerTourTree.reRoot(node00, 1, keyToNodes);

        assertNotEquals(SplayTree.getRootNode(optNodeAfterReRoot.get()), SplayTree.getRootNode(node00));
        assertEquals(Integer.valueOf(1), EulerTourTree.getEulerTourRoot(optNodeAfterReRoot.get()));
    }

    @Test
    public void testReRoot2(){
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

        node = EulerTourTree.reRoot(node, 1, keyToNodes).get();
        assertEquals(Integer.valueOf(1), EulerTourTree.getEulerTourRoot(node));

        node = EulerTourTree.reRoot(node, 2, keyToNodes).get();
        assertEquals(Integer.valueOf(2), EulerTourTree.getEulerTourRoot(node));
    }

    @Test
    public void testReRoot3(){
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

        node = EulerTourTree.reRoot(node, 4, keyToNodes).get();
        assertEquals(Integer.valueOf(4), EulerTourTree.getEulerTourRoot(node));
    }

    @Test
    public void testAddEdgeToNonExistingVertex(){
        try {
            Node treeNode = EulerTourTree.createNewEulerTourTree(0, 1, keyToNodes);
            Optional<Node> optTreeNode = EulerTourTree.addEdgeToNonExistingVertex(treeNode, 0, 2, keyToNodes);
            optTreeNode = EulerTourTree.addEdgeToNonExistingVertex(optTreeNode.get(), 1, 3, keyToNodes);
            optTreeNode = EulerTourTree.addEdgeToNonExistingVertex(optTreeNode.get(), 1, 4, keyToNodes);
            optTreeNode = EulerTourTree.addEdgeToNonExistingVertex(optTreeNode.get(), 2, 5, keyToNodes);
            optTreeNode = EulerTourTree.addEdgeToNonExistingVertex(optTreeNode.get(), 2, 6, keyToNodes);

            assertEquals(Integer.valueOf(2), EulerTourTree.getEulerTourRoot(optTreeNode.get()));
            assertEquals(new Pair<>(0,2), SplayTree.getRootNode(optTreeNode.get()).get().key);

            treeNode = EulerTourTree.reRoot(optTreeNode.get(), 0, keyToNodes).get();
            assertEquals(Integer.valueOf(0), EulerTourTree.getEulerTourRoot(treeNode));

            treeNode = EulerTourTree.reRoot(treeNode, 1, keyToNodes).get();
            assertEquals(Integer.valueOf(1), EulerTourTree.getEulerTourRoot(treeNode));

        } catch (Exception e){
            System.out.println("Exception occurred!");
        }

//        tree.show();
//
//        for(LinkedHashSet<Node> nodes: keyToNodes.values()) {
//            for(Node n: nodes) {
//                System.out.println(n.key+" reference: "+n);
//                if(n.parent != null) System.out.println("Parent: "+n.parent.key);
//            }
//        }

    }

    @Test
    public void testLink(){
        try {
            Node treeNodeLeft = EulerTourTree.createNewEulerTourTree(0, 1, keyToNodes);
            Node treeNodeRight = EulerTourTree.createNewEulerTourTree(2, 3, keyToNodes);
            EulerTourTree.link(1, 2, keyToNodes);

            Node splayRoot = SplayTree.getRootNode(treeNodeRight).get();
            assertEquals(new Pair<>(2,1), splayRoot.key);
            assertEquals(new Pair<>(1,1), splayRoot.right.key);
            assertEquals(new Pair<>(2,2), splayRoot.left.key);
            assertEquals(new Pair<>(2,2), splayRoot.left.left.right.key);
            assertEquals(new Pair<>(3,2), splayRoot.left.left.right.right.right.right.key);
            assertEquals(new Pair<>(1,0), splayRoot.left.left.left.left.left.left.right.key);
            assertEquals(Integer.valueOf(1), EulerTourTree.getEulerTourRoot(splayRoot));
        } catch (Exception e){
            System.out.println("Exception occurred!");
        }

    }

    @Test
    public void testCutTree(){
        try {
            Node treeNode = EulerTourTree.createNewEulerTourTree(0, 1, keyToNodes);
            Optional<Node> optTreeNode = EulerTourTree.addEdgeToNonExistingVertex(treeNode, 0, 2, keyToNodes);
            optTreeNode = EulerTourTree.addEdgeToNonExistingVertex(optTreeNode.get(), 1, 3, keyToNodes);
            optTreeNode = EulerTourTree.addEdgeToNonExistingVertex(optTreeNode.get(), 1, 4, keyToNodes);
            optTreeNode = EulerTourTree.addEdgeToNonExistingVertex(optTreeNode.get(), 2, 5, keyToNodes);
            optTreeNode = EulerTourTree.addEdgeToNonExistingVertex(optTreeNode.get(), 2, 6, keyToNodes);

            Pair<Optional<Node>, Optional<Node>> trees = EulerTourTree.cut(0, 2, keyToNodes);

            Node splayRootLeft = SplayTree.getRootNode(trees.getFirst().get()).get();
            Node splayRootRight = SplayTree.getRootNode(trees.getSecond().get()).get();

            assertEquals(new Pair<>(0,0), splayRootLeft.key);
            assertEquals(new Pair<>(0,1), splayRootLeft.right.left.key);
            assertEquals(new Pair<>(0,0), splayRootLeft.right.right.right.key);
            assertEquals(new Pair<>(4,1), splayRootLeft.right.left.right.right.right.right.right.right.right.right.key);
            assertEquals(Integer.valueOf(13), splayRootLeft.sizeOfTree);

            assertEquals(new Pair<>(2,2), splayRootRight.key);
            assertEquals(new Pair<>(2,5), splayRootRight.right.key);
            assertEquals(new Pair<>(5,2), splayRootRight.right.right.right.key);
            assertEquals(new Pair<>(2,2), splayRootRight.right.right.right.right.right.right.right.right.key);
            assertEquals(Integer.valueOf(9), splayRootRight.sizeOfTree);

        } catch (Exception e){
            System.out.println("Exception occurred!");
        }
    }

    @Test
    public void testCutTree1(){
        try {
            Node treeNode = EulerTourTree.createNewEulerTourTree(0, 1, keyToNodes);
            Pair<Optional<Node>, Optional<Node>> trees = EulerTourTree.cut(0, 1, keyToNodes);
            Node splayRootRight = SplayTree.getRootNode(trees.getFirst().get()).get();
            Node splayRootLeft = SplayTree.getRootNode(trees.getSecond().get()).get();

            assertEquals(new Pair<>(1,1), splayRootRight.key);
            assertEquals(new Pair<>(0,0), splayRootLeft.key);
        } catch (Exception e){
            System.out.println("Exception occurred!");
        }
    }

    @Test
    public void testCutTree2(){
        try {
            Node treeNode = EulerTourTree.createNewEulerTourTree(0, 1, keyToNodes);
            Optional<Node> optTreeNode = EulerTourTree.addEdgeToNonExistingVertex(treeNode, 0, 2, keyToNodes);

            Pair<Optional<Node>, Optional<Node>> trees = EulerTourTree.cut(0, 1, keyToNodes);

            Node splayRootLeft = SplayTree.getRootNode(trees.getFirst().get()).get();
            Node splayRootRight = SplayTree.getRootNode(trees.getSecond().get()).get();

            assertEquals(new Pair<>(0,0), splayRootRight.key);
            assertEquals(Integer.valueOf(5), splayRootRight.sizeOfTree);
            assertEquals(new Pair<>(1,1), splayRootLeft.key);
            assertEquals(Integer.valueOf(1), splayRootLeft.sizeOfTree);
            assertEquals(7, keyToNodes.size());
            assertEquals(1, keyToNodes.get(new Pair<>(1,1)).size());
            assertEquals(2, keyToNodes.get(new Pair<>(0,0)).size());
            assertEquals(1, keyToNodes.get(new Pair<>(0,2)).size());
            assertEquals(1, keyToNodes.get(new Pair<>(2,2)).size());
            assertEquals(1, keyToNodes.get(new Pair<>(2,0)).size());

        } catch (Exception e){
            System.out.println("Exception occurred!");
        }
    }

    @Test
    public void testDeleteEdge(){

    }

}

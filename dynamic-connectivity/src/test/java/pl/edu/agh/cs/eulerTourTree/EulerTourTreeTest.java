package pl.edu.agh.cs.eulerTourTree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.splay.Node;
import pl.edu.agh.cs.eulerTourTree.splay.SplayTree;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class EulerTourTreeTest {

    Map<Pair<Integer, Integer>, Set<Node>> keyToNodes = new HashMap<>();
    static SelfBalancingTree splayTree = new SplayTree();
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
        EulerTourTree.addNode(new Pair<>(0,0), keyToNodes);
        assertThrows(RuntimeException.class, () -> EulerTourTree.createNewEulerTourTree(0, 1, keyToNodes));

        EulerTourTree.addNode(new Pair<>(1,1), keyToNodes);
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
        assertEquals(newNode, keyToNodes.get(new Pair<>(0, 0)).iterator().next());
    }

    @Test public void testReRootPreconditions(){
        assertThrows(RuntimeException.class, () -> EulerTourTree.reRoot(0, keyToNodes));
    }

    @Test public void testReRoot(){
        Node node = EulerTourTree.addNode(new Pair<>(0,0), keyToNodes);

        Optional<Node> optNodeAfterReroot = EulerTourTree.reRoot(0, keyToNodes);
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
        Optional<Node> optNodeAfterReRoot = EulerTourTree.reRoot(1, keyToNodes);

        assertNotEquals(splayTree.getRootNode(optNodeAfterReRoot.get()), splayTree.getRootNode(node00));
        assertEquals(Integer.valueOf(1), EulerTourTree.getEulerTourRoot(optNodeAfterReRoot.get()));
    }

    @Test
    public void testReRoot2(){
        Node node = new Node(new Pair<>(0,0));
        node.right = EulerTourTree.addNode(new Pair<>(0,1), keyToNodes);
        node.right.right = EulerTourTree.addNode(new Pair<>(1,1), keyToNodes);
        node.right.right.right = EulerTourTree.addNode(new Pair<>(1,0), keyToNodes);
        node.right.right.right.right = EulerTourTree.addNode(new Pair<>(0,0), keyToNodes);
        node.right.right.right.right.right = EulerTourTree.addNode(new Pair<>(0,2), keyToNodes);
        node.right.right.right.right.right.right = EulerTourTree.addNode(new Pair<>(2,2), keyToNodes);
        node.right.right.right.right.right.right.right = EulerTourTree.addNode(new Pair<>(2,0), keyToNodes);
        node.right.right.right.right.right.right.right.right = EulerTourTree.addNode(new Pair<>(0,0), keyToNodes);

        dfsSetParent(node);

        node = EulerTourTree.reRoot(1, keyToNodes).get();
        assertEquals(Integer.valueOf(1), EulerTourTree.getEulerTourRoot(node));

        node = EulerTourTree.reRoot(2, keyToNodes).get();
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

        node = EulerTourTree.reRoot(4, keyToNodes).get();
        assertEquals(Integer.valueOf(4), EulerTourTree.getEulerTourRoot(node));
    }

    @Test public void testLink(){
        Node node11 = EulerTourTree.addNode(new Pair<>(1,1), keyToNodes);
        assertThrows(RuntimeException.class, () -> EulerTourTree.link(0, 1, keyToNodes));
        assertThrows(RuntimeException.class, () -> EulerTourTree.link(1, 0, keyToNodes));
        assertDoesNotThrow(() -> EulerTourTree.link(1, 1, keyToNodes));
        assertEquals(1, keyToNodes.size());

        Node node00 = EulerTourTree.addNode(new Pair<>(0, 0), keyToNodes);
        EulerTourTree.link(0, 1, keyToNodes);

        assertEquals(4, keyToNodes.size());
        assertEquals(2, keyToNodes.get(new Pair<>(0,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,1)).size());

        Optional<Node> splayRootNode = splayTree.getRootNode(keyToNodes.get(new Pair<>(0,0)).iterator().next());

        assertEquals(new Pair<>(1,0), splayRootNode.get().key);
        assertEquals(node11, splayRootNode.get().left);

        assertDoesNotThrow(() -> EulerTourTree.link(0, 1, keyToNodes));
    }

    @Test
    public void testLink2(){
        Node treeNodeLeft = EulerTourTree.createNewEulerTourTree(0, 1, keyToNodes);
        Node treeNodeRight = EulerTourTree.createNewEulerTourTree(2, 3, keyToNodes);
        EulerTourTree.link(1, 2, keyToNodes);

        Node splayRoot = splayTree.getRootNode(treeNodeRight).get();
        assertEquals(new Pair<>(2,1), splayRoot.key);
        assertEquals(new Pair<>(1,1), splayRoot.right.key);
        assertEquals(new Pair<>(2,2), splayRoot.left.key);
        assertEquals(new Pair<>(2,3), splayRoot.left.left.right.key);
//        assertEquals(new Pair<>(3,2), splayRoot.left.left.right.right.right.right.key);
//        assertEquals(new Pair<>(1,0), splayRoot.left.left.left.left.left.left.right.key);
        assertEquals(Integer.valueOf(1), EulerTourTree.getEulerTourRoot(splayRoot));
    }

    @Test public void testCutTree0(){
        Node npde00 = EulerTourTree.addNode(new Pair<>(0,0), keyToNodes);
        assertThrows(RuntimeException.class, () -> EulerTourTree.cut(1,0, keyToNodes));
        assertThrows(RuntimeException.class, () -> EulerTourTree.cut(0,1, keyToNodes));

        Pair<Optional<Node>, Optional<Node>> out = EulerTourTree.cut(0, 0, keyToNodes);
        assertTrue(out.getFirst().isPresent());
        assertTrue(out.getSecond().isEmpty());
        assertEquals(new Pair<>(0,0), out.getFirst().get().key);

        Node node11 = EulerTourTree.addNode(new Pair<>(1,1), keyToNodes);
        Pair<Optional<Node>, Optional<Node>> out2 = EulerTourTree.cut(0, 1, keyToNodes);
        assertTrue(out2.getFirst().isPresent());
        assertTrue(out2.getSecond().isPresent());
        assertEquals(new Pair<>(0, 0), out2.getFirst().get().key);
        assertEquals(new Pair<>(1, 1), out2.getSecond().get().key);
    }

    @Test
    public void testCutTree(){
        EulerTourTree.createNewEulerTourTree(0, 1, keyToNodes);
        EulerTourTree.addEdgeToNonExistingVertex(0, 2, keyToNodes);
        EulerTourTree.addEdgeToNonExistingVertex(1, 3, keyToNodes);
        EulerTourTree.addEdgeToNonExistingVertex(1, 4, keyToNodes);
        EulerTourTree.addEdgeToNonExistingVertex(2, 5, keyToNodes);
        EulerTourTree.addEdgeToNonExistingVertex(2, 6, keyToNodes);

        Pair<Optional<Node>, Optional<Node>> trees = EulerTourTree.cut(0, 2, keyToNodes);

        Node splayRootLeft = splayTree.getRootNode(trees.getFirst().get()).get();
        Node splayRootRight = splayTree.getRootNode(trees.getSecond().get()).get();

        assertEquals(new Pair<>(0,0), splayRootLeft.key);
        assertEquals(new Pair<>(0,1), splayRootLeft.right.left.key);
        assertEquals(new Pair<>(0,0), splayRootLeft.right.right.right.key);
//        assertEquals(new Pair<>(4,1), splayRootLeft.right.left.right.right.right.right.right.right.right.right.key);
        assertEquals(Integer.valueOf(13), splayRootLeft.sizeOfTree);

        assertEquals(new Pair<>(2,2), splayRootRight.key);
        assertEquals(new Pair<>(2,5), splayRootRight.right.key);
        assertEquals(new Pair<>(5,2), splayRootRight.right.right.right.key);
//        assertEquals(new Pair<>(2,2), splayRootRight.right.right.right.right.right.right.right.right.key);
        assertEquals(Integer.valueOf(9), splayRootRight.sizeOfTree);
    }

    @Test
    public void testCutTree3(){
        EulerTourTree.createNewEulerTourTree(0, 1, keyToNodes);
        EulerTourTree.addEdgeToNonExistingVertex(0, 2, keyToNodes);
        EulerTourTree.addEdgeToNonExistingVertex(1, 3, keyToNodes);
        EulerTourTree.addEdgeToNonExistingVertex(1, 4, keyToNodes);
        EulerTourTree.addEdgeToNonExistingVertex(2, 5, keyToNodes);
        EulerTourTree.addEdgeToNonExistingVertex(2, 6, keyToNodes);

        Pair<Optional<Node>, Optional<Node>> trees = EulerTourTree.cut(2, 5, keyToNodes);

        Node splayRootLeft = splayTree.getRootNode(trees.getFirst().get()).get();
        Node splayRootRight = splayTree.getRootNode(trees.getSecond().get()).get();

        assertEquals(new Pair<>(5,5), splayRootLeft.key);
        assertEquals(Integer.valueOf(1), splayRootLeft.sizeOfTree);
        assertEquals(new Pair<>(0,2), splayRootRight.key);
        assertEquals(Integer.valueOf(21), splayRootRight.sizeOfTree);
        assertEquals(new Pair<>(2,2), splayRootRight.right.key);
        assertEquals(new Pair<>(2,6), splayRootRight.right.right.key);
        assertEquals(new Pair<>(6,2), splayRootRight.right.right.right.right.key);

        Pair<Optional<Node>, Optional<Node>> trees1 = EulerTourTree.cut(0, 2, keyToNodes);

        Node splayRootLeft1 = splayTree.getRootNode(trees1.getFirst().get()).get();
        Node splayRootRight1 = splayTree.getRootNode(trees1.getSecond().get()).get();

        assertEquals(new Pair<>(0,0), splayRootLeft1.key);
        assertEquals(Integer.valueOf(13), splayRootLeft1.sizeOfTree);
        assertEquals(new Pair<>(1,1), splayRootLeft1.right.key);
        assertEquals(new Pair<>(0,0), splayRootLeft1.right.right.right.key);

        assertEquals(new Pair<>(2,2), splayRootRight1.key);
        assertEquals(Integer.valueOf(5), splayRootRight1.sizeOfTree);
        assertEquals(new Pair<>(2,6), splayRootRight1.right.key);
        assertEquals(new Pair<>(6,2), splayRootRight1.right.right.right.key);

    }

    @Test
    public void testCutTree1(){
        EulerTourTree.createNewEulerTourTree(0, 1, keyToNodes);
        Pair<Optional<Node>, Optional<Node>> trees = EulerTourTree.cut(0, 1, keyToNodes);
        Node splayRootRight = splayTree.getRootNode(trees.getFirst().get()).get();
        Node splayRootLeft = splayTree.getRootNode(trees.getSecond().get()).get();

        assertEquals(new Pair<>(1,1), splayRootRight.key);
        assertEquals(new Pair<>(0,0), splayRootLeft.key);

        assertEquals(4, keyToNodes.size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,0)).size());
        assertEquals(0, keyToNodes.get(new Pair<>(1,0)).size());
        assertEquals(0, keyToNodes.get(new Pair<>(0,1)).size());
    }

    @Test
    public void testCutTree2(){
        EulerTourTree.createNewEulerTourTree(0, 1, keyToNodes);
        EulerTourTree.addEdgeToNonExistingVertex(0, 2, keyToNodes);

        Pair<Optional<Node>, Optional<Node>> trees = EulerTourTree.cut(0, 1, keyToNodes);

        Node splayRootLeft = splayTree.getRootNode(trees.getFirst().get()).get();
        Node splayRootRight = splayTree.getRootNode(trees.getSecond().get()).get();

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
        assertEquals(0, keyToNodes.get(new Pair<>(0,1)).size());
        assertEquals(0, keyToNodes.get(new Pair<>(1,0)).size());
    }

    @Test
    public void testAddEdgeToNonExistingVertex() {
        assertThrows(RuntimeException.class, () -> EulerTourTree.addEdgeToNonExistingVertex(0,1, keyToNodes));
    }

    @Test
    public void testAddEdgeToNonExistingVertex1(){
        EulerTourTree.createNewEulerTourTree(0, 1, keyToNodes);
        EulerTourTree.addEdgeToNonExistingVertex(0, 2, keyToNodes);
        EulerTourTree.addEdgeToNonExistingVertex(1, 3, keyToNodes);
        EulerTourTree.addEdgeToNonExistingVertex(1, 4, keyToNodes);
        EulerTourTree.addEdgeToNonExistingVertex(2, 5, keyToNodes);
        Optional<Node> optTreeNode = EulerTourTree.addEdgeToNonExistingVertex(2, 6, keyToNodes);

        assertEquals(Integer.valueOf(2), EulerTourTree.getEulerTourRoot(optTreeNode.get()));
        assertEquals(new Pair<>(0,2), splayTree.getRootNode(optTreeNode.get()).get().key);

        Node treeNode = EulerTourTree.reRoot(0, keyToNodes).get();
        assertEquals(Integer.valueOf(0), EulerTourTree.getEulerTourRoot(treeNode));

        treeNode = EulerTourTree.reRoot(1, keyToNodes).get();
        assertEquals(Integer.valueOf(1), EulerTourTree.getEulerTourRoot(treeNode));
    }

    @Test
    public void testDeleteEdge(){
        Node node11 = EulerTourTree.addNode(new Pair<>(1,1), keyToNodes);
        assertThrows(RuntimeException.class, () -> EulerTourTree.deleteEdge(0, 1, keyToNodes));
        assertThrows(RuntimeException.class, () -> EulerTourTree.deleteEdge(1, 0, keyToNodes));
        assertDoesNotThrow(() -> EulerTourTree.deleteEdge(1, 1, keyToNodes));
        assertEquals(1, keyToNodes.size());

        Node node00 = EulerTourTree.addNode(new Pair<>(0, 0), keyToNodes);
        EulerTourTree.deleteEdge(0, 1, keyToNodes);

        EulerTourTree.link(0, 1, keyToNodes);

        assertEquals(4, keyToNodes.size());
        assertEquals(2, keyToNodes.get(new Pair<>(0,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,1)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,1)).size());

        EulerTourTree.deleteEdge(0, 1, keyToNodes);

        assertEquals(4, keyToNodes.size());
        assertEquals(1, keyToNodes.get(new Pair<>(0,0)).size());
        assertEquals(0, keyToNodes.get(new Pair<>(0,1)).size());
        assertEquals(0, keyToNodes.get(new Pair<>(1,0)).size());
        assertEquals(1, keyToNodes.get(new Pair<>(1,1)).size());

    }

    @Test
    public void testGetEulerTourRoot(){
        assertEquals(Integer.valueOf(-1), EulerTourTree.getEulerTourRoot(null));
        EulerTourTree.createNewEulerTourTree(0, 1, keyToNodes);
        assertEquals(Integer.valueOf(0), EulerTourTree.getEulerTourRoot(keyToNodes.get(new Pair<>(0,0)).iterator().next()));
        assertEquals(Integer.valueOf(0), EulerTourTree.getEulerTourRoot(keyToNodes.get(new Pair<>(1,1)).iterator().next()));
    }

    @Test
    public void testGetVertices(){
        assertTrue(EulerTourTree.getVertices(null).isEmpty());

        EulerTourTree.createNewEulerTourTree(0, 1, keyToNodes);
        Set<Integer> vertices = EulerTourTree.getVertices(keyToNodes.get(new Pair<>(0,0)).iterator().next());
        assertTrue(vertices.contains(0));
        assertTrue(vertices.contains(1));
        assertFalse(vertices.contains(2));

        EulerTourTree.addEdgeToNonExistingVertex(0, 2, keyToNodes);
        EulerTourTree.reRoot(1, keyToNodes);
        vertices = EulerTourTree.getVertices(keyToNodes.get(new Pair<>(0,0)).iterator().next());
        assertTrue(vertices.contains(0));
        assertTrue(vertices.contains(1));
        assertTrue(vertices.contains(2));
    }

    @Test
    public void testGetEdges(){
        assertTrue(EulerTourTree.getEdges(null).isEmpty());

        EulerTourTree.createNewEulerTourTree(0, 1, keyToNodes);
        Set<Pair<Integer, Integer>> edges = EulerTourTree.getEdges(keyToNodes.get(new Pair<>(0,0)).iterator().next());
        assertTrue(edges.contains(new Pair<>(0,1)));
        assertFalse(edges.contains(new Pair<>(1,0)));
        assertFalse(edges.contains(new Pair<>(0,2)));

        EulerTourTree.addEdgeToNonExistingVertex(0, 2, keyToNodes);
        EulerTourTree.reRoot(1, keyToNodes);
        edges = EulerTourTree.getEdges(keyToNodes.get(new Pair<>(0,0)).iterator().next());
        assertTrue(edges.contains(new Pair<>(0,1)));
        assertFalse(edges.contains(new Pair<>(1,0)));
        assertFalse(edges.contains(new Pair<>(2,0)));
        assertTrue(edges.contains(new Pair<>(0,2)));
    }

    @Test
    public void testGetSizeOfATree(){
        assertEquals(0, EulerTourTree.getSizeOfTree(null));
        Node node = EulerTourTree.createNewEulerTourTree(0, 1, keyToNodes);
        assertEquals(5, EulerTourTree.getSizeOfTree(node));
    }

}

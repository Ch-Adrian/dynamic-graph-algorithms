package pl.edu.agh.cs.eulerTourTree.splay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.cs.common.Pair;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TestSplayTree {

    public ArrayList<ArrayList<Integer>> tree1;
    public Node root1;

    public ArrayList<ArrayList<Integer>> tree2;
    public Node root2;

    public Node dfs_start(ArrayList<ArrayList<Integer>> tree){
        boolean[] visited = new boolean[tree.size()];
        for(int i = 0; i < tree.size(); i++) visited[i] = false;
        ArrayList<Integer> eulerTour = new ArrayList<>();

        dfs(visited, tree, eulerTour, 1);
        Node root = new Node(new Pair<>(eulerTour.get(0), eulerTour.get(0)));
        for(int i = 0; i<eulerTour.size(); i++){
            if(i != 0)
                SplayTree.insertToRight(root, new Pair<>(eulerTour.get(i), eulerTour.get(i)));
            if(i+1 < eulerTour.size())
                SplayTree.insertToRight(root, new Pair<>(eulerTour.get(i), eulerTour.get(i+1)));
        }

        return root;
    }

    public void dfs(boolean[] visited, ArrayList<ArrayList<Integer>> tree, ArrayList<Integer> eulerTour, int nodeId){
        visited[nodeId] = true;
        eulerTour.add(nodeId);

        for(int i = 0; i < tree.get(nodeId).size(); i++){
            if(!visited[tree.get(nodeId).get(i)]){
                dfs(visited, tree, eulerTour, tree.get(nodeId).get(i));
                eulerTour.add(nodeId);
            }
        }

    }

    public void prepareTree1(){
        tree1 = new ArrayList<>();
        for(int i = 1; i <= 5; i++){ tree1.add(new ArrayList<>()); }
        tree1.get(1).add(2);
        tree1.get(2).add(1);
        tree1.get(2).add(3);
        tree1.get(3).add(2);
        tree1.get(2).add(4);
        tree1.get(4).add(2);

        root1 = dfs_start(tree1);
    }

    public void prepareTree2(){
        tree2 = new ArrayList<>();
        for(int i = 1; i <= 8; i++){ tree2.add(new ArrayList<>()); }
        tree2.get(1).add(2);
        tree2.get(2).add(3);
        tree2.get(3).add(2);
        tree2.get(2).add(4);
        tree2.get(4).add(5);
        tree2.get(5).add(6);
        tree2.get(6).add(5);
        tree2.get(5).add(7);
        tree2.get(7).add(5);
        tree2.get(5).add(4);
        tree2.get(4).add(2);
        tree2.get(2).add(1);

        root2 = dfs_start(tree2);

    }

    @BeforeEach
    public void init(){
        prepareTree1();
        prepareTree2();
    }

    @Test
    public void testUpdateSize1(){
        SplayTree.updateSize(null);
        Node treeNode = new Node(new Pair<>(0,0));

        assertEquals(Integer.valueOf(1), treeNode.sizeOfTree);

        Node treeNodeLeft = new Node(new Pair<>(1,0));
        Node treeNodeRight = new Node(new Pair<>(0,1));
        treeNode.right = treeNodeRight;
        treeNodeLeft.parent = treeNode;
        treeNode.left = treeNodeLeft;
        treeNodeRight.parent = treeNode;

        SplayTree.updateSize(treeNode);
        assertEquals(Integer.valueOf(3), treeNode.sizeOfTree);
    }

    @Test
    public void testLeftAndRightRotate(){
        SplayTree.leftRotate(root1.right);
        assertEquals(new Pair<>(1,2), root1.parent.key);
        SplayTree.leftRotate(root1.parent.right);
        assertEquals(new Pair<>(2,2), root1.parent.parent.key);
        root1 = root1.parent.parent;
        assertEquals(new Pair<>(2,3), root1.right.key);
        assertEquals(new Pair<>(2,2), root1.right.parent.key);
        assertEquals(new Pair<>(2,2), root1.left.parent.key);
        assertNull(root1.left.right);
        assertNull(root1.left.left.left);
        assertNull(root1.left.left.right);
        assertEquals(new Pair<>(1,2), root1.left.left.parent.key);

        SplayTree.leftRotate(root1.right);
        root1 = root1.parent;
        SplayTree.rightRotate(root1.left.left);

        assertEquals(new Pair<>(1,2), root1.left.key);
        assertEquals(new Pair<>(2,3), root1.left.parent.key);
        assertEquals(new Pair<>(2,2), root1.left.right.key);
        assertEquals(new Pair<>(1,2), root1.left.right.parent.key);
        assertNull(root1.left.right.left);
    }

    @Test
    public void testGetRoot(){
        assertEquals(root1, SplayTree.getRootNode(root1).get());
        SplayTree.leftRotate(root1.right.right);
        SplayTree.leftRotate(root1.right);
        assertEquals(new Pair<>(2,2), SplayTree.getRootNode(root1).get().key);
        assertEquals(new Pair<>(2,2), SplayTree.getRootNode(root1.right).get().key);
    }

    public void dfsNode(Node root){
        System.out.println(root);
        if(root.left != null) {
            System.out.println("left: ");
            dfsNode(root.left);
        }
        if(root.right != null) {
            System.out.println("right: ");
            dfsNode(root.right);
        }
        System.out.println("end");
    }

    @Test
    public void testSplay(){
        SplayTree.splay(root1.right.right.right.right.right.right.right);
        assertEquals(new Pair<>(2,4), root1.parent.key);
        assertNull(root1.left);
        assertEquals(new Pair<>(1,2), root1.right.key);
        root1 = SplayTree.getRootNode(root1).get();
        assertEquals(new Pair<>(1,1), root1.left.key);
        assertNull(root1.parent);
        assertEquals(new Pair<>(4,4), root1.right.key);
        assertEquals(new Pair<>(2,4), root1.left.parent.key);
        assertEquals(new Pair<>(1,1), root1.left.right.parent.key);
    }

    @Test
    public void testSplay2(){
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

        SplayTree.splay(node.right.right);
        assertEquals(new Pair<>(1,1), SplayTree.getRootNode(node).get().key);

    }

    @Test
    public void testDetach(){
        Optional<Node> optNode = SplayTree.detachSubTreeFromTree(null);
        assertTrue(optNode.isEmpty());

        Node treeNode = new Node(new Pair<>(0,0));
        optNode = SplayTree.detachSubTreeFromTree(treeNode);
        assertTrue(optNode.isPresent());
        assertEquals(treeNode, optNode.get());

        Node treeNodeLeft = new Node(new Pair<>(1,0));
        Node treeNodeRight = new Node(new Pair<>(0,1));

        treeNode.left = treeNodeLeft;
        treeNodeLeft.parent = treeNode;
        treeNode.right = treeNodeRight;
        treeNodeRight.parent = treeNode;

        optNode = SplayTree.detachSubTreeFromTree(treeNodeLeft);
        assertTrue(optNode.isPresent());
        assertEquals(treeNodeLeft, optNode.get());

        assertEquals(Integer.valueOf(2), treeNode.sizeOfTree);

        optNode = SplayTree.detachSubTreeFromTree(treeNodeRight);
        assertTrue(optNode.isPresent());
        assertEquals(treeNodeRight, optNode.get());

        assertEquals(Integer.valueOf(1), treeNode.sizeOfTree);
    }

    @Test
    public void testDetach2(){
        SplayTree.splay(root1.right.right.right.right.right.right.right);
        SplayTree.splay(root1.right.right.right.right);
        Optional<Node> rightTreeRoot = SplayTree.getRootNode(root1);

        Optional<Node> leftTreeRoot = SplayTree.detachSubTreeFromTree(root1);

        assertEquals(new Pair<>(3,3), rightTreeRoot.get().key);
        assertTrue(leftTreeRoot.isPresent());
        assertEquals(new Pair<>(1, 1), leftTreeRoot.get().key);
        assertNull(rightTreeRoot.get().left);
        assertNull(leftTreeRoot.get().parent);
    }

    @Test
    public void testPredecessor(){
        SplayTree.splay(root1.right.right.right.right.right.right.right);
        assertEquals(new Pair<>(2,2), SplayTree.predecessor(SplayTree.getRootNode(root1).get()).get().key);
    }

    @Test
    public void testSuccessor(){
        SplayTree.splay(root1.right.right.right.right.right.right.right);
        SplayTree.splay(root1.right.right.right.right);
        assertEquals(new Pair<>(3,2), SplayTree.successor(SplayTree.getRootNode(root1).get()).get().key);
    }

    @Test
    public void testFirstNode(){
        SplayTree.splay(root1.right.right.right.right.right.right.right);
        SplayTree.splay(root1.right.right.right.right);
        assertEquals(new Pair<>(1,1), SplayTree.firstNode(root1).get().key);
    }

    @Test
    public void testLastNode(){
        SplayTree.splay(root1.right.right.right.right.right.right.right);
        SplayTree.splay(root1.right.right.right.right);
        assertEquals(new Pair<>(1,1), SplayTree.lastNode(root1).get().key);
    }

    @Test
    public void testRemoveNode(){
        SplayTree.splay(root1.right.right.right.right.right.right.right);
        SplayTree.splay(root1.right.right.right.right);
        Node root;
        root = SplayTree.removeNode(root1).get();
        assertEquals(new Pair<>(1,2), root.left.key);
        assertEquals(new Pair<>(3,3), root.right.parent.key);
    }

    @Test
    public void testSplit(){
        Pair<Optional<Node>, Optional<Node>> pairOut = SplayTree.split(null);
        assertTrue(pairOut.getFirst().isEmpty());
        assertTrue(pairOut.getSecond().isEmpty());

        Node treeNode = new Node(new Pair<>(0,0));
        pairOut = SplayTree.split(treeNode);

        assertTrue(pairOut.getFirst().isPresent());
        assertEquals(treeNode, pairOut.getFirst().get());
        assertTrue(pairOut.getSecond().isEmpty());
    }

    @Test
    public void testSplit2(){
        Pair<Optional<Node>, Optional<Node>> trees = SplayTree.split(root1.right.right.right.right.right.right.right);
        assertTrue(trees.getFirst().isPresent());
        assertTrue(trees.getSecond().isPresent());
        assertEquals(new Pair<>(1,1), trees.getFirst().get().key);
        assertEquals(new Pair<>(4,4), trees.getSecond().get().key);
    }

    @Test
    public void testJoin(){
        Node treeNode = new Node(new Pair<>(0,0));
        Optional<Node> optNode = SplayTree.join(treeNode, null);
        assertTrue(optNode.isPresent());
        assertEquals(treeNode, optNode.get());

        optNode = SplayTree.join(null,treeNode);
        assertTrue(optNode.isPresent());
        assertEquals(treeNode, optNode.get());

        optNode = SplayTree.join(null, null);
        assertTrue(optNode.isEmpty());
    }

    @Test
    public void testJoin2(){
        Pair<Optional<Node>, Optional<Node>> trees = SplayTree.split(root1.right.right.right.right.right.right.right);
        assertTrue(trees.getFirst().isPresent());
        assertTrue(trees.getSecond().isPresent());
        Optional<Node> concatenatedTree = SplayTree.join(trees.getFirst().get(), trees.getSecond().get());
        assertTrue(concatenatedTree.isPresent());
        assertEquals(new Pair<>(2, 4), concatenatedTree.get().key);
    }

    @Test
    public void testGetRootNode(){
        assertTrue(SplayTree.getRootNode(null).isEmpty());
        Node treeNode = new Node(new Pair<>(0,0));
        assertEquals(treeNode, SplayTree.getRootNode(treeNode).get());
        Node parentNode = new Node(new Pair<>(1,1));
        parentNode.right = treeNode;
        treeNode.parent = parentNode;
        assertEquals(parentNode, SplayTree.getRootNode(treeNode).get());
    }

    @Test
    public void testPredecessor2(){
        assertTrue(SplayTree.predecessor(null).isEmpty());
        Node treeNode = new Node(new Pair<>(0,0));
        assertTrue(SplayTree.predecessor(treeNode).isEmpty());
        Node parentNode = new Node(new Pair<>(2,2));
        parentNode.left = treeNode;
        treeNode.parent = parentNode;
        assertEquals(treeNode, SplayTree.predecessor(parentNode).get());
        Node rightChildNode = new Node(new Pair<>(1,1));
        rightChildNode.parent = treeNode;
        treeNode.right = rightChildNode;
        assertEquals(rightChildNode, SplayTree.predecessor(parentNode).get());
    }

    @Test
    public void testSuccessor2(){
        assertTrue(SplayTree.successor(null).isEmpty());
        Node treeNode = new Node(new Pair<>(0,0));
        assertTrue(SplayTree.successor(treeNode).isEmpty());
        Node parentNode = new Node(new Pair<>(2,2));
        parentNode.right = treeNode;
        treeNode.parent = parentNode;
        assertEquals(treeNode, SplayTree.successor(parentNode).get());
        Node leftChildNode = new Node(new Pair<>(1,1));
        leftChildNode.parent = treeNode;
        treeNode.left = leftChildNode;
        assertEquals(leftChildNode, SplayTree.successor(parentNode).get());
    }

    @Test
    public void testFirstNode2(){
        assertTrue(SplayTree.firstNode(null).isEmpty());
        Node treeNode = new Node(new Pair<>(0,0));
        assertEquals(treeNode, SplayTree.firstNode(treeNode).get());
        Node leftNode = new Node(new Pair<>(1,1));
        Node rightNode = new Node(new Pair<>(2,2));
        treeNode.left = leftNode;
        leftNode.parent = treeNode;
        treeNode.right = rightNode;
        rightNode.parent = treeNode;
        assertEquals(leftNode, SplayTree.firstNode(rightNode).get());
    }

    @Test
    public void testLastNode2(){
        assertTrue(SplayTree.lastNode(null).isEmpty());
        Node treeNode = new Node(new Pair<>(0,0));
        assertEquals(treeNode, SplayTree.lastNode(treeNode).get());
        Node leftNode = new Node(new Pair<>(1,1));
        Node rightNode = new Node(new Pair<>(2,2));
        treeNode.left = leftNode;
        leftNode.parent = treeNode;
        treeNode.right = rightNode;
        rightNode.parent = treeNode;
        assertEquals(rightNode, SplayTree.lastNode(leftNode).get());
    }

    private static void resetNodes(ArrayList<Node> nodes){
        for(Node n: nodes){
            n.parent = null;
            n.left = null;
            n.right = null;
        }
    }

    @Test
    public void testRemoveNode2(){

        assertTrue(SplayTree.removeNode(null).isEmpty());
        Node treeNode = new Node(new Pair<>(3,3));
        assertTrue(SplayTree.removeNode(treeNode).isEmpty());
        Node parentNode = new Node(new Pair<>(0,0));
        Node leftNode = new Node(new Pair<>(1,1));
        Node rightNode = new Node(new Pair<>(4,4));
        Node parentNode2 = new Node(new Pair<>(5,5));
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(treeNode);
        nodes.add(parentNode);
        nodes.add(parentNode2);
        nodes.add(leftNode);
        nodes.add(rightNode);

        parentNode.right = treeNode;
        treeNode.parent = parentNode;
        assertEquals(parentNode, SplayTree.removeNode(treeNode).get());
        resetNodes(nodes);


        parentNode.right = treeNode;
        treeNode.parent = parentNode;
        treeNode.left = leftNode;
        leftNode.parent = treeNode;
        assertEquals(parentNode, SplayTree.removeNode(treeNode).get());
        resetNodes(nodes);

        parentNode.right = treeNode;
        treeNode.parent = parentNode;
        treeNode.right = rightNode;
        rightNode.parent = treeNode;
        assertEquals(parentNode, SplayTree.removeNode(treeNode).get());
        resetNodes(nodes);

        parentNode2.left = treeNode;
        treeNode.parent = parentNode2;
        treeNode.left = leftNode;
        leftNode.parent = treeNode;
        assertEquals(leftNode, SplayTree.removeNode(treeNode).get());
        resetNodes(nodes);

        parentNode2.left = treeNode;
        treeNode.parent = parentNode2;
        treeNode.right = rightNode;
        rightNode.parent = treeNode;
        assertEquals(parentNode2, SplayTree.removeNode(treeNode).get());
        assertEquals(rightNode, parentNode2.left);
        resetNodes(nodes);

        parentNode2.left = treeNode;
        treeNode.parent = parentNode2;
        treeNode.right = rightNode;
        rightNode.parent = treeNode;
        treeNode.left = leftNode;
        leftNode.parent = treeNode;
        assertEquals(leftNode, SplayTree.removeNode(treeNode).get());
        assertEquals(parentNode2, leftNode.right);
        assertEquals(rightNode, parentNode2.left);
        resetNodes(nodes);

        Node leftRightNode = new Node(new Pair<>(2,2));
        parentNode2.left = treeNode;
        treeNode.parent = parentNode2;
        treeNode.right = rightNode;
        rightNode.parent = treeNode;
        treeNode.left = leftNode;
        leftNode.parent = treeNode;
        leftNode.right = leftRightNode;
        leftRightNode.parent = leftNode;
        assertEquals(leftRightNode, SplayTree.removeNode(treeNode).get());
        assertEquals(parentNode2, leftRightNode.right);
        assertEquals(rightNode, parentNode2.left);
        assertEquals(leftNode, leftRightNode.left);
        resetNodes(nodes);
    }

    @Test
    public void testGetSize(){
        assertEquals(Integer.valueOf(0), SplayTree.getSizeOfTree(null));
        assertEquals(Integer.valueOf(13), SplayTree.getSizeOfTree(root1));
        SplayTree.splay(root1.right.right.right.right.right.right.right);
        assertEquals(Integer.valueOf(13), SplayTree.getSizeOfTree(root1));
        assertEquals(Integer.valueOf(7), root1.sizeOfTree);
        assertEquals(Integer.valueOf(5), root1.parent.right.sizeOfTree);

        SplayTree.splay(root1.right.right.right.right);
        assertEquals(Integer.valueOf(13), SplayTree.getSizeOfTree(root1));
        assertEquals(Integer.valueOf(4), root1.sizeOfTree);
        assertEquals(Integer.valueOf(2), root1.parent.right.left.sizeOfTree);
        assertEquals(Integer.valueOf(8), root1.parent.right.sizeOfTree);

        SplayTree.insertToRight(SplayTree.getRootNode(root1).get(), new Pair<>(8,8));
        assertEquals(Integer.valueOf(14), root1.parent.sizeOfTree);
        SplayTree.removeNode(SplayTree.lastNode(SplayTree.getRootNode(root1).get()).get());
        assertEquals(Integer.valueOf(13), root1.parent.sizeOfTree);

        Node rightTree = root1.parent;
        SplayTree.detachSubTreeFromTree(root1);
        assertEquals(Integer.valueOf(4), root1.sizeOfTree);
        assertEquals(Integer.valueOf(9), rightTree.sizeOfTree);

        SplayTree.join(root1, rightTree);
        assertEquals(Integer.valueOf(13), SplayTree.getSizeOfTree(root1));

    }

}

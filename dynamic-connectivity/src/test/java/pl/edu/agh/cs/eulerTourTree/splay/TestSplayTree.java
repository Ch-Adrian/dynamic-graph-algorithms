package pl.edu.agh.cs.eulerTourTree.splay;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.cs.common.Pair;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
        assertEquals(root1, SplayTree.getRootNode(root1));
        SplayTree.leftRotate(root1.right.right);
        SplayTree.leftRotate(root1.right);
        assertEquals(new Pair<>(2,2), SplayTree.getRootNode(root1).key);
        assertEquals(new Pair<>(2,2), SplayTree.getRootNode(root1.right).key);
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
        root1 = SplayTree.getRootNode(root1);
        assertEquals(new Pair<>(1,1), root1.left.key);
        assertNull(root1.parent);
        assertEquals(new Pair<>(4,4), root1.right.key);
        assertEquals(new Pair<>(2,4), root1.left.parent.key);
        assertEquals(new Pair<>(1,1), root1.left.right.parent.key);
    }

    @Test
    public void testDetach(){
        SplayTree.splay(root1.right.right.right.right.right.right.right);
        SplayTree.splay(root1.right.right.right.right);
        Node rightTreeRoot = SplayTree.getRootNode(root1);
        Node leftTreeRoot = SplayTree.detachNodeFromTree(root1);
        assertEquals(new Pair<>(3,3), rightTreeRoot.key);
        assertEquals(new Pair<>(1,1), leftTreeRoot.key);
        assertNull(rightTreeRoot.left);
        assertNull(leftTreeRoot.parent);
    }

    @Test
    public void testPredecessor(){
        SplayTree.splay(root1.right.right.right.right.right.right.right);
        assertEquals(new Pair<>(2,2), SplayTree.predecessor(SplayTree.getRootNode(root1)).key);
    }

    @Test
    public void testSuccessor(){
        SplayTree.splay(root1.right.right.right.right.right.right.right);
        SplayTree.splay(root1.right.right.right.right);
        assertEquals(new Pair<>(3,2), SplayTree.successor(SplayTree.getRootNode(root1)).key);
    }

    @Test
    public void testFirstNode(){
        SplayTree.splay(root1.right.right.right.right.right.right.right);
        SplayTree.splay(root1.right.right.right.right);
        assertEquals(new Pair<>(1,1), SplayTree.firstNode(root1).key);
    }

    @Test
    public void testLastNode(){
        SplayTree.splay(root1.right.right.right.right.right.right.right);
        SplayTree.splay(root1.right.right.right.right);
        assertEquals(new Pair<>(1,1), SplayTree.lastNode(root1).key);
    }

    @Test
    public void testRemoveNode(){
        SplayTree.splay(root1.right.right.right.right.right.right.right);
        SplayTree.splay(root1.right.right.right.right);
        Node root = SplayTree.getRootNode(root1);
        SplayTree.removeNode(root1);
        assertEquals(new Pair<>(1,2), root.left.key);
        assertEquals(new Pair<>(3,3), root1.right.parent.key);
    }

    @Test
    public void testSplit(){
        Pair<Node, Node> trees = SplayTree.split(root1.right.right.right.right.right.right.right);
        assertEquals(new Pair<>(1,1), trees.getFirst().key);
        assertEquals(new Pair<>(4,4), trees.getSecond().key);
    }

    @Test
    public void testJoin(){
        Pair<Node, Node> trees = SplayTree.split(root1.right.right.right.right.right.right.right);
        Node concatenatedTree = SplayTree.join(trees.getFirst(), trees.getSecond());
        assertEquals(new Pair<>(2,4), concatenatedTree.key);
        dfsNode(concatenatedTree);
    }

}

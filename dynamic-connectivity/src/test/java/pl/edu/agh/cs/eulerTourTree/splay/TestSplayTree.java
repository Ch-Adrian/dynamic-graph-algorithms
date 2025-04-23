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
    public void testSplay(){
        SplayTree.splay(root1.right.right.right.right.right.right.right);
        root1 = SplayTree.getRootNode(root1);

    }

}

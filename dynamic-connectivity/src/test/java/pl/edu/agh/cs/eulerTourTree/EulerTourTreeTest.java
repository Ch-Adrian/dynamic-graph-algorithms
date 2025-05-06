package pl.edu.agh.cs.eulerTourTree;

import org.junit.jupiter.api.Test;
import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.splay.Node;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EulerTourTreeTest {


    public static void dfs(Node n){
        System.out.println("dfs: "+n.key);
        if(n.left != null){
            System.out.println("Go left: ");
            dfs(n.left);
        }
        if(n.right != null) {
            System.out.println("Go right: ");
            dfs(n.right);
        }
        System.out.println("back");
    }

    @Test
    public void testReroot1(){
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
}

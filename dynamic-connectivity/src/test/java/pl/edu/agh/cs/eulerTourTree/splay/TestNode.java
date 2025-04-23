package pl.edu.agh.cs.eulerTourTree.splay;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.edu.agh.cs.common.Pair;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class TestNode {

    static Node parent;
    static Node left;
    static Node right;
    static Node root;

    @BeforeAll
    public static void init(){
        parent = new Node(new Pair<>(0, 0));
        left = new Node(new Pair<>(2, 0));
        right = new Node(new Pair<>(0, 2));
        root = new Node(new Pair<>(1, 1));

        root.parent = parent;
        parent.left = root;
        root.left = left;
        left.parent = root;
        root.right = right;
        right.parent = root;

    }

    @Test
    public void testNode(){
        assertEquals(1, (int)root.key.getFirst());
        assertEquals(1, (int)root.key.getSecond());
        assertEquals(2, (int)root.left.key.getFirst());
        assertEquals(2, (int)root.right.key.getSecond());
        assertEquals(0, (int)root.parent.key.getFirst());
        assertEquals(1, (int)parent.left.key.getSecond());
        assertNull(parent.right);
        assertEquals(1, (int)left.parent.key.getFirst());
        assertEquals(1, (int)right.parent.key.getFirst());
    }
}

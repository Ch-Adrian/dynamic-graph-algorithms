package pl.edu.agh.cs.linkCutTree.splay;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SplayTreeTest {

    private static Node tree;

    @BeforeEach
    public void createMiniTree(){
        Node left = new Node(0);
        Node right = new Node(2);
        Node node = new Node(1);
        Node parent = new Node(3);

        parent.pathParent = new Node(4);
        node.parent = parent;
        parent.left = node;

        node.left = left;
        left.parent = node;

        node.right = right;
        right.parent = node;
        tree = node;
        SplayTree splayTree = new SplayTree();
        splayTree.updateSize(left);
        splayTree.updateSize(right);
        splayTree.updateSize(node);
        splayTree.updateSize(parent);
        assert(left.sizeOfTree == 1);
        assert(right.sizeOfTree == 1);
        assert(node.sizeOfTree == 3);
        assert(parent.sizeOfTree == 4);
        assert(parent.pathParent.sizeOfTree == 1);
    }

    @Test
    public void rightRotateTest(){
        SplayTree splayTree = new SplayTree();
        Node parent = tree.parent;
        Node node = tree;

        assertEquals(Integer.valueOf(4), parent.pathParent.key);
        splayTree.rightRotate(node);

        assertEquals(Integer.valueOf(4), node.pathParent.key);
        assertNull(parent.pathParent);
        assertEquals(Integer.valueOf(4), splayTree.getSizeOfSplayTree(parent));
    }

    @Test
    public void leftRotateTest(){
        Node parent = tree.parent;
        Node node = tree;
        parent.right = node;
        parent.left = null;

        SplayTree splayTree = new SplayTree();

        assertEquals(Integer.valueOf(4), parent.pathParent.key);
        splayTree.leftRotate(node);

        assertEquals(Integer.valueOf(4), node.pathParent.key);
        assertNull(parent.pathParent);
        assertNull(parent.left);
        assertEquals(Integer.valueOf(2), parent.sizeOfTree);
        assertEquals(Integer.valueOf(4), splayTree.getSizeOfSplayTree(parent));
    }

    @Test
    public void splayTest(){
        SplayTree splayTree = new SplayTree();
        Node left = tree.left;

        splayTree.splay(left);
        assertEquals(Integer.valueOf(4), left.pathParent.key);
        assertNull(left.parent);
        assertEquals(Integer.valueOf(1), left.right.key);
        assertEquals(Integer.valueOf(4), splayTree.getSizeOfSplayTree(left));
    }



}

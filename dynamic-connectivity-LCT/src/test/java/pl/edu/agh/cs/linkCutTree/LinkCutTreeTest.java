package pl.edu.agh.cs.linkCutTree;

import org.junit.jupiter.api.Test;
import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.linkCutTree.splay.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class LinkCutTreeTest {


    @Test
    public void createNewLinkCutTreeTest(){
        Map<Integer, Optional<Node>> keyToNode = new HashMap<>();
        LinkCutTree linkCutTree = new LinkCutTree();
        linkCutTree.createNewLinkCutTree(0, 1, keyToNode);

        assertEquals(Integer.valueOf(0), keyToNode.get(0).get().key);
        assertEquals(Integer.valueOf(1), keyToNode.get(1).get().key);
        assertEquals(Integer.valueOf(1), keyToNode.get(0).get().parent.key);
        assertEquals(Integer.valueOf(0), keyToNode.get(1).get().left.key);
        assertEquals(Integer.valueOf(2), linkCutTree.getSizeOfTree(keyToNode.get(0).get()));
    }

    @Test
    public void addNonExistentNodeTest(){
        Map<Integer, Optional<Node>> keyToNode = new HashMap<>();
        LinkCutTree linkCutTree = new LinkCutTree();
        linkCutTree.createNewLinkCutTree(0, 1, keyToNode);
        linkCutTree.addNonExistingNodeToTree(keyToNode.get(1).get(), 2, keyToNode);

        assertEquals(Integer.valueOf(2), keyToNode.get(2).get().key);
        assertEquals(Integer.valueOf(2), keyToNode.get(1).get().parent.key);
        assertEquals(Integer.valueOf(1), keyToNode.get(2).get().left.key);
        assertEquals(Integer.valueOf(3), linkCutTree.getSizeOfTree(keyToNode.get(2).get()));
    }

    @Test
    public void detachRightSideTest(){
        Map<Integer, Optional<Node>> keyToNode = new HashMap<>();
        LinkCutTree linkCutTree = new LinkCutTree();
        linkCutTree.createNewLinkCutTree(0, 1, keyToNode);
        linkCutTree.addNonExistingNodeToTree(keyToNode.get(1).get(), 2, keyToNode);

        linkCutTree.splay(keyToNode.get(1).get());
        linkCutTree.detachRightSide(keyToNode.get(1).get());

        assertEquals(Integer.valueOf(1), keyToNode.get(2).get().pathParent.key);
        assertNull(keyToNode.get(1).get().right);
        assertEquals(Integer.valueOf(3), linkCutTree.getSizeOfTree(keyToNode.get(0).get()));
        assertEquals(Integer.valueOf(3), linkCutTree.getSizeOfTree(keyToNode.get(2).get()));

    }

    @Test
    public void accessTestBasic(){
        Map<Integer, Optional<Node>> keyToNode = new HashMap<>();
        Node a = new Node(1);
        Node b = new Node(2);
        Node c = new Node(3);
        Node d = new Node(4);
        a.parent = b;
        b.left = a;
        b.parent = c;
        c.left = b;
        c.right = d;
        d.parent = c;
        Node e = new Node(5);
        e.pathParent = c;

        keyToNode.put(1, Optional.of(a));
        keyToNode.put(2, Optional.of(b));
        keyToNode.put(3, Optional.of(c));
        keyToNode.put(4, Optional.of(d));
        keyToNode.put(5, Optional.of(e));

        LinkCutTree linkCutTree = new LinkCutTree();

        linkCutTree.access(e);

        assertEquals(Integer.valueOf(3), d.pathParent.key);
        assertEquals(Integer.valueOf(5), c.parent.key);
        assertNull(e.pathParent);
        assertNull(e.parent);

    }

    @Test
    public void findLinkCutRootTest(){
        Map<Integer, Optional<Node>> keyToNode = new HashMap<>();
        LinkCutTree linkCutTree = new LinkCutTree();
        linkCutTree.createNewLinkCutTree(0, 1, keyToNode);
        linkCutTree.addNonExistingNodeToTree(keyToNode.get(1).get(), 2, keyToNode);
        linkCutTree.addNonExistingNodeToTree(keyToNode.get(1).get(), 3, keyToNode);

        assertEquals(Integer.valueOf(0), linkCutTree.findLinkCutRoot(keyToNode.get(2).get()).get().key);
    }

    @Test
    public void linkTest(){
        Node a = new Node(1);
        Node b = new Node(2);

        LinkCutTree linkCutTree = new LinkCutTree();
        assertEquals(Integer.valueOf(1), linkCutTree.getSizeOfTree(a));
        linkCutTree.link(a, b);

        assertEquals(Integer.valueOf(1), b.left.key);
        assertEquals(Integer.valueOf(1), linkCutTree.findLinkCutRoot(b).get().key);
        assertEquals(Integer.valueOf(2), linkCutTree.getSizeOfTree(a));

        Node z = new Node(0);

        linkCutTree.link(z, a);

        assertEquals(Integer.valueOf(0), a.left.key);
        assertEquals(a, b.pathParent);
        assertEquals(a, z.parent);
        assertNull(b.parent);
        assertEquals(Integer.valueOf(3), linkCutTree.getSizeOfTree(a));

    }

    @Test
    public void addNodeTest(){
        Map<Integer, Optional<Node>> keyToNode = new HashMap<>();
        LinkCutTree linkCutTree = new LinkCutTree();
        Optional<Node> node = linkCutTree.addNode(0, keyToNode);

        assertEquals(node.get(), keyToNode.get(0).get());
    }

    @Test
    public void cutTest(){
        Map<Integer, Optional<Node>> keyToNode = new HashMap<>();
        LinkCutTree linkCutTree = new LinkCutTree();
        linkCutTree.createNewLinkCutTree(0, 1, keyToNode);
        linkCutTree.addNonExistingNodeToTree(keyToNode.get(1).get(), 2, keyToNode);
        linkCutTree.addNonExistingNodeToTree(keyToNode.get(1).get(), 3, keyToNode);

        assertEquals(Integer.valueOf(4), linkCutTree.getSizeOfTree(keyToNode.get(0).get()));
        assertEquals(Integer.valueOf(4), linkCutTree.getSizeOfTree(keyToNode.get(2).get()));
        linkCutTree.cut(keyToNode.get(1).get());

        assertEquals(Integer.valueOf(3), linkCutTree.getSizeOfTree(keyToNode.get(2).get()));
        assertEquals(Integer.valueOf(3), linkCutTree.getSizeOfTree(keyToNode.get(3).get()));
        assertEquals(Integer.valueOf(1), linkCutTree.getSizeOfTree(keyToNode.get(0).get()));
        assertNull(keyToNode.get(1).get().parent);
        assertNull(keyToNode.get(1).get().pathParent);
        assertNull(keyToNode.get(0).get().right);
        assertNotEquals(linkCutTree.findLinkCutRoot(keyToNode.get(0).get()),
                linkCutTree.findLinkCutRoot(keyToNode.get(3).get()));
    }

    @Test
    public void deleteTreeEdge(){
        Map<Integer, Optional<Node>> keyToNode = new HashMap<>();
        LinkCutTree linkCutTree = new LinkCutTree();
        linkCutTree.createNewLinkCutTree(0, 1, keyToNode);
        linkCutTree.addNonExistingNodeToTree(keyToNode.get(1).get(), 2, keyToNode);
        linkCutTree.addNonExistingNodeToTree(keyToNode.get(1).get(), 3, keyToNode);

        assertEquals(Integer.valueOf(4), linkCutTree.getSizeOfTree(keyToNode.get(0).get()));
        linkCutTree.deleteTreeEdge(0, 1, keyToNode);

        assertNull(keyToNode.get(1).get().parent);
        assertNull(keyToNode.get(1).get().pathParent);
        assertNull(keyToNode.get(0).get().right);
        assertNotEquals(linkCutTree.findLinkCutRoot(keyToNode.get(0).get()),
                linkCutTree.findLinkCutRoot(keyToNode.get(3).get()));
        assertEquals(Integer.valueOf(1), linkCutTree.getSizeOfTree(keyToNode.get(0).get()));

        linkCutTree.deleteTreeEdge(3, 1, keyToNode);

        assertEquals(Integer.valueOf(1), linkCutTree.getSizeOfTree(keyToNode.get(3).get()));
        assertEquals(Integer.valueOf(2), linkCutTree.getSizeOfTree(keyToNode.get(2).get()));
        assertNull(keyToNode.get(3).get().parent);
        assertNull(keyToNode.get(3).get().pathParent);
        assertNull(keyToNode.get(3).get().left);
        assertNotEquals(linkCutTree.findLinkCutRoot(keyToNode.get(1).get()),
                linkCutTree.findLinkCutRoot(keyToNode.get(3).get()));

    }

}

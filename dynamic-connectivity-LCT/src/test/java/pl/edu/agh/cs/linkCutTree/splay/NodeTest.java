package pl.edu.agh.cs.linkCutTree.splay;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NodeTest {

    @Test
    public void testPathParent(){
        Node node = new Node(0);
        assertNull(node.pathParent);
        node.pathParent = new Node(1);
        assertEquals(1, (int) node.pathParent.key);
    }
}

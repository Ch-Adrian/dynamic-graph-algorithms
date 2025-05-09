package pl.edu.agh.cs.common;

import pl.edu.agh.cs.eulerTourTree.splay.Node;

import java.util.Objects;

public class UniqueNode {

    private long uniqueKey;
    private Node node;
    public UniqueNode(Node node) {
        this.uniqueKey = node.getUniqueKey();
        this.node = node;
    }

    public Node getNode(){ return this.node; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UniqueNode that)) return false;
        return uniqueKey == that.uniqueKey && Objects.equals(node, that.node);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueKey, node);
    }
}

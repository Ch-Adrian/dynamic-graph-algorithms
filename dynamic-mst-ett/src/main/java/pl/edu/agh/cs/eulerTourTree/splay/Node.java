package pl.edu.agh.cs.eulerTourTree.splay;

import pl.edu.agh.cs.common.Pair;

import java.io.Serializable;
import java.util.Objects;

public class Node implements Serializable {

    private static final long serialVersionUID = 1L;
    public static long nodeCounter = 0;
    private long uniqueKey = 0;
    public Pair<Integer, Integer> key;
    public Node left;
    public Node right;
    public Node parent;
    public Integer sizeOfTree;

    public Node(Pair<Integer, Integer> key) {
        this.uniqueKey = nodeCounter++;
        this.key = key;
        this.sizeOfTree = 1;
    }

    public long getUniqueKey(){ return this.uniqueKey; }

    @Override
    public String toString() {
        return "Node{" +
                "key=" + key +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return uniqueKey == node.uniqueKey && Objects.equals(key, node.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueKey, key);
    }
}

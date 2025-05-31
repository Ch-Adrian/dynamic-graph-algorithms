package pl.edu.agh.cs.eulerTourTree.splay;

import pl.edu.agh.cs.common.Pair;

import java.util.Objects;

public class Node {

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
        if (!(o instanceof Node node)) return false;
        return uniqueKey == node.uniqueKey && Objects.equals(key, node.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueKey, key);
    }


}

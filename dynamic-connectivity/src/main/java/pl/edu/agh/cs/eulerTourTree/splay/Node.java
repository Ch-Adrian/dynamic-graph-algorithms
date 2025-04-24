package pl.edu.agh.cs.eulerTourTree.splay;

import pl.edu.agh.cs.common.Pair;

public class Node {

    public Pair<Integer, Integer> key;
    public Node left;
    public Node right;
    public Node parent;
    public int sizeOfTree;

    public Node(Pair<Integer, Integer> key) {
        this.key = key;
        this.sizeOfTree = 1;
    }

    @Override
    public String toString() {
        return "Node{" +
                "key=" + key +
                '}';
    }
}

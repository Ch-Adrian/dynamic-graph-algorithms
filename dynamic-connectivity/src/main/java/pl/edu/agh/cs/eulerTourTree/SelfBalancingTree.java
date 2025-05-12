package pl.edu.agh.cs.eulerTourTree;

import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.splay.Node;
import pl.edu.agh.cs.eulerTourTree.splay.SplayTree;

import java.util.Optional;

public interface SelfBalancingTree {

    Node insertToRight(Node treeNode, Pair<Integer, Integer> key);

    Pair<Optional<Node>, Optional<Node>> split(Node treeNode);

    Optional<Node> join(Node leftTree, Node rightTree);

    Optional<Node> getRootNode(Node treeNode);

    Optional<Node> predecessor(Node treeNode);

    Optional<Node> successor(Node treeNode);

    Optional<Node> firstNode(Node treeNode);

    Optional<Node> lastNode(Node treeNode);

    Optional<Node> removeNode(Node treeNode);

    Integer getSizeOfTree(Node treeNode);

}

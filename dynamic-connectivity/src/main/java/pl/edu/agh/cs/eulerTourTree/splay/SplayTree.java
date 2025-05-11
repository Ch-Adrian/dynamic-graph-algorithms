package pl.edu.agh.cs.eulerTourTree.splay;

import pl.edu.agh.cs.common.Pair;

import javax.swing.text.html.Option;
import java.util.Optional;

public class SplayTree {

    public static void updateSize(Node treeNode){
        if(treeNode == null) return;
        int size = 0;
        if(treeNode.left != null) size += treeNode.left.sizeOfTree;
        if(treeNode.right != null) size += treeNode.right.sizeOfTree;
        treeNode.sizeOfTree = size + 1;
    }

    public static void rightRotate(Node node){
        if(node == null) return;
        if(node.parent == null) return;
        Node parent = node.parent;
        Node grandParent = parent.parent;

        if(grandParent != null){
            if(grandParent.right == parent){
                grandParent.right = node;
            }
            else {
                grandParent.left = node;
            }
            node.parent = grandParent;
        } else {
            node.parent = null;
        }

        parent.left = node.right;
        if(parent.left != null){
            parent.left.parent = parent;
        }
        node.right = parent;
        parent.parent = node;
        updateSize(parent);
        updateSize(node);
    }

    public static void leftRotate(Node node){
        if(node == null) return;
        if(node.parent == null) return;
        Node parent = node.parent;
        Node grandParent = parent.parent;

        if(grandParent != null){
            if(grandParent.right == parent){
                grandParent.right = node;
            }
            else {
                grandParent.left = node;
            }
            node.parent = grandParent;
        } else {
            node.parent = null;
        }

        parent.right = node.left;
        if(parent.right != null){
            parent.right.parent = parent;
        }
        node.left = parent;
        parent.parent = node;
        updateSize(parent);
        updateSize(node);
    }

    public static void splay(Node node){
        if(node == null) return;

        while(node.parent != null){
            if(node.parent.parent == null){
                if(node.parent.left == node){
                    SplayTree.rightRotate(node);
                } else {
                    SplayTree.leftRotate(node);
                }
            } else {
                if(node.parent.parent.left == node.parent){
                    if(node.parent.left == node){
                        SplayTree.rightRotate(node);
                    } else {
                        SplayTree.leftRotate(node);
                    }
                } else {
                    if(node.parent.left == node){
                        SplayTree.rightRotate(node);
                    } else {
                        SplayTree.leftRotate(node);
                    }
                }
            }
        }
    }

    public static Node insertToRight(Node treeNode, Pair<Integer, Integer> key) {
        Node newNode = new Node(key);
        if (treeNode == null) return newNode;

        for (SplayTree.splay(treeNode); treeNode.right != null; treeNode.sizeOfTree++, treeNode = treeNode.right);
        treeNode.sizeOfTree++;
        treeNode.right = newNode;
        newNode.parent = treeNode;
        return newNode;
    }

    public static Optional<Node> detachSubTreeFromTree(Node treeNode){
        if(treeNode == null) return Optional.empty();
        if(treeNode.parent == null) return Optional.of(treeNode);

        if(treeNode.parent.left == treeNode){
            treeNode.parent.left = null;
        } else {
            treeNode.parent.right = null;
        }
        updateSize(treeNode.parent);
        treeNode.parent = null;
        return Optional.of(treeNode);
    }

    public static Pair<Optional<Node>, Optional<Node>> split(Node treeNode){
        /* Splits tree where treeNode is the last node in sequence. */
        if(treeNode == null) return new Pair<>(Optional.empty(), Optional.empty());
        Optional<Node> succ = SplayTree.successor(treeNode);
        if(succ.isEmpty()) return new Pair<>(Optional.of(treeNode), succ);

        SplayTree.splay(succ.get());
        Optional<Node> leftSubTree = SplayTree.detachSubTreeFromTree(succ.get().left);
        return new Pair<>(leftSubTree, succ);
    }

    public static Optional<Node> join(Node leftTree, Node rightTree){
        if(rightTree == null && leftTree != null) return Optional.of(leftTree);
        if(rightTree != null && leftTree == null) return Optional.of(rightTree);
        if(rightTree == null) return Optional.empty();

        Node rightMostOfLeftTree = SplayTree.lastNode(leftTree).get();
        SplayTree.splay(rightMostOfLeftTree);
        assert(rightMostOfLeftTree.right == null);

        rightMostOfLeftTree.right = SplayTree.getRootNode(rightTree).get();
        rightMostOfLeftTree.right.parent = rightMostOfLeftTree;
        updateSize(rightMostOfLeftTree);

        return Optional.of(rightMostOfLeftTree);
    }

    public static Optional<Node> getRootNode(Node treeNode){
        if(treeNode == null) return Optional.empty();
        if(treeNode.parent == null) return Optional.of(treeNode);
        for(treeNode = treeNode.parent; treeNode.parent != null; treeNode = treeNode.parent);
        return Optional.of(treeNode);
    }

    public static Optional<Node> predecessor(Node treeNode){
        if(treeNode == null) return Optional.empty();
        Optional<Node> root = SplayTree.getRootNode(treeNode);
        SplayTree.splay(treeNode);
        if (treeNode.left == null){
            return Optional.empty();
        }
        for(treeNode = treeNode.left; treeNode.right != null; treeNode = treeNode.right);
        SplayTree.splay(root.get());
        return Optional.of(treeNode);
    }

    public static Optional<Node> successor(Node treeNode){
        if(treeNode == null) return Optional.empty();
        Optional<Node> root = SplayTree.getRootNode(treeNode);
        SplayTree.splay(treeNode);
        if (treeNode.right == null){
            return Optional.empty();
        }
        for(treeNode = treeNode.right; treeNode.left != null; treeNode = treeNode.left);
        SplayTree.splay(root.get());
        return Optional.of(treeNode);
    }

    public static Optional<Node> firstNode(Node treeNode){
        if(treeNode == null) return Optional.empty();
        Optional<Node> optRoot = SplayTree.getRootNode(treeNode);
        Node root = optRoot.get();
        for(; root.left != null; root = root.left);
        return Optional.of(root);
    }

    public static Optional<Node> lastNode(Node treeNode){
        if(treeNode == null) return Optional.empty();
        Optional<Node> optRoot = SplayTree.getRootNode(treeNode);
        Node root = optRoot.get();
        for(; root.right != null; root = root.right);
        return Optional.of(root);
    }

    public static Optional<Node> removeNode(Node treeNode){
        if(treeNode == null) return Optional.empty();
        SplayTree.splay(treeNode);
        assert (treeNode.parent == null);
        Node pred;
        if(treeNode.left == null && treeNode.right == null){
            return Optional.empty();
        } else if(treeNode.left == null){
            pred = treeNode.right;
            pred.parent = null;
        } else if(treeNode.right == null){
            pred = treeNode.left;
            pred.parent = null;
        }
        else {
            pred = SplayTree.predecessor(treeNode).get();
            assert (pred.right == null);
            pred.right = treeNode.right;
            treeNode.right.parent = pred;
            if(treeNode.left.parent == treeNode){
                treeNode.left.parent = null;
            }
            SplayTree.updateSize(pred);
            SplayTree.splay(pred);
        }

        treeNode.parent = null;
        treeNode.right = null;
        treeNode.left = null;
        treeNode.sizeOfTree = 1;

        return Optional.of(pred);
    }

    public static Integer getSizeOfTree(Node treeNode){
        if(treeNode == null) return 0;
        return SplayTree.getRootNode(treeNode).get().sizeOfTree;
    }
}

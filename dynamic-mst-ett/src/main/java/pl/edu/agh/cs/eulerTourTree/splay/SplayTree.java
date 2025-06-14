package pl.edu.agh.cs.eulerTourTree.splay;

import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.SelfBalancingTree;

import java.io.Serializable;
import java.util.Optional;

public class SplayTree implements SelfBalancingTree, Serializable {

    private static final long serialVersionUID = 1L;

    public void updateSize(Node treeNode){
        if(treeNode == null) return;
        int size = 0;
        if(treeNode.left != null) size += treeNode.left.sizeOfTree;
        if(treeNode.right != null) size += treeNode.right.sizeOfTree;
        treeNode.sizeOfTree = size + 1;
    }

    public void rightRotate(Node node){
        if(node == null) return;
        if(node.parent == null) return;
        if(node.parent.left != node) return;

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

    public void leftRotate(Node node){
        if(node == null) return;
        if(node.parent == null) return;
        if(node.parent.right != node) return;

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

    public void splay(Node node){
        if(node == null) return;

        while(node.parent != null){
            if(node.parent.parent == null) {
                if (node.parent.left == node) {
                    rightRotate(node);
                } else {
                    leftRotate(node);
                }
            } else if (node == node.parent.left  && node.parent.parent.left == node.parent){
                rightRotate(node.parent);
                rightRotate(node);
            } else if (node == node.parent.right && node.parent.parent.right == node.parent){
                leftRotate(node.parent);
                leftRotate(node);
            } else if (node == node.parent.right && node.parent.parent.left == node.parent){
                leftRotate(node);
                rightRotate(node);
            } else {
                rightRotate(node);
                leftRotate(node);
            }
        }

    }

    public  Node insertToRight(Node treeNode, Pair<Integer, Integer> key) {
        Node newNode = new Node(key);
        if (treeNode == null) return newNode;

        for (splay(treeNode); treeNode.right != null; treeNode.sizeOfTree++, treeNode = treeNode.right);
        treeNode.sizeOfTree++;
        treeNode.right = newNode;
        newNode.parent = treeNode;
//        splay(newNode);
        return newNode;
    }

    public  Optional<Node> detachSubTreeFromTree(Node treeNode){
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

    public  Pair<Optional<Node>, Optional<Node>> split(Node treeNode){
        /* Splits tree where treeNode is the last node in sequence. */
        if(treeNode == null) return new Pair<>(Optional.empty(), Optional.empty());
        Optional<Node> succ = successor(treeNode);
        if(succ.isEmpty()) return new Pair<>(Optional.of(treeNode), succ);

        splay(succ.get());
        Optional<Node> leftSubTree = detachSubTreeFromTree(succ.get().left);
        return new Pair<>(leftSubTree, succ);
    }

    public  Optional<Node> join(Node leftTree, Node rightTree){
        if(rightTree == null && leftTree != null) return Optional.of(leftTree);
        if(rightTree != null && leftTree == null) return Optional.of(rightTree);
        if(rightTree == null) return Optional.empty();

        Node rightMostOfLeftTree = lastNode(leftTree).get();
        splay(rightMostOfLeftTree);
        assert(rightMostOfLeftTree.right == null);

        rightMostOfLeftTree.right = getRootNode(rightTree).get();
        rightMostOfLeftTree.right.parent = rightMostOfLeftTree;
        updateSize(rightMostOfLeftTree);

        return Optional.of(rightMostOfLeftTree);
    }

    public  Optional<Node> getRootNode(Node treeNode){
        if(treeNode == null) return Optional.empty();
        if(treeNode.parent == null) return Optional.of(treeNode);
        for(treeNode = treeNode.parent; treeNode.parent != null; treeNode = treeNode.parent);
        return Optional.of(treeNode);
    }

    public  Optional<Node> predecessor(Node treeNode){
        if(treeNode == null) return Optional.empty();
        Optional<Node> root = getRootNode(treeNode);
        splay(treeNode);
        if (treeNode.left == null){
            return Optional.empty();
        }
        for(treeNode = treeNode.left; treeNode.right != null; treeNode = treeNode.right);
        splay(root.get());
        return Optional.of(treeNode);
    }

    public  Optional<Node> successor(Node treeNode){
        if(treeNode == null) return Optional.empty();
        Optional<Node> root = getRootNode(treeNode);
        splay(treeNode);
        if (treeNode.right == null){
            return Optional.empty();
        }
        for(treeNode = treeNode.right; treeNode.left != null; treeNode = treeNode.left);
        splay(root.get());
        return Optional.of(treeNode);
    }

    public  Optional<Node> firstNode(Node treeNode){
        if(treeNode == null) return Optional.empty();
        Optional<Node> optRoot = getRootNode(treeNode);
        Node root = optRoot.get();
        for(; root.left != null; root = root.left);
        return Optional.of(root);
    }

    public  Optional<Node> lastNode(Node treeNode){
        if(treeNode == null) return Optional.empty();
        Optional<Node> optRoot = getRootNode(treeNode);
        Node root = optRoot.get();
        for(; root.right != null; root = root.right);
        return Optional.of(root);
    }

    public  Optional<Node> removeNode(Node treeNode){
        if(treeNode == null) return Optional.empty();
        splay(treeNode);
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
            pred = predecessor(treeNode).get();
            assert (pred.right == null);
            pred.right = treeNode.right;
            treeNode.right.parent = pred;
            if(treeNode.left.parent == treeNode){
                treeNode.left.parent = null;
            }
            updateSize(pred);
            splay(pred);
        }

        treeNode.parent = null;
        treeNode.right = null;
        treeNode.left = null;
        treeNode.sizeOfTree = 1;

        return Optional.of(pred);
    }

    public  Integer getSizeOfTree(Node treeNode){
        if(treeNode == null) return 0;
        return getRootNode(treeNode).get().sizeOfTree;
    }

}

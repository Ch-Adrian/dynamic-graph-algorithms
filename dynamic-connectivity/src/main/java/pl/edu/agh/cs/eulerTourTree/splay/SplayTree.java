package pl.edu.agh.cs.eulerTourTree.splay;

import pl.edu.agh.cs.common.Pair;

public class SplayTree {

    static void rightRotate(Node node){
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
    }

    static void leftRotate(Node node){
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
    }

    static void splay(Node node){

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

    static Node insertToRight(Node treeNode, Pair<Integer, Integer> key) {
        Node newNode = new Node(key);
        if (treeNode == null) return newNode;

        for (SplayTree.splay(treeNode); treeNode.right != null; treeNode = treeNode.right) ;
        treeNode.right = newNode;
        newNode.parent = treeNode;
        return newNode;
    }

    static Node detachNodeFromTree(Node treeNode){
        if(treeNode == null || treeNode.parent == null) return treeNode;
        if(treeNode.parent.left == treeNode){
            treeNode.parent.left = null;
        } else {
            treeNode.parent.right = null;
        }
        treeNode.parent = null;
        return treeNode;
    }

    static Pair<Node, Node> split(Node treeNode){
        /* Splits tree where treeNode is the last node in sequence. */
        if(treeNode == null) return null;
        Node succ = SplayTree.successor(treeNode);
        if(succ == null) return new Pair<>(treeNode, succ);

        SplayTree.splay(succ);
        Node leftSubTree = SplayTree.detachNodeFromTree(succ.left);
        return new Pair<>(leftSubTree, succ);
    }

    static Node join(Node leftTree, Node rightTree){
        Node leftMostOfLeftTree = SplayTree.lastNode(leftTree);
        SplayTree.splay(leftMostOfLeftTree);
        assert(leftMostOfLeftTree.right == null);
        leftMostOfLeftTree.right = rightTree;
        return leftMostOfLeftTree;
    }

    static Node getRootNode(Node treeNode){
        if(treeNode == null) return null;
        if(treeNode.parent == null) return treeNode;
        for(treeNode = treeNode.parent; treeNode.parent != null; treeNode = treeNode.parent);
        return treeNode;
    }

    static Node predecessor(Node treeNode){
        if(treeNode == null) return null;
        Node root = SplayTree.getRootNode(treeNode);
        SplayTree.splay(treeNode);
        if (treeNode.left == null){
            return null;
        }
        for(treeNode = treeNode.left; treeNode.right != null; treeNode = treeNode.right);
        SplayTree.splay(root);
        return treeNode;
    }

    static Node successor(Node treeNode){
        if(treeNode == null) return null;
        Node root = SplayTree.getRootNode(treeNode);
        SplayTree.splay(treeNode);
        if (treeNode.right == null){
            return null;
        }
        for(treeNode = treeNode.right; treeNode.left != null; treeNode = treeNode.left);
        SplayTree.splay(root);
        return treeNode;
    }

    static Node firstNode(Node treeNode){
        if(treeNode == null) return null;
        Node root = SplayTree.getRootNode(treeNode);
        for(; root.left != null; root = root.left);
        return root;
    }

    static Node lastNode(Node treeNode){
        if(treeNode == null) return null;
        Node root = SplayTree.getRootNode(treeNode);
        for(; root.right != null; root = root.right);
        return root;
    }

    static void removeNode(Node treeNode){
        Node parent = treeNode.parent;
        treeNode.parent = null;

        if(treeNode.left == null && treeNode.right == null){
            if(parent.right == treeNode){
                parent.right = null;
            } else {
                parent.left = null;
            }
            return;
        } else if(treeNode.right == null){
            if(parent.right == treeNode){
                parent.right = treeNode.left;
            } else {
                parent.left = treeNode.left;
            }
            treeNode.left.parent = parent;
        } else if(treeNode.left == null){
            if(parent.right == treeNode){
                parent.right = treeNode.right;
            } else {
                parent.left = treeNode.right;
            }
            treeNode.right.parent = parent;
        } else {
            Node pred = SplayTree.predecessor(treeNode);
            if(pred.right == null)
                pred.right = treeNode.right;
            else
                throw new RuntimeException("Right child should be null.");
            if(parent.right == treeNode){
                parent.right = treeNode.left;
            } else {
                parent.left = treeNode.left;
            }
            treeNode.left.parent = parent;
        }
    }

}

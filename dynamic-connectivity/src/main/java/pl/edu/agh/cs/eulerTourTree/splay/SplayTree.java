package pl.edu.agh.cs.eulerTourTree.splay;

import pl.edu.agh.cs.common.Pair;

public class SplayTree {

    private static void updateSize(Node treeNode){
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

    public static Node detachNodeFromTree(Node treeNode){
        if(treeNode == null || treeNode.parent == null) return treeNode;
        if(treeNode.parent.left == treeNode){
            treeNode.parent.left = null;
        } else {
            treeNode.parent.right = null;
        }
        updateSize(treeNode.parent);
        treeNode.parent = null;
        return treeNode;
    }

    public static Pair<Node, Node> split(Node treeNode){
        /* Splits tree where treeNode is the last node in sequence. */
        if(treeNode == null) return null;
        Node succ = SplayTree.successor(treeNode);
        if(succ == null) return new Pair<>(treeNode, succ);

        SplayTree.splay(succ);
        Node leftSubTree = SplayTree.detachNodeFromTree(succ.left);
        return new Pair<>(leftSubTree, succ);
    }

    public static Node join(Node leftTree, Node rightTree){
        if(rightTree == null && leftTree != null) return leftTree;
        if(rightTree != null && leftTree == null) return rightTree;
        if(rightTree == null && leftTree == null) return null;

        Node rightMostOfLeftTree = SplayTree.lastNode(leftTree);
        SplayTree.splay(rightMostOfLeftTree);
        assert(rightMostOfLeftTree.right == null);

        rightMostOfLeftTree.right = SplayTree.getRootNode(rightTree);
        rightMostOfLeftTree.right.parent = rightMostOfLeftTree;
        updateSize(rightMostOfLeftTree);

        return rightMostOfLeftTree;
    }

    public static Node getRootNode(Node treeNode){
        if(treeNode == null) return null;
        if(treeNode.parent == null) return treeNode;
        for(treeNode = treeNode.parent; treeNode.parent != null; treeNode = treeNode.parent);
        return treeNode;
    }

    public static Node predecessor(Node treeNode){
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

    public static Node successor(Node treeNode){
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

    public static Node firstNode(Node treeNode){
        if(treeNode == null) return null;
        Node root = SplayTree.getRootNode(treeNode);
        for(; root.left != null; root = root.left);
        return root;
    }

    public static Node lastNode(Node treeNode){
        if(treeNode == null) return null;
        Node root = SplayTree.getRootNode(treeNode);
        for(; root.right != null; root = root.right);
        return root;
    }

    public static Node removeNode(Node treeNode){
        /* returns root node of a tree */
        if(treeNode == null) return null;
        Node parent = treeNode.parent;

        if(treeNode.left == null && treeNode.right == null){
            Node parent2 = parent;
            if(parent != null) {
                if (parent.right == treeNode) {
                    parent.right = null;
                } else {
                    parent.left = null;
                }
                while(parent != null){
                    updateSize(parent);
                    parent = parent.parent;
                }
                treeNode.parent = null;
            }
            return SplayTree.getRootNode(parent2);
        } else if(treeNode.right == null){
            Node replaced = treeNode.left;
            if(parent != null) {
                if (parent.right == treeNode) {
                    parent.right = treeNode.left;
                } else {
                    parent.left = treeNode.left;
                }
                while(parent != null){
                    updateSize(parent);
                    parent = parent.parent;
                }
                treeNode.left.parent = parent;
                treeNode.parent = null;
            } else treeNode.left.parent = null;
            treeNode.left = null;
            return SplayTree.getRootNode(replaced);
        } else if(treeNode.left == null){
            Node replaced = treeNode.right;
            if(parent != null) {
                if (parent.right == treeNode) {
                    parent.right = treeNode.right;
                } else {
                    parent.left = treeNode.right;
                }
                treeNode.right.parent = parent;
                while(parent != null){
                    updateSize(parent);
                    parent = parent.parent;
                }
                treeNode.parent = null;
            } else treeNode.right.parent = null;
            treeNode.right = null;
            return SplayTree.getRootNode(replaced);
        } else {
            Node pred = SplayTree.predecessor(treeNode);
            assert(pred.right == null || pred.right.equals(treeNode));
            pred.right = treeNode.right;
            treeNode.right.parent = pred;

            if(!pred.equals(treeNode.parent)) {
                if (parent != null) {
                    if (parent.right == treeNode) {
                        parent.right = treeNode.left;
                    } else {
                        parent.left = treeNode.left;
                    }
                    treeNode.left.parent = parent;
                    while (parent != null) {
                        updateSize(parent);
                        parent = parent.parent;
                    }
                    treeNode.parent = null;
                } else treeNode.left.parent = null;
                updateSize(treeNode.left);
                treeNode.left = null;
                return SplayTree.getRootNode(pred);
            } else {
                Node firstOfRight = SplayTree.firstNode(treeNode.right);
                assert (firstOfRight.left == null);
                firstOfRight.left = treeNode.left;
                treeNode.left.parent = firstOfRight;
                updateSize(firstOfRight);
                while (firstOfRight != null) {
                    updateSize(firstOfRight);
                    firstOfRight = firstOfRight.parent;
                }
                treeNode.parent = null;
                treeNode.left = null;
                treeNode.right = null;
                return SplayTree.getRootNode(pred);
            }
        }
    }

    public static int getSizeOfTree(Node treeNode){
        if(treeNode == null) return 0;
        return SplayTree.getRootNode(treeNode).sizeOfTree;
    }
}

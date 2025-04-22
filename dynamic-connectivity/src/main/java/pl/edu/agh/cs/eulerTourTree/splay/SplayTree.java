package pl.edu.agh.cs.eulerTourTree.splay;

import pl.edu.agh.cs.common.Pair;

public class SplayTree {

    static void rightRotate(Node node){
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
}

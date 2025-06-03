package pl.edu.agh.cs.linkCutTree;

import pl.edu.agh.cs.forest.Forest;
import pl.edu.agh.cs.linkCutTree.splay.Node;
import pl.edu.agh.cs.linkCutTree.splay.SplayTree;

import java.util.*;

public class LinkCutTree extends SplayTree {

    public Optional<Node> createNewLinkCutTree(Integer u, Integer v, Map<Integer, Optional<Node>> keyToNode) {

        /* preconditions */
        if(Forest.checkIfVertexHasNodeInTheTree(u, keyToNode) || Forest.checkIfVertexHasNodeInTheTree(v, keyToNode)){
            throw new RuntimeException("There is node already in the tree!");
        }

        addNode(u, keyToNode);
        if(u.equals(v)) return keyToNode.get(u);

        addNode(v, keyToNode);
        this.link(keyToNode.get(u).get(), keyToNode.get(v).get());

        return keyToNode.get(u);
    }

    public void addNonExistingNodeToTree(Node tree, Integer u, Map<Integer, Optional<Node>> keyToNode){

        /* preconditions */
        if(Forest.checkIfVertexHasNodeInTheTree(u, keyToNode)){
            throw new RuntimeException("There is node already in the tree!");
        }

        Optional<Node> optNode = addNode(u, keyToNode);
        this.link(tree, optNode.get());
    }

    public void detachRightSide(Node node){
        if(node == null) return;
        if(node.right != null){
            node.virtualSize += node.right.sizeOfTree;
            node.right.pathParent = node;
            node.right.parent = null;
            node.right = null;
            super.updateSize(node);
        }
    }

    public void access(Node node){
        if(node == null) return;
        splay(node);
        detachRightSide(node);

        Node pointer = node;
        while(node.pathParent != null){
            pointer = node.pathParent;
            splay(pointer);
            detachRightSide(pointer);
            pointer.right = node;
            pointer.virtualSize -= node.sizeOfTree;
            node.parent = pointer;
            node.pathParent = null;
            super.updateSize(pointer);
            splay(node);
        }
    }

    public void makeRoot(Node node){
        access(node);
        node.rev = !node.rev;
        pushDown(node);
    }

    public Optional<Node> findLinkCutRoot(Node node){
        if(node == null) return Optional.empty();
        this.access(node);
        Node pointer = node;
        pushDown(pointer);
        for(; pointer.left != null;){
            pointer = pointer.left;
            pushDown(pointer);
        }
        this.access(pointer);
        return Optional.of(pointer);
    }

    public void link(Node parent, Node child){
        if(parent == null || child == null) return;
        // child is a root in a general tree and splay tree
        // parent is a root in a splay tree

        //this.access(child);
        //this.access(parent);
        //assert (child.left == null);
        //child.left = parent;
        //parent.parent = child;
        //super.updateSize(child);

        this.makeRoot(parent);
        this.access(child);
        parent.pathParent = child;
        child.virtualSize += parent.sizeOfTree;
        super.updateSize(child);
    }

    public Optional<Node> addNode(Integer key, Map<Integer, Optional<Node>> keyToNode){
        Optional<Node> optNode = Optional.of(new Node(key));
        keyToNode.put(key, optNode);
        return optNode;
    }

    public void cut(Node tree){
        if(tree == null) return;
        this.access(tree);
        if(tree.left != null) {
            tree.left.parent = null;
            tree.left = null;
            super.updateSize(tree);
        }
    }

    public void deleteTreeEdge(Integer u, Integer v, Map<Integer, Optional<Node>> keyToNode){

        if(!Forest.checkIfVertexHasNodeInTheTree(u, keyToNode) || !Forest.checkIfVertexHasNodeInTheTree(v, keyToNode)){
            throw new RuntimeException("Node doesn't exist in the tree!");
        }

        Node nodeU = keyToNode.get(u).get();
        Node nodeV = keyToNode.get(v).get();

        this.access(nodeU);
        this.access(nodeV);

        if(Objects.equals(nodeU.pathParent, nodeV)){
            this.cut(nodeU);
        } else if(Objects.equals(nodeV.pathParent, nodeU)){
            this.cut(nodeV);
        } else if(nodeV.left == nodeU){
            this.cut(nodeV);
        } else {
            throw new RuntimeException("Delete Tree Edge error!");
        }

    }

    public Integer getSizeOfTree(Node node){
        Optional<Node> root = findLinkCutRoot(node);
        return getRootNode(root.get()).get().sizeOfTree;
    }

}

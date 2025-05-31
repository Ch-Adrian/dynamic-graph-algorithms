package pl.edu.agh.cs.linkCutTree;

import pl.edu.agh.cs.common.Pair;
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

    private void detachRightSide(Node node){
        if(node == null) return;
        if(node.right != null){
            node.right.pathParent = node;
            node.right.parent = null;
            node.right = null;
            super.updateSize(node);
        }
    }

    private void access(Node node){
        if(node == null) return;
        splay(node);
        detachRightSide(node);

        Node pointer = node;
        while(node.pathParent != null){
            pointer = node.pathParent;
            splay(pointer);
            detachRightSide(pointer);
            pointer.right = node;
            node.parent = pointer;
            node.pathParent = null;
            splay(node);
        }
    }

    public Optional<Node> findLinkCutRoot(Node node){
        if(node == null) return Optional.empty();
        this.access(node);
        Node pointer;
        for(pointer = node; pointer.left != null; pointer = pointer.left);
        this.access(pointer);
        return Optional.of(pointer);
    }

    public void link(Node parent, Node child){
        if(parent == null || child == null) return;
        // child is a root in a general tree and splay tree
        // parent is a root in a splay tree
        this.access(child);
        this.access(parent);
        assert(child.left == null);
        child.left = parent;
        parent.parent = child;
    }

    public Optional<Node> addNode(Integer key, Map<Integer, Optional<Node>> keyToNode){
        Optional<Node> optNode = Optional.of(new Node(key));
        keyToNode.put(key, optNode);
        return optNode;
    }

    public void cut(Node tree){
        if(tree == null) return;
        this.access(tree);
        tree.left.parent = null;
        tree.left = null;
    }

    public void deleteTreeEdge(Integer u, Integer v, Map<Integer, Optional<Node>> keyToNode){

        if(!Forest.checkIfVertexHasNodeInTheTree(u, keyToNode) || !Forest.checkIfVertexHasNodeInTheTree(v, keyToNode)){
            throw new RuntimeException("Node doesn't exist in the tree!");
        }

        Node nodeU = keyToNode.get(u).get();
        Node nodeV = keyToNode.get(v).get();

        this.access(nodeU);
        this.access(nodeV);

        if(nodeU.pathParent.equals(nodeV)){
            this.cut(nodeU);
        } else if(nodeV.pathParent.equals(nodeU)){
            this.cut(nodeV);
        } else if(nodeV.left == nodeU){
            this.cut(nodeV);
        } else {
            throw new RuntimeException("Delete Tree Edge error!");
        }

    }

    public Set<Pair<Integer, Integer>> getEdges(Node treeNode){
        Set<Pair<Integer, Integer>> listOfEdges = new HashSet<>();
        Optional<Node> optSplayRoot = this.getRootNode(treeNode);
        optSplayRoot.ifPresent(node -> dfsEdges(node, listOfEdges));
        return listOfEdges;
    }

    public Set<Integer> getVertices(Node treeNode){
        Set<Integer> listOfVertices = new HashSet<>();
        Optional<Node> optSplayRoot = this.getRootNode(treeNode);
        optSplayRoot.ifPresent(node -> dfsVertices(node, listOfVertices));
        return listOfVertices;
    }

    private void dfsVertices(Node root, Set<Integer> listOfVertices){
        listOfVertices.add(root.key);
        if(root.left != null){
            dfsVertices(root.left, listOfVertices);
        }
        if(root.right != null){
            dfsVertices(root.right, listOfVertices);
        }

    }

    private void dfsEdges(Node root, Set<Pair<Integer, Integer>> listOfEdges) {
        if(root.left != null){
            if(!listOfEdges.contains(new Pair<>(root.left.key, root.key)) &&
                    !listOfEdges.contains(new Pair<>(root.key, root.left.key)))
                listOfEdges.add(new Pair<>(root.left.key, root.key));
            dfsEdges(root.left, listOfEdges);
        }
        if(root.right != null){
            if(!listOfEdges.contains(new Pair<>(root.right.key, root.key)) &&
                    !listOfEdges.contains(new Pair<>(root.key, root.right.key)))
                listOfEdges.add(new Pair<>(root.right.key, root.key));
            dfsEdges(root.right, listOfEdges);
        }
    }


}

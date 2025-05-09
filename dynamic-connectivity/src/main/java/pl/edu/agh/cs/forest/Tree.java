package pl.edu.agh.cs.forest;

import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.EulerTourTree;
import pl.edu.agh.cs.eulerTourTree.splay.Node;

import java.util.*;

public class Tree {

    private Integer Tkey;
    private EulerTourTree eulerTourTree;

    public Tree(Integer key, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes) {
        this.Tkey = key;
        this.eulerTourTree = new EulerTourTree(keyToNodes);
    }

    public Tree(Integer key, Node treeNode, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes) {
        this.Tkey = key;
        this.eulerTourTree = new EulerTourTree(keyToNodes);
        this.eulerTourTree.setSplayRoot(treeNode);
    }

    public Integer getTkey() {
        return Tkey;
    }

    public void linkTwoTreesWithEdge(int internalVertexId, int externalVertexId, Tree externalTree){
        eulerTourTree.link(internalVertexId, externalVertexId, externalTree.eulerTourTree);
    }

    public void addTreeEdge(int u, int v){
        /* We know that u or v belongs to tree, but not both. */
        eulerTourTree.addEdge(u, v);
    }

    public Integer getRoot(){
        return eulerTourTree.getRoot();
    }

    public Node deleteEdge(int u, int v){
        return this.eulerTourTree.deleteEdge(u, v);
    }

    public Set<Pair<Integer, Integer>> getEdges(){
        return eulerTourTree.getEdges();
    }

    public Set<Integer> getVertices(){
        return eulerTourTree.getVertices();
    }

    public Integer getSize() {
        return eulerTourTree.getSizeOfTree();
    }

    public void show(){
        this.eulerTourTree.show();
    }


}

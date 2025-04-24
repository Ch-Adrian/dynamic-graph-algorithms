package pl.edu.agh.cs.forest;

import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.EulerTourTree;
import pl.edu.agh.cs.eulerTourTree.splay.Node;

import java.util.ArrayList;

public class Tree {

    private Integer Tkey;
    private EulerTourTree eulerTourTree;

    public Tree(Integer key) {
        this.Tkey = key;
    }

    public Integer getTkey() {
        return Tkey;
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

    public ArrayList<Pair<Integer, Integer>> getEdges(){
        return eulerTourTree.getEdges();
    }

    public Integer getSize() {

    }


}

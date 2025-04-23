package pl.edu.agh.cs.eulerTourTree;

import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.splay.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EulerTourTree {

    private Node splayRoot;
    private Map<Pair<Integer, Integer>, Set<Node>> edgeToNodes;
    private Map<Integer, Set<Node>> verticeToNode;

    public EulerTourTree() {
        edgeToNodes = new HashMap<>();
    }

    public void reRoot(){

    }

    public void link(int u, int v) {

    }

    public void cut(int u, int v) {

    }



}

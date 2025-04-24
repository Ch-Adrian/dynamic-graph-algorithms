package pl.edu.agh.cs.eulerTourTree;

import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.splay.Node;
import pl.edu.agh.cs.eulerTourTree.splay.SplayTree;

import java.util.*;

public class EulerTourTree {

    private Node splayRoot;
    private Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes;

    public EulerTourTree() {
        keyToNodes = new HashMap<>();
    }

    public Node reRoot(Integer vertex){
        Node newRoot = keyToNodes.get(new Pair<>(vertex, vertex)).getFirst();
        if(Objects.equals(newRoot.key.getFirst(), newRoot.key.getSecond()) &&
                Objects.equals(newRoot.key.getFirst(), vertex)){
            return newRoot;
        }

        SplayTree.splay(newRoot);
        Node leftSubTree = SplayTree.detachNodeFromTree(newRoot.left);
        Node firstSubTree = SplayTree.firstNode(leftSubTree);
        SplayTree.removeNode(firstSubTree);

        SplayTree.join(newRoot, leftSubTree);
        SplayTree.join(newRoot, new Node(newRoot.key));

        return SplayTree.getRootNode(newRoot);
    }

    public Node link(Integer u, Integer v) {
        Node rootU = keyToNodes.get(new Pair<>(u, u)).getFirst();
        Node rootV = keyToNodes.get(new Pair<>(v, v)).getFirst();

        SplayTree.splay(rootU);
        SplayTree.splay(rootV);

        SplayTree.join(rootU, rootV);
        SplayTree.join(rootU, new Node(rootU.key));
        return SplayTree.getRootNode(rootU);
    }

    public Pair<Node, Node> cut(Integer u, Integer v) {
        Node edgeUV = keyToNodes.get(new Pair<>(u, v)).getFirst();
        Node edgeVU = keyToNodes.get(new Pair<>(v, u)).getFirst();

        Pair<Node, Node> roots = SplayTree.split(edgeUV);
        Node J;
        Node K;
        Node L;
        if(SplayTree.getRootNode(roots.getFirst()) == SplayTree.getRootNode(edgeVU)) {
            Pair<Node, Node> trees2 = SplayTree.split(edgeVU);
            J = trees2.getFirst();
            K = trees2.getSecond();
            L = roots.getFirst();
        } else {
            Pair<Node, Node> trees2 = SplayTree.split(edgeVU);
            J = roots.getFirst();
            K = trees2.getFirst();
            L = trees2.getSecond();
        }

        SplayTree.removeNode(edgeUV);
        SplayTree.removeNode(edgeVU);
        keyToNodes.remove(new Pair<>(u, v));
        keyToNodes.remove(new Pair<>(v, u));

        SplayTree.removeNode(SplayTree.lastNode(J));
        Node joined = SplayTree.join(J, L);
        this.splayRoot = SplayTree.getRootNode(K);
        return new Pair<>(K, joined);
    }

    public void addEdge(int u, int v){
        if(this.splayRoot == null){
            this.splayRoot = new Node(new Pair<>(u,u));
            keyToNodes.put(new Pair<>(u,u), new LinkedHashSet<>());
            keyToNodes.put(new Pair<>(v,u), new LinkedHashSet<>());
            keyToNodes.put(new Pair<>(u,v), new LinkedHashSet<>());
            keyToNodes.put(new Pair<>(v,v), new LinkedHashSet<>());

            keyToNodes.get(new Pair<>(u,u)).add(this.splayRoot);
            keyToNodes.get(new Pair<>(u,v)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(u,v)));
            keyToNodes.get(new Pair<>(v,v)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(v,v)));
            keyToNodes.get(new Pair<>(v,u)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(v,u)));
            keyToNodes.get(new Pair<>(u,u)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(u,u)));
            return;
        }
        if(keyToNodes.containsKey(new Pair<>(u, u)) && !keyToNodes.containsKey(new Pair<>(v,v))){
            keyToNodes.put(new Pair<>(v,u), new LinkedHashSet<>());
            keyToNodes.put(new Pair<>(u,v), new LinkedHashSet<>());
            keyToNodes.put(new Pair<>(v,v), new LinkedHashSet<>());

            this.reRoot(u);
            keyToNodes.get(new Pair<>(u,v)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(u,v)));
            keyToNodes.get(new Pair<>(v,v)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(v,v)));
            keyToNodes.get(new Pair<>(v,u)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(v,u)));
            keyToNodes.get(new Pair<>(u,u)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(u,u)));
        } else if(keyToNodes.containsKey(new Pair<>(v,v)) && !keyToNodes.containsKey(new Pair<>(u,u))){
            keyToNodes.put(new Pair<>(v,u), new LinkedHashSet<>());
            keyToNodes.put(new Pair<>(u,v), new LinkedHashSet<>());
            keyToNodes.put(new Pair<>(u,u), new LinkedHashSet<>());

            this.reRoot(v);
            keyToNodes.get(new Pair<>(u,v)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(v,u)));
            keyToNodes.get(new Pair<>(v,v)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(u,u)));
            keyToNodes.get(new Pair<>(v,u)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(u,v)));
            keyToNodes.get(new Pair<>(u,u)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(v,v)));
        } else {
            throw new Error("Illegal operation. Only one vertex must be present in a tree.");
        }
    }

    public Node deleteEdge(int u, int v){
        if(this.splayRoot == null){return null;}
        if(keyToNodes.containsKey(new Pair<>(u,v)) && keyToNodes.containsKey(new Pair<>(v,u))){
            Pair<Node, Node> trees = this.cut(u, v);
            if(keyToNodes.get(new Pair<>(u,u)).size() <= 1){
                keyToNodes.remove(new Pair<>(u,u));
                return null;
            }
            else if(keyToNodes.get(new Pair<>(v,v)).size() <= 1){
                keyToNodes.remove(new Pair<>(v,v));
                return null;
            } else {
                return trees.getSecond();
            }
        } else {
            throw new Error(String.format("Edge {%d, %d}doesn't exists!", u, v));
        }
    }

    public Integer getRoot(){
        return SplayTree.firstNode(this.splayRoot).key.getFirst();
    }

    public ArrayList<Pair<Integer, Integer>> getEdges(){
        ArrayList<Pair<Integer, Integer>> listOfEdges = new ArrayList<>();
        for(Pair<Integer, Integer> edge: keyToNodes.keySet()){
            if(edge.getFirst() != edge.getSecond()){
                listOfEdges.add(edge);
            }
        }
        return listOfEdges;
    }


}

package pl.edu.agh.cs.forest;

import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.EulerTourTree;
import pl.edu.agh.cs.eulerTourTree.SelfBalancingTree;
import pl.edu.agh.cs.eulerTourTree.splay.Node;
import pl.edu.agh.cs.eulerTourTree.splay.SplayTree;

import java.util.*;

public class Forest {

    private Map<Integer, LinkedHashSet<Integer>> nonTreeEdges;
    private Integer level = -1;
    private ArrayList<Forest> hierarchicalForests = new ArrayList<>();
    private Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes;
    static SelfBalancingTree splayTree = new SplayTree();

    public Forest(Integer level, ArrayList<Forest> forests) {
        this.nonTreeEdges = new HashMap<>();
        this.keyToNodes = new HashMap<>();
        this.level = level;
        this.hierarchicalForests = forests;
    }

    public Map<Integer, LinkedHashSet<Integer>> getNonTreeEdges(){ return nonTreeEdges; }
    public Map<Pair<Integer, Integer>, LinkedHashSet<Node>> getKeyToNodes() { return keyToNodes; }

    public Optional<Node> getRepresentativeTreeNode(Integer u){
        if(checkIfVertexHasNodeInTheTree(u, keyToNodes))
            return splayTree.getRootNode(keyToNodes.get(new Pair<>(u,u)).getFirst());
        else return Optional.empty();
    }

    public static boolean checkIfVertexHasNodeInTheTree(Integer v, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes){
        if(keyToNodes.containsKey(new Pair<>(v,v)))
            return !keyToNodes.get(new Pair<>(v,v)).isEmpty();
        return false;
    }

    private void showNonTreeEdges(){
        System.out.println("Non tree edges: ");
        for(Integer u : this.nonTreeEdges.keySet()){
            System.out.printf("Begin: %d%n", u);
            for(Integer v : this.nonTreeEdges.get(u)){
                System.out.printf("/tend: %d%n", v);
            }
        }
    }

    private void showkeyToNodes(){
        System.out.println("Key To Node: ");
        for(Pair<Integer, Integer> key: this.keyToNodes.keySet()){
            System.out.printf("For key: %s%n", key);
            for(Node node : this.keyToNodes.get(key)){
                System.out.printf("/tNode: %s%n", node);
            }
        }
    }

    public void addTreeEdge(Integer u, Integer v) {
        Optional<Node> nodeU = getRepresentativeTreeNode(u);
        Optional<Node> nodeV = getRepresentativeTreeNode(v);

        if(nodeU.isPresent() && nodeV.isPresent() && !u.equals(v)) {
            EulerTourTree.link(u, v, keyToNodes);
        } else if(nodeU.isPresent() && !u.equals(v)) {
            EulerTourTree.addEdgeToNonExistingVertex(u, v, keyToNodes);
        } else if(nodeV.isPresent() && !u.equals(v)) {
            EulerTourTree.addEdgeToNonExistingVertex(v, u, keyToNodes);
        } else {
            EulerTourTree.createNewEulerTourTree(u, v, keyToNodes);
        }
    }

    public void addNonTreeEdge(Integer u, Integer v){
        if(u.equals(v)) return;
        if(!this.nonTreeEdges.containsKey(u))
            this.nonTreeEdges.put(u, new LinkedHashSet<>());
        if(!this.nonTreeEdges.containsKey(v))
            this.nonTreeEdges.put(v, new LinkedHashSet<>());
        this.nonTreeEdges.get(u).add(v);
        this.nonTreeEdges.get(v).add(u);
    }

    public void deleteNonTreeEdge(Integer u, Integer v) {
        if(u.equals(v)) return;
        if(!this.nonTreeEdges.containsKey(u) || !this.nonTreeEdges.get(u).contains(v))
            return;
        if(!this.nonTreeEdges.containsKey(v) || !this.nonTreeEdges.get(v).contains(u))
            return;

        this.nonTreeEdges.get(u).remove(v);
        this.nonTreeEdges.get(v).remove(u);
    }

    public LinkedHashSet<Integer> getNonTreeEdges(Integer u){
        if(!this.nonTreeEdges.containsKey(u))
            this.nonTreeEdges.put(u, new LinkedHashSet<>());
        return this.nonTreeEdges.get(u);
    }

    public void deleteTreeEdge(Integer u, Integer v) {
        if(u.equals(v)) return;
        Optional<Node> nodeU = getRepresentativeTreeNode(u);
        Optional<Node> nodeV = getRepresentativeTreeNode(v);

        if(nodeU.isPresent() && nodeV.isPresent() && Objects.equals(nodeU.get(), nodeV.get())){
            EulerTourTree.deleteEdge(u, v, keyToNodes);
        }
    }

    public void findReplacementEdge(Integer v, Integer w, Integer level) {
        if(level < 0 || v.equals(w)) return;

        Optional<Node> nodeV = getRepresentativeTreeNode(v);
        Optional<Node> nodeW = getRepresentativeTreeNode(w);

        if(nodeV.isPresent() && nodeW.isPresent() && nodeV.get().equals(nodeW.get())) return;

        Node Tmin;
        if(nodeV.isEmpty() || nodeW.isEmpty()) return;
        else if(nodeV.get().equals(nodeW.get())) return;
        else if(splayTree.getSizeOfTree(nodeV.get()) > splayTree.getSizeOfTree(nodeW.get())){
            Tmin = nodeW.get();
        } else {
            Tmin = nodeV.get();
        }

        for (Pair<Integer, Integer> edge : EulerTourTree.getEdges(Tmin)) {
            if (!this.hierarchicalForests.get(level + 1).checkIfTreeEdgeExists(edge.getFirst(), edge.getSecond()))
                this.hierarchicalForests.get(level + 1).addTreeEdge(edge.getFirst(), edge.getSecond());
        }
        boolean nonTreeEdgeFound = false;
        Pair<Integer, Integer> nonTreeEdge = null;

        for(Integer vertex: EulerTourTree.getVertices(Tmin)){
            for(Integer nonTreeEdgeEnd: this.hierarchicalForests.get(level).getNonTreeEdges(vertex)){
                if(!getRepresentativeTreeNode(nonTreeEdgeEnd).get().equals(splayTree.getRootNode(Tmin).get())){
                    nonTreeEdgeFound = true;
                    nonTreeEdge = new Pair<>(vertex, nonTreeEdgeEnd);
                    for (Forest hierarchicalForest : hierarchicalForests) {
                        hierarchicalForest.deleteNonTreeEdge(vertex, nonTreeEdgeEnd);
                    }
                    break;
                } else {
                    if(!hierarchicalForests.get(level+1).checkIfNonTreeEdgeExists(vertex, nonTreeEdgeEnd))
                        hierarchicalForests.get(level+1).addNonTreeEdge(vertex, nonTreeEdgeEnd);
                }
            }
            if(nonTreeEdgeFound) break;
        }
        if(nonTreeEdgeFound){
            for(int lvl= 0; lvl <= level; lvl++){
                hierarchicalForests.get(lvl).addTreeEdge(nonTreeEdge.getFirst(), nonTreeEdge.getSecond());
            }
        } else {
            if(level-1 >= 0)
                this.hierarchicalForests.get(level-1).findReplacementEdge(v, w, level-1);
        }
    }

    public boolean checkIfNonTreeEdgeExists(Integer u, Integer v){
        if(this.nonTreeEdges.containsKey(u))
            return this.nonTreeEdges.get(u).contains(v);
        return false;
    }

    public boolean checkIfTreeEdgeExists(Integer v, Integer u){
        Optional<Node> nodeU = getRepresentativeTreeNode(u);
        Optional<Node> nodeV = getRepresentativeTreeNode(v);
        if(this.keyToNodes.containsKey(new Pair<>(v,u)))
            return !this.keyToNodes.get(new Pair<>(v,u)).isEmpty() &&
                    nodeU.isPresent() &&
                    nodeV.isPresent() &&
                    Objects.equals(nodeU.get(), nodeV.get());
        else return false;
    }

    public boolean isConnected(Integer u, Integer v){
        Optional<Node> nodeU = getRepresentativeTreeNode(u);
        Optional<Node> nodeV = getRepresentativeTreeNode(v);
        return nodeU.isPresent() && nodeV.isPresent() && Objects.equals(nodeU.get(), nodeV.get());
    }

    public Integer getAmtOfTrees(){
        Set<Node> trees = new HashSet<>();
        for(Pair<Integer, Integer> p: keyToNodes.keySet()){
            getRepresentativeTreeNode(p.getFirst()).ifPresent(trees::add);
        }
        return trees.size();
    }

}

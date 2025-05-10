package pl.edu.agh.cs.forest;

import pl.edu.agh.cs.DynamicConnectivity;
import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.EulerTourTree;
import pl.edu.agh.cs.eulerTourTree.splay.Node;
import pl.edu.agh.cs.eulerTourTree.splay.SplayTree;

import java.util.*;

public class Forest {

    private Map<Integer, LinkedHashSet<Integer>> nonTreeEdges;
    private Integer level = -1;
    private ArrayList<Forest> hierarchicalForests = new ArrayList<>();
    private Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes;

    public Forest(Integer level, ArrayList<Forest> dcAlgo) {
        this.nonTreeEdges = new HashMap<>();
        this.keyToNodes = new HashMap<>();
        this.level = level;
        this.hierarchicalForests = dcAlgo;
    }

    public Map<Integer, LinkedHashSet<Integer>> getNonTreeEdges(){ return nonTreeEdges; }
    public Map<Pair<Integer, Integer>, LinkedHashSet<Node>> getKeyToNodes() { return keyToNodes; }

    public Node getRepresentativeTreeNode(Integer u){
        if(checkIfVertexHasNodeInTheTree(u))
            return SplayTree.getRootNode(keyToNodes.get(new Pair<>(u,u)).getFirst());
        else return null;
    }

    public boolean checkIfVertexHasNodeInTheTree(Integer v){
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
        Node nodeU = getRepresentativeTreeNode(u);
        Node nodeV = getRepresentativeTreeNode(v);

        if(nodeU != null && nodeV != null){
            EulerTourTree.link(u, v, keyToNodes);
        } else if(nodeU != null){
            EulerTourTree.addEdgeToNonExistingVertex(nodeU, u, v, keyToNodes);
        } else if(nodeV != null){
            EulerTourTree.addEdgeToNonExistingVertex(nodeV, v, u, keyToNodes);
        } else {
            EulerTourTree.createNewEulerTourTree(u, v, keyToNodes);
        }
    }

    public void addNonTreeEdge(Integer u, Integer v){
        if(!this.nonTreeEdges.containsKey(u))
            this.nonTreeEdges.put(u, new LinkedHashSet<>());
        if(!this.nonTreeEdges.containsKey(v))
            this.nonTreeEdges.put(v, new LinkedHashSet<>());
        this.nonTreeEdges.get(u).add(v);
        this.nonTreeEdges.get(v).add(u);
    }

    public void deleteNonTreeEdge(Integer u, Integer v){
        if(this.nonTreeEdges.containsKey(u) && this.nonTreeEdges.containsKey(v)){
            this.nonTreeEdges.get(u).remove(v);
            this.nonTreeEdges.get(v).remove(u);
        }
    }

    public LinkedHashSet<Integer> getNonTreeEdges(Integer u){
        if(!this.nonTreeEdges.containsKey(u))
            this.nonTreeEdges.put(u, new LinkedHashSet<>());
        return this.nonTreeEdges.get(u);
    }

    public void deleteTreeEdge(Integer u, Integer v) {
        if(!this.checkIfTreeEdgeExists(u, v)) return;
        Node nodeU = getRepresentativeTreeNode(u);
        Node nodeV = getRepresentativeTreeNode(v);

        if(Objects.equals(nodeU, nodeV) && nodeU != null){
            EulerTourTree.deleteEdge(nodeU, u, v, keyToNodes);
        }
    }

    public void findReplacementEdge(Integer v, Integer w, Integer level) {
        Node nodeV = getRepresentativeTreeNode(v);
        Node nodeW = getRepresentativeTreeNode(w);
        Node Tmin;
        if(nodeV == null){
            Tmin = null;
        }
        else if(nodeW == null){
            Tmin = null;
        }
        else if(SplayTree.getSizeOfTree(nodeV) > SplayTree.getSizeOfTree(nodeW)){
            Tmin = nodeW;
        } else {
            Tmin = nodeV;
        }

        if(Tmin != null){
            for(Pair<Integer, Integer> edge: EulerTourTree.getEdges(Tmin)){
                this.hierarchicalForests.get(level+1).addTreeEdge(edge.getFirst(), edge.getSecond());
            }
            boolean nonTreeEdgeFound = false;
            Pair<Integer, Integer> nonTreeEdge = null;

            for(Integer vertex: EulerTourTree.getVertices(Tmin)){
                for(Integer nonTreeEdgeEnd: this.hierarchicalForests.get(level).getNonTreeEdges(vertex)){
                    if(!getRepresentativeTreeNode(nonTreeEdgeEnd).equals(SplayTree.getRootNode(Tmin))){
                        nonTreeEdgeFound = true;
                        nonTreeEdge = new Pair<>(vertex, nonTreeEdgeEnd);
                        for (Forest hierarchicalForest : hierarchicalForests) {
                            hierarchicalForest.deleteNonTreeEdge(vertex, nonTreeEdgeEnd);
                        }
                        break;
                    } else {
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
                this.findReplacementEdge(v, w, level-1);
            }
        }
    }

    public boolean checkIfNonTreeEdgeExists(Integer u, Integer v){
        if(this.nonTreeEdges.containsKey(u))
            return this.nonTreeEdges.get(u).contains(v);
        return false;
    }

    public boolean checkIfTreeEdgeExists(Integer v, Integer u){
        Node nodeU = getRepresentativeTreeNode(u);
        Node nodeV = getRepresentativeTreeNode(v);
        if(nodeU != null && Objects.equals(nodeU, nodeV)){
            return true;
        }
        return false;
    }

    public boolean isConnected(Integer u, Integer v){
        Node nodeU = getRepresentativeTreeNode(u);
        Node nodeV = getRepresentativeTreeNode(v);
        return Objects.equals(nodeU, nodeV) && nodeU != null;
    }

    public Integer getAmtOfTrees(){
        Set<Node> trees = new HashSet<>();
        for(Pair<Integer, Integer> p: keyToNodes.keySet()){
            trees.add(getRepresentativeTreeNode(p.getFirst()));
        }
        return trees.size();
    }

}

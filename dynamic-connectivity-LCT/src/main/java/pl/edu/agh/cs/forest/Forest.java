package pl.edu.agh.cs.forest;

import pl.edu.agh.cs.common.Pair;

import pl.edu.agh.cs.linkCutTree.LinkCutTree;
import pl.edu.agh.cs.linkCutTree.splay.Node;

import java.util.*;

public class Forest {

    private Map<Integer, LinkedHashSet<Integer>> nonTreeEdges;
    private Map<Integer, LinkedHashSet<Integer>> treeEdges;
    private Integer level = -1;
    private ArrayList<Forest> hierarchicalForests = new ArrayList<>();
    private Map<Integer, Optional<Node>> keyToNode;
    static LinkCutTree linkCutTree = new LinkCutTree();

    public Forest(Integer level, ArrayList<Forest> forests) {
        this.nonTreeEdges = new HashMap<>();
        this.keyToNode = new HashMap<>();
        this.level = level;
        this.hierarchicalForests = forests;
        this.treeEdges = new HashMap<>();
    }

    public Map<Integer, LinkedHashSet<Integer>> getNonTreeEdges(){ return nonTreeEdges; }
    public Map<Integer, Optional<Node>> getKeyToNodes() { return keyToNode; }

    public Optional<Node> getRepresentativeTreeNode(Integer u){
        if(checkIfVertexHasNodeInTheTree(u, keyToNode))
            return linkCutTree.findLinkCutRoot(keyToNode.get(u).get());
        else return Optional.empty();
    }

    public static boolean checkIfVertexHasNodeInTheTree(Integer v, Map<Integer , Optional<Node>> keyToNode){
        if(keyToNode.containsKey(v))
            return keyToNode.get(v).isPresent();
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
        for(Integer key: this.keyToNode.keySet()){
            System.out.printf("For key: %s%n", key);
            System.out.printf("/tNode: %s%n", this.keyToNode.get(key).get());
        }
    }

    public void addTreeEdge(Integer u, Integer v) {
        Optional<Node> nodeU = getRepresentativeTreeNode(u);
        Optional<Node> nodeV = getRepresentativeTreeNode(v);
        if(!this.treeEdges.containsKey(u))
            this.treeEdges.put(u, new LinkedHashSet<>());
        if(!this.treeEdges.containsKey(v))
            this.treeEdges.put(v, new LinkedHashSet<>());
        this.treeEdges.get(u).add(v);
        this.treeEdges.get(v).add(u);

        if(nodeU.isPresent() && nodeV.isPresent() && !u.equals(v)) {
            linkCutTree.link(keyToNode.get(u).get(), keyToNode.get(v).get());
        } else if(nodeU.isPresent() && !u.equals(v)) {
            linkCutTree.addNonExistingNodeToTree(keyToNode.get(u).get(), v, keyToNode);
        } else if(nodeV.isPresent() && !u.equals(v)) {
            linkCutTree.addNonExistingNodeToTree(keyToNode.get(v).get(), u, keyToNode);
        } else {
            linkCutTree.createNewLinkCutTree(u, v, keyToNode);
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
        if(treeEdges.containsKey(u)) {
            if (!treeEdges.get(u).contains(v)) {
                return;
            }
        }
        else return;
        Optional<Node> nodeU = getRepresentativeTreeNode(u);
        Optional<Node> nodeV = getRepresentativeTreeNode(v);

        if(nodeU.isPresent() && nodeV.isPresent() && Objects.equals(nodeU.get(), nodeV.get())) {
            linkCutTree.deleteTreeEdge(u, v, keyToNode);
            this.treeEdges.get(u).remove(v);
            this.treeEdges.get(v).remove(u);
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
        else if(linkCutTree.getSizeOfTree(nodeV.get()) > linkCutTree.getSizeOfTree(nodeW.get())){
            Tmin = nodeW.get();
        } else {
            Tmin = nodeV.get();
        }

        for (Pair<Integer, Integer> edge : getEdges(Tmin.key)) {
            if (!this.hierarchicalForests.get(level + 1).checkIfTreeEdgeExists(edge.getFirst(), edge.getSecond()))
                this.hierarchicalForests.get(level + 1).addTreeEdge(edge.getFirst(), edge.getSecond());
        }
        boolean nonTreeEdgeFound = false;
        Pair<Integer, Integer> nonTreeEdge = null;

        for(Integer vertex: getVertices(Tmin.key)){
            for(Integer nonTreeEdgeEnd: this.hierarchicalForests.get(level).getNonTreeEdges(vertex)){
                Optional<Node> represented = getRepresentativeTreeNode(nonTreeEdgeEnd);
                if(!represented.get().equals(linkCutTree.getRootNode(Tmin).get())){
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

    public Set<Pair<Integer, Integer>> getEdges(Integer key){
        Optional<Node> optNode = getRepresentativeTreeNode(key);
        Set<Pair<Integer, Integer>> edges = new HashSet<>();
        optNode.ifPresent(node -> dfsEdges(node.key, -1, edges));
        return edges;
    }

    public Set<Integer> getVertices(Integer key){
        Optional<Node> optNode = getRepresentativeTreeNode(key);
        Set<Integer> vertices = new HashSet<>();
        optNode.ifPresent(node -> dfsVertices(node.key, -1, vertices));
        return vertices;
    }

    private void dfsEdges(Integer rootKey, Integer prev, Set<Pair<Integer, Integer>> edges){
        for (Integer n : treeEdges.get(rootKey)) {
            if(!n.equals(prev)) {
                edges.add(new Pair<>(rootKey, n));
                dfsEdges(n, rootKey, edges);
            }
        }
    }

    public void dfsVertices(Integer rootKey, Integer prev, Set<Integer> vertices){
        vertices.add(rootKey);
        for(Integer n: treeEdges.get(rootKey)){
            if(!n.equals(prev))
                dfsVertices(n, rootKey, vertices);
        }
    }

    public boolean checkIfNonTreeEdgeExists(Integer u, Integer v){
        if(this.nonTreeEdges.containsKey(u))
            return this.nonTreeEdges.get(u).contains(v);
        return false;
    }

    public boolean checkIfTreeEdgeExists(Integer v, Integer u){
        if(this.treeEdges.containsKey(v))
            return this.treeEdges.get(v).contains(u);
        return false;
    }

    public boolean isConnected(Integer u, Integer v){
        Optional<Node> nodeU = getRepresentativeTreeNode(u);
        Optional<Node> nodeV = getRepresentativeTreeNode(v);
        return nodeU.isPresent() && nodeV.isPresent() && Objects.equals(nodeU.get(), nodeV.get());
    }

    public Integer getAmtOfTrees(){
        Set<Node> trees = new HashSet<>();
        for(Integer p: keyToNode.keySet()){
            getRepresentativeTreeNode(p).ifPresent(trees::add);
        }
        return trees.size();
    }

}

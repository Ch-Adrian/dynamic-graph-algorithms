package pl.edu.agh.cs.forest;

import pl.edu.agh.cs.DynamicConnectivity;
import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.EulerTourTree;
import pl.edu.agh.cs.eulerTourTree.splay.Node;
import pl.edu.agh.cs.eulerTourTree.splay.SplayTree;

import java.util.*;

public class Forest {

    private Map<Integer, LinkedHashSet<Integer>> nonTreeEdges;
    private Map<Integer, Node> vertexToNode;
    private int level = -1;
    private DynamicConnectivity dynamicConnectivity;
    private Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes;

    public Forest(int level, DynamicConnectivity dcAlgo) {
        this.nonTreeEdges = new HashMap<>();
        this.keyToNodes = new HashMap<>();
        this.vertexToNode = new HashMap<>();
        this.level = level;
        this.dynamicConnectivity = dcAlgo;
    }

    public Node getRepresentativeTreeNode(int u){
        return SplayTree.getRootNode(vertexToNode.get(u));
    }

    private void createNewTree(Node treeNode){
        this.vertexToNode.put(treeNode.key.getFirst(), treeNode);
        this.vertexToNode.put(treeNode.key.getSecond(), treeNode);
    }

    public void createNewTree(int u, int v){
        EulerTourTree.addEdge(null, u, v, keyToNodes);
        this.vertexToNode.put(u, keyToNodes.get(new Pair<>(u,u)).getFirst());
        this.vertexToNode.put(v, keyToNodes.get(new Pair<>(v,v)).getFirst());
    }


    private void showVertexIdToNode(){
        System.out.println("Vertex Id To Tkey: ");
        for(Integer vertexId : this.vertexToNode.keySet()){
            System.out.printf("vertexId: %d node: %s%n", vertexId, this.vertexToNode.get(vertexId));
        }
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

    public void addTreeEdge(int u, int v) {
        Node nodeU = SplayTree.getRootNode(this.vertexToNode.get(u));
        Node nodeV = SplayTree.getRootNode(this.vertexToNode.get(v));

        if(nodeU != null && nodeV != null){
            EulerTourTree.link(u, v, keyToNodes);
        } else if(nodeU != null){
            EulerTourTree.addEdge(nodeU, u, v, keyToNodes);
            this.vertexToNode.put(v, nodeU);
        } else if(nodeV != null){
            EulerTourTree.addEdge(nodeV, u, v, keyToNodes);
            this.vertexToNode.put(u, nodeV);
        } else {
            this.createNewTree(u, v);
        }
    }

    public void addNonTreeEdge(int u, int v){
        if(!this.nonTreeEdges.containsKey(u))
            this.nonTreeEdges.put(u, new LinkedHashSet<>());
        if(!this.nonTreeEdges.containsKey(v))
            this.nonTreeEdges.put(v, new LinkedHashSet<>());
        this.nonTreeEdges.get(u).add(v);
        this.nonTreeEdges.get(v).add(u);
    }

    public void deleteNonTreeEdge(int u, int v){
        if(this.nonTreeEdges.containsKey(u) && this.nonTreeEdges.containsKey(v)){
            this.nonTreeEdges.get(u).remove(v);
            this.nonTreeEdges.get(v).remove(u);
        }
    }

    public LinkedHashSet<Integer> getNonTreeEdges(int u){
        if(!this.nonTreeEdges.containsKey(u))
            this.nonTreeEdges.put(u, new LinkedHashSet<>());
        return this.nonTreeEdges.get(u);
    }

    public void deleteTreeEdge(int u, int v) {
        if(!this.checkIfTreeEdgeExists(u, v)) return;
        Node nodeU = SplayTree.getRootNode(this.vertexToNode.get(u));
        Node nodeV = SplayTree.getRootNode(this.vertexToNode.get(v));

        if(nodeU != null && nodeV != null){
            if(nodeU.equals(nodeV)){
                Pair<Node, Node> trees = EulerTourTree.deleteEdge(nodeU, u,v, keyToNodes);
                assert trees != null;
                if(SplayTree.getRootNode(trees.getFirst()).equals(SplayTree.getRootNode(nodeU))){
                    this.vertexToNode.put(u, trees.getFirst());
                    this.vertexToNode.put(v, trees.getSecond());
                } else {
                    this.vertexToNode.put(v, trees.getFirst());
                    this.vertexToNode.put(u, trees.getSecond());
                }
            } else {
                throw new Error("Cannot be situation that edge connects two different trees!");
            }
        }
    }

    public void findReplacementEdge(int v, int w, int level) {
        Node nodeV = SplayTree.getRootNode(this.vertexToNode.get(v));
        Node nodeW = SplayTree.getRootNode(this.vertexToNode.get(w));
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
                this.dynamicConnectivity.getForestForLevel(level+1).addTreeEdge(edge.getFirst(), edge.getSecond());
            }
            boolean nonTreeEdgeFound = false;
            Pair<Integer, Integer> nonTreeEdge = null;

            for(Integer vertex: EulerTourTree.getVertices(Tmin)){
                for(Integer nonTreeEdgeEnd: this.dynamicConnectivity.getForestForLevel(level).getNonTreeEdges(vertex)){
                    if(!SplayTree.getRootNode(this.vertexToNode.get(nonTreeEdgeEnd)).equals(SplayTree.getRootNode(Tmin))){
                        nonTreeEdgeFound = true;
                        nonTreeEdge = new Pair<>(vertex, nonTreeEdgeEnd);
                        for(int lvl= 0; lvl < dynamicConnectivity.getAmtOfLevels(); lvl++){
                            dynamicConnectivity.getForestForLevel(lvl).deleteNonTreeEdge(vertex, nonTreeEdgeEnd);
                        }
                        break;
                    } else {
                        dynamicConnectivity.getForestForLevel(level+1).addNonTreeEdge(vertex, nonTreeEdgeEnd);
                    }
                }
                if(nonTreeEdgeFound) break;
            }
            if(nonTreeEdgeFound){
                for(int lvl= 0; lvl <= level; lvl++){
                    dynamicConnectivity.getForestForLevel(lvl).addTreeEdge(nonTreeEdge.getFirst(), nonTreeEdge.getSecond());
                }
            } else {
                this.findReplacementEdge(v, w, level-1);
            }
        }
    }

    public boolean checkIfNonTreeEdgeExists(int u, int v){
        if(this.nonTreeEdges.containsKey(u))
            return this.nonTreeEdges.get(u).contains(v);
        return false;
    }

    public boolean checkIfTreeEdgeExists(int v, int u){
        Node nodeU = SplayTree.getRootNode(this.vertexToNode.get(u));
        Node nodeV = SplayTree.getRootNode(this.vertexToNode.get(v));
        if(nodeU != null && Objects.equals(nodeU, nodeV)){
            return true;
        }
        return false;
    }

    public boolean isConnected(Integer u, Integer v){
        Node nodeU = SplayTree.getRootNode(this.vertexToNode.get(u));
        Node nodeV = SplayTree.getRootNode(this.vertexToNode.get(v));
        return Objects.equals(nodeU, nodeV);
    }
}

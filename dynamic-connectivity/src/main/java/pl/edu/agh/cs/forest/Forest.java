package pl.edu.agh.cs.forest;

import pl.edu.agh.cs.DynamicConnectivity;
import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.splay.Node;

import java.util.*;

public class Forest {

    private Map<Integer, LinkedHashSet<Integer>> nonTreeEdges;
    private Map<Integer, Node> vertexToNode;
    private Map<Node, Tree> nodeToTree;
    private int level = -1;
    private DynamicConnectivity dynamicConnectivity;
    private Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes;

    public Forest(int level, DynamicConnectivity dcAlgo) {
        this.nonTreeEdges = new HashMap<>();
        this.nodeToTree = new HashMap<>();
        this.keyToNodes = new HashMap<>();
        this.vertexToNode = new HashMap<>();
        this.level = level;
        this.dynamicConnectivity = dcAlgo;
    }

    private void createNewTree(Node treeNode){
        Tree t = new Tree(treeNode, keyToNodes);
        this.vertexToNode.put(treeNode.key.getFirst(), treeNode);
        this.vertexToNode.put(treeNode.key.getSecond(), treeNode);
    }

    public void createNewTree(int u, int v){
        Tree t = new Tree(treeCounter, keyToNodes);
        t.addTreeEdge(u, v);
        this.vertexIdToTkey.put(u, treeCounter);
        this.vertexIdToTkey.put(v, treeCounter);
        this.tkeyToTree.put(treeCounter, t);
        treeCounter++;
    }

    private void showTkeyToTree(){
        System.out.println("Tkey To Tree: ");
        for(Integer tkey : this.tkeyToTree.keySet()){
            System.out.printf("tkey: %d tree: %s%n", tkey, this.tkeyToTree.get(tkey));
        }
    }

    private void showVertexIdToTkey(){
        System.out.println("Vertex Id To Tkey: ");
        for(Integer vertexId : this.vertexIdToTkey.keySet()){
            System.out.printf("vertexId: %d tkey: %d%n", vertexId, this.vertexIdToTkey.get(vertexId));
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

    public void addTreeEdge(int u, int v) throws Exception {
        System.out.printf("AddTreeEdge %d, %d%n", u, v);
        Integer tkeyU = vertexIdToTkey.get(u);
        Integer tkeyV = vertexIdToTkey.get(v);

        if(tkeyU != null && tkeyV != null){
            if(Objects.equals(tkeyU, tkeyV)){
                throw new Exception("This edge should be a non tree edge!");
            } else {
                System.out.println("link two trees!:"+tkeyU+" "+tkeyV);
                this.tkeyToTree.get(tkeyU).show();
                tkeyToTree.get(tkeyU).linkTwoTreesWithEdge(u, v, tkeyToTree.get(tkeyV));
//                this.showTkeyToTree();
//                this.showVertexIdToTkey();
//                this.tkeyToTree.get(tkeyU).show();
                for(Integer treeVertex: this.tkeyToTree.get(tkeyU).getVertices()){
                    System.out.println(tkeyU+" "+treeVertex);
                    this.vertexIdToTkey.put(treeVertex, tkeyU); //TODO: to optimize
                }

            }
        } else if(tkeyU != null){
            tkeyToTree.get(tkeyU).addTreeEdge(u, v);
            vertexIdToTkey.put(v, tkeyU);
        } else if(tkeyV != null){
            tkeyToTree.get(tkeyV).addTreeEdge(v, u);
            vertexIdToTkey.put(u, tkeyV);
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

    public Tree getTree(int u) {
        return tkeyToTree.get(vertexIdToTkey.get(u));
    }

    public int getAmtOfTrees(){ return this.tkeyToTree.values().size(); }

    public void deleteTreeEdge(int u, int v) {
        if(!this.checkIfTreeEdgeExists(u, v)) return;
        Integer tkeyU = vertexIdToTkey.get(u);
        Integer tkeyV = vertexIdToTkey.get(v);

        if(tkeyU != null && tkeyV != null){
            if(Objects.equals(tkeyU, tkeyV)){
                Node newTree = tkeyToTree.get(tkeyU).deleteEdge(u, v);

                Integer t = this.createNewTree(newTree);
                for(Integer treeVertex: this.tkeyToTree.get(t).getVertices()){
                    this.vertexIdToTkey.put(treeVertex, t); //TODO: to optimize
                }

            } else {
                if(Objects.equals(tkeyToTree.get(tkeyU).getRoot(), tkeyToTree.get(tkeyV).getRoot())){
                    Node newTree = tkeyToTree.get(tkeyU).deleteEdge(u, v);
                    Integer t = this.createNewTree(newTree);
                    for(Integer treeVertex: this.tkeyToTree.get(t).getVertices()){
                        this.vertexIdToTkey.put(treeVertex, t); //TODO: to optimize
                    }
                } else {
                    throw new Error("Cannot be situation that edge connects two different trees!");
                }
            }
        }
    }

    public void findReplacementEdge(int v, int w, int level) throws Exception {
        Tree Tv = this.tkeyToTree.get(vertexIdToTkey.get(v));
        Tree Tw = this.tkeyToTree.get(vertexIdToTkey.get(w));
        Tree Tmin;
        Integer V;
        if(Tv == null){
            Tmin = null;
            V = v;
        }
        else if(Tw == null){
            Tmin = null;
            V = w;
        }
        else if(Tv.getSize() > Tw.getSize()){
            Tmin = Tw;
            V = w;
        } else {
            Tmin = Tv;
            V = v;
        }

        if(Tmin != null){
            for(Pair<Integer, Integer> edge: Tmin.getEdges()){
                this.dynamicConnectivity.getForestForLevel(level+1).addTreeEdge(edge.getFirst(), edge.getSecond());
            }
            boolean nonTreeEdgeFound = false;
            Pair<Integer, Integer> nonTreeEdge = null;

            for(Integer vertex: Tmin.getVertices()){
                for(Integer nonTreeEdgeEnd: this.dynamicConnectivity.getForestForLevel(level).getNonTreeEdges(vertex)){
                    if(this.getTree(nonTreeEdgeEnd) != Tmin){
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
        if(this.nonTreeEdges.containsKey(u)){
            if(this.nonTreeEdges.get(u).contains(v))
                return true;
        }
        return false;
    }

    public boolean checkIfTreeEdgeExists(int u, int v){
        Integer tkeyU = vertexIdToTkey.get(u);
        Integer tkeyV = vertexIdToTkey.get(v);
        if(tkeyU != null && Objects.equals(tkeyV, tkeyU)){
            return true;
        }
        return false;
    }

    public boolean isConnected(Integer v, Integer w){
        this.showTkeyToTree();
        this.showVertexIdToTkey();
        return this.vertexIdToTkey.get(v).equals(this.vertexIdToTkey.get(w));
    }
}

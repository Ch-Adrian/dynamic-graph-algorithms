package pl.edu.agh.cs.forest;

import pl.edu.agh.cs.DynamicConnectivity;
import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.splay.Node;

import java.util.*;

public class Forest {

    private Map<Integer, LinkedHashSet<Integer>> nonTreeEdges;
    private Map<Integer, Integer> vertexIdToTkey;
    private Map<Integer, Tree> tkeyToTree;
    private ArrayList<Tree> trees;
    private int level;
    private int treeCounter = 0;
    private DynamicConnectivity dynamicConnectivity;
    private Map<Pair<Integer, Integer>, ArrayList<Node>> keyToNodes;

    public Forest(int level, DynamicConnectivity dcAlgo) {
        this.vertexIdToTkey = new HashMap<>();
        this.nonTreeEdges = new HashMap<>();
        this.tkeyToTree = new HashMap<>();
        this.keyToNodes = new HashMap<>();
        this.trees = new ArrayList<>();
        this.level = level;
        this.dynamicConnectivity = dcAlgo;
    }

    private Integer createNewTree(Node treeNode){
        Tree t = new Tree(treeCounter, treeNode, keyToNodes);
        this.trees.add(t);
        this.tkeyToTree.put(treeCounter, t);
        treeCounter++;
        return treeCounter-1;
    }

    public void createNewTree(int u, int v){
        Tree t = new Tree(treeCounter, keyToNodes);
        t.addTreeEdge(u, v);
        this.trees.add(t);
        this.vertexIdToTkey.put(u, treeCounter);
        this.vertexIdToTkey.put(v, treeCounter);
        this.tkeyToTree.put(treeCounter, t);
        treeCounter++;
    }

    public void addTreeEdge(int u, int v) throws Exception {
        Integer tkeyU = vertexIdToTkey.get(u);
        Integer tkeyV = vertexIdToTkey.get(v);
        if(tkeyU != null && tkeyV != null){
            if(Objects.equals(tkeyU, tkeyV)){
                throw new Exception("This edge should be a non tree edge!");
            } else {
                if(u == 4 && v == 8) System.out.println("Check if edge 4-8 exists2: "+this.checkIfTreeEdgeExists(4, 8));
                tkeyToTree.get(tkeyU).linkTwoTreesWithEdge(u, v, tkeyToTree.get(tkeyV));
                if(u == 4 && v == 8) System.out.println("Check if edge 4-8 exists:3.5 "+this.checkIfTreeEdgeExists(4, 8));
                for(Integer treeVertex: this.tkeyToTree.get(tkeyU).getVertices()){
                    if(u == 4 && v == 8) System.out.println("TreeVertex: "+treeVertex);
                    this.vertexIdToTkey.put(treeVertex, tkeyU); //TODO: to optimize
                    if(this.vertexIdToTkey.get(treeVertex).equals(tkeyU)){
                        if(u == 4 && v == 8) System.out.println("Workds!");
                    }
                }
                if(u == 4 && v == 8) System.out.println("Check if edge 4-8 exists:3 "+this.checkIfTreeEdgeExists(4, 8));
            }
        } else if(tkeyU != null){
            if(u == 4 && v == 8) tkeyToTree.get(tkeyU).show();
            if(u == 4 && v == 8) System.out.println("Add edge to tkeyU");
            tkeyToTree.get(tkeyU).addTreeEdge(u, v);
            if(u == 4 && v == 8) tkeyToTree.get(tkeyU).show();
            vertexIdToTkey.put(v, tkeyU);
            for(Tree t2: this.tkeyToTree.values()){
                for(Integer vertex: t2.getVertices()){
                    System.out.println("Vertex: "+vertex+" vertexIdToTkey: "+vertexIdToTkey.get(vertex));
                }
            }
        } else if(tkeyV != null){
            tkeyToTree.get(tkeyV).addTreeEdge(v, u);
            vertexIdToTkey.put(u, tkeyV);
        } else {
            this.createNewTree(u, v);
        }
        if(u == 4 && v == 8) System.out.println("Check if edge 4-8 exists4: "+this.checkIfTreeEdgeExists(4, 8));
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

    public int getAmtOfTrees(){ return this.trees.size(); }

    public void deleteTreeEdge(int u, int v) {
        System.out.println("Function: deleteTreeEdge: from: "+u+" to "+v);

        if(!this.checkIfTreeEdgeExists(u, v)) return;
        Integer tkeyU = vertexIdToTkey.get(u);
        Integer tkeyV = vertexIdToTkey.get(v);
        System.out.println(tkeyU+", "+tkeyV);
        if(tkeyU != null && tkeyV != null){
            if(Objects.equals(tkeyU, tkeyV)){
                System.out.println("Show first tree:");
                trees.get(0).show();
                Node newTree = tkeyToTree.get(tkeyU).deleteEdge(u, v);
                System.out.println("Show first modified tree:");
                trees.get(0).show();
                System.out.println("Show new tree: ");


                Integer t = this.createNewTree(newTree);
                trees.get(1).show();
                for(Integer treeVertex: this.tkeyToTree.get(t).getVertices()){
                    System.out.println(treeVertex);
                    this.vertexIdToTkey.put(treeVertex, t); //TODO: to optimize
                }

                for(Tree t2: this.tkeyToTree.values()){
                    for(Integer vertex: t2.getVertices()){
                        System.out.println("Vertex: "+vertex+" vertexIdToTkey: "+vertexIdToTkey.get(vertex));
                    }
                }

            } else {
                if(Objects.equals(tkeyToTree.get(tkeyU).getRoot(), tkeyToTree.get(tkeyV).getRoot())){
                    assert(this.checkIfTreeEdgeExists(4,8));
                    Node newTree = tkeyToTree.get(tkeyU).deleteEdge(u, v);
                    assert(this.checkIfTreeEdgeExists(4,8));

                    Integer t = this.createNewTree(newTree);
                    for(Integer treeVertex: this.tkeyToTree.get(t).getVertices()){
                        this.vertexIdToTkey.put(treeVertex, t); //TODO: to optimize
                    }

                } else {
                    throw new Error("Cannot be situation that edge connects two different trees!");
                }
            }
        }
        System.out.println("amount of trees: "+this.trees.size());
        assert(this.checkIfTreeEdgeExists(4,8));
    }

    public void findReplacementEdge(int v, int w, int level) throws Exception {
        System.out.println("Find Replacement Edge function where lvl: "+level);
        Tree Tv = this.tkeyToTree.get(vertexIdToTkey.get(v));
        Tree Tw = this.tkeyToTree.get(vertexIdToTkey.get(w));
        System.out.println("Tv: "+Tv+", Tw: "+Tw);
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
        System.out.println("Tmin: "+Tmin+", V: "+V);

        if(Tmin != null){
            System.out.println("Tmin size: "+Tmin.getSize());
            for(Pair<Integer, Integer> edge: Tmin.getEdges()){
                System.out.println("add tree edge: "+edge);
                this.dynamicConnectivity.getForestForLevel(level+1).addTreeEdge(edge.getFirst(), edge.getSecond());
            }
            boolean nonTreeEdgeFound = false;
            Pair<Integer, Integer> nonTreeEdge = null;

            for(Integer vertex: Tmin.getVertices()){
                System.out.println("Consider vertex: "+vertex);
                for(Integer nonTreeEdgeEnd: this.dynamicConnectivity.getForestForLevel(level).getNonTreeEdges(vertex)){
                    System.out.println("the nonTreeEdge ending: "+nonTreeEdgeEnd);
                    if(this.getTree(nonTreeEdgeEnd) != Tmin){
                        System.out.println("Found nonTreeEdge that connects two trees.");
                        nonTreeEdgeFound = true;
                        nonTreeEdge = new Pair<>(vertex, nonTreeEdgeEnd);
                        for(int lvl= 0; lvl < dynamicConnectivity.getAmtOfLevels(); lvl++){
                            assert(this.checkIfTreeEdgeExists(4,8));
                            dynamicConnectivity.getForestForLevel(lvl).deleteNonTreeEdge(vertex, nonTreeEdgeEnd);
                            assert(this.checkIfTreeEdgeExists(4,8));
                        }
                        break;
                    } else {
                        dynamicConnectivity.getForestForLevel(level+1).addNonTreeEdge(vertex, nonTreeEdgeEnd);
                    }
                }
                if(nonTreeEdgeFound) break;
            }
            assert(this.checkIfTreeEdgeExists(4,8));
            if(nonTreeEdgeFound){
                System.out.println("Non tree edge has been found! on lvl: "+level);
                assert(this.checkIfTreeEdgeExists(4,8));
                for(int lvl= 0; lvl <= level; lvl++){
                    System.out.println("!!!!!!!!!!!!!!!!: "+nonTreeEdge);
                    dynamicConnectivity.getForestForLevel(lvl).addTreeEdge(nonTreeEdge.getFirst(), nonTreeEdge.getSecond());
                }
                assert(this.checkIfTreeEdgeExists(4,8));
            } else {
                this.findReplacementEdge(v, w, level-1);
            }
            assert(this.checkIfTreeEdgeExists(4,8));
        }
        assert(this.checkIfTreeEdgeExists(4,8));
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
        return this.vertexIdToTkey.get(v).equals(this.vertexIdToTkey.get(w));
    }
}

package pl.edu.agh.cs.eulerTourTree;

import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.splay.Node;
import pl.edu.agh.cs.eulerTourTree.splay.SplayTree;

import java.util.*;

public class EulerTourTree {

    private Node splayRoot;
    private static Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes = new HashMap<>();

    public EulerTourTree() {}
    public EulerTourTree(Node splayRoot) { this.splayRoot = splayRoot; }

    public Node getSplayRoot(){
        return splayRoot;
    }

    public void setSplayRoot(Node treeNode){
        this.splayRoot = SplayTree.getRootNode(treeNode);
    }

    public static void resetKeyToNodes(){
        EulerTourTree.keyToNodes.clear();
    }

    void setKeyToNodes(Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes){
        EulerTourTree.keyToNodes = keyToNodes;
    }

    public Node addNode(Pair<Integer, Integer> key){
        Node n = new Node(key);
        if(!EulerTourTree.keyToNodes.containsKey(key)) {
            EulerTourTree.keyToNodes.put(key, new LinkedHashSet<>());
        }
        EulerTourTree.keyToNodes.get(key).add(n);
        return n;
    }

    public static void dfs(Node n){
        System.out.println("dfs: "+n.key);
        if(n.left != null){
            System.out.println("Go left: ");
            dfs(n.left);
        }
        if(n.right != null) {
            System.out.println("Go right: ");
            dfs(n.right);
        }
        System.out.println("back");
    }

    public Node reRoot(Integer vertex){
        Node newRoot = EulerTourTree.keyToNodes.get(new Pair<>(vertex, vertex)).getFirst();

        if(Objects.equals(newRoot.key.getFirst(), newRoot.key.getSecond()) &&
                Objects.equals(newRoot.key.getFirst(), vertex) &&
                SplayTree.firstNode(this.getSplayRoot()).equals(newRoot)){
            return newRoot;
        }

        SplayTree.splay(newRoot);
        Node firstSubTree = SplayTree.firstNode(newRoot);
        SplayTree.removeNode(firstSubTree);

        Node leftSubTree = SplayTree.detachNodeFromTree(newRoot.left);
        SplayTree.join(newRoot, leftSubTree);
        SplayTree.join(newRoot, this.addNode(newRoot.key));
        this.setSplayRoot(newRoot);
        return SplayTree.getRootNode(newRoot);
    }

    public static Node getFirstNode(Integer v){
        return EulerTourTree.keyToNodes.get(new Pair<>(v, v)).getFirst();
    }

    public void link(Integer internal, Integer external, EulerTourTree externalETT) {
        Node rootInternal = getFirstNode(internal);
        Node rootExternal = getFirstNode(external);

        this.reRoot(internal);
        externalETT.reRoot(external);

        SplayTree.join(rootInternal, this.addNode(new Pair<>(internal, external)));
        SplayTree.join(rootInternal, rootExternal);
        SplayTree.join(rootInternal, this.addNode(new Pair<>(external, internal)));
        SplayTree.join(rootInternal, this.addNode(new Pair<>(internal, internal)));
        this.setSplayRoot(this.getSplayRoot());

    }

    public Pair<EulerTourTree, EulerTourTree> cut(Integer u, Integer v) {
        Node edgeUV = EulerTourTree.keyToNodes.get(new Pair<>(u, v)).getFirst();
        Node edgeVU = EulerTourTree.keyToNodes.get(new Pair<>(v, u)).getFirst();

        Pair<Node, Node> roots = SplayTree.split(edgeUV);

        Node J;
        Node K;
        Node L;
        if(SplayTree.getRootNode(roots.getFirst()) == SplayTree.getRootNode(edgeVU)) {
            Pair<Node, Node> trees2 = SplayTree.split(edgeVU);
            dfs(trees2.getSecond());
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
        EulerTourTree.keyToNodes.remove(new Pair<>(u, v));
        EulerTourTree.keyToNodes.remove(new Pair<>(v, u));

        SplayTree.removeNode(SplayTree.lastNode(J));
        Node joined = SplayTree.join(J, L);
        this.splayRoot = SplayTree.getRootNode(K);

        return new Pair<>(new EulerTourTree(K), new EulerTourTree(joined));
    }

    public void addEdge(int u, int v){
        if(this.splayRoot == null){
            this.splayRoot = new Node(new Pair<>(u,u));
            EulerTourTree.keyToNodes.put(new Pair<>(u,u), new LinkedHashSet<>());
            EulerTourTree.keyToNodes.put(new Pair<>(v,u), new LinkedHashSet<>());
            EulerTourTree.keyToNodes.put(new Pair<>(u,v), new LinkedHashSet<>());
            EulerTourTree.keyToNodes.put(new Pair<>(v,v), new LinkedHashSet<>());

            EulerTourTree.keyToNodes.get(new Pair<>(u,u)).add(this.splayRoot);
            EulerTourTree.keyToNodes.get(new Pair<>(u,v)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(u,v)));
            EulerTourTree.keyToNodes.get(new Pair<>(v,v)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(v,v)));
            EulerTourTree.keyToNodes.get(new Pair<>(v,u)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(v,u)));
            EulerTourTree.keyToNodes.get(new Pair<>(u,u)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(u,u)));
            return;
        }
        if(EulerTourTree.keyToNodes.containsKey(new Pair<>(u, u)) &&
                !EulerTourTree.keyToNodes.containsKey(new Pair<>(v,v))){
            EulerTourTree.keyToNodes.put(new Pair<>(v,u), new LinkedHashSet<>());
            EulerTourTree.keyToNodes.put(new Pair<>(u,v), new LinkedHashSet<>());
            EulerTourTree.keyToNodes.put(new Pair<>(v,v), new LinkedHashSet<>());

            this.reRoot(u);
            EulerTourTree.keyToNodes.get(new Pair<>(u,v)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(u,v)));
            EulerTourTree.keyToNodes.get(new Pair<>(v,v)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(v,v)));
            EulerTourTree.keyToNodes.get(new Pair<>(v,u)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(v,u)));
            EulerTourTree.keyToNodes.get(new Pair<>(u,u)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(u,u)));
        } else if(EulerTourTree.keyToNodes.containsKey(new Pair<>(v,v)) &&
                !EulerTourTree.keyToNodes.containsKey(new Pair<>(u,u))){
            EulerTourTree.keyToNodes.put(new Pair<>(v,u), new LinkedHashSet<>());
            EulerTourTree.keyToNodes.put(new Pair<>(u,v), new LinkedHashSet<>());
            EulerTourTree.keyToNodes.put(new Pair<>(u,u), new LinkedHashSet<>());

            this.reRoot(v);
            EulerTourTree.keyToNodes.get(new Pair<>(u,v)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(v,u)));
            EulerTourTree.keyToNodes.get(new Pair<>(v,v)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(u,u)));
            EulerTourTree.keyToNodes.get(new Pair<>(v,u)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(u,v)));
            EulerTourTree.keyToNodes.get(new Pair<>(u,u)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(v,v)));
        } else {
            throw new Error("Illegal operation. At most one vertex must be present in a tree.");
        }
    }

    public Node deleteEdge(int u, int v){
        if(this.splayRoot == null){return null;}
        if(EulerTourTree.keyToNodes.containsKey(new Pair<>(u,v)) &&
                EulerTourTree.keyToNodes.containsKey(new Pair<>(v,u))){
            Pair<EulerTourTree, EulerTourTree> trees = this.cut(u, v);
            if(EulerTourTree.keyToNodes.get(new Pair<>(u,u)).size() <= 1){
                EulerTourTree.keyToNodes.remove(new Pair<>(u,u));
                return null;
            }
            else if(EulerTourTree.keyToNodes.get(new Pair<>(v,v)).size() <= 1){
                EulerTourTree.keyToNodes.remove(new Pair<>(v,v));
                return null;
            } else {
                return trees.getSecond().getSplayRoot();
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
        for(Pair<Integer, Integer> edge: EulerTourTree.keyToNodes.keySet()){
            if(edge.getFirst() != edge.getSecond()){
                listOfEdges.add(edge);
            }
        }
        return listOfEdges;
    }

    public Set<Integer> getVertices(){
        Set<Integer> listOfVertices = new HashSet<>();
        for(Pair<Integer, Integer> edge: EulerTourTree.keyToNodes.keySet()){
            listOfVertices.add(edge.getFirst());
        }
        return listOfVertices;
    }

    public int getSizeOfTree(){
        return SplayTree.getSizeOfTree(splayRoot);
    }

}

package pl.edu.agh.cs.eulerTourTree;

import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.splay.Node;
import pl.edu.agh.cs.eulerTourTree.splay.SplayTree;

import java.util.*;

public class EulerTourTree {

    private Node splayRoot;
    private Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes;

    public EulerTourTree(Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes) { this.keyToNodes = keyToNodes; }
    public EulerTourTree(Node splayRoot, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes) { this.splayRoot = splayRoot; this.keyToNodes = keyToNodes; }

    public Node getSplayRoot(){
        return splayRoot;
    }

    public void setSplayRoot(Node treeNode){
        this.splayRoot = SplayTree.getRootNode(treeNode);
    }

    public void resetKeyToNodes(){
        this.keyToNodes.clear();
    }

    void setKeyToNodes(Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes){
        this.keyToNodes = keyToNodes;
    }

    public Node addNode(Pair<Integer, Integer> key){
        Node n = new Node(key);
        if(!this.keyToNodes.containsKey(key)) {
            this.keyToNodes.put(key, new LinkedHashSet<>());
        }
        this.keyToNodes.get(key).add(n);
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
        Node newRoot = this.keyToNodes.get(new Pair<>(vertex, vertex)).getFirst();

        if(Objects.equals(newRoot.key.getFirst(), newRoot.key.getSecond()) &&
                Objects.equals(newRoot.key.getFirst(), vertex) &&
                SplayTree.firstNode(this.getSplayRoot()).equals(newRoot)){
                return newRoot;
        }

        SplayTree.splay(newRoot);
        Node firstNode = SplayTree.firstNode(newRoot);
        SplayTree.removeNode(firstNode);
        this.keyToNodes.get(firstNode.key).remove(firstNode);

        Node leftSubTree = SplayTree.detachNodeFromTree(newRoot.left);
        SplayTree.join(newRoot, leftSubTree);
        SplayTree.join(newRoot, this.addNode(newRoot.key));

        this.setSplayRoot(newRoot);
        return SplayTree.getRootNode(newRoot);
    }

    public Node getFirstNode(Integer v){
        return this.keyToNodes.get(new Pair<>(v, v)).getFirst();
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

    public EulerTourTree cut(Integer u, Integer v) {
        Node edgeUV = this.keyToNodes.get(new Pair<>(u, v)).getFirst();
        Node edgeVU = this.keyToNodes.get(new Pair<>(v, u)).getFirst();
        this.show();

        Pair<Node, Node> roots = SplayTree.split(edgeUV);
        Node J;
        Node K;
        Node L;

        if(SplayTree.getRootNode(roots.getFirst()).equals(SplayTree.getRootNode(edgeUV))) {
            roots.setFirst(SplayTree.removeNode(edgeUV));
        } else {
            roots.setSecond(SplayTree.removeNode(edgeUV));
        }
        this.keyToNodes.get(new Pair<>(u, v)).remove(edgeUV);

        if(SplayTree.getRootNode(roots.getFirst()).equals(SplayTree.getRootNode(edgeVU))) {
            Pair<Node, Node> trees2 = SplayTree.split(edgeVU);
            J = trees2.getFirst();
            K = trees2.getSecond();
            L = roots.getSecond();
            if(SplayTree.getRootNode(J).equals(SplayTree.getRootNode(edgeVU))) {
                J = SplayTree.removeNode(edgeVU);
            } else {
                K = SplayTree.removeNode(edgeVU);
            }
        } else {
            Pair<Node, Node> trees2 = SplayTree.split(edgeVU);
            J = roots.getFirst();
            K = trees2.getFirst();
            L = trees2.getSecond();
            if(SplayTree.getRootNode(K).equals(SplayTree.getRootNode(edgeVU))) {
                K = SplayTree.removeNode(edgeVU);
            } else {
                L = SplayTree.removeNode(edgeVU);
            }
        }
        this.keyToNodes.get(new Pair<>(v, u)).remove(edgeVU);

        J = SplayTree.removeNode(SplayTree.lastNode(J));
        Node joined = SplayTree.join(J, L);
        this.setSplayRoot(this.getSplayRoot());

        if(SplayTree.getRootNode(K).equals(SplayTree.getRootNode(this.splayRoot))){
            this.setSplayRoot(K);
            return new EulerTourTree(joined, this.keyToNodes);
        } else {
            this.setSplayRoot(joined);
            return new EulerTourTree(K, this.keyToNodes);
        }

    }

    public void addEdge(int u, int v){
        if(this.splayRoot == null){
            this.splayRoot = new Node(new Pair<>(u,u));
            this.keyToNodes.put(new Pair<>(u,u), new LinkedHashSet<>());
            this.keyToNodes.put(new Pair<>(v,v), new LinkedHashSet<>());

            this.keyToNodes.get(new Pair<>(u,u)).add(this.splayRoot);
            insertEdgeToETT(u, v);
            return;
        }
        if(this.keyToNodes.containsKey(new Pair<>(u, u)) &&
                !this.keyToNodes.containsKey(new Pair<>(v,v))){
            this.keyToNodes.put(new Pair<>(v,v), new LinkedHashSet<>());

            this.reRoot(u);
            insertEdgeToETT(u, v);
        } else if(this.keyToNodes.containsKey(new Pair<>(v,v)) &&
                !this.keyToNodes.containsKey(new Pair<>(u,u))){
            this.keyToNodes.put(new Pair<>(u,u), new LinkedHashSet<>());

            this.reRoot(v);
            insertEdgeToETT(u,v);
        } else {
            throw new Error("Illegal operation. At most one vertex must be present in a tree.");
        }
    }

    private void dfsCheckParent(Node node){
        System.out.println("node: "+node.key);
        if(node.left != null){
            System.out.println("Left: "+node.left.key+" Parent: "+node.left.parent.key);
            if(node.left.parent != node) System.out.println("Left child lacks parent: "+node.left.parent);
            this.dfsCheckParent(node.left);
        }
        if(node.right != null){
            System.out.println("Right: "+node.right.key+" Parent: "+node.right.parent.key);
            if(node.right.parent != node) System.out.println("Right child lacks parent: "+node.right.parent);
            this.dfsCheckParent(node.right);
        }
    }

    private void insertEdgeToETT(int u, int v) {
        this.keyToNodes.put(new Pair<>(v,u), new LinkedHashSet<>());
        this.keyToNodes.put(new Pair<>(u,v), new LinkedHashSet<>());

        this.keyToNodes.get(new Pair<>(u,v)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(u,v)));
        this.keyToNodes.get(new Pair<>(v,v)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(v,v)));
        this.keyToNodes.get(new Pair<>(v,u)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(v,u)));
        this.keyToNodes.get(new Pair<>(u,u)).add(SplayTree.insertToRight(this.splayRoot, new Pair<>(u,u)));
    }

    public Node deleteEdge(int u, int v){
        if(this.splayRoot == null) { return null; }
        if(this.keyToNodes.containsKey(new Pair<>(u,v)) &&
                this.keyToNodes.containsKey(new Pair<>(v,u))){
            EulerTourTree secondTree = this.cut(u, v);
            return secondTree.getSplayRoot();
        } else {
            throw new Error(String.format("Edge {%d, %d}doesn't exists!", u, v));
        }
    }

    public Integer getRoot(){
        return SplayTree.firstNode(this.splayRoot).key.getFirst();
    }

    public Set<Pair<Integer, Integer>> getEdges(){
        Set<Pair<Integer, Integer>> listOfEdges = new HashSet<>();
        this.dfsEdges(this.getSplayRoot(), listOfEdges);
        return listOfEdges;
    }

    public Set<Integer> getVertices(){
        Set<Integer> listOfVertices = new HashSet<>();
        this.dfsVertices(this.getSplayRoot(), listOfVertices);
        return listOfVertices;
    }

    private void dfsVertices(Node root, Set<Integer> listOfVertices){
        listOfVertices.add(root.key.getFirst());
        if(root.left != null){
            dfsVertices(root.left, listOfVertices);
        }
        if(root.right != null){
            dfsVertices(root.right, listOfVertices);
        }

    }

    private void dfsEdges(Node root, Set<Pair<Integer, Integer>> listOfEdges) {
        if(!root.key.getFirst().equals(root.key.getSecond()) && !listOfEdges.contains(new Pair<>(root.key.getSecond(),root.key.getFirst()))){
            listOfEdges.add(root.key);
        }
        if(root.left != null){
            dfsEdges(root.left, listOfEdges);
        }
        if(root.right != null){
            dfsEdges(root.right, listOfEdges);
        }
    }

    public int getSizeOfTree(){
        return SplayTree.getSizeOfTree(splayRoot);
    }

    public void show(){
        System.out.println("Show ETT:");
        this.dfsShow(this.getSplayRoot());
    }

    private void dfsShow(Node node){
        System.out.println("node: "+node.key);
        if(node.left != null){
            System.out.println("Left");
            dfsShow(node.left);
        }
        if(node.right != null){
            System.out.println("Right");
            dfsShow(node.right);
        }
        System.out.println("back");
    }

}

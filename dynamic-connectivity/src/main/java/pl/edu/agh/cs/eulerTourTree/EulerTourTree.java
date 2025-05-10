package pl.edu.agh.cs.eulerTourTree;

import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.splay.Node;
import pl.edu.agh.cs.eulerTourTree.splay.SplayTree;

import java.util.*;

public class EulerTourTree {

    public static Node createNewEulerTourTree(int u, int v, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes) throws Exception {
        if(!keyToNodes.get(new Pair<>(u,u)).isEmpty() || !keyToNodes.get(new Pair<>(v,v)).isEmpty())
            throw new Exception("At least one vertex is present in the tree.");
        Node splayRoot = new Node(new Pair<>(u,u));
        keyToNodes.put(new Pair<>(u,u), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(v,v), new LinkedHashSet<>());

        keyToNodes.get(new Pair<>(u,u)).add(splayRoot);
        insertEdgeToETT(splayRoot, u, v, keyToNodes);
        return splayRoot;
    }

    public static Node addNode(Pair<Integer, Integer> key, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes){
        Node n = new Node(key);
        if(!keyToNodes.containsKey(key)) {
            keyToNodes.put(key, new LinkedHashSet<>());
        }
        keyToNodes.get(key).add(n);
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

    public static Node reRoot(Node treeNode, Integer vertex, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes){
        Node newRoot = keyToNodes.get(new Pair<>(vertex, vertex)).getFirst();

        if(Objects.equals(newRoot.key.getFirst(), newRoot.key.getSecond()) &&
                Objects.equals(newRoot.key.getFirst(), vertex) &&
                SplayTree.firstNode(SplayTree.getRootNode(treeNode)).equals(newRoot)){
                return newRoot;
        }

        SplayTree.splay(newRoot);
        Node firstNode = SplayTree.firstNode(newRoot);
        SplayTree.removeNode(firstNode);
        keyToNodes.get(firstNode.key).remove(firstNode);

        Node leftSubTree = SplayTree.detachNodeFromTree(newRoot.left);
        SplayTree.join(newRoot, leftSubTree);
        SplayTree.join(newRoot, addNode(newRoot.key, keyToNodes));

        return SplayTree.getRootNode(newRoot);
    }

    public static Node getFirstNode(Integer v, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes){
        return keyToNodes.get(new Pair<>(v, v)).getFirst();
    }

    public static void link(Integer internal, Integer external, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes) {
        Node rootInternal = getFirstNode(internal, keyToNodes);
        Node rootExternal = getFirstNode(external, keyToNodes);

        rootInternal = reRoot(rootInternal, internal, keyToNodes);
        rootExternal = reRoot(rootExternal, external, keyToNodes);

        SplayTree.join(rootInternal, addNode(new Pair<>(internal, external), keyToNodes));
        SplayTree.join(rootInternal, rootExternal);
        SplayTree.join(rootInternal, addNode(new Pair<>(external, internal), keyToNodes));

        SplayTree.join(rootInternal, addNode(new Pair<>(internal, internal), keyToNodes));
    }

    public static Pair<Node,Node> cut(Integer u, Integer v, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes) {
        Node edgeUV = keyToNodes.get(new Pair<>(u, v)).getFirst();
        Node edgeVU = keyToNodes.get(new Pair<>(v, u)).getFirst();

        Pair<Node, Node> roots = SplayTree.split(edgeUV);
        Node J;
        Node K;
        Node L;

        roots.setFirst(SplayTree.removeNode(edgeUV));
        keyToNodes.get(new Pair<>(u, v)).remove(edgeUV);

        if(SplayTree.getRootNode(roots.getFirst()).equals(SplayTree.getRootNode(edgeVU))) {
            Pair<Node, Node> trees2 = SplayTree.split(edgeVU);
            J = SplayTree.removeNode(edgeVU);
            K = trees2.getSecond();
            L = roots.getSecond();
        } else {
            Pair<Node, Node> trees2 = SplayTree.split(edgeVU);
            J = roots.getFirst();
            K = SplayTree.removeNode(edgeVU);
            L = trees2.getSecond();
        }
        keyToNodes.get(new Pair<>(v, u)).remove(edgeVU);

        J = SplayTree.removeNode(SplayTree.lastNode(J));
        Node joined = SplayTree.join(J, L);

        return new Pair<>(K, joined);
    }

    public static void addEdge(Node treeNode, int u, int v, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes){
        Node splayRoot = SplayTree.getRootNode(treeNode);
        if(keyToNodes.containsKey(new Pair<>(u, u)) &&
                !keyToNodes.containsKey(new Pair<>(v,v))){
            keyToNodes.put(new Pair<>(v,v), new LinkedHashSet<>());

            splayRoot = reRoot(splayRoot, u, keyToNodes);
            insertEdgeToETT(splayRoot, u, v, keyToNodes);
        } else if(keyToNodes.containsKey(new Pair<>(v,v)) &&
                !keyToNodes.containsKey(new Pair<>(u,u))){
            keyToNodes.put(new Pair<>(u,u), new LinkedHashSet<>());

            splayRoot = reRoot(splayRoot, v, keyToNodes);
            insertEdgeToETT(splayRoot, u,v, keyToNodes);
        } else {
            throw new Error("Illegal operation. At most one vertex must be present in a tree.");
        }
    }

    private static void dfsCheckParent(Node node){
        System.out.println("node: "+node.key);
        if(node.left != null){
            System.out.println("Left: "+node.left.key+" Parent: "+node.left.parent.key);
            if(node.left.parent != node) System.out.println("Left child lacks parent: "+node.left.parent);
            dfsCheckParent(node.left);
        }
        if(node.right != null){
            System.out.println("Right: "+node.right.key+" Parent: "+node.right.parent.key);
            if(node.right.parent != node) System.out.println("Right child lacks parent: "+node.right.parent);
            dfsCheckParent(node.right);
        }
    }

    private static void insertEdgeToETT(Node treeNode, int u, int v, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes) {
        Node splayRoot = SplayTree.getRootNode(treeNode);
        keyToNodes.put(new Pair<>(v,u), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(u,v), new LinkedHashSet<>());

        keyToNodes.get(new Pair<>(u,v)).add(SplayTree.insertToRight(splayRoot, new Pair<>(u,v)));
        keyToNodes.get(new Pair<>(v,v)).add(SplayTree.insertToRight(splayRoot, new Pair<>(v,v)));
        keyToNodes.get(new Pair<>(v,u)).add(SplayTree.insertToRight(splayRoot, new Pair<>(v,u)));
        keyToNodes.get(new Pair<>(u,u)).add(SplayTree.insertToRight(splayRoot, new Pair<>(u,u)));
    }

    public static Pair<Node, Node> deleteEdge(Node treeNode, int u, int v, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes){
        Node splayRoot = SplayTree.getRootNode(treeNode);
        if(splayRoot == null) { return null; }
        if(keyToNodes.containsKey(new Pair<>(u,v)) &&
                keyToNodes.containsKey(new Pair<>(v,u))){
            Pair<Node, Node> trees = cut(u, v, keyToNodes);
            return trees;
        } else {
            throw new Error(String.format("Edge {%d, %d}doesn't exists!", u, v));
        }
    }

    public static Integer getEulerTourRoot(Node treeNode){
        return SplayTree.firstNode(treeNode).key.getFirst();
    }

    public static Set<Pair<Integer, Integer>> getEdges(Node treeRoot){
        Set<Pair<Integer, Integer>> listOfEdges = new HashSet<>();
        dfsEdges(SplayTree.getRootNode(treeRoot), listOfEdges);
        return listOfEdges;
    }

    public static Set<Integer> getVertices(Node treeNode){
        Set<Integer> listOfVertices = new HashSet<>();
        dfsVertices(SplayTree.getRootNode(treeNode), listOfVertices);
        return listOfVertices;
    }

    private static void dfsVertices(Node root, Set<Integer> listOfVertices){
        listOfVertices.add(root.key.getFirst());
        if(root.left != null){
            dfsVertices(root.left, listOfVertices);
        }
        if(root.right != null){
            dfsVertices(root.right, listOfVertices);
        }

    }

    private static void dfsEdges(Node root, Set<Pair<Integer, Integer>> listOfEdges) {
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

    public static int getSizeOfTree(Node treeNode){
        return SplayTree.getSizeOfTree(treeNode);
    }

    public static void show(Node treeNode){
        System.out.println("Show ETT:");
        dfsShow(SplayTree.getRootNode(treeNode));
    }

    private static void dfsShow(Node node){
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

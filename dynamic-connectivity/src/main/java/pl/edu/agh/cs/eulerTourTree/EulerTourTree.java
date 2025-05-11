package pl.edu.agh.cs.eulerTourTree;

import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.splay.Node;
import pl.edu.agh.cs.eulerTourTree.splay.SplayTree;

import java.util.*;

public class EulerTourTree {

    public static Node createNewEulerTourTree(int u, int v, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes) {
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

    public static Optional<Node> reRoot(Node treeNode, Integer vertex, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes){
        Node newRoot = keyToNodes.get(new Pair<>(vertex, vertex)).getFirst();
        Optional<Node> optTreeNode = SplayTree.getRootNode(treeNode);
        if(optTreeNode.isPresent() && Objects.equals(newRoot.key.getFirst(), newRoot.key.getSecond()) &&
                Objects.equals(newRoot.key.getFirst(), vertex) &&
                Objects.equals(SplayTree.firstNode(optTreeNode.get()), newRoot)){
                return Optional.of(newRoot);
        }

        SplayTree.splay(newRoot);
        Node firstNode = SplayTree.firstNode(newRoot);
        SplayTree.removeNode(firstNode);
        keyToNodes.get(firstNode.key).remove(firstNode);

        Optional<Node> leftSubTree = SplayTree.detachSubTreeFromTree(newRoot.left);
        leftSubTree.ifPresent(node -> SplayTree.join(newRoot, node));
        SplayTree.join(newRoot, addNode(newRoot.key, keyToNodes));

        return SplayTree.getRootNode(newRoot);
    }

    public static Node getFirstNode(Integer v, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes){
        return keyToNodes.get(new Pair<>(v, v)).getFirst();
    }

    public static void link(Integer internal, Integer external, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes) {
        Node rootInternal = getFirstNode(internal, keyToNodes);
        Node rootExternal = getFirstNode(external, keyToNodes);

        Optional<Node> optRootInternal = reRoot(rootInternal, internal, keyToNodes);
        Optional<Node> optRootExternal = reRoot(rootExternal, external, keyToNodes);

        if(optRootExternal.isPresent() && optRootInternal.isPresent()) {
            rootExternal = optRootExternal.get();
            rootInternal = optRootInternal.get();

            SplayTree.join(rootInternal, addNode(new Pair<>(internal, external), keyToNodes));
            SplayTree.join(rootInternal, rootExternal);
            SplayTree.join(rootInternal, addNode(new Pair<>(external, internal), keyToNodes));
            SplayTree.join(rootInternal, addNode(new Pair<>(internal, internal), keyToNodes));
        }
    }

    public static Pair<Optional<Node>, Optional<Node>> cut(Integer u, Integer v, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes) {
        Node edgeUV = keyToNodes.get(new Pair<>(u, v)).getFirst();
        Node edgeVU = keyToNodes.get(new Pair<>(v, u)).getFirst();

        Pair<Optional<Node>, Optional<Node>> roots = SplayTree.split(edgeUV);
        Optional<Node> J = null;
        Optional<Node> K = null;
        Node L = null;

        roots.setFirst(Optional.of(SplayTree.removeNode(edgeUV)));
        keyToNodes.get(new Pair<>(u, v)).remove(edgeUV);

        Pair<Optional<Node>, Optional<Node>> trees2 = SplayTree.split(edgeVU);
        if(SplayTree.getRootNode(roots.getFirst().get()).equals(SplayTree.getRootNode(edgeVU))) {
            J = SplayTree.removeNode(edgeVU);
            if(trees2.getSecond().isPresent())
                K = trees2.getSecond();
            if(roots.getSecond().isPresent())
                L = roots.getSecond().get();
        } else {
            if(roots.getFirst().isPresent())
                J = roots.getFirst();
            K = SplayTree.removeNode(edgeVU);
            if(trees2.getSecond().isPresent())
                L = trees2.getSecond().get();
        }
        keyToNodes.get(new Pair<>(v, u)).remove(edgeVU);

        J = SplayTree.removeNode(SplayTree.lastNode(J.get()));
        Optional<Node> joined;
        if(J.isPresent())
            joined = SplayTree.join(J.get(), L);
        else
            joined = SplayTree.join(null, J);


        return new Pair<>(K, joined);
    }

    public static Optional<Node> addEdgeToNonExistingVertex(Node treeNodeRepresentative, Integer treeVertex, Integer nonExistingVertex, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes){
        Optional<Node> optSplayRoot = SplayTree.getRootNode(treeNodeRepresentative);
        if(!keyToNodes.containsKey(new Pair<>(nonExistingVertex, nonExistingVertex)))
            keyToNodes.put(new Pair<>(nonExistingVertex, nonExistingVertex), new LinkedHashSet<>());

        if(optSplayRoot.isPresent()) {
            optSplayRoot = reRoot(optSplayRoot.get(), treeVertex, keyToNodes);
            optSplayRoot.ifPresent(node -> insertEdgeToETT(node, treeVertex, nonExistingVertex, keyToNodes));
        }
        return optSplayRoot;
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

    private static void insertEdgeToETT(Node treeNode, Integer splayVertex, Integer nonExistingVertex, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes) {
        Optional<Node> optSplayRoot = SplayTree.getRootNode(treeNode);
        if(optSplayRoot.isEmpty()) return;

        keyToNodes.put(new Pair<>(nonExistingVertex, splayVertex), new LinkedHashSet<>());
        keyToNodes.put(new Pair<>(splayVertex, nonExistingVertex), new LinkedHashSet<>());

        keyToNodes.get(new Pair<>(splayVertex, nonExistingVertex))
                .add(SplayTree.insertToRight(optSplayRoot.get(), new Pair<>(splayVertex, nonExistingVertex)));
        keyToNodes.get(new Pair<>(nonExistingVertex, nonExistingVertex))
                .add(SplayTree.insertToRight(optSplayRoot.get(), new Pair<>(nonExistingVertex, nonExistingVertex)));
        keyToNodes.get(new Pair<>(nonExistingVertex, splayVertex))
                .add(SplayTree.insertToRight(optSplayRoot.get(), new Pair<>(nonExistingVertex, splayVertex)));
        keyToNodes.get(new Pair<>(splayVertex, splayVertex))
                .add(SplayTree.insertToRight(optSplayRoot.get(), new Pair<>(splayVertex, splayVertex)));
    }

    public static void deleteEdge(Node treeNode, Integer u, Integer v, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes){
        if(keyToNodes.containsKey(new Pair<>(u,v)) &&
                keyToNodes.containsKey(new Pair<>(v,u))){
            cut(u, v, keyToNodes);
        }
    }

    public static Integer getEulerTourRoot(Node treeNode){
        return SplayTree.firstNode(treeNode).key.getFirst();
    }

    public static Set<Pair<Integer, Integer>> getEdges(Node treeNode){
        Set<Pair<Integer, Integer>> listOfEdges = new HashSet<>();
        Optional<Node> optSplayRoot = SplayTree.getRootNode(treeNode);
        optSplayRoot.ifPresent(node -> dfsEdges(node, listOfEdges));
        return listOfEdges;
    }

    public static Set<Integer> getVertices(Node treeNode){
        Set<Integer> listOfVertices = new HashSet<>();
        Optional<Node> optSplayRoot = SplayTree.getRootNode(treeNode);
        optSplayRoot.ifPresent(node -> dfsVertices(node, listOfVertices));
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
        Optional<Node> optSplayRoot = SplayTree.getRootNode(treeNode);
        optSplayRoot.ifPresent(EulerTourTree::dfsShow);
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

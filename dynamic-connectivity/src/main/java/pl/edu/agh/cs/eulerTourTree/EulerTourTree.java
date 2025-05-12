package pl.edu.agh.cs.eulerTourTree;

import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.splay.Node;
import pl.edu.agh.cs.eulerTourTree.splay.SplayTree;
import pl.edu.agh.cs.forest.Forest;

import java.util.*;

public class EulerTourTree {

    public static Node createNewEulerTourTree(Integer u, Integer v, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes) {

        /* preconditions */
        if(Forest.checkIfVertexHasNodeInTheTree(u, keyToNodes) || Forest.checkIfVertexHasNodeInTheTree(v, keyToNodes)){
           throw new RuntimeException("There is node already in the tree!");
        }

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
                Objects.equals(SplayTree.firstNode(optTreeNode.get()).get().key, newRoot.key)){
                return Optional.of(newRoot);
        }

        SplayTree.splay(newRoot);
        Node firstNode = SplayTree.firstNode(newRoot).get();
        SplayTree.removeNode(firstNode);
        keyToNodes.get(firstNode.key).remove(firstNode);

        Optional<Node> leftSubTree = SplayTree.detachSubTreeFromTree(newRoot.left);
        leftSubTree.ifPresent(node -> SplayTree.join(newRoot, node));
        SplayTree.join(newRoot, addNode(newRoot.key, keyToNodes));

        return SplayTree.getRootNode(newRoot);
    }

    public static Optional<Node> getFirstNodeFromKeyToNodesList(Integer v, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes){
        if(Forest.checkIfVertexHasNodeInTheTree(v, keyToNodes))
            return Optional.of(keyToNodes.get(new Pair<>(v, v)).getFirst());
        else return Optional.empty();
    }

    public static void link(Integer internal, Integer external, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes) {
        Optional<Node> rootInternal = getFirstNodeFromKeyToNodesList(internal, keyToNodes);
        Optional<Node> rootExternal = getFirstNodeFromKeyToNodesList(external, keyToNodes);

        rootInternal = reRoot(rootInternal.get(), internal, keyToNodes);
        rootExternal = reRoot(rootExternal.get(), external, keyToNodes);

        if(rootExternal.isPresent() && rootInternal.isPresent()) {
            SplayTree.join(rootInternal.get(), addNode(new Pair<>(internal, external), keyToNodes));
            SplayTree.join(rootInternal.get(), rootExternal.get());
            SplayTree.join(rootInternal.get(), addNode(new Pair<>(external, internal), keyToNodes));
            SplayTree.join(rootInternal.get(), addNode(new Pair<>(internal, internal), keyToNodes));
        }
    }

    public static Pair<Optional<Node>, Optional<Node>> cut(Integer u, Integer v, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes) {
        Node edgeUV = keyToNodes.get(new Pair<>(u, v)).getFirst();
        Node edgeVU = keyToNodes.get(new Pair<>(v, u)).getFirst();

        Pair<Optional<Node>, Optional<Node>> roots = SplayTree.split(edgeUV);
        Optional<Node> J = Optional.empty();
        Optional<Node> K = Optional.empty();
        Optional<Node> L = Optional.empty();

        roots.setFirst(SplayTree.removeNode(edgeUV));
        keyToNodes.get(new Pair<>(u, v)).remove(edgeUV);

        if(SplayTree.getRootNode(roots.getFirst().get()).equals(SplayTree.getRootNode(edgeVU))) {
            Pair<Optional<Node>, Optional<Node>> trees2 = SplayTree.split(edgeVU);
            J = SplayTree.removeNode(edgeVU);
            K = trees2.getSecond();
            L = roots.getSecond();
        } else {
            Pair<Optional<Node>, Optional<Node>> trees2 = SplayTree.split(edgeVU);
            J = roots.getFirst();
            K = SplayTree.removeNode(edgeVU);
            L = trees2.getSecond();
        }
        keyToNodes.get(new Pair<>(v, u)).remove(edgeVU);

        Optional<Node> lastNode = SplayTree.lastNode(J.get());
        J = SplayTree.removeNode(lastNode.get());
        keyToNodes.get(lastNode.get().key).remove(lastNode.get());

        Optional<Node> joined;
        if(J.isPresent() && L.isPresent())
            joined = SplayTree.join(J.get(), L.get());
        else if(J.isPresent())
            joined = SplayTree.join(J.get(), null);
        else if(L.isPresent())
            joined = SplayTree.join(null, L.get());
        else
            joined = Optional.empty();

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
        return  optSplayRoot;
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

    public static void deleteEdge(Integer u, Integer v, Map<Pair<Integer, Integer>, LinkedHashSet<Node>> keyToNodes){
        if(keyToNodes.containsKey(new Pair<>(u,v)) &&
                keyToNodes.containsKey(new Pair<>(v,u))){
            cut(u, v, keyToNodes);
        }
    }

    public static Integer getEulerTourRoot(Node treeNode){
        if(treeNode == null) return -1;
        return SplayTree.firstNode(treeNode).get().key.getFirst();
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

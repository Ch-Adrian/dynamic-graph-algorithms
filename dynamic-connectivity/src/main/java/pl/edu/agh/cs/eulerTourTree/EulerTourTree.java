package pl.edu.agh.cs.eulerTourTree;

import pl.edu.agh.cs.common.Pair;
import pl.edu.agh.cs.eulerTourTree.splay.Node;
import pl.edu.agh.cs.eulerTourTree.splay.SplayTree;
import pl.edu.agh.cs.forest.Forest;

import java.util.*;

public class EulerTourTree {

    static SelfBalancingTree splayTree = new SplayTree();

    public static Node createNewEulerTourTree(Integer u, Integer v, Map<Pair<Integer, Integer>, Set<Node>> keyToNodes) {

        /* preconditions */
        if(Forest.checkIfVertexHasNodeInTheTree(u, keyToNodes) || Forest.checkIfVertexHasNodeInTheTree(v, keyToNodes)){
           throw new RuntimeException("There is node already in the tree!");
        }

        Node splayRoot = new Node(new Pair<>(u,u));
        keyToNodes.put(new Pair<>(u,u), new HashSet<>());
        keyToNodes.put(new Pair<>(v,v), new HashSet<>());

        keyToNodes.get(new Pair<>(u,u)).add(splayRoot);
        if(u.equals(v)) return splayRoot;

        insertEdgeToETT(splayRoot, u, v, keyToNodes);
        return splayRoot;
    }

    public static Node addNode(Pair<Integer, Integer> key, Map<Pair<Integer, Integer>, Set<Node>> keyToNodes){
        Node n = new Node(key);
        if(!keyToNodes.containsKey(key)) {
            keyToNodes.put(key, new HashSet<>());
        }
        keyToNodes.get(key).add(n);
        return n;
    }

    public static Optional<Node> reRoot(Integer vertex, Map<Pair<Integer, Integer>, Set<Node>> keyToNodes){

        /* preconditions */
        if(!Forest.checkIfVertexHasNodeInTheTree(vertex, keyToNodes))
            throw new RuntimeException(String.format("There is no vertex: %d%n", vertex));

        Node newRoot = keyToNodes.get(new Pair<>(vertex, vertex)).iterator().next();
        if(Objects.equals(EulerTourTree.getEulerTourRoot(newRoot), vertex)){
            return Optional.of(newRoot);
        }

        Node firstNode = splayTree.firstNode(newRoot).get();
        splayTree.removeNode(firstNode);
        keyToNodes.get(firstNode.key).remove(firstNode);

        Optional<Node> newRootPredecessor = splayTree.predecessor(newRoot);
        if(newRootPredecessor.isPresent()) {
            Pair<Optional<Node>, Optional<Node>> trees = splayTree.split(newRootPredecessor.get());
            splayTree.join(trees.getSecond().get(), trees.getFirst().get());
        }

        splayTree.join(newRoot, addNode(newRoot.key, keyToNodes));

        return splayTree.getRootNode(newRoot);
    }

    public static void link(Integer internal, Integer external,
                            Map<Pair<Integer, Integer>, Set<Node>> keyToNodes) {

        /* check preconditions */
        if(!Forest.checkIfVertexHasNodeInTheTree(internal, keyToNodes))
            throw new RuntimeException(String.format("There is no vertex: %d%n", internal));
        if(!Forest.checkIfVertexHasNodeInTheTree(external, keyToNodes))
             throw new RuntimeException(String.format("There is no vertex: %d%n", external));

        if(internal.equals(external)) return;
        if(splayTree.getRootNode(keyToNodes.get(new Pair<>(internal, internal)).iterator().next()).get().equals(
                 splayTree.getRootNode(keyToNodes.get(new Pair<>(external, external)).iterator().next()).get()))
             return;

        Optional<Node> rootInternal = reRoot(internal, keyToNodes);
        Optional<Node> rootExternal = reRoot(external, keyToNodes);

        if(rootExternal.isPresent() && rootInternal.isPresent()) {
            splayTree.join(rootInternal.get(), addNode(new Pair<>(internal, external), keyToNodes));
            splayTree.join(rootInternal.get(), rootExternal.get());
            splayTree.join(rootInternal.get(), addNode(new Pair<>(external, internal), keyToNodes));
            splayTree.join(rootInternal.get(), addNode(new Pair<>(internal, internal), keyToNodes));
        }
    }

    public static Pair<Optional<Node>, Optional<Node>> cut(Integer u,
                                                           Integer v,
                                                           Map<Pair<Integer, Integer>, Set<Node>> keyToNodes) {

        /* check preconditions */
        if(!Forest.checkIfVertexHasNodeInTheTree(u, keyToNodes))
            throw new RuntimeException(String.format("There is no vertex: %d%n", u));
        if(!Forest.checkIfVertexHasNodeInTheTree(v, keyToNodes))
            throw new RuntimeException(String.format("There is no vertex: %d%n", v));

        if(u.equals(v))
            return new Pair<>(Optional.of(keyToNodes.get(new Pair<>(u,u)).iterator().next()),
                Optional.empty());

        if(!splayTree.getRootNode(keyToNodes.get(new Pair<>(u, u)).iterator().next()).get().equals(
                splayTree.getRootNode(keyToNodes.get(new Pair<>(v, v)).iterator().next()).get()))
            return new Pair<>(Optional.of(keyToNodes.get(new Pair<>(u,u)).iterator().next()),
                    Optional.of(keyToNodes.get(new Pair<>(v,v)).iterator().next()));


        Node edgeUV = keyToNodes.get(new Pair<>(u, v)).iterator().next();
        Node edgeVU = keyToNodes.get(new Pair<>(v, u)).iterator().next();

        Pair<Optional<Node>, Optional<Node>> roots = splayTree.split(edgeUV);
        Optional<Node> J = Optional.empty();
        Optional<Node> K = Optional.empty();
        Optional<Node> L = Optional.empty();

        roots.setFirst(splayTree.removeNode(edgeUV));
        keyToNodes.get(new Pair<>(u, v)).remove(edgeUV);

        if(splayTree.getRootNode(roots.getFirst().get()).equals(splayTree.getRootNode(edgeVU))) {
            Pair<Optional<Node>, Optional<Node>> trees2 = splayTree.split(edgeVU);
            J = splayTree.removeNode(edgeVU);
            K = trees2.getSecond();
            L = roots.getSecond();
        } else {
            Pair<Optional<Node>, Optional<Node>> trees2 = splayTree.split(edgeVU);
            J = roots.getFirst();
            K = splayTree.removeNode(edgeVU);
            L = trees2.getSecond();
        }
        keyToNodes.get(new Pair<>(v, u)).remove(edgeVU);

        Optional<Node> lastNode = splayTree.lastNode(J.get());
        J = splayTree.removeNode(lastNode.get());
        keyToNodes.get(lastNode.get().key).remove(lastNode.get());

        Optional<Node> joined;
        if(J.isPresent() && L.isPresent())
            joined = splayTree.join(J.get(), L.get());
        else if(J.isPresent())
            joined = splayTree.join(J.get(), null);
        else if(L.isPresent())
            joined = splayTree.join(null, L.get());
        else
            joined = Optional.empty();

        return new Pair<>(K, joined);
    }

    public static Optional<Node> addEdgeToNonExistingVertex(Integer treeVertex,
                                                            Integer nonExistingVertex,
                                                            Map<Pair<Integer, Integer>, Set<Node>> keyToNodes) {

        if(!Forest.checkIfVertexHasNodeInTheTree(treeVertex, keyToNodes))
            throw new RuntimeException(String.format("There is no vertex: %d%n", treeVertex));

        Optional<Node> optSplayRoot = splayTree.getRootNode(keyToNodes.get(new Pair<>(treeVertex, treeVertex)).iterator().next());
        if(!keyToNodes.containsKey(new Pair<>(nonExistingVertex, nonExistingVertex)))
            keyToNodes.put(new Pair<>(nonExistingVertex, nonExistingVertex), new HashSet<>());

        if(optSplayRoot.isPresent()) {
            optSplayRoot = reRoot(treeVertex, keyToNodes);
            optSplayRoot.ifPresent(node -> insertEdgeToETT(node, treeVertex, nonExistingVertex, keyToNodes));
        }
        return  optSplayRoot;
    }

    private static void insertEdgeToETT(Node treeNode, Integer splayVertex,
                                        Integer nonExistingVertex,
                                        Map<Pair<Integer, Integer>, Set<Node>> keyToNodes) {
        Optional<Node> optSplayRoot = splayTree.getRootNode(treeNode);
        if(optSplayRoot.isEmpty()) return;

        keyToNodes.put(new Pair<>(nonExistingVertex, splayVertex), new HashSet<>());
        keyToNodes.put(new Pair<>(splayVertex, nonExistingVertex), new HashSet<>());

        keyToNodes.get(new Pair<>(splayVertex, nonExistingVertex))
                .add(splayTree.insertToRight(optSplayRoot.get(), new Pair<>(splayVertex, nonExistingVertex)));
        keyToNodes.get(new Pair<>(nonExistingVertex, nonExistingVertex))
                .add(splayTree.insertToRight(optSplayRoot.get(), new Pair<>(nonExistingVertex, nonExistingVertex)));
        keyToNodes.get(new Pair<>(nonExistingVertex, splayVertex))
                .add(splayTree.insertToRight(optSplayRoot.get(), new Pair<>(nonExistingVertex, splayVertex)));
        keyToNodes.get(new Pair<>(splayVertex, splayVertex))
                .add(splayTree.insertToRight(optSplayRoot.get(), new Pair<>(splayVertex, splayVertex)));
    }

    public static void deleteEdge(Integer u, Integer v,
                                  Map<Pair<Integer, Integer>, Set<Node>> keyToNodes){

        /* preconditions */
        if(!Forest.checkIfVertexHasNodeInTheTree(u, keyToNodes))
            throw new RuntimeException(String.format("There is no vertex: %d%n", u));
        if(!Forest.checkIfVertexHasNodeInTheTree(v, keyToNodes))
            throw new RuntimeException(String.format("There is no vertex: %d%n", v));

        if(u.equals(v)) return;
        if(!splayTree.getRootNode(keyToNodes.get(new Pair<>(u, u)).iterator().next()).get().equals(
                splayTree.getRootNode(keyToNodes.get(new Pair<>(v, v)).iterator().next()).get()))
            return;

        cut(u, v, keyToNodes);

    }

    public static Integer getEulerTourRoot(Node treeNode){
        if(treeNode == null) return -1;
        return splayTree.firstNode(treeNode).get().key.getFirst();
    }

    public static Set<Pair<Integer, Integer>> getEdges(Node treeNode){
        Set<Pair<Integer, Integer>> listOfEdges = new HashSet<>();
        Optional<Node> optSplayRoot = splayTree.getRootNode(treeNode);
        optSplayRoot.ifPresent(node -> dfsEdges(node, listOfEdges));
        return listOfEdges;
    }

    public static Set<Integer> getVertices(Node treeNode){
        Set<Integer> listOfVertices = new HashSet<>();
        Optional<Node> optSplayRoot = splayTree.getRootNode(treeNode);
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
        if(!root.key.getFirst().equals(root.key.getSecond()) &&
                !listOfEdges.contains(new Pair<>(root.key.getSecond(),root.key.getFirst()))){
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
        if(treeNode == null) return 0;
        return splayTree.getSizeOfTree(treeNode);
    }

    public static void show(Node treeNode){
        System.out.println("Show ETT:");
        Optional<Node> optSplayRoot = splayTree.getRootNode(treeNode);
        optSplayRoot.ifPresent(EulerTourTree::dfsShow);
    }

    public static void dfsShow(Node node){
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

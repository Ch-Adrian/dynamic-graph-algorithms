package pl.edu.agh.cs;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.Queue;

public class EvenShiloachTree extends Graph implements Serializable {

    private static final long serialVersionUID = 1L;
    private OperatingMode operatingMode;
    private Vertex source = null;
    private Deque<Pair<Vertex, Integer>> queue = new ArrayDeque<>();

    public EvenShiloachTree(){
        this.operatingMode = OperatingMode.INCREMENTAL;
    }

    public OperatingMode getOperatingMode(){ return this.operatingMode; }

    public void changeOperatingMode(){
        if(this.operatingMode.equals(OperatingMode.INCREMENTAL)){ this.operatingMode = OperatingMode.DECREMENTAL; }
        else { this.operatingMode = OperatingMode.INCREMENTAL; }
    }

    @Override
    public void addEdge(Integer v, Integer w) throws Exception {
        if(!this.operatingMode.equals(OperatingMode.INCREMENTAL))
            throw new Exception("Incremental mode needed to operate!");

        super.addEdge(v,w);
    }

    @Override
    public void addVertex(Integer vertexId) throws Exception {
        if(!this.operatingMode.equals(OperatingMode.INCREMENTAL))
            throw new Exception("Incremental mode needed to operate!");

        super.addVertex(vertexId);
    }

    @Override
    public void deleteEdge(Integer v, Integer w) throws Exception {
        if(!this.operatingMode.equals(OperatingMode.DECREMENTAL))
            throw new Exception("Decremental mode needed!");

        Vertex V = null;
        Vertex W = null;
        if(!vertices.containsKey(v)){
            throw new Exception(String.format("Vertex %d doesn't exists.", v));
        } else V = vertices.get(v);
        if(!vertices.containsKey(w)){
            throw new Exception(String.format("Vertex %d doesn't exists.", w));
        } else W = vertices.get(w);

        this.deleteEdge(V, W);
    }

    @Override
    public void deleteEdge(Vertex v, Vertex w) throws Exception {
        if(!this.operatingMode.equals(OperatingMode.DECREMENTAL))
            throw new Exception("Decremental mode needed!");

        super.deleteEdge(v,w);
        this.updateRank(v);
        this.updateRank(w);
    }

    private void updateRank(Vertex v){
//        System.out.println("Update rank for vertex " + v.getId());
        Queue<Vertex> updateQ = new ArrayDeque<>();
        updateQ.add(v);

        while(!updateQ.isEmpty()) {
            v = updateQ.poll();
//            System.out.println("Next vertex " + v.getId());

            if (!v.equals(this.source) && !v.isUnreachable()) {
//                System.out.println("is reachable");
                if (v.parentsIsEmpty()) {
//                    System.out.println("parents is empty");
                    if (!v.friendsIsEmpty()) {
                        v.shiftByOneRank();
                    } else if (!v.childrenIsEmpty()) {
                        v.shiftByTwoRanks();
                    } else v.setUnreachable();

                    if (v.getRank() >= (this.vertices.size()))
                        v.setUnreachable();

                    for(Vertex n: v.getNeighbours()){
//                        System.out.println("Add neighbour " + n.getId());
                        updateQ.add(n);
                    }

//                    updateQ.addAll(v.getNeighbours());
                }
            }

        }
    }

    public void runBFS() throws Exception {
        if(!this.operatingMode.equals(OperatingMode.DECREMENTAL))
            throw new Exception("Decremental mode needed!");

        if(this.source == null){
            this.source = this.vertices.values().iterator().next();
//            System.out.println("Source: " + this.source);
        }

        this.queue = new ArrayDeque<>();
        for(Vertex v: this.vertices.values()){
            v.cleanNeighbours();
            v.resetVisited();
        }
        this.bfsSearch(this.source);
    }

    private void bfsSearch(Vertex vertex){
        vertex.setRank(0);
        this.queue.add(new Pair<>(vertex, 0));

        while(!this.queue.isEmpty()){
            Pair<Vertex, Integer> current = this.queue.remove();
            Vertex currentVertex = current.getFirst();
            currentVertex.visit();
            Integer distance = current.getSecond();

            for(Edge edge: currentVertex.getAdjacencyEdges()){
                try {
                    Vertex ending = edge.getEnding(currentVertex);

                    if(!ending.hasChild(currentVertex)) {
                        if(ending.getRank().equals(currentVertex.getRank())){
                            ending.addFriend(currentVertex);
                            currentVertex.addFriend(ending);
                        }
                        else {
                            ending.addParent(currentVertex);
                            currentVertex.addChildren(ending);
                            if(!ending.isVisited()) {
                                ending.setRank(distance + 1);
                                this.queue.add(new Pair<>(ending, distance + 1));
                            }
                        }
                    }
                }
                catch(Exception e){
                    System.err.println(e.getMessage());
                    continue;
                }

            }
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EvenShiloachTree)) return false;
        EvenShiloachTree that = (EvenShiloachTree) o;
        return operatingMode == that.operatingMode && Objects.equals(source, that.source) && Objects.equals(queue, that.queue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operatingMode, source, queue);
    }
}

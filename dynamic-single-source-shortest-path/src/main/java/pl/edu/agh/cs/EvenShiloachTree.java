package pl.edu.agh.cs;

import java.util.ArrayDeque;
import java.util.Deque;

public class EvenShiloachTree extends Graph {

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

        if(!v.getParent().equals(w) && !w.getParent().equals(v) && v.getRank().equals(w.getRank())){
            super.deleteEdge(v,w);
        }
        else {

        }

    }

    public void runBFS() throws Exception {
        if(!this.operatingMode.equals(OperatingMode.DECREMENTAL))
            throw new Exception("Decremental mode needed!");

        if(this.source == null){
            this.source = this.vertices.values().iterator().next();
        }

        this.queue = new ArrayDeque<>();
        for(Vertex v: this.vertices.values()){
            v.cleanNeighbours();
        }
        this.bfsSearch(this.source);
    }

    private void bfsSearch(Vertex vertex){
        this.queue.add(new Pair<>(vertex, 0));
        vertex.setRank(0);

        while(!this.queue.isEmpty()){
            Pair<Vertex, Integer> current = this.queue.remove();
            Vertex currentVertex = current.getFirst();
            Integer distance = current.getSecond();

            for(Edge edge: currentVertex.getAdjacencyEdges()){
                try {
                    Vertex ending = edge.getEnding(currentVertex);
                    if(!ending.hasParent(currentVertex)) {
                        if(ending.getRank().equals(currentVertex.getRank())){
                            ending.addFriend(currentVertex);
                            currentVertex.addFriend(ending);
                        }
                        else {
                            ending.addParent(currentVertex);
                            currentVertex.addChildren(ending);
                            ending.setRank(distance + 1);
                            this.queue.add(new Pair<>(ending, distance + 1));
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

}

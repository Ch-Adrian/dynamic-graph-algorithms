package pl.edu.agh.cs;

import java.util.HashMap;
import java.util.Map;

public abstract class Graph {

    protected Map<Integer, Vertex> vertices;

    public Graph(){
        this.vertices = new HashMap<>();
    }

    public void addEdge(Integer v, Integer w) throws Exception {
        Vertex V = null;
        Vertex W = null;
        if(!vertices.containsKey(v)){
            V = new Vertex(v);
            vertices.put(v, V);
        } else V = vertices.get(v);
        if(!vertices.containsKey(w)){
            W = new Vertex(w);
            vertices.put(w, W);
        } else W = vertices.get(w);

        this.addEdge(V, W);
    }

    public void addVertex(Vertex vertex) throws Exception {
        if(!this.vertices.containsKey(vertex.getId()))
            this.vertices.put(vertex.getId(), vertex);
        else
            throw new RuntimeException("Vertex already exists");
    }

    public void addVertex(Integer vertexId) throws Exception {
        if(!this.vertices.containsKey(vertexId))
            this.vertices.put(vertexId, new Vertex(vertexId));
        else
            throw new RuntimeException("Vertex already exists");
    }

    public void addEdge(Vertex v, Vertex w) throws Exception {
        if(!this.vertices.containsKey(v.getId()))
            this.vertices.put(v.getId(), v);

        if(!this.vertices.containsKey(w.getId()))
            this.vertices.put(w.getId(), w);

        Edge edge = new Edge(v, w);

        v.addDirectedEdge(edge, w.getId());
        w.addDirectedEdge(edge, v.getId());
    }

    public void deleteEdge(Integer v, Integer w) throws Exception {
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

    public void deleteEdge(Vertex v, Vertex w) throws Exception {
        if(!this.vertices.containsKey(v.getId()))
            throw new Exception(String.format("Vertex %d doesn't exists.", v.getId()));

        if(!this.vertices.containsKey(w.getId()))
            throw new Exception(String.format("Vertex %d doesn't exists.", w.getId()));

        v.deleteEdge(w);
        w.deleteEdge(v);

    }


}

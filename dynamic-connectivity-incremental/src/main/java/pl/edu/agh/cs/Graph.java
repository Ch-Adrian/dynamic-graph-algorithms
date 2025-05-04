package pl.edu.agh.cs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graph {

    private Map<Integer, Vertex> vertices = new HashMap<>();

    public Graph(){}

    public void addVertex(Vertex vertex){
        if(!this.vertices.containsKey(vertex.getId()))
            this.vertices.put(vertex.getId(), vertex);
        else
            throw new RuntimeException("Vertex already exists");
    }

    public void addVertex(Integer vertexId){
        if(!this.vertices.containsKey(vertexId))
            this.vertices.put(vertexId, new Vertex(vertexId));
        else
            throw new RuntimeException("Vertex already exists");
    }

    public void addNonDirectedEdge(Vertex vertexFrom, Vertex vertexTo){

        if(!this.vertices.containsKey(vertexFrom.getId()))
            this.vertices.put(vertexFrom.getId(), vertexFrom);

        if(!this.vertices.containsKey(vertexTo.getId()))
            this.vertices.put(vertexTo.getId(), vertexTo);

        vertexFrom.addDirectedEdge(vertexTo);
        vertexTo.addDirectedEdge(vertexFrom);
    }

    public void addNonDirectedEdge(int idVertexFrom, int idVertexTo){
        Vertex vertexFrom;
        Vertex vertexTo;
        if(this.vertices.containsKey(idVertexFrom))
            vertexFrom = this.vertices.get(idVertexFrom);
        else vertexFrom = new Vertex(idVertexFrom);
        if(this.vertices.containsKey(idVertexTo))
            vertexTo = this.vertices.get(idVertexTo);
        else vertexTo = new Vertex(idVertexTo);
        this.addNonDirectedEdge(vertexFrom, vertexTo);
    }


    public boolean isConnected(int idVertexFrom, int idVertexTo){
        Integer rootFrom;
        Integer rootTo;
        if(this.vertices.containsKey(idVertexFrom))
            rootFrom = this.vertices.get(idVertexFrom).findRoot().getIdSet();
        else throw new RuntimeException(String.format("Vertex %d does not exist", idVertexFrom));
        if(this.vertices.containsKey(idVertexTo))
            rootTo = this.vertices.get(idVertexTo).findRoot().getIdSet();
        else throw new RuntimeException(String.format("Vertex %d does not exist", idVertexTo));

        return rootTo.equals(rootFrom);

    }





}

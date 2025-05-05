package pl.edu.agh.cs;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Vertex {

    private Integer id;
    private Map<Integer, Vertex> adjacencyList;
    private Integer rank;
    private Integer idSet;

    public Vertex(Integer id) {
        this.id = id;
        this.adjacencyList = new HashMap<>();
        this.rank = 0;
        this.idSet = id;
    }

    public Integer getId() {
        return id;
    }

    public Integer getIdSet(){
        return this.idSet;
    }

    public void setIdSet(Integer newIdSet){
        this.idSet = newIdSet;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer newRank){
        this.rank = newRank;
    }

    public void addEdgeTo(Vertex endVertex){
        this.adjacencyList.put(endVertex.getId(), endVertex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex vertex)) return false;
        return Objects.equals(id, vertex.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

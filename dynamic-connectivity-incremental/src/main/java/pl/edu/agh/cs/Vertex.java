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
        this.idSet= id;
    }

    public void addDirectedEdge(Vertex endVertex){
        this.adjacencyList.put(endVertex.getId(), endVertex);
        Vertex end = endVertex.findRoot();
        Vertex begin  = this.findRoot();

        if(!end.equals(begin)){
            Integer rankEnd = end.getRank();
            Integer rankBegin = begin.getRank();
            if(rankEnd < rankBegin){
                end.setIdSet(begin.getIdSet());
            } else if(rankEnd > rankBegin){
                begin.setIdSet(end.getIdSet());
            } else {
                begin.setRank(begin.getRank() + 1);
                end.setIdSet(begin.getIdSet());
            }

        }

    }

    public Vertex findRoot(){
        Vertex parent = this;
        Integer pointerId = this.idSet;
        while(!pointerId.equals(this.id)) {
            parent = parent.adjacencyList.get(pointerId);
            pointerId = parent.getIdSet();
        }
        return parent;
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

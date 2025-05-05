package pl.edu.agh.cs;

import java.util.*;

public class Vertex {
    
    private Integer id;
    private Map<Integer, Edge> adjacencyList;
    private Integer rank;
    private Map<Integer, Vertex> parents;
    private Map<Integer, Vertex> friends;
    private Map<Integer, Vertex> children;
    private boolean visited = false;

    public Vertex(Integer id) {
        this.id = id;
        this.adjacencyList = new HashMap<>();
        this.rank = -1;
        this.cleanNeighbours();
    }

    public void visit(){
        this.visited = true;
    }

    public boolean isVisited(){ return this.visited; }

    public void resetVisited(){ this.visited = false; }

    public void addDirectedEdge(Edge edge, Integer ending){
        if(!adjacencyList.containsKey(ending))
            this.adjacencyList.put(ending, edge);
        else throw new IllegalArgumentException("Edge already exists");
    }

    public Integer getId() {
        return id;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer newRank){
        this.rank = newRank;
    }

    public Collection<Edge> getAdjacencyEdges(){
        return this.adjacencyList.values();
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

    public void addParent(Vertex vertex){
        if(!this.parents.containsKey(vertex.getId())) {
            this.parents.put(vertex.getId(), vertex);
        }
    }

    public boolean hasParent(Vertex vertex){
        return this.hasParent(vertex.getId());
    }

    public boolean hasParent(Integer vertexId){
        return this.parents.containsKey(vertexId);
    }

    public boolean hasChild(Vertex vertex){
        return this.hasChild(vertex.getId());
    }

    public boolean hasChild(Integer vertexId){
        return this.children.containsKey(vertexId);
    }

    public boolean parentsIsEmpty(){
        return this.parents.isEmpty();
    }

    public boolean friendsIsEmpty(){
        return this.friends.isEmpty();
    }

    public boolean childrenIsEmpty(){
        return this.children.isEmpty();
    }

    public void shiftByOneRank(){
        this.rank++;
        for(Vertex p: this.parents.values()) p.children.remove(this.getId()); //p.removeFromEverywhere(this);
        for(Vertex f: this.friends.values()) f.friends.remove(this.getId()); //f.removeFromEverywhere(this);
        for(Vertex c: this.children.values()) c.parents.remove(this.getId()); //c.removeFromEverywhere(this);

        this.parents = this.friends;
        this.friends = this.children;
        this.children = new HashMap<>();

        for(Vertex p: this.parents.values()) p.children.put(this.getId(), this);
        for(Vertex f: this.friends.values()) f.friends.put(this.getId(), this);
    }

    public void shiftByTwoRanks(){
        this.rank += 2;
        for(Vertex p: this.parents.values()) p.children.remove(this.getId()); //p.removeFromEverywhere(this);
        for(Vertex f: this.friends.values()) f.friends.remove(this.getId()); //f.removeFromEverywhere(this);
        for(Vertex c: this.children.values()) c.parents.remove(this.getId()); //c.removeFromEverywhere(this);

        this.parents = this.children;
        this.friends = new HashMap<>();
        this.children = new HashMap<>();

        for(Vertex p: this.parents.values()) p.children.put(this.getId(), this);
    }

    public void setUnreachable(){
        this.rank = -1;
    }

    public boolean isUnreachable(){
        return this.rank.equals(-1);
    }

    public ArrayList<Vertex> getNeighbours(){
        ArrayList<Vertex> neighbours = new ArrayList<>();
        neighbours.addAll(this.parents.values());
        neighbours.addAll(this.friends.values());
        neighbours.addAll(this.children.values());
        return neighbours;
    }

    public void addChildren(Vertex vertex){
        if(!this.children.containsKey(vertex.getId()))
            this.children.put(vertex.getId(), vertex);
    }

    public void addFriend(Vertex vertex){
        if(!this.friends.containsKey(vertex.getId()))
            this.friends.put(vertex.getId(), vertex);
    }

    public void deleteEdge(Vertex v){
        if(adjacencyList.containsKey(v.getId()))
            this.adjacencyList.remove(v.getId());
        else throw new IllegalArgumentException("Edge doesn't exists");

        this.parents.remove(v.getId());
        this.friends.remove(v.getId());
        this.children.remove(v.getId());
    }

    public void cleanNeighbours(){
        this.parents = new HashMap<>();
        this.children = new HashMap<>();
        this.friends = new HashMap<>();
    }

    private void removeFromEverywhere(Vertex v){
        this.children.remove(v.getId());
        this.parents.remove(v.getId());
        this.friends.remove(v.getId());
    }



}

package pl.edu.agh.cs.common;

import java.io.Serializable;
import java.util.Objects;

public class Edge implements Serializable {

    private static final long serialVersionUID = 1L;
    private Pair<Integer, Integer> endings;
    private Integer weight;

    public Edge(Integer begin, Integer end, Integer weight) {
        this.endings = new Pair<>(begin, end);
        this.weight = weight;
    }

    public Integer getWeight() { return this.weight; }
    public Integer getBegin() { return this.endings.getFirst();}
    public Integer getEnd() { return this.endings.getSecond();}
    public Pair<Integer, Integer> getEndings() { return this.endings;}

    @Override
    public String toString() {
        return "Edge{" +
                "endings=" + endings +
                ", weight=" + weight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge edge = (Edge) o;
        return Objects.equals(endings, edge.endings);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(endings);
    }
}

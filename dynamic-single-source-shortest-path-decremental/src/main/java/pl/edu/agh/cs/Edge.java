package pl.edu.agh.cs;

import java.io.Serializable;
import java.util.ArrayList;

public class Edge implements Serializable {

    private static final long serialVersionUID = 1L;
    private final Vertex source;
    private final Vertex destination;
    private int rank = 0;

    public Edge(Vertex source, Vertex destination) {
        this.source = source;
        this.destination = destination;
        this.rank = 0;
    }

    public Vertex getSource() { return this.source; }
    public Vertex getDestination() { return this.destination; }
    public Vertex getEnding(Vertex source) throws Exception {
        if(this.source.equals(source)){return this.destination;}
        else if(this.destination.equals(source)) return this.source;
        else throw new Exception("Vertex doesn't exists!");
    }

    public int getRank() { return this.rank; }
    public void setRank(int rank) { this.rank = rank; }

}

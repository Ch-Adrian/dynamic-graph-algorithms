package pl.edu.agh.cs;

import com.sun.source.doctree.SerialDataTree;
import pl.edu.agh.cs.common.Edge;
import pl.edu.agh.cs.common.EdgeComparator;
import pl.edu.agh.cs.common.OperatingMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class DynamicMST implements Serializable {

    private static final long serialVersionUID = 1L;
    private OperatingMode operatingMode;
    private ArrayList<Edge> edges;
    private DynamicMSTETT dynamicMSTETT;

    public DynamicMST(Integer n){
        this.operatingMode = OperatingMode.INCREMENTAL;
        this.edges = new ArrayList<>();
        this.dynamicMSTETT = new DynamicMSTETT(n);
    }

    public void changeOperatinMode(OperatingMode newMode){
        this.operatingMode = newMode;
    }

    public void addEdge(Integer begin, Integer end, Integer weight){
        if(this.operatingMode == OperatingMode.DECREMENTAL) return;

        this.edges.add(new Edge(begin, end, weight));
    }

    public void calculateMST(){
        if(this.operatingMode == OperatingMode.INCREMENTAL) return;
        this.edges.sort(new EdgeComparator());

        for(Edge edge : this.edges){
            this.dynamicMSTETT.addEdge(edge.getBegin(), edge.getEnd(), edge.getWeight());
        }
    }

    public void deleteEdge(Edge edge){
        if(this.operatingMode == OperatingMode.INCREMENTAL) return;
        this.dynamicMSTETT.deleteEdge(edge.getBegin(), edge.getEnd());
    }

    public boolean checkIfTreeEdgeExists(Integer begin, Integer end){
        return this.dynamicMSTETT.checkIfTreeEdgeExists(begin, end);
    }

    public boolean checkIfNonTreeEdgeExists(Integer begin, Integer end){
        return this.dynamicMSTETT.checkifNonTreeEdgeExists(begin, end);
    }

    public boolean isConnected(Integer begin, Integer end){
        return dynamicMSTETT.isConnected(begin, end);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DynamicMST)) return false;
        DynamicMST that = (DynamicMST) o;
        return operatingMode == that.operatingMode && Objects.equals(edges, that.edges) && Objects.equals(dynamicMSTETT, that.dynamicMSTETT);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operatingMode, edges, dynamicMSTETT);
    }
}

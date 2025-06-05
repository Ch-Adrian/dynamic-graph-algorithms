package pl.edu.agh.cs.common;

import java.io.Serializable;
import java.util.Comparator;

public class EdgeComparator implements Comparator<Edge>, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int compare(Edge edge, Edge t1) {
        return edge.getWeight() - t1.getWeight();
    }

}

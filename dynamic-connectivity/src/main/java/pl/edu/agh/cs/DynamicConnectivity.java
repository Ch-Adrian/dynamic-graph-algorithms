package pl.edu.agh.cs;

import pl.edu.agh.cs.eulerTourTree.splay.Node;
import pl.edu.agh.cs.forest.Forest;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class DynamicConnectivity {

    private final ArrayList<Forest> forests = new ArrayList<>();

    public DynamicConnectivity(Integer n) {
        int amtOfLevels = (int) Math.ceil(Math.log(n)) + 1;
        for (int i = 0; i <= amtOfLevels; i++) {
            forests.add(new Forest(i, forests));
        }
    }

    public Forest getForestForLevel(Integer level) {
        return forests.get(level);
    }

    public void addEdge(Integer from, Integer to) {
        if(from.equals(to)) return;

        Optional<Node> treeFrom = this.forests.get(0).getRepresentativeTreeNode(from);
        Optional<Node> treeTo = this.forests.get(0).getRepresentativeTreeNode(to);

        if(treeFrom.isPresent() && treeTo.isPresent() && Objects.equals(treeFrom, treeTo)) {
            this.forests.get(0).addNonTreeEdge(from, to);
        } else {
            this.forests.get(0).addTreeEdge(from, to);
        }
    }

    public void deleteEdge(Integer from, Integer to) {
        if(from.equals(to)) return;

        boolean nonTreeEdgeFound = this.forests.get(0).checkIfNonTreeEdgeExists(from, to);
        if(nonTreeEdgeFound) {
            for(Forest forest : forests) {
                forest.deleteNonTreeEdge(from, to);
            }
        } else {
            int highestLvl = -1;
            for(Forest forest : forests) {
                if(forest.checkIfTreeEdgeExists(from, to)) {
                    highestLvl++;
                    forest.deleteTreeEdge(from, to);
                }
            }
            this.forests.get(highestLvl).findReplacementEdge(from, to, highestLvl);

        }
    }

    public boolean isConnected(Integer v, Integer w){
        return this.forests.get(0).isConnected(v, w);
    }
}

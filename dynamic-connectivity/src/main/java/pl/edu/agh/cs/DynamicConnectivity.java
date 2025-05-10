package pl.edu.agh.cs;

import pl.edu.agh.cs.eulerTourTree.splay.Node;
import pl.edu.agh.cs.forest.Forest;

import java.util.ArrayList;
import java.util.Objects;

public class DynamicConnectivity {

    private ArrayList<Forest> forests = new ArrayList<>();
    private int amtOfLevels = 1;

    public DynamicConnectivity(int n) {
        this.amtOfLevels = (int) Math.ceil(Math.log(n));
        for (int i = 0; i <= this.amtOfLevels; i++) {
            forests.add(new Forest(i, this));
        }
    }

    public Forest getForestForLevel(int level) {
        return forests.get(level);
    }

    public int getAmtOfLevels(){
        return amtOfLevels;
    }

    public void addEdge(int from, int to) {
        Node treeFrom = this.forests.getFirst().getRepresentativeTreeNode(from);
        Node treeTo = this.forests.getFirst().getRepresentativeTreeNode(to);

        if(Objects.equals(treeFrom, treeTo) && treeFrom != null) {
            this.forests.getFirst().addNonTreeEdge(from, to);
        } else {
            this.forests.getFirst().addTreeEdge(from, to);
        }
    }

    public void deleteEdge(int from, int to) throws Exception {
        boolean nonTreeEdgeFound = this.forests.getFirst().checkIfNonTreeEdgeExists(from, to);
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
        return this.forests.getFirst().isConnected(v, w);
    }


}

package pl.edu.agh.cs;

import pl.edu.agh.cs.forest.Forest;
import pl.edu.agh.cs.forest.Tree;

import java.util.ArrayList;

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

    public void addEdge(int from, int to) throws Exception {
        if(from == 4 && to == 8) System.out.println("Adding edge " + from + " to " + to);
        Tree treeFrom = this.forests.getFirst().getTree(from);
        Tree treeTo = this.forests.get(0).getTree(to);

        if(treeFrom == treeTo && treeFrom != null) {
            this.forests.get(0).addNonTreeEdge(from, to);
        } else if(treeFrom == null && treeTo != null) {
            this.forests.get(0).addTreeEdge(from, to);
        } else if(treeFrom != null && treeTo == null) {
            this.forests.get(0).addTreeEdge(from, to);
        } else if(treeFrom != null && treeTo != null) {
            this.forests.get(0).addTreeEdge(from, to);
        } else {
            this.forests.get(0).createNewTree(from, to);
        }
    }

    public void deleteEdge(int from, int to) throws Exception {
        System.out.println("Deleting edge " + from + " to " + to);
        boolean nonTreeEdgeFound = this.forests.getFirst().checkIfNonTreeEdgeExists(from, to);
        if(nonTreeEdgeFound) {
            for(Forest forest : forests) {
                System.out.println("deleting non tree edge " + from + " to " + to);
                forest.deleteNonTreeEdge(from, to);
            }
        } else {
            int highestLvl = -1;
            for(Forest forest : forests) {
                if(forest.checkIfTreeEdgeExists(from, to)) {
                    highestLvl++;
                    System.out.println("highestLvL: "+highestLvl);
                    System.out.println("deleting tree edge " + from + " to " + to);
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

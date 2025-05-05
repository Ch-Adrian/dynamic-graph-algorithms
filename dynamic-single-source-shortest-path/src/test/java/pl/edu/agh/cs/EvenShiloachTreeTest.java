package pl.edu.agh.cs;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EvenShiloachTreeTest {

    @Test
    public void overallTest1() {
        EvenShiloachTree evenShiloachTree = new EvenShiloachTree();
        ArrayList<Pair<Integer, Integer>> edges = new ArrayList<>();
        edges.add(new Pair<>(0, 1));
        edges.add(new Pair<>(0, 2));
        edges.add(new Pair<>(2, 3));
        edges.add(new Pair<>(2, 4));
        edges.add(new Pair<>(3, 5));
        edges.add(new Pair<>(4, 6));
        edges.add(new Pair<>(5, 7));
        edges.add(new Pair<>(6, 8));
        edges.add(new Pair<>(7, 9));
        edges.add(new Pair<>(7, 8));

        for(Pair<Integer, Integer> edge : edges){
            try {
                evenShiloachTree.addEdge(edge.getFirst(), edge.getSecond());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                return;
            }
        }

        evenShiloachTree.changeOperatingMode();
        try {
            evenShiloachTree.runBFS();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        assertEquals(Integer.valueOf(0), evenShiloachTree.vertices.get(0).getRank());
        assertEquals(Integer.valueOf(1), evenShiloachTree.vertices.get(1).getRank());
        assertEquals(Integer.valueOf(1), evenShiloachTree.vertices.get(2).getRank());
        assertEquals(Integer.valueOf(2), evenShiloachTree.vertices.get(3).getRank());
        assertEquals(Integer.valueOf(2), evenShiloachTree.vertices.get(4).getRank());
        assertEquals(Integer.valueOf(3), evenShiloachTree.vertices.get(5).getRank());
        assertEquals(Integer.valueOf(3), evenShiloachTree.vertices.get(6).getRank());
        assertEquals(Integer.valueOf(4), evenShiloachTree.vertices.get(7).getRank());
        assertEquals(Integer.valueOf(4), evenShiloachTree.vertices.get(8).getRank());
        assertEquals(Integer.valueOf(5), evenShiloachTree.vertices.get(9).getRank());


        try {
            evenShiloachTree.deleteEdge(2, 3);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        for(Vertex v: evenShiloachTree.vertices.values()){
            System.out.println(String.format("Vertex %d has rank: %d", v.getId(), v.getRank()));
        }

        assertEquals(Integer.valueOf(0), evenShiloachTree.vertices.get(0).getRank());
        assertEquals(Integer.valueOf(1), evenShiloachTree.vertices.get(1).getRank());
        assertEquals(Integer.valueOf(1), evenShiloachTree.vertices.get(2).getRank());
        assertEquals(Integer.valueOf(7), evenShiloachTree.vertices.get(3).getRank());
        assertEquals(Integer.valueOf(2), evenShiloachTree.vertices.get(4).getRank());
        assertEquals(Integer.valueOf(6), evenShiloachTree.vertices.get(5).getRank());
        assertEquals(Integer.valueOf(3), evenShiloachTree.vertices.get(6).getRank());
        assertEquals(Integer.valueOf(5), evenShiloachTree.vertices.get(7).getRank());
        assertEquals(Integer.valueOf(4), evenShiloachTree.vertices.get(8).getRank());
        assertEquals(Integer.valueOf(6), evenShiloachTree.vertices.get(9).getRank());

    }

}

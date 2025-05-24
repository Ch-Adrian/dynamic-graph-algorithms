package pl.edu.agh.cs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Graph graph = new Graph();

        String filePath = "/home/adrian/dev/dynamic-graph-algorithms/dynamic-connectivity-incremental/Email-EuAll.txt";

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tokens = line.split("\t");
                Integer val1 = Integer.parseInt(tokens[0]);
                Integer val2 = Integer.parseInt(tokens[1]);
                graph.addNonDirectedEdge(val1, val2);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
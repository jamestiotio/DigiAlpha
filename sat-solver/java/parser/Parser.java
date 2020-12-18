package parser;

import dfs.Vertex;
import dfs.Graph;
import dfs.Tarjan;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class Parser {
    public static void main(String[] args) throws Exception {
        String path = args[0];
        try {
            long startTime = System.nanoTime();
            Graph g = Tarjan.buildGraph(path);
            assert g != null;
            g.setIndex(1);
            Set<Vertex> vertices = g.getAdjacencyListsKeySet();

            for (Vertex vertex : vertices) {
                if (vertex.getIndex() != 0) {
                    break;
                } else {
                    Tarjan.tarjan(g, vertex);
                }
            }

            if (Tarjan.satisfiable(g)) {
                System.out.println("SATISFIABLE");

                // TODO: For larger CNFs, there might be a slight offset error on the total size for some reason (need to check logic or math further).
                HashMap<Integer, Boolean> solution = Tarjan.solve(g);

                for (int i = 1; i <= solution.size(); i++) {
                    Boolean value = solution.get(i);

                    if (Boolean.TRUE.equals(value)) {
                        System.out.print('1');
                    } else {
                        System.out.print('0');
                    }

                    System.out.print(' ');
                }

                System.out.println("");
            } else {
                System.out.println("UNSATISFIABLE");
            }

            long endTime = System.nanoTime();
            long runTime = endTime - startTime;
            System.out.println("Time taken for Tarjan's algorithm: " + runTime / 1000000.0 + " ms");
        }

        catch (InvalidInputException e) {
            System.out.println("InvalidInputException was raised!");
        }

        catch (IOException e) {
            System.out.println("IOException was raised!");
        }
    }
}

package dfs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Tarjan {
    public static Graph buildGraph(String path) throws Exception {
        File file = new File(path);
        Scanner sc = new Scanner(file);

        while (sc.next().charAt(0) == 'c')
            sc.nextLine();

        sc.next();

        int NO_OF_VARIABLES = sc.nextInt();
        int NO_OF_CLAUSES = sc.nextInt();

        ArrayList<ArrayList<Integer>> edges = new ArrayList<>();

        for (int i = 0; i < NO_OF_CLAUSES; i++) {
            int literal = sc.nextInt();
            ArrayList<Integer> clause = new ArrayList<>();

            while (literal != 0 && sc.hasNext()) {
                clause.add(literal);
                literal = sc.nextInt();
            }

            if (clause.size() == 1) {
                ArrayList<Integer> edge = new ArrayList<>();
                edge.add(-clause.get(0));
                edge.add(clause.get(0));
                edges.add(edge);
            } else if (clause.size() == 2) {
                ArrayList<Integer> firstEdge = new ArrayList<>();
                ArrayList<Integer> secondEdge = new ArrayList<>();
                firstEdge.add(-clause.get(0));
                firstEdge.add(clause.get(1));
                secondEdge.add(-clause.get(1));
                secondEdge.add(clause.get(0));
                edges.add(firstEdge);
                edges.add(secondEdge);
            } else {
                System.out.println("This is not a 2-SAT problem!");
                return null;
            }
        }

        return new Graph(NO_OF_VARIABLES, edges);
    }

    public static void tarjan(Graph g, Vertex vertex) {
        vertex.setIndex(g.getIndex());
        vertex.setLowLink(g.getIndex());
        g.setIndex(g.getIndex() + 1);
        g.pushToStack(vertex);
        vertex.setInStack(true);

        for (Vertex next : g.getVertexFromAdjacencyLists(vertex)) {
            if (next.getIndex() == 0) {
                tarjan(g, next);
                vertex.setLowLink(Math.min(vertex.getLowLink(), next.getLowLink()));
            } else if (next.getInStack()) {
                vertex.setLowLink(Math.min(vertex.getLowLink(), next.getIndex()));
            }
        }

        if (vertex.getLowLink() == vertex.getIndex()) {
            ArrayList<Vertex> scc = new ArrayList<>();
            Vertex poppedVertex = null;

            while (vertex != poppedVertex) {
                poppedVertex = g.popFromStack();
                poppedVertex.setInStack(false);
                scc.add(poppedVertex);
            }

            g.addToSCCList(scc);
        }
    }

    public static boolean satisfiable(Graph g) {
        for (ArrayList<Vertex> scc : g.getSCCList()) {
            HashMap<Integer, Boolean> variables = new HashMap<>();
            for (Vertex v : scc) {
                int varID = Math.abs(v.getID());

                if (variables.containsKey(varID)) {
                    return false;
                } else {
                    variables.put(varID, true);
                }
            }
        }

        return true;
    }

    public static HashMap<Integer, Boolean> solve(Graph g) {
        HashMap<Integer, Boolean> solution = new HashMap<>();

        for (ArrayList<Vertex> scc : g.getSCCList()) {
            for (Vertex v : scc) {
                if (solution.containsKey(Math.abs(v.getID())))
                    continue;

                int id = v.getID();

                if (id > 0) {
                    solution.put(id, true);
                } else if (id < 0) {
                    solution.put(-id, false);
                }
            }
        }

        return solution;
    }
}

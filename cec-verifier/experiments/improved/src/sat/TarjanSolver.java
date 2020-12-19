package sat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class TarjanSolver {
    // Define class data fields and attributes
    private static int varNum;
    static private int index = 0;

    static int[] definedIndex = new int[]{};
    static int[] prevIndex = new int[]{};
    static ArrayList<Integer>[] graph;
    static int[] answer = new int[]{};
    static LinkedList<Integer> stack = new LinkedList<>();
    static ArrayList<Integer>[] scc;

    /**
     * Solve a 2-SAT problem using Tarjan's strongly-connected components algorithm.
     * Please note that this implementation is slightly inefficient.
     * It will still solve in polynomial time.
     * 
     * @param formula
     * @param numOfVars
     * @return the integer array which contains the result for all vertices.
     */
    public static int[] solve(ArrayList<ArrayList<Integer>> formula, int numOfVars) {
        // Form definite size for each attribute
        varNum = numOfVars * 2 + 1;
        definedIndex = new int[varNum];
        prevIndex = new int[varNum];
        graph = new ArrayList[varNum];
        answer = new int[varNum];
        scc = new ArrayList[varNum];

        // Create the associated graph from the formula
        createGraph(formula);

        // Get all of the strongly-connected components
        getAllSCC();

        /**
         * Once all SCCs have been found, check if each SCC can traverse to its negation.
         * If it does, the formula is not satisfiable.
         * Otherwise, the answer will indicate the truth value of each vertex.
         * Note: The answer might contain something like 5879 => 5233. The number above or below
         * the number of variables should indicate if it is true or false.
         * If a variable points to null, ignore it.
         */
        for (int l = 1; l < varNum; l++) {
            ArrayList<Integer> temp = scc[l];

            if (temp != null) {
                for (int u : temp) {
                    int nu = negate(u);

                    if (answer[nu] == l) {
                        return null;
                    }

                    answer[u] = l;
                }
            }
        }

        // TODO: Still need to check the correctness of the condition answer[i] <= numOfVars (another size offset error might also be probable for this case).
        // Might wanna default back to optimized DPLL algorithm for now, even for 2-SAT problems.
        // The optimized DPLL algorithm always outputs the correct result/answer.
        for (int i = 1; i <= numOfVars; i++) {
            if (answer[i] <= numOfVars) {
                System.out.print("1 ");
            }
            else {
                System.out.print("0 ");
            }
        }

        System.out.print("\n");

        return answer;
    }

    /**
     * Given a formula in the form of nested ArrayLists, create the adjacency list representation of vertices (directed graph).
     * The formula given is in Conjunctive Normal Form, where each clause can have at most 2 literals.
     * To convert it into an adjacency list of vertices, each clause is converted into 2 implication statements.
     * For example: (a v ~b) becomes (~a -> ~b) and (b -> a).
     * For a single-literal clause, such as (a), it is the same as (a v a).
     * An implied statement will be (~a -> a), which means a must be true.
     * If one literal is false, the other must be true for the clause to be true.
     * For this solver, instead of using the built-in Formula class, a nested ArrayList is given.
     * 
     * @param formula, the nested ArrayList of variables.
     */
    public static void createGraph(ArrayList<ArrayList<Integer>> formula) {
        int first, second, negatedFirst, negatedSecond;

        for (ArrayList<Integer> c : formula) {
            if (c.size() == 1) {
                first = (int) c.get(0);

                if (first < 0) first = (varNum - 1) / 2 - first;

                second = negate(first);

                if (graph[second] == null) graph[second] = new ArrayList<>(Arrays.asList(first));
                else graph[second].add(first);

                continue;
            }

            first = (int) c.get(0);

            if (first < 0) first = (varNum - 1) / 2 - first;

            negatedFirst = negate(first);
            second = (int) c.get(1);

            if (second < 0) second = (varNum - 1) / 2 - second;

            negatedSecond = negate(second);

            if (graph[negatedFirst] == null) graph[negatedFirst] = new ArrayList<>(Arrays.asList(second));
            else graph[negatedFirst].add(second);

            if (graph[negatedSecond] == null) graph[negatedSecond] = new ArrayList<>(Arrays.asList(first));
            else graph[negatedSecond].add(first);
        }
    }

    /**
     * This method returns a 'negated' variable.
     * Using the number of variables as the counter, all negated variables will be their value + (varNum - 1) / 2.
     * 
     * @param l
     * @return the negation of l.
     */
    public static int negate(int l) {
        if (l <= (varNum - 1) / 2) return l + (varNum - 1) / 2;
        return l - (varNum - 1) / 2;
    }

    /**
     * Going through all variables, find all of the strongly-connected components using
     * Tarjan's algorithm.
     * If a vertex has already been visited, skip it.
     */
    private static void getAllSCC() {
        for (int i = 1; i < varNum; i++ ) {
            if (definedIndex[i] == 0) tarjanDFS(i);
        }
    }

    /**
     * Given the vertices, find all strongly-connected components of a vertex.
     * 
     * definedIndex indicates if a vertex has been discovered and the time when it was discovered.
     * 
     * prevIndex indicates the index of the vertex which was traversed earlier. This
     * previous index will show whether that vertex can reach this vertex.
     * 
     * stack holds all found nodes in a LIFO order.
     *
     * Initially, when starting with the first vertex, which is variable 1, the index is 0.
     * Adding 1 starts the 'counter', so to speak. The index is then stored in the definedIndex and prevIndex lists,
     * and the vertex is added to the stack.
     *
     * Then, the vertex's adjacent vertices are searched through.
     * 
     * If there is a vertex, it is checked whether it has been traversed or not. If not, it will execute a DFS through
     * the vertex until it hits a vertex with no adjacent vertices or until it hits a vertex that has already been
     * traversed.
     * 
     * If the vertex that was hit has been traversed, the definedIndex of that vertex would be compared
     * with the current prevIndex. If it is smaller, the current prevIndex is changed to it.
     *
     * Otherwise, once the call has been made, the traversed vertex's prevIndex is compared to the
     * current vertex's prevIndex, and will be changed if the traversed one is lower, which would imply
     * that the vertices inside the stack are strongly connected.
     *
     * If the vertex that has been traversed hits the first/head vertex, the recursion will stop
     * and the whole stack up until the head vertex will be added to the list of strongly-connected
     * components.
     *
     * @param l
     */
    private static void tarjanDFS(int l) {
        index++;
        definedIndex[l] = index;
        prevIndex[l] = index;
        stack.add(l);
        ArrayList<Integer> temp = graph[l];

        if (temp != null) {
            for (int u : temp) {
                if (definedIndex[u] == 0) {
                    tarjanDFS(u);
                    prevIndex[l] = Math.min(prevIndex[l], prevIndex[u]);
                }
                else if (stack.contains(u)) {
                    prevIndex[l] = Math.min(prevIndex[l], definedIndex[u]);
                }
            }
        }

        if (definedIndex[l] == prevIndex[l]) {
            int u;
            ArrayList<Integer> newSCC = new ArrayList<>();
            while (true) {
                u = stack.removeLast();
                newSCC.add(u);
                if (u == l) break;
            }

            scc[l] = newSCC;
        }
    }
}

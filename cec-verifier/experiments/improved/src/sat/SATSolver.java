package sat;

import sat.env.*;
import sat.formula.*;

import java.util.ArrayList;
import java.util.HashMap;

// Define two types of solvers in this class
public class SATSolver {
    /**
     * First solver: Tarjan's algorithm for 2-SAT
     * 
     * Solve the problem using Tarjan's algorithm.
     * Tarjan's algorithm is based on the notion of strongly-connected components
     * from graph theory. This algorithm would be able to solve a 2-SAT problem
     * in linear time, which is much faster than the naive DPLL algorithm.
     * 
     * @return an integer array which contains the result from the solver, if any
     *         (if the problem is unsatisfiable, no output will be produced, a null
     *         object will be returned and the whole program will terminate).
     */
    public static int[] tarjanSolve(ArrayList<ArrayList<Integer>> formula, int numOfVars) {
        int[] result = TarjanSolver.solve(formula, numOfVars);

        if (result == null) {
            System.out.println("FORMULA UNSATISFIABLE");
        }
        else {
            System.out.println("FORMULA SATISFIABLE");
        }

        return result;
    }

    /**
     * Second solver: Optimized DPLL algorithm for k-SAT (k >= 3).
     * 
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     * 
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static HashMap<Variable, Bool> dpllSolve(Formula formula) {
        HashMap<Variable, Bool> answer = new HashMap<>();
        HashMap<Variable, Bool> result = DPLLSolver.solve(formula.getClauses(), answer);
        
        if (result == null) {
            System.out.println("FORMULA UNSATISFIABLE");
        }
        else {
            System.out.println("FORMULA SATISFIABLE");
        }

        return result;
    }
}

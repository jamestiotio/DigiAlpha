package sat;

import immutable.ImList;
import sat.env.Bool;
import sat.env.Environment;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     * 
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        // Check if empty
        Environment e = new Environment();
        if (formula.getSize() == 0){
            return null;
        }else{
            return solve(formula.getClauses(), e);
        }
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     * 
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        if (clauses.size() == 0) {
            return env;
        }
        int cSize = Integer.MAX_VALUE;
        Clause cSmallest = new Clause();
        for (Clause c : clauses){
            if (c.isEmpty()) {
                return null;
            }else if (c.isUnit()){
                cSmallest = c;
                break;
            }else if (c.size() < cSize){
                cSize = c.size();
                cSmallest = c;
            }
        }
        Literal l = cSmallest.chooseLiteral();
        Environment newEnv = env.putTrue(l.getVariable());
        newEnv = solve(substitute(clauses,l), newEnv);
        return newEnv;
    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     * 
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses,
            Literal l) {
        for (Clause c : clauses){
            if (c.contains(l)){
                clauses = clauses.remove(c);
            }else if (c.contains(l.getNegation())){
                Clause new_c = c.reduce(l);
                clauses = clauses.remove(c);
                clauses = clauses.add(new_c);
            }
        }
        return clauses;
    }

}

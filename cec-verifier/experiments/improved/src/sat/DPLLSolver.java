package sat;

import sat.env.*;
import sat.formula.*;
import immutable.ImList;

import java.util.HashMap;

public class DPLLSolver {
    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     * 
     * @param clauses
     *            formula in conjunctive normal form.
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    public static HashMap<Variable, Bool> solve(ImList<Clause> clauses, HashMap<Variable, Bool> env) {
        if (clauses.isEmpty()) return env;

        Clause smallest = clauses.first();

        for (Clause c : clauses.rest()) {
            if (c.isEmpty()) return null;

            if (smallest.size() > c.size()) smallest = c;

            if (smallest.isUnit()) break;
        }

        Literal randomLiteral = smallest.chooseLiteral();
        Variable randomVariable = randomLiteral.getVariable();
        ImList<Clause> secondClauses = substitute(clauses, randomLiteral);

        if (secondClauses == null) return null;

        if (randomLiteral instanceof PosLiteral) env.put(randomVariable, Bool.TRUE);
        else env.put(randomVariable, Bool.FALSE);

        if (smallest.isUnit()) return solve(secondClauses, env);
        else {
            HashMap<Variable,Bool> outputEnv = solve(secondClauses, env);

            if (outputEnv == null) {
                secondClauses = substitute(clauses, randomLiteral.getNegation());

                if (secondClauses == null) return null;

                if (randomLiteral instanceof PosLiteral) env.put(randomVariable, Bool.FALSE);
                else env.put(randomVariable, Bool.TRUE);

                return solve(secondClauses, env);
            }

            return outputEnv;
        }
    }

    /**
     * Given a clause list and literal, produce a new list resulting from
     * setting that literal to true.
     * 
     * @param clauses
     *            a list of clauses.
     * @param l
     *            a literal to set to true.
     * @return a new list of clauses resulting from setting l to true.
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses, Literal l) {
        ImList<Clause> newClauses = clauses;
        Clause tempClause;

        for (Clause c : clauses) {
            if (c.contains(l) || c.contains(l.getNegation())) {
                tempClause = c.reduce(l);

                if (tempClause != null) {
                    if (tempClause.isEmpty()) return null;

                    newClauses = newClauses.add(tempClause);
                }

                newClauses = newClauses.remove(c);
            }
        }

        return newClauses;
    }
}
package sat;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/
import sat.env.*;
import sat.formula.*;
import sat.formula.Clause;
import sat.formula.Literal;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SATSolverTest {
    public static void main(String args[]) throws IOException {
        // Check length of args; exactly 1 required
        if (args.length != 1) {
            throw new IllegalArgumentException("Exactly 1 argument required!");
        }
        // Construct formula
        String filepath = args[0];
        List<String> fileContents = readFile(filepath);
        Formula f = parse(fileContents);
        try {
            FileWriter myWriter = new FileWriter("BoolAssignment.txt");
            System.out.println("SAT solver starts!!!");
            long started = System.nanoTime();
            Environment e = SATSolver.solve(f);
            long time = System.nanoTime();
            long timeTaken= time - started;
            System.out.println("Time:" + timeTaken/1000000.0 + "ms");
            if (e != null) {
                System.out.println("satisfiable");
                String results = e.toString();
                // Remove "Environment:[]"
                results = results.substring(13, results.length()-1);
                String[] arrayResults = results.split(", ");
                for (String line : arrayResults) {
                    line = line.replace("->", ":");
                    myWriter.write(line + "\n");
                }
                myWriter.close();
            }else{
                System.out.println("not satisfiable");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> readFile (String fName) throws IOException {
        List<String> lines;
        lines = Files.readAllLines((Paths.get(fName)));
        return lines;
    }

    public static Formula parse(List<String> lines) {
        ArrayList<sat.formula.Literal> literals = new ArrayList<Literal>();
        ArrayList<sat.formula.Clause> clauses = new ArrayList<Clause>();

        for (String line: lines) {
            if (!(line.toCharArray().length != 0) || line.charAt(0) == 'c' || line.charAt(0) == 'p'){ continue; }
            else {
                String[] literalString = line.split("\\s+");
                // \s+ matches all whitespaces

                for (String literal: literalString) {
                    // random stuff from somewhere
                    if (literal.equals("")) { continue; }

                    if (literal.charAt(0) == '-') {
                        // negative number, remove - and add to list
                        Literal l = NegLiteral.make((literal.substring(1)));
                        literals.add(l);
                    } else if (literal.charAt(0) != '0') {
                        // just a positive number
                        literals.add(PosLiteral.make(literal));
                    } else {
                        // end of line, make a clause
                        clauses.add(makeCl(literals));
                        literals.clear();
                    }
                }
            }
        }

        return makeFm(clauses);
    }
    
    private static Formula makeFm(ArrayList<Clause> e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }
    
    private static Clause makeCl(ArrayList<Literal> e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }
}
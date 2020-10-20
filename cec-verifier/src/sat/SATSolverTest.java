package sat;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/

import immutable.ImList;
import sat.env.*;
import sat.formula.*;
import sat.formula.Clause;
import sat.formula.Literal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();



	
	// TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability
    public static void main(String args[]) throws IOException {
        List<String> fileContents = readFile("/Users/shuyijia/AndroidStudioProjects/MyApp001/code2d/src/main/java/sampleCNF/s8Sat.cnf");
        Formula f = parse(fileContents);
        SATSolver.solve(f);

//        ImList<Clause> cls = f.getClauses();
//        int cSize = Integer.MAX_VALUE;
//        Clause cSmallest = new Clause();
//        for (Clause c : cls){
//            if (c.size() < cSize){
//                cSize = c.size();
//                cSmallest = c;
//            }
//        }
//        System.out.println(cSmallest);
//        Environment e = new Environment();
//        Literal l = cSmallest.chooseLiteral();
//        Environment newE = e.putTrue(l.getVariable());
//        System.out.println(newE);

    }

    public static List<String> readFile (String fName) throws IOException {
        List<String> lines = Collections.emptyList();
        lines = Files.readAllLines((Paths.get(fName)));

        return lines;
    }

    public static Formula parse(List<String> lines) {
        ArrayList<sat.formula.Literal> literals = new ArrayList<Literal>();
        ArrayList<sat.formula.Clause> clauses = new ArrayList<Clause>();
        Formula formula = new Formula();

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
    
	
    public void testSATSolver1(){
    	// (a v b)
    	//Environment e = SATSolver.solve(makeFm(makeCl(a,b))	);
/*
    	assertTrue( "one of the literals should be set to true",
    			Bool.TRUE == e.get(a.getVariable())  
    			|| Bool.TRUE == e.get(b.getVariable())	);
    	
*/    	
    }
    
    
    public void testSATSolver2(){
    	// (~a)
    	//Environment e = SATSolver.solve(makeFm(makeCl(na)));
/*
    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
*/    	
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
package sat;

import sat.env.*;
import sat.formula.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SATSolverTest {
    // Main program entry
    public static void main(String[] args) throws IOException {
        // Check length of args; exactly 1 required
        if (args.length != 1) {
            throw new IllegalArgumentException("Exactly 1 argument required!");
        }
        // Construct formula
        String filename = args[0];
        String regexp = "\\s{1,10}";

        // Initialize variables
        Formula f = new Formula();
        Clause tempClause = new Clause();

        String templine = "";
        String[] templist = new String[]{};

        int longestClause = 1;
        boolean pCheck = false;
        boolean clauseCheck = false;
        int pCount = 3;
        int numOfVariables = 0;
        int variableValue = 0;
        ArrayList<Literal> literalList = new ArrayList<>();
        ArrayList<ArrayList<Integer>> formulaList = new ArrayList<>();
        ArrayList<Integer> clauseList = new ArrayList<>();

        // Parse CNF file input into a full SAT boolean formula expression (literals and clauses)
        try {
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            templine = new String(data, StandardCharsets.UTF_8.name());
            templist = templine.split(regexp);

            for (String s : templist) {
                if (!s.equals("p") && !pCheck && !clauseCheck) continue;

                if (s.equals("p")) {
                    pCheck = true;
                    continue;
                }

                if (pCheck) {
                    if (pCount == 3) {
                        pCount--;
                        continue;
                    }
                    else if (pCount == 2) {
                        numOfVariables = Integer.parseInt(s);
                        pCount--;
                        continue;
                    }
                    else {
                        pCheck = false;
                        clauseCheck = true;
                        continue;
                    }
                }
                else {
                    variableValue = Integer.parseInt(s);
                    if (variableValue == 0){
                        for (Literal l : literalList) {
                            tempClause =tempClause.add(l);
                        }

                        if (longestClause < tempClause.size()) longestClause = tempClause.size();

                        f = f.addClause(tempClause);
                        tempClause = new Clause();
                        literalList = new ArrayList<>();
                        formulaList.add(clauseList);
                        clauseList = new ArrayList<>();
                    }
                    else if (variableValue < 0) {
                        clauseList.add(variableValue);
                        literalList.add(NegLiteral.make(s.substring(1)));
                    }
                    else {
                        clauseList.add(variableValue);
                        literalList.add(PosLiteral.make(s));
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("File read failed.");
        }

        // Define placeholder output variables
        int[] tarjanOutput = null;
        HashMap<Variable,Bool> dpllOutput = null;

        // Start to record the time
        System.out.println("SAT solver starts!!!");
        long startTime = System.nanoTime();

        // If it is a 2-SAT problem, solve using Tarjan's algorithm
        // Else, solve using the optimized DPLL algorithm
        if (longestClause <= 2) tarjanOutput = SATSolver.tarjanSolve(formulaList, numOfVariables);
        else dpllOutput = SATSolver.dpllSolve(f);

        long endTime = System.nanoTime();
        long runTime = endTime - startTime;

        // Print time required to console
        System.out.println("Time taken: " + runTime / 1000000.0 + " ms");

        // Print boolean assignment output to console
        if (longestClause <= 2) {
            if (tarjanOutput != null) {
                System.out.println(Arrays.toString(tarjanOutput)); // The output of this is somehow not always correct yet
            }
        }
        else {
            if (dpllOutput != null) {
                try {
                    FileWriter myWriter = new FileWriter("BoolAssignment.txt");
                    String results = dpllOutput.toString();
                    results = results.substring(1, results.length() - 1);
                    String[] arrayResults = results.split(", ");
                    for (String line : arrayResults) {
                        line = line.replace("=", ":");
                        myWriter.write(line + "\n");
                    }
                    myWriter.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
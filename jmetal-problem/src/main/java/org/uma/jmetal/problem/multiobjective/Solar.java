package org.uma.jmetal.problem.multiobjective;



import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.Arrays;
import java.util.List;

public class Solar extends AbstractDoubleProblem{

	
	// Our constant values
    double C_PA = 1005;
    double A = 102.2;
    double H_V = 18000000;

    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree ;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints ;

    // Constructor
    public Solar() {
        setNumberOfVariables(8);
        setNumberOfObjectives(3);
        setNumberOfConstraints(3);
        setName("BioSolar") ;

        List<Double> lowerLimit = Arrays.asList(200.0, 288.0, 284.0, 0.01, 0.5, 0.5, 0.5, 0.0006) ;
        List<Double> upperLimit = Arrays.asList(1000.0, 333.0, 313.0, 0.05, 0.9, 1.0, 1.0, 0.03) ;

        // Setting the bounds
        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);

        overallConstraintViolationDegree = new OverallConstraintViolation<DoubleSolution>() ;
        numberOfViolatedConstraints = new NumberOfViolatedConstraints<DoubleSolution>() ;
    }


    /** Evaluate() method */
    @Override
    public void evaluate(DoubleSolution solution) {
        double [] f = new double[3];
        double is, tda, ta, ma, t, eur, n, mf;
        is = solution.getVariableValue(0);
        tda = solution.getVariableValue(1);
        ta = solution.getVariableValue(2);
        ma = solution.getVariableValue(3);
        t = solution.getVariableValue(4);
        eur = solution.getVariableValue(5);
        n = solution.getVariableValue(6);
        mf  = solution.getVariableValue(7);



        // for solar mode
        double ks = (n * t * A) / (ma * C_PA);
        double delta_ts = tda - ta;

        double num1 = (ma* C_PA * ks * is) - (ta * Math.log(((ks * is + ta)/ta)));
        double den1 = (ma * C_PA * ((delta_ts) * Math.log(eur))) - (ta * (Math.log(((delta_ts) * (Math.log(eur)) + ta) / ta )));

        f[0] = num1 / den1;
        f[1] = ks;
        f[2] = delta_ts;



        solution.setObjective(0, f[0]);
        solution.setObjective(1, f[1]);
        solution.setObjective(2, f[2]);
    }

    /** EvaluateConstraints() method */
    private void evaluateConstraints(DoubleSolution solution)  {
        double[] constraint = new double[this.getNumberOfConstraints()];

        double is, tda, ta, ma, t, eur, n, mf;
        is = solution.getVariableValue(0);
        tda = solution.getVariableValue(1);
        ta = solution.getVariableValue(2);
        ma = solution.getVariableValue(3);
        t = solution.getVariableValue(4);
        eur = solution.getVariableValue(5);
        n = solution.getVariableValue(6);
        mf  = solution.getVariableValue(7);
        
        
        
        constraint[0] = -(0.002 -((n * ta)/(ma * C_PA))); 

        constraint[1] = -(12.69 - (tda - ta) );
            
        
        // for solar mode
        double ks = (n * t * A) / (ma * C_PA);
        double delta_ts = tda - ta;

        double num1 = (ma* C_PA * ks * is) - (ta * Math.log(((ks * is + ta)/ta)));
        double den1 = (ma * C_PA * ((delta_ts) * Math.log(eur))) - (ta * (Math.log(((delta_ts) * (Math.log(eur)) + ta) / ta )));

        double f0 = num1 / den1;
        
        constraint[2] = -(-1 - f0);

        double overallConstraintViolation = 0.0;
        int violatedConstraints = 0;
        for (int i = 0; i < getNumberOfConstraints(); i++) {
            if (constraint[i]<0.0){
                overallConstraintViolation+=constraint[i];
                violatedConstraints++;
            }
        }

        overallConstraintViolationDegree.setAttribute(solution, overallConstraintViolation);
        numberOfViolatedConstraints.setAttribute(solution, violatedConstraints);
    }

}

package org.uma.jmetal.problem.multiobjective;



import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.Arrays;
import java.util.List;

public class Biomass extends AbstractDoubleProblem{

	
	// Our constant values
    double C_PA = 1005;
    double A = 102.2;
    double H_V = 18000000;

    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree ;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints ;

    // Constructor
    public Biomass() {
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



        // for biomass mode

        double cb = (mf * n * H_V * n) / (ma * C_PA);
        double cbe = (tda - ta) / eur;

        double num2 = ma * C_PA - ta * Math.log((ta + cb)/ta);
        double den2 = ma * C_PA * cbe - ta * Math.log((ta + cbe) / ta);


        f[0] = num2 / den2;
        f[1] = cb;
        f[2] = cbe;
        

        solution.setObjective(0, f[0]);
        solution.setObjective(1, f[1]);
        solution.setObjective(2, f[2]);
        
        this.evaluateConstraints(solution);
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
        
        
        
        constraint[0] = -(8.98 -((mf * n * H_V * n)/(ma * C_PA)));
        		

        constraint[1] = -(15.811 -((tda - ta)/eur));
        
        
        double cb = (mf * n * H_V * n) / (ma * C_PA);
        double cbe = (tda - ta) / eur;

        double num2 = ma * C_PA - ta * Math.log((ta + cb)/ta);
        double den2 = ma * C_PA * cbe - ta * Math.log((ta + cbe) / ta);


        double f0 = num2 / den2;
        
        
        constraint[2] = -(1 - f0);

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


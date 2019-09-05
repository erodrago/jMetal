package org.uma.jmetal.problem.multiobjective;



import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.Arrays;
import java.util.List;

public class BioSolar extends AbstractDoubleProblem{

	
	// Our constant values
    double C_PA = 1005;
    double A = 102.2;
    double H_V = 18000000;

    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree ;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints ;

    // Constructor
    public BioSolar() {
        setNumberOfVariables(8);
        setNumberOfObjectives(3);
        setNumberOfConstraints(4);
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





        // for solar biomass mode

        double k_sb = (t * A * n) / (ma * C_PA);
	    
	double c_sb = ((mf * n * H_V * n)/(ma * C_PA) + ta)

        double c_es_b = (tda - ta) / eur;

        double num3 = ma * C_PA * (k_sb * is + c_sb * ta) - ta * Math.log(((k_sb * is + c_sb) + ta) / ta);

        double den3 = ma * C_PA * c_es_b - ta * Math.log((ta + c_es_b)/ta);


        f[0] = num3 / den3;
        f[1] = k_sb;
        f[2] = c_es_b;

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
        
        
        
        constraint[0] = -(0.0318 - ((n*ta)/(ma*C_PA)) );

        constraint[1] = -(315 - ((mf*n*H_V*n)/(ma*C_PA)+ta) );
        
        constraint[2] = -(9.77 -((tda - ta)/eur) );
        
        
        // for solar biomass mode

        double k_sb = (t * A * n) / (ma * C_PA);

        double c_es_b = (tda - ta) / eur;

	double c_sb = ((mf * n * H_V * n)/(ma * C_PA) + ta)
        double num3 = ma * C_PA * (k_sb * is + c_sb * ta) - ta * Math.log(((k_sb * is + c_sb) + ta) / ta);

        double den3 = ma * C_PA * c_es_b - ta * Math.log((ta + c_es_b)/ta);


        double f0 = num3 / den3;
        
        constraint[3] = -(1 - f0);

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

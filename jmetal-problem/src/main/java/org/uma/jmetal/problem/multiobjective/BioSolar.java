package jmetal.problems;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.util.JMException;


/**
 * Class representing the biosolar problem
 * */
public class BioSolar extends Problem{

	
	// lower and upper limit
//	public static final Double [] LOWERLIMIT = {};
//	public static final Double [] UPPERLIMIT = {};
//	
	double C_PA = 1005;
	double A = 102.2;
	double H_V = 18000000;
	
	
	/**
	 * Constructor
	 * Creates a default instance of the Biosolar problem*/
	
	public BioSolar() {
		numberOfVariables_ = 8;
		numberOfObjectives_ = 3;
		
		problemName_ = "BioSolar";
		
		lowerLimit_ = new double[numberOfVariables_];
		upperLimit_ = new double[numberOfVariables_];
		
		//setting the limits
		lowerLimit_[0] = 200;
		lowerLimit_[1] = 288;
		lowerLimit_[2] = 284;
		lowerLimit_[3] = 0.01;
		lowerLimit_[4] = 0.5;
		lowerLimit_[5] = 0.5;
		lowerLimit_[6] = 0.5;
		lowerLimit_[7] = 0.0006;
		
		
		upperLimit_[0] = 1000;
		upperLimit_[1] = 333;
		upperLimit_[2] = 313;
		upperLimit_[3] = 0.05;
		upperLimit_[4] = 0.9;
		upperLimit_[5] = 1;
		upperLimit_[6] = 1;
		upperLimit_[7] = 0.03;
	
	}
	
		  
	@Override
	public void evaluate(Solution solution) throws JMException {
		// TODO Auto-generated method stub
		
		Variable[] mydecisionVariables = solution.getDecisionVariables();
		
		double [] f = new double[numberOfObjectives_]; 
		double is, tda, ta, ma, t, eur, n, mf;
		
		is = mydecisionVariables[0].getValue();
		tda = mydecisionVariables[1].getValue();
		ta = mydecisionVariables[2].getValue();
		ma = mydecisionVariables[3].getValue();
		t = mydecisionVariables[4].getValue();
		eur = mydecisionVariables[5].getValue();
		n = mydecisionVariables[6].getValue();
		mf = mydecisionVariables[7].getValue();
		
		
		// for solar mode
		double ks = (n * t * A) / (ma * C_PA);
		double delta_ts = tda - ta;
		
		double num1 = ma* C_PA * ks - ta * Math.log((ks * is + ta)/ta);
		double den1 = ma * C_PA * (delta_ts * Math.log(eur)) - ta * Math.log((delta_ts * Math.log(eur) + ta) / ta );
		
		f[0] = num1 / den1;
		
		// for biomass mode
		
		double cb = (mf * n * H_V * n) / (ma * C_PA); 
		double cbe = (tda - ta) / eur;
		
		double num2 = ma * C_PA - ta * Math.log((ta + cb)/ta); 
		double den2 = ma * C_PA * cbe - ta * Math.log((ta + cbe) / ta);
		
		
		f[1] = num2 / den2;
		
		// for solar biomass mode
		
		double k_sb = (t * A * n) / (ma * C_PA);
		
		double c_es_b = (tda - ta) / eur;
		
		double num3 = ma * C_PA * (k_sb * is + c_es_b * ta) - ta * Math.log((k_sb * is + c_es_b) / ta);
		
		double den3 = ma * C_PA * c_es_b - ta * Math.log((ta + c_es_b)/ta);
		
		
		f[2] = num3 / den3;
		
		solution.setObjective(0, f[0]);
		solution.setObjective(1, f[1]);
		solution.setObjective(2, f[2]);
	}

}

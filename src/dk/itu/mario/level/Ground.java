/*
 * Author: Ori Popowski
 */



package dk.itu.mario.level;

import dk.itu.mario.utils.Utils;

public class Ground
{
    static int    gensize = 1000;    /* generation size */
	static double prob_cross = 0.91; /* probability for crossover */
	static double prob_mutat = 0.0;  /* probability for mutation */
	static int    gens = 8000;       /* num. of generations */
	static int    jump = 4;          /* height of Mario's jump */
	
	private int width;
	private int height;
	
	public Ground(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	/* Generate a series of heights using evolutionary algorithm */
    public int[] ground()
    {
    	int[][] gen;      /* current generation */
    	int[][] sel;      /* the selected individuals for breeding a new generation */
    	int[] best;       /* best individual in all generations */
    	double[] fitness; /* fitness for generation */
    	Mutator m;
    	Selector s;
    	Fitness f;
    	Crosser c;
    	
    	gen = new int[gensize][width];
    	fitness = new double[gensize];
    	best = new int[width];
    	
    	/* generate first generation */
    	for (int i = 0; i < gen.length; ++i)
    		for (int j = 0; j < gen[0].length; ++j)
    			gen[i][j] = Utils.randInt(0, height-2);
    	
    	best = gen[0];
    	f = new Fitness();
    	for (int k = 1; k < gens+1; ++k) {
    		/* calculate fitness */
    		for (int i = 0; i < fitness.length; ++i)
    			fitness[i] = fitness(gen[i]);
    		best = f.best(fitness, gen, fitness(best), best);
    		
    		/******************DEBUG******************/
    		if (k % 1000 == 0 || k == 1) {
	    		double avg = 0;
	    		for (int i = 0; i < fitness.length; ++i)
	    			avg += fitness[i];
	    		System.out.println(
	    			"GENRATION #" + k + "\t: Avg. fitness:\t"+ avg/fitness.length +
	    			" Best fitness: " + fitness(best)
	    		);
    		}
    		/*****************^DEBUG^*****************/
    		
    		s = new Selector();
    		/* select best fitting */
    		sel = s.select(gen, fitness);

    		c = new Crosser();
    		/* breed best fitting into a new generation */
    		int[][] sons = new int[2][width];
    		for (int i = 0; i < gen.length;) {
    			sons = c.crossoverRepair(sel[i], sel[i+1], prob_cross, height);
    			gen[i++] = sons[0];
    			gen[i++] = sons[1];
    		}
    		
    		m = new Mutator();
    		/* mutate new generation */
    		if (prob_mutat > 0)
    			m.mutate(gen, prob_mutat, 0, height-1);
    	}
    	return best;
    }
    
    /* Return fitness for a given heights series */
    private double fitness(int[] heights)
    {
    	double[] diffs;     /* height differences between elements */ 
    	double fitness;
    	double x;
    	
    	diffs = new double[heights.length-1];
    	
    	/* build array of height differences */
    	for (int i = 0; i < heights.length-1; ++i)
    		diffs[i] = Math.abs(heights[i] - heights[i+1]);
    	
    	fitness = 0;
    	/* compute fitness using height diff's array */
    	for (int i = 0; i < heights.length-1; ++i) {
    		x = diffs[i];
    		fitness += (x > jump) ? (1 - x/(height-1)) : 1;
    	}
    		
    	fitness /= heights.length-1;
    	
    	return fitness;
    }
}

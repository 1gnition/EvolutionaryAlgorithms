/*
 * Author: Ori Popowski
 */



package dk.itu.mario.level;

import dk.itu.mario.utils.Utils;

public class Platforms 
{
    static int     gensize = 1000;    /* generation size */
	static double  prob_cross = 0.7;  /* probability for crossover */
	static double  prob_mutat = 0.0;  /* probability for mutation */
	static int     gens = 1600;       /* num. of generations */
	static double  P = 1.0/7.0;       /* desired percent of platforms */
	static double  H = 3.0;           /* desired height of platforms */
	static double  C = 4.0;           /* desired clusters size */
	
	private int width;
	private int height;
	private int[] ground;
	
	public Platforms(int width, int height, int[] ground)
	{
		this.width = width;
		this.height = height;
		this.ground = ground;
	}
    
    public int[] platforms()
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
				gen[i][j] = Utils.randInt(0, height-1 - ground[j]); /* height above GROUND! */
    	
    	f = new Fitness();
    	best = gen[0];
    	for (int k = 1; k < gens+1; ++k) {
    		/* calculate fitness */
    		for (int i = 0; i < fitness.length; ++i)
    			fitness[i] = fitness(gen[i], k);
    		best = f.best(fitness, gen, fitness(best, k), best);
    		
    		/******************DEBUG******************/
    		if (k  == 1 || k % 100 == 0) {
	    		double avg = 0;
	    		for (int i = 0; i < fitness.length; ++i)
	    			avg += fitness[i];
	    		System.out.println(
	    			"GENRATION #" + k + "\t: Avg. fitness:\t"+ avg/fitness.length
	    		);
	    		double avg1, avg2, avg3;
	    		avg1 = avg2 = avg3 = 0;
	    		for (int i = 0; i < gen.length; ++i)
	    			avg1 += fitnessPercent(gen[i]);
	    		avg1 = avg1/gen.length;
	    		for (int i = 0; i < gen.length; ++i)
	    			avg2 += fitnessHeight(gen[i]);
	    		avg2 = avg2/gen.length;
	    		for (int i = 0; i < gen.length; ++i)
	    			avg3 += fitnessCultering(gen[i]);
	    		avg3 = avg3/gen.length;
	    		System.out.println(
		    			"GENRATION #" + k + "\t: tot. fitness:\t"+ (avg1+avg2+avg3)/3
		    	);
	    		double b = (fitnessPercent(best) + fitnessCultering(best) + fitnessHeight(best))/3;
	    		System.out.println(
		    			"GENRATION #" + k + "\t: best fitness:\t"+ b
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
    			if (k < 1400)
    				sons = c.crossoverInterleave(sel[i], sel[i+1], prob_cross);
    			else
    				sons = c.crossover(sel[i], sel[i+1], prob_cross);
    			gen[i++] = sons[0];
    			gen[i++] = sons[1];
    		}
    		m = new Mutator();
    		/* mutate new generation */
    		if (prob_mutat > 0) {
    			int[] max = new int[width];
    			for (int i = 0; i < width; ++i)
    				max[i] = height-1-ground[i];
    			m.mutate(gen, prob_mutat, new int[width], max);
    		}
    	}
    	return best;
    }
    
    private double fitness(int[] platforms, int k)
    {
    	if (k < 1000)
    		return fitnessPercent(platforms);
    	else if (k < 1400)
    		return fitnessHeight(platforms);
    	else
    		return fitnessCultering(platforms);
    }
    
    /* Percentage of platform blocks */
    private double fitnessPercent(int[] platforms)
    {
    	double b;
    	
    	b = 0.0;
    	for (int i = 0; i < platforms.length; ++i)
    		b += (platforms[i] > 0) ? 1 : 0;
    	
    	b /= platforms.length;
    	
    	if (b > P)
    		return (1.0 - (b - P)/(1.0 - P));
    	else
    		return (1.0 - (P - b)/P);
    }
    
    private double fitnessHeight(int[] platforms)
    {
    	double count = 0.0;
    	double b = 0.0;
    	for (int i = 0; i < platforms.length; ++i)
    		b += (platforms[i] > 0) ? 1 : 0;
    	
    	for (int i = 0; i < platforms.length; ++i)
    		if (platforms[i] == H)
    			++count;
    	
    	return count/b;
    }
    
    /* Clustering */
    private double fitnessCultering(int[] platforms)
    {
    	int clusters = 0;    /* total num. of cluster */
    	double bad = 0.0;    /* num. of bad clusters */
    	double curr = 0.0;   /* num. of blocks in current cluster */
    	boolean in = false;  /* are we in a cluster right now? */
    	
    	for (int i = 0; i < platforms.length; ++i)
    		if (platforms[i] > 0) {
    			if (in)
    				++curr;
    			else {
    				in = true;
    				++clusters;
    				curr = 1;
    			}
    		}
    		else {
    			if (in) {
    				in = false;
    				if (curr < C)
    					++bad;
    			}
    		}
    	return 1 - bad/clusters;
    }
}

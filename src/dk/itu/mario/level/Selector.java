/*
 * Author: Ori Popowski
 */



package dk.itu.mario.level;

import dk.itu.mario.utils.Utils;

public class Selector {
    /* Return the next generation using fitness proportioned selection */
    public int[][] select(int[][] gen, double[] fitness)
    {
    	double sum;
    	double rand;
    	int matched;
    	int[][] selection;
    	double[] interval;
    	
    	selection = new int[gen.length][gen[0].length];
    	interval = new double[gen.length];
    	
    	/* make an interval [0, sum] so that every
    	 * subinterval [interval[i-1], interval[i]] matches to
    	 * the fitness of the i'th individual */
    	interval[0] = fitness[0];
    	for (int i = 1; i < gen.length; ++i)
    		interval[i] = interval[i-1] + fitness[i];
    	sum = interval[interval.length-1];
    	
    	
    	for (int i = 0; i < gen.length; ++i) {
    		/* choose a random value in the interval */
    		rand = Utils.randDouble(0.0, sum);
    		/* find in what subinterval it falls */
    		matched = match(rand, interval);
    		/* choose the matching series */
    		System.arraycopy(gen[matched], 0, selection[i], 0, selection[i].length);
    	}
    	return selection;
    }
    
    /* Take a number `rand' in the interval and return the
    the first index `i' whose following element is
    greater than `rand' */
	 private int match(double rand, double[] interval)
	 {
	 	int i = 0;
	 	while (rand > interval[i])
	 		++i;
	 	return i;
	 }
}

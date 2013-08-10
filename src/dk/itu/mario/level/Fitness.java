/*
 * Author: Ori Popowski
 */



package dk.itu.mario.level;

public class Fitness {
    public int[] best(double[] fitness, int[][] gen, double max, int[] best)
    {
    	int j;
    	
    	j = -1;
    	for (int i = 0; i < gen.length; ++i)
    		if (fitness[i] > max) {
    			max = fitness[i];
    			j = i;
    		}
            
    	if (j > 0)
    		return gen[j];
    	else
    		return best;
    }
}

/*
 * Author: Ori Popowski
 */



package dk.itu.mario.level;

import dk.itu.mario.utils.Utils;

public class Mutator {
	public void mutate(int[][] gen, double prob, int min, int max)
    {
    	for (int i = 0; i < gen.length; ++i)
    		for (int j = 0; j < gen[0].length; ++j)
    			if (Utils.randDouble(0.0, 1.0) <= prob)
    				gen[i][j] = Utils.randInt(min, max);
    }
	public void mutate(int[][] gen, double prob, int[] min, int[] max)
    {
    	for (int i = 0; i < gen.length; ++i)
    		for (int j = 0; j < gen[0].length; ++j)
    			if (Utils.randDouble(0.0, 1.0) <= prob)
    				gen[i][j] = Utils.randInt(min[j], max[j]);
    }
}

/*
 * Author: Ori Popowski
 */



package dk.itu.mario.level;

import dk.itu.mario.utils.Utils;

public class Crosser {
	
    /* Return two sons with probability `prob' for a crossover */
    public int[][] crossover(int[] mom, int[] dad, double prob)
    {
    	int[][] sons;
    	int i;
    	
        sons = new int[2][mom.length];

		System.arraycopy(mom, 0, sons[0], 0, sons[0].length);
		System.arraycopy(dad, 0, sons[1], 0, sons[1].length);
		
		if (Utils.randDouble(0.0, 1.0) <= prob) {
			i = Utils.randInt(1, sons[0].length-2);
    		swap(sons, i);
		}
    	return sons;
    }
    
    
	/* Return two sons with probability `prob' for a crossover.
	   Repair stitch point by leveling left and right portions */
	public int[][] crossoverRepair(int[] mom, int[] dad, double prob, int height)
	{
		int[][] sons;
		int i;

		sons = new int[2][mom.length];

		System.arraycopy(mom, 0, sons[0], 0, sons[0].length);
		System.arraycopy(dad, 0, sons[1], 0, sons[1].length);

		if (Utils.randDouble(0.0, 1.0) <= prob) {
			i = Utils.randInt(1, sons[0].length-2);
			swapRepair(sons, i, height);
		}
		return sons;
	}
	
    /* Return two sons with probability `prob' for a crossover */
    public int[][] crossoverInterleave(int[] mom, int[] dad, double prob)
    {
    	int[][] sons;
    	
        sons = new int[2][mom.length];

		System.arraycopy(mom, 0, sons[0], 0, sons[0].length);
		System.arraycopy(dad, 0, sons[1], 0, sons[1].length);
		
		if (Utils.randDouble(0.0, 1.0) <= prob)
    		swapInterleave(sons, 0);
    	return sons;
    }
    
    /* Swap sons starting at index `j' */
    private void swap(int[][] sons, int j)
    { 	
    	int tmp;
    	for (int i = j; i < sons[0].length; ++i) {
			tmp = sons[0][i];
			sons[0][i] = sons[1][i];
			sons[1][i] = tmp;
    	}
    }

	/* Swap sons starting at index `j' and return the result */
	private void swapRepair(int[][] sons, int j, int height)
	{ 	
		int tmp;
		for (int i = j; i < sons[0].length; ++i) {
			tmp = sons[0][i];
			sons[0][i] = sons[1][i];
			sons[1][i] = tmp;
		}
		repair(sons[0], j, height);
		repair(sons[1], j, height);
	}

	/* Repair a crossed over array so that the stitch point
    will have a minimal heights difference, by lowering/raising
    the left/right portion of the array */
	private void repair(int[] arr, int j, int height)
	{
		int diff;
		int up, down;
		boolean neg;

		neg = false;
		diff = arr[j] - arr[j-1];
		if (diff < 0) {
			neg = true;
			diff *= -1;
			Utils.reverse(arr);
			j = arr.length - j;
		}

		up = distanceToTop(arr, 0, j, height);
		down = distanceToBottom(arr, j, arr.length, height);

		up = Utils.min(diff, up);
		diff -= up;
		down = Utils.min(diff, down);

		for (int i = 0; i < arr.length; ++i)
			if (i < j)
				arr[i] += up;
			else
				arr[i] -= down;

		if (neg)
			Utils.reverse(arr);
	}
    
    private void swapInterleave(int[][] sons, int j)
    { 	
    	int tmp;
    	int k;
    	for (int i = 0; i < sons[0].length; ++i) {
    		k = Utils.randInt(i, sons[0].length-1);
			tmp = sons[0][k];
			sons[0][k] = sons[1][i];
			sons[1][i] = tmp;
    	}
    }

	/* Take portion of `arr' and return to minimum
 	   distance to the top of the screen */
	private int distanceToTop(int[] arr, int beg, int end, int height)
	{
		return height-2 - Utils.max(arr, beg, end);
	}

	/* Take portion of `arr' and return to minimum
       distance to the bottom of the screen */
	private int distanceToBottom(int[] arr, int beg, int end, int height)
	{
		return Utils.min(arr, beg, end, height-1);
	}
}

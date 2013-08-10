/*
 * Author: Ori Popowski
 */
package dk.itu.mario.utils;

import java.util.ArrayList;
import java.util.Random;

public class Utils {
	public static int min(int a, int b)
    {
    	return (a < b) ? a : b;
    }
    
	public static int max(int a, int b)
    {
    	return (a > b) ? a : b;
    }
	
    public static int max(int[] arr, int beg, int end)
    {
    	if (beg >= end)
    		System.err.println("error: max");
    	
    	int max = 0;
    	for (; beg < end; ++beg)
    		max = max(arr[beg], max);
    	return max;
    }
    
    public static int min(int[] arr, int beg, int end, int max)
    {
    	if (beg >= end)
    		System.err.println("error: min");
    	
    	int min = max;
    	for (; beg < end; ++beg)
    		min = min(arr[beg], min);
    	return min;
    }
    
    /* Return random int between min and max */
    public static int randInt(int min, int max)
    {
    	Random r = new Random();
    	return (int) (min + (max - min + 1) * r.nextDouble());  
    }
    
    /* Return random double between min and max */
    public static double randDouble(double min, double max)
    {
    	Random r = new Random();
    	return min + (max - min) * r.nextDouble();  
    }
    
  
    /* Reverse array `arr' */
    public static void reverse(int[] arr)
    {
        int len = arr.length;
        
    	for (int i = 0; i < len/2; i++) {
            int tmp = arr[i];
            arr[i] = arr[len-i-1];
            arr[len-i-1] = tmp;
        }
    }
    
    public static class UniqueInt {
        private ArrayList<Integer> a;
        
        public UniqueInt(int min, int max) {
            a = new ArrayList<Integer>(max-min+1);
            for (; min < max+1; ++min)
                a.add(min);
            java.util.Collections.shuffle(a);
        }
        
        public int nextInt() throws IndexOutOfBoundsException {
            return a.remove(0).intValue();
        }
    }
}

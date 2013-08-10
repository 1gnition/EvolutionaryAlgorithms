/*
 * Author: Ori Popowski
 */



package dk.itu.mario.level;

import dk.itu.mario.MarioInterface.GamePlay;
import dk.itu.mario.MarioInterface.LevelInterface;
import dk.itu.mario.engine.sprites.SpriteTemplate;
import dk.itu.mario.utils.Utils;

public class CustomizedLevel extends Level implements LevelInterface {

	static double bonus =   1.0/2.0;
	static double powerup = 2.0/8.0;
	static double coin =    5.0/8.0;
	static double enemy =   1.0/50.0;
	

    public CustomizedLevel(int width, int height, long seed,
                           int difficulty, int type, GamePlay playerMetrics) {
        super(width, height);
        Ground g = new Ground(width, height);
//        int[] ground = g.ground();
//        for (int i = 0; i < ground.length; ++i)
//        	System.out.print(ground[i] + ", ");
        int[] ground = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 3, 3, 3, 4, 4, 3, 3, 2, 2, 3, 3, 3, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 5, 5, 5, 5, 5, 8, 8, 8, 8, 8, 6, 6, 6, 9, 8, 9, 9, 11, 11, 7, 7, 10, 9, 9, 7, 9, 8, 8, 8, 6, 8, 8, 8, 6, 9, 6, 6, 6, 8, 8, 8, 8, 9, 9, 8, 7, 7, 7, 7, 8, 8, 8, 8, 4, 4, 4, 7, 7, 7, 7, 7, 7, 7, 9, 9, 7, 7, 7, 7, 7, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 4, 4, 4, 4, 2, 2, 2, 2, 2, 2, 2, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};
        Platforms p = new Platforms(width, height, ground);
        buildGround(ground);
        buildPlatforms(p.platforms(), ground);
//        putEnemies(ground);
    	xExit = 320;
    	yExit = invert(ground[height-1]);
    }
    
    /* for testing purposes only */
    public int[] manualGround()
    {
    	int[] a = new int[width];
//    	for (int i = 0; i < width; ++i)
//    		a[i] = randInt(0, 7);
//    	
//    	a[9] = a[10] = a[11] = 4;
//    	a[11] = 9;
//    	a[12] = a[13] = 6;
//    	return a;
    	for (int i = 0; i < width; ++i)
    		setBlock(i, height-1, Level.HILL_TOP);
    	return a;
    }
   
    
    /* Take a series of heights and covert it into a level */
    public void buildGround(int[] h)
    {
    	int i, j;
        
        /* put only hill top, fill later */
    	for (i = 0; i < h.length; ++i)
    		setBlock(i, invert(h[i]), Level.HILL_TOP);
    	
    	/* create side walls of stairs */
    	for (i = 0; i < h.length-1; ++i) {
    		if (h[i] - h[i+1] > 0) {
    			setBlock(i, invert(h[i]), map(2,8));
    			for (j = h[i+1]+1; j < h[i]; ++j)
    				setBlock(i, invert(j), map(2,9));
    			setBlock(i, invert(h[i+1]), map(3,9));
    		}
    		if (h[i+1] - h[i] > 0) {
    			setBlock(i+1, invert(h[i+1]), map(0,8));
    			for (j = h[i]+1; j < h[i+1]; ++j)
    				setBlock(i+1, invert(j), map(0,9));
    			setBlock(i+1, invert(h[i]), map(3,8));
    		}
    		/* if we have a one-block wide stair */
    		if (0 < i && i < width-1
    	    &&  h[i] > h[i-1]
    	    &&  h[i] > h[i+1]) {
    			/* put stones */
    			int max = Utils.max(h[i-1], h[i+1]);
    			for (j = max+1; j <= h[i]; ++j)
    				setBlock(i, invert(j), map(9,0));
    			if (h[i-1] == h[i+1])
    				setBlock(i, invert(max), Level.HILL_TOP);
    			else if (h[i-1] <  h[i+1])
    				setBlock(i, invert(max), map(0,8));
    			else
    				setBlock(i, invert(max), map(2,8));
    		}
    	}
    	
    	/* fill with land under the surface */
    	for (i = 0; i < width; ++i)
    		for (j = height-1; getBlock(i, j) == 0; --j)
    			setBlock(i, j, map(1,9));
    }
    
    private void buildPlatforms(int[] p, int[] g)
    {
    	double x;
    	for (int i = 0; i < p.length-1; ++i) {
    		if (p[i] > 0) {
    			x = Utils.randDouble(0.0, 1.0);
				if (x <= powerup)
					setBlock(i, invert(p[i]+g[i]), Level.BLOCK_POWERUP);
    			else if (x <= coin)
    				setBlock(i, invert(p[i]+g[i]), Level.BLOCK_COIN);
    			else
    				setBlock(i, invert(p[i]+g[i]), Level.BLOCK_EMPTY);
    		}
    	}
    }
    
    private void putEnemies(int[] g)
    {
    	double x;
    	for (int i = 0; i < g.length-1; ++i) {
    		x = Utils.randDouble(0.0, 1.0);
    		if (x < enemy)
    			setSpriteTemplate(i, invert(g[i])-1, new SpriteTemplate(
                        SpriteTemplate.GREEN_TURTLE, false));
    	}
    }
    
    private byte map(int x, int y)
    {
    	return (byte) (x + 16*y);
    }
    
    private int invert(int height)
    {
    	return this.height-1 - height; 
    }
}

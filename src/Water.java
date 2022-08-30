/**
 * Class to model the water on the terrain
 */
public class Water {

    /**
     * Water 2D array to store integer depths
     */
    static int [][] waterDepths; // regular grid of waterValues
    /**
     * Constant for water depths
     */
    static final int waterDepthPerClick=3;


    /**
     * Initialise the terrain array with values of zero
     * @param terrain the dimensions of terrain stored in object
     */
    static void readWaterArray(Terrain terrain){            //initialising water array
        waterDepths= new int[terrain.dimx][terrain.dimy];
        for (int i = 0; i < terrain.dimx ; i++) {
            for (int j = 0; j < terrain.dimy ; j++) {
                waterDepths[i][j]=0;
            }
        }
    }


    /**
     * Thread safe method to increment water by a single unit
     * @param x x coordinate of water location
     * @param y y coordinate of water location
     */
    static synchronized void increaseWaterDepth(int x, int y){
        waterDepths[x][y]=waterDepths[x][y]+1;
    }

    /**
     * Thread safe method to decrement water by a single unit
     * @param x x coordinate of water location
     * @param y y coordinate of water location
     */
    static synchronized void decreaseWaterDepth(int x, int y){
        waterDepths[x][y]=waterDepths[x][y]-1;
    }

    /**
     * Method to get current surface for comparison
     * @param x the surfaces x position
     * @param y the surfaces y position
     * @return current surface representing water and terrain height
     */
    static synchronized float getSurface(int x, int y){
        return (float) (((waterDepths[x][y])*0.01)+Terrain.height[x][y]);

    }

}

import java.util.concurrent.RecursiveAction;

/**
 * Subclass of recursive action inherits methods
 */
public class Parallel extends RecursiveAction {


    float[][] height;
    int[][] water;
    int SEQUENTIAL_CUTOFF=10000;
    int lo;
    int hi;
    Terrain land;

    /**
     *
     * @param land land object
     * @param height The 2D grid modelling the terrain
     * @param water The 2D grid modelling the water
     * @param lo The lower bound for splitting tasks
     * @param hi The upper bound for splitting tasks
     */
    public Parallel(Terrain land,float[][] height, int[][] water, int lo, int hi) {
        this.land= land;
        this.height = height;
        this.water = water;
        this.lo = lo;
        this.hi = hi;
    }

    @Override
    /**
     * Overidden compute method splits the tasks
     */
    protected void compute() {
        if (hi-lo<SEQUENTIAL_CUTOFF){
            computeDirectly();
        }else{
            int middle= (lo+hi)/2;
            Parallel subTask1 = new Parallel(land,height,water,lo,middle);
            Parallel subTask2 = new Parallel(land,height,water,middle,hi);
            invokeAll(subTask1,subTask2);//Programs runs as intended using invoke all or fork,compute and join

	    //subTask1.fork();
            //subTask2.compute();
            //subTask1.join();

        }

    }


    /**
     * Sequential algorithm for forkjoin
     */
    protected void computeDirectly(){
        //code here
        int [] coordinates = new int[2];
        int [] lowCoordinates = new int[2];
        synchronized (water){
            for (int pIndex =lo; pIndex < hi ; pIndex++) {
                land.locate(pIndex,coordinates);
                land.getPermute(pIndex,coordinates);
                float currentSurface = Water.getSurface(coordinates[0], coordinates[1]);

                if ((coordinates[0]>0) && (coordinates[0]<land.dimx-1) && (coordinates[1]>0) && (coordinates[1]<land.dimy-1) && Water.waterDepths[coordinates[0]][coordinates[1]]>0){

                    float currentLow= currentSurface;
                    lowCoordinates[0]=coordinates[0];
                    lowCoordinates[1]=coordinates[1];

                    for (int x = (coordinates[0]-1); x <(coordinates[0]+2) ; x++) {
                        if((x>0) && (x<land.dimx-1)){
                            for (int y = (coordinates[1]-1); y <(coordinates[1]+2) ; y++) {
                                if ((y>0) && (y<land.dimy-1)){ //checks if boundary
                                    if(Water.getSurface(x,y)<currentLow){
                                        currentLow=Water.getSurface(x,y);
                                        lowCoordinates[0]=x;
                                        lowCoordinates[1]=y;
                                    }
                                }
                            }

                        }
                    }
                    FlowPanel.moveWater(land,lowCoordinates[0],lowCoordinates[1],coordinates[0], coordinates[1]);
                }
            }
        }

    }

        }









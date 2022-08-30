import java.io.File;
import java.awt.image.*;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;

public class Terrain {

	/**
	 * Float height array
	 */
	public static float [][] height; // regular grid of height values made static public
	/**
	 * dimensions of grid
	 */
	int dimx, dimy; // data dimensions
	/**
	 * image for landscape
	 */
	BufferedImage img; // greyscale image for displaying the terrain top-down
	/**
	 * maximum height value
	 */
	float maxh = -10000.0f;
	/**
	 * minimum height value
	 */
	float minh = 10000.0f;

	/**
	 * Arraylist of permuted points
	 */
	ArrayList<Integer> permute;	// permuted list of integers in range [0, dimx*dimy)
	
	// overall number of elements in the height grid

	/**
	 * overall number of elements in the height grid
	 * @return dimensions
	 */
	int dim(){
		return dimx*dimy;
	}
	
	// get x-dimensions (number of columns)

	/**
	 * gets dimension of x
	 * @return dimesions of x
	 */
	int getDimX(){
		return dimx;
	}
	
	// get y-dimensions (number of rows)

	/**
	 * gets dimension of y
	 * @return
	 */
	int getDimY(){
		return dimy;
	}
	
	// get greyscale image

	/**
	 * gets the landscape image
	 * @return grayscale image
	 */
	public BufferedImage getImage() {
		  return img;
	}
	
	// convert linear position into 2D location in grid

	/**
	 * convert linear position into 2D location in grid
	 * @param pos linear position
	 * @param ind loads null array with 2D indexes
	 */
	void locate(int pos, int [] ind)
	{
		ind[0] = (int) pos / dimy; // x
		ind[1] = pos % dimy; // y	
	}
	
	// convert height values to greyscale colour and populate an image

	/**
	 * convert height values to greyscale colour and populate an image
	 */
	void deriveImage()
	{
		img = new BufferedImage(dimx, dimy, BufferedImage.TYPE_INT_ARGB);

		
		// determine range of heights
		for(int x=0; x < dimx; x++)
			for(int y=0; y < dimy; y++) {
				float h = height[x][y];
				if(h > maxh)
					maxh = h;
				if(h < minh)
					minh = h;
			}
		
		for(int x=0; x < dimx; x++)
			for(int y=0; y < dimy; y++) {
				 // find normalized height value in range
				 float val = (height[x][y] - minh) / (maxh - minh);
				 Color col = new Color(val, val, val, 1.0f);
				 img.setRGB(x, y, col.getRGB());
			}
	}
	
	// generate a permuted list of linear index positions to allow a random
	// traversal over the terrain

	/**
	 *  generate a permuted list of linear index positions to allow a random
	 * 	traversal over the terrain
	 */
	void genPermute() {
		permute = new ArrayList<Integer>();
		for(int idx = 0; idx < dim(); idx++)
			permute.add(idx);
		java.util.Collections.shuffle(permute);
	}
	
	// find permuted 2D location from a linear index in the
	// range [0, dimx*dimy)

	/**
	 * find permuted 2D location from a linear index in the
	 * @param i index
	 * @param loc location
	 */
	void getPermute(int i, int [] loc) {
		locate(permute.get(i), loc);
	}
	
	// read in terrain from file

	/**
	 * read in terrain from file
	 * @param fileName input file received as arg
	 */
	void readData(String fileName){ 
		try{ 
			Scanner sc = new Scanner(new File(fileName));
			sc.useLocale(Locale.US);
			
			// read grid dimensions
			// x and y correpond to columns and rows, respectively.
			// Using image coordinate system where top left is (0, 0).
			dimy = sc.nextInt(); 
			dimx = sc.nextInt();
			
			// populate height grid
			height = new float[dimx][dimy];
			
			for(int y = 0; y < dimy; y++){
				for(int x = 0; x < dimx; x++)	
					height[x][y] = sc.nextFloat();
				}
				
			sc.close(); 
			
			// create randomly permuted list of indices for traversal 
			genPermute(); 
			
			// generate greyscale heightfield image
			deriveImage();
		} 
		catch (IOException e){ 
			System.out.println("Unable to open input file "+fileName);
			e.printStackTrace();
		}
		catch (java.util.InputMismatchException e){ 
			System.out.println("Malformed input file "+fileName);
			e.printStackTrace();
		}
	}
}

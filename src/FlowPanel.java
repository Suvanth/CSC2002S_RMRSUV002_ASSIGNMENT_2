import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;
import java.util.concurrent.ForkJoinPool;

/**
 * Flow Panel class extends jpanel and implements runnable
 */
public class FlowPanel extends JPanel implements Runnable {
	/**
	 * land obj
	 */
	Terrain land;
	/**
	 * graphics var
	 */
	public static Graphics graphics;
	/**
	 * Simulation step label
	 */
	JLabel label = new JLabel();
	/**
	 * flag for changing sim state
	 */
	volatile boolean boolPause = true;

	/**
	 * atomic time step counter
	 */
	public AtomicInteger atomicCounter = new AtomicInteger(0);


	/**
	 * Constructor
	 * @param terrain initialise terrain object
	 */
	FlowPanel(Terrain terrain) {
		land = terrain;

	}

	/**
	 * Alters boolean flag
	 */
	public void startSim() {
		boolPause = false;
	}

	/**
	 * Alters boolean flag
	 */
	public void pauseSim() {
		boolPause = true;
	}








	// responsible for painting the terrain and water
	// as images
	@Override
	/**
	 * responsible for painting the terrain and water as images
	 */
	protected void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();

		super.paintComponent(g);

		// draw the landscape in greyscale as an image
		if (land.getImage() != null) {
			g.drawImage(land.getImage(), 0, 0, null);
		}

		graphics = getGraphics();
		graphics.setColor(Color.BLUE); //painting of squares


	}

	/**
	 * Paint water drops on image
	 * @param x x pos
	 * @param y y pos
	 */
	public void paintPixel(int x, int y) {
		Color waterCol = new Color(0,0,153);
		for (int i = x; i < x + 6; i++) {
			for (int j = y; j < y + 6; j++) {
				if ((i>0) && (i<land.dimx-1) && (j>0) && (j< land.dimy-1)){// change to i and j
					land.img.setRGB(i, j, waterCol.getRGB());
					Water.waterDepths[i][j] = Water.waterDepths[i][j] + Water.waterDepthPerClick;
				}
			}
		}
		repaint();
	}

	/**
	 * Method to move water on terrain image
	 * @param land land obj
	 * @param xnew new water pos x
	 * @param ynew new water pos y
	 * @param xold old water pos x
	 * @param yold old water pos y
	 */
	public static synchronized void moveWater(Terrain land,int xnew, int ynew, int xold, int yold) {
		int rbgVal=0;
		if((xnew==xold) && (ynew==yold)){
			Water.increaseWaterDepth(xold,yold);
			if (Water.waterDepths[xold][yold]==1){
				Color waterCol = new Color(210, 128, 239);
				rbgVal=waterCol.getRGB();
			}
			if (Water.waterDepths[xold][yold]==2){
				Color waterCol = new Color(0, 0, 255);
				rbgVal=waterCol.getRGB();
			}
			if (Water.waterDepths[xold][yold] >= 3) {
				Color waterCol = new Color(0, 0, 153);
				rbgVal=waterCol.getRGB();
			}



			land.img.setRGB(xnew,ynew, rbgVal);
		}
		else{
			Water.increaseWaterDepth(xnew, ynew);
			Water.decreaseWaterDepth(xold, yold);
			if (Water.waterDepths[xnew][ynew]==1){
				Color waterCol = new Color(210, 128, 239);
				rbgVal=waterCol.getRGB();
			}
			if (Water.waterDepths[xnew][ynew]==2){
				Color waterCol = new Color(0, 0, 255);
				rbgVal=waterCol.getRGB();
			}
			if (Water.waterDepths[xnew][ynew] >= 3) {
				Color waterCol = new Color(0, 0, 153);
				rbgVal=waterCol.getRGB();
			}


			land.img.setRGB(xnew, ynew, rbgVal);

		}
		if(Water.waterDepths[xold][yold]<1){
			float val = (land.height[xold][yold] - land.minh) / (land.maxh - land.minh);
			Color col = new Color(val, val, val, 1.0f);
			land.img.setRGB(xold, yold, col.getRGB());


		}





	}


	/**
	 * runs the simulation main driver
	 */
	public void run() {
		while (true) {
			while (boolPause == false) {
				Parallel mainTask = new Parallel(land,Terrain.height, Water.waterDepths,0,land.dim());
				ForkJoinPool pool = new ForkJoinPool();
				pool.invoke(mainTask);
				label.setText("Simulation step: "+atomicCounter.incrementAndGet());
				repaint();

			}

			}

		}
	}





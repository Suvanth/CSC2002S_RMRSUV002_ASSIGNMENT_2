import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Flow {
	/**
	 * initial time
	 */
	static long startTime = 0;
	/**
	 * frame x dimensions
	 */
	static int frameX;
	/**
	 * frame y dimensions
	 */
	static int frameY;
	/**
	 * flow panel for content
	 */
	static FlowPanel fp;





	// start timer

	/**
	 * timer start method
	 */
	private static void tick(){
		startTime = System.currentTimeMillis();
	}
	
	// stop timer, return time elapsed in seconds

	/**
	 * stop timer, return time elapsed in seconds
	 * @return end time
	 */
	private static float tock(){
		return (System.currentTimeMillis() - startTime) / 1000.0f; 
	}

	/**
	 * Method to setup the GUI for user
	 * @param frameX dimesion of frame x
	 * @param frameY dimension of frame y
	 * @param landdata land obj that is used for genertaing landscape
	 */
	public static void setupGUI(int frameX,int frameY,Terrain landdata) {

		
		Dimension fsize = new Dimension(800, 800);
    	JFrame frame = new JFrame("Waterflow"); 
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.getContentPane().setLayout(new BorderLayout());
    	
      	JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
   
		fp = new FlowPanel(landdata);
		fp.setPreferredSize(new Dimension(frameX,frameY));
		//fp label stuff
		//adding it to b panel
		g.add(fp);
	    
		// to do: add a MouseListener, buttons and ActionListeners on those buttons
		/**
		 * Mouse listener to receive user mouse clicks
		 */
		g.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
					if ((e.getX()>0) && (e.getX()<frameX-1) && (e.getY()>0) && (e.getY()<frameY-1)){
						fp.paintPixel(e.getX(),e.getY());

					}


				}
		});
	   	
		JPanel b = new JPanel();
	    b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));
		JButton endB = new JButton("End");
		JButton playB = new JButton("Play");
		JButton pauseB = new JButton("Pause");
		JButton resetB = new JButton("Reset");



		// add the listener to the jbutton to handle the "pressed" event
		/**
		 * end the simulation and dispose
		 */
		endB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// to do ask threads to stop
				fp.pauseSim();
				Water.readWaterArray(landdata);
				frame.dispose();

			}
		});

		/**
		 * play button to start simulation
		 */
		playB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fp.startSim();

			}





		});

		/**
		 * Action listener for pause
		 */
		pauseB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fp.pauseSim();
				fp.repaint();

			}
		});

		/**
		 * Action listener for reset button
		 */
		resetB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fp.pauseSim();
				fp.label.setText("Simulation Counter= 0");
				fp.atomicCounter.set(0);

				Water.readWaterArray(landdata);
				landdata.deriveImage();
				fp.repaint();
			}
		});
		fp.label.setText("Simulation Counter= 0");


		b.add(fp.label);
		b.add(Box.createHorizontalGlue());
		b.add(resetB);
		b.add(Box.createHorizontalGlue());
		b.add(pauseB);
		b.add(Box.createHorizontalGlue());
		b.add(playB);
		b.add(Box.createHorizontalGlue());
		b.add(endB);




		g.add(b);
    	
		frame.setSize(frameX, frameY+65);	// a little extra space at the bottom for buttons
      	frame.setLocationRelativeTo(null);  // center window on screen
      	frame.add(g); //add contents to window
        frame.setContentPane(g);
        frame.setVisible(true);
        Thread fpt = new Thread(fp);


        fpt.start();


	}


	/**
	 * Main driver of simulation
	 * @param args input filename
	 */
	public static void main(String[] args) {
		Terrain landdata = new Terrain();
		
		// check that number of command line arguments is correct
		if(args.length != 1)
		{
			System.out.println("Incorrect number of command line arguments. Should have form: java -jar flow.java intputfilename");
			System.exit(0);
		}
				
		// landscape information from file supplied as argument
		// 
		landdata.readData(args[0]);
		Water.readWaterArray(landdata);
		
		frameX = landdata.getDimX();
		frameY = landdata.getDimY();
		SwingUtilities.invokeLater(()->setupGUI(frameX, frameY, landdata));

		
		// to do: initialise and start simulation
	}
}

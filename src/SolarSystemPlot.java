import java.awt.*;
import java.awt.event.*;

/**
 * The object that will display the solar system
 */
public class SolarSystemPlot extends Canvas implements SolarSystemInterface {

    private int plotWidth = SolarSystemInterface.plotWidth;		 // width of the plot (pixels)
    private int plotHeight = SolarSystemInterface.plotHeight;	 // height of the plot (pixels)

    private static int plotCount = 0;	    // points added
    private String plotTitle;				// plot title
    private double xMin, xMax, yMin, yMax;	// max and min of x and y
    private double xRange, yRange; 			// plot ranges

    private int pointSize = 3;				// width of plot symbols, 3 pixels by default
    private Color defaultBackground = Color.black;

    private int lastx, lasty;				// pixel coordinates of last point plotted
    private Frame plotFrame;				// the window that will hold the plot

    /*
     * Off Screen Image and Graphics
     */
    private Image offScreenImage;			// off-screen image where we draw
    private Graphics offScreenGraphics;		// graphics context for off-screen image


    /**
     * Creates a new plot
     */
    public SolarSystemPlot(String title, double xMin, double xMax, double yMin, double yMax) {

        this.plotCount += 1;
        this.plotTitle = title;

        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;

        this.xRange = xMax - xMin;
        this.yRange = yMax - yMin;

        this.plotFrame = new Frame(plotTitle);

        this.plotFrame.addWindowListener(new WindowAdapter() {	// remove this if you don't want the program
            public void windowClosing(WindowEvent e) {		// to quit when close-box is clicked
                System.exit(0);
            }});

        Panel panel = new Panel();				        // panel to hold the canvas
        this.plotFrame.add(panel,BorderLayout.CENTER);	// panel added to the center of the window
        panel.add(this);							    // add canvas to the window

        this.setSize(this.plotWidth + 1,this.plotHeight + 1);			// size of the plot canvas

        this.plotFrame.setResizable(true);
        this.plotFrame.pack();
        this.offScreenImage = createImage(plotWidth + 1,plotHeight + 1);	// this image is where points will be added to

        this.offScreenGraphics = offScreenImage.getGraphics();		                // get the offscreen graphics

        clearThePlot();                                                             // sets the background to black
        this.plotFrame.setLocation(this.plotWidth + 20 * plotCount,20 * plotCount);
        this.plotFrame.setVisible(true);
    }

    /**
     * Add point
     */
    public synchronized void addPoint(Color color, int pointSize, double X, double Y) {

        offScreenGraphics.setColor(color);
        setPointSize(pointSize);
        int pixelx = (int) Math.round(plotWidth * (X - xMin) / xRange);	    // convert x to a screen coordinate
        int pixely = (int) Math.round(plotHeight * (yMax - Y) / yRange);	// remember that screen y is measured downward
        int offset = (int) (pointSize / 2.0);				                // offset of top-left corner (rounded down)

        offScreenGraphics.fillOval(pixelx-offset,pixely-offset,pointSize-1,pointSize-1);

        lastx = pixelx;
        lasty = pixely;

    }

    /**
     * Updates the off-screen image
     */
    public synchronized void repaint() {
        super.repaint();
    }

    /**
     * Change point size
     */
    public void setPointSize(int pointSize) {
        this.pointSize = pointSize;
    }

    /**
     * Copies the off-screen image
     */
    public synchronized void paint(Graphics g) {
        g.drawImage(offScreenImage,0,0,plotWidth+1,plotHeight+1,this);
    }

    /**
     * Does not redraw the background
     */
    public synchronized void update(Graphics g) {
        paint(g);
    }

    /**
     * Sets the plot to black
     */
    public synchronized void clearThePlot() {
        offScreenGraphics.setColor(defaultBackground);
        offScreenGraphics.fillRect(0,0,plotWidth+1,plotHeight+1);	// paint the background black
    }

}
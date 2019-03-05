import java.awt.*;
import java.awt.event.*;

public class SolarSystemPlot extends Canvas implements SolarSystemInterface {

    private int plotWidth = SolarSystemInterface.plotWidth;			// width of plot in pixels
    private int plotHeight = SolarSystemInterface.plotHeight;			// height of plot in pixels

    // constant to define plotted point shapes (could add more later):
    public static final int CIRCLE = 1;

    private static int plotCount = 0;	// number of Plot objects created so far (used to position window)
    private String plotTitle;				// title of plot
    private double xMin, xMax, yMin, yMax;	// the ranges of values for the plot to cover
    private double xRange, yRange; 			// difference between min and max
    private double xGridInterval, yGridInterval;	// grid spacing interval, these default to 0.1 for now

    final Color[] colors = {Color.blue, Color.red, Color.black, Color.cyan, Color.darkGray, Color.gray, Color.green, Color.lightGray, Color.magenta,
                            Color.orange, Color.pink, Color.yellow, Color.white};

    private Color pointColor = colors[0];	// color of plotted points (and lines), blue by default
    int pointSize = 3;				// width of plot symbols, 3 pixels by default
    private int pointShape = CIRCLE;		// default point shape is a circle

    private boolean connected = false;		// whether to connect the dots
    private boolean firstPoint = true;		// whether the next point to be plotted is the first
    private int lastx, lasty;				// pixel coordinates of last point plotted

    /*
     * Graphical User Interface fields
     */
    private Frame plotFrame;				// the window that will hold the plot
    private Panel controlPanel;				// panel with buttons (declared here so subclasses can add more buttons)

    /*
     * Off Screen Image and Graphics
     */
    private Image offScreenImage;			// off-screen image where we draw
    private Graphics offScreenGraphics;		// graphics context for off-screen image
    /** Creates a new plot with specified title, ranges, and grid spacings. */
    public SolarSystemPlot(String title, double xMin, double xMax, double yMin, double yMax) {

        this.plotCount += 1;
        this.plotTitle = title;

        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;

        final double INTERVAL = 0.1;

        this.xRange = xMax - xMin;
        this.yRange = yMax - yMin;
        this.xGridInterval = INTERVAL;
        this.yGridInterval = INTERVAL;

        this.plotFrame = new Frame(this.plotTitle);
        this.plotFrame.addWindowListener(new WindowAdapter() {	// remove this if you don't want the program
            public void windowClosing(WindowEvent e) {		// to quit when close-box is clicked
                System.exit(0);
            }});
        Panel centerPanel = new Panel();				// to avoid resizing the canvas, create a panel to hold it
        this.plotFrame.add(centerPanel,BorderLayout.CENTER);	// add the panel to the window
        centerPanel.add(this);							// and add the canvas to the panel
        this.setSize(this.plotWidth+1,this.plotHeight+1);			// now we can set the canvas's size and it'll work
        // (+1 is so gridlines show when they're at edges)
        this.controlPanel = new Panel();						// create a panel to hold the buttons
        this.plotFrame.add(controlPanel,BorderLayout.NORTH);	// put it at the top of the window
        //Button clearButton = new Button("Clear");		// create a button to clear the plot
        /*clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { clearThePlot(); }
        });											// tell it what to do when the button is clicked
        this.controlPanel.add(clearButton);	*/				// note that southPanel has the default FlowLayout
        this.plotFrame.setResizable(true);
        this.plotFrame.pack();								// make the frame just large enough to hold its components
        this.offScreenImage = createImage(plotWidth+1,plotHeight+1);	// create the off-screen image where we'll draw

        this.offScreenGraphics = offScreenImage.getGraphics();		// get its graphics context
        /*
        offScreenGraphics.setColor(colors[7]);
        offScreenGraphics.fillRect(-plotWidth/2, -plotHeight/2, plotWidth, plotHeight);
        */
        clearThePlot();
        this.plotFrame.setLocation(this.plotWidth+20*plotCount,20*plotCount);	// put the window in the upper-right part of the screen
        this.plotFrame.setVisible(true);						// show the window!
        requestFocus();									// take focus away from the clear button
    }

    /** Adds a new point to the plot. */
    public synchronized void addPoint(Color color, int pointSize, double X, double Y) {
        offScreenGraphics.setColor(color);
        setPointSize(pointSize);
        int pixelx = (int) Math.round(plotWidth * (X-xMin) / xRange);	// convert x to a screen coordinate
        int pixely = (int) Math.round(plotHeight * (yMax-Y) / yRange);	// remember that screen y is measured downward
        int offset = (int) (pointSize/2.0);				// offset of top-left corner (rounded down)

        if (pointShape == CIRCLE) {
            offScreenGraphics.fillOval(pixelx-offset,pixely-offset,pointSize-1,pointSize-1);
        } else {
            offScreenGraphics.fillRect(pixelx-offset,pixely-offset,pointSize,pointSize);	// default is SQUARE
        }

        if (connected && !firstPoint) {
            offScreenGraphics.drawLine(lastx,lasty,pixelx,pixely);
        }
        lastx = pixelx;
        lasty = pixely;
        firstPoint = false;
        // tell Java that our paint method needs to be called
        //repaint();
    }

    public synchronized void repaint() {
        super.repaint();
    }

    /** Changes the size of the plotted points (newSize in pixels). */
    public void setPointSize(int pointSize) {
        this.pointSize = pointSize;
    }

    /** Changes the color of the plotted points and lines. */
    public synchronized void setColor(Color pointColor) {
        this.pointColor = pointColor;
    }

    /** Changes the shape of the plotted points (see constants above for allowed values). */
    public void setPointShape(int pointShape) {
        this.pointShape = pointShape;
    }

    /** Sets a flag to determine whether the plotted points will be connected by lines. */
    public void setConnected(boolean flag) {
        connected = flag;
    }

    /** Paints the canvas (by simply copying the off-screen image). */
    public synchronized void paint(Graphics g) {
        g.drawImage(offScreenImage,0,0,plotWidth+1,plotHeight+1,this);
    }

    /** Override update to avoid redrawing background. */
    public synchronized void update(Graphics g) {
        paint(g);
    }

    /** Clears the plot and draws the axes and grid lines. */
    public synchronized void clearThePlot() {
        offScreenGraphics.setColor(Color.black);       //Set to black
        offScreenGraphics.fillRect(0,0,plotWidth+1,plotHeight+1);	// paint the background white
        firstPoint = true;
    }

}
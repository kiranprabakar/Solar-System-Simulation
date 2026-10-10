import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

/**
 * The object that will display the solar system at true scale, with zooming and panning
 */
public class SolarSystemPlot extends Canvas implements SolarSystemInterface {

    private int plotWidth = SolarSystemInterface.plotWidth;		 // width of the plot (pixels)
    private int plotHeight = SolarSystemInterface.plotHeight;	 // height of the plot (pixels)

    private double centerX, centerY;            // the point in space shown at the center of the plot (meters)
    private double metersPerPixel;              // the zoom level

    private List<SolarSystemBody> bodies = new ArrayList<>();   // the bodies drawn in the last frame
    private String status = "";                                 // the status line drawn in the last frame

    private int dragX, dragY;                   // where the last mouse drag event happened

    private Runnable onSpace;                   // what to do when the space bar is pressed over the display

    private final int[] starfieldX, starfieldY, starfieldBrightness;    // background stars (pixels)

    /*
     * Off Screen Image and Graphics
     */
    private Image offScreenImage;			// off-screen image where we draw
    private Graphics2D offScreenGraphics;	// graphics context for off-screen image


    /**
     * Creates a new plot
     *
     * @param title - the window title
     */
    public SolarSystemPlot(String title) {

        Frame plotFrame = new Frame(title);         // the window that will hold the plot

        plotFrame.addWindowListener(new WindowAdapter() {	    // quits the program when the window is closed
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }});

        Panel panel = new Panel();				        // panel to hold the canvas
        plotFrame.add(panel, BorderLayout.CENTER);	    // panel added to the center of the window
        panel.add(this);							    // add canvas to the window

        this.setSize(plotWidth, plotHeight);			// size of the plot canvas

        plotFrame.setResizable(false);
        plotFrame.pack();
        this.offScreenImage = createImage(plotWidth, plotHeight);	        // this image is where each frame is drawn
        this.offScreenGraphics = (Graphics2D) offScreenImage.getGraphics();
        offScreenGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Random random = new Random(42);                 // the same starfield every time
        int stars = 250;
        starfieldX = new int[stars];
        starfieldY = new int[stars];
        starfieldBrightness = new int[stars];
        for (int i = 0; i < stars; i++) {
            starfieldX[i] = random.nextInt(plotWidth);
            starfieldY[i] = random.nextInt(plotHeight);
            starfieldBrightness[i] = 40 + random.nextInt(80);
        }

        addMouseWheelListener(e -> zoom(e.getX(), e.getY(), Math.pow(1.2, e.getPreciseWheelRotation())));

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && onSpace != null) {
                    onSpace.run();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                requestFocus();                         // so the space bar reaches the display
                dragX = e.getX();
                dragY = e.getY();
            }

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {           // double-click fits all planets in view
                    fitToBodies(bodies);
                    redraw();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {    // drags the view
                centerX -= (e.getX() - dragX) * metersPerPixel;
                centerY += (e.getY() - dragY) * metersPerPixel;
                dragX = e.getX();
                dragY = e.getY();
                redraw();
            }
        });

        resetView();
        redraw();
        plotFrame.setLocation(plotWidth + 20, 20);
        plotFrame.setVisible(true);
    }

    /**
     * @param onSpace - what to do when the space bar is pressed over the display
     */
    public void setOnSpace(Runnable onSpace) {
        this.onSpace = onSpace;
    }

    /**
     * Zooms the view while keeping the point under the mouse still
     *
     * @param pixelX - x - coordinate of the mouse
     * @param pixelY - y - coordinate of the mouse
     * @param factor - how much to zoom out (less than 1 zooms in)
     */
    private void zoom(int pixelX, int pixelY, double factor) {

        double worldX = toWorldX(pixelX);
        double worldY = toWorldY(pixelY);

        metersPerPixel = Math.max(1E3, Math.min(1E12, metersPerPixel * factor));    // from about the size of a moon to far beyond the planets

        centerX = worldX - (pixelX - plotWidth / 2.0) * metersPerPixel;
        centerY = worldY + (pixelY - plotHeight / 2.0) * metersPerPixel;

        redraw();

    }

    /**
     * Shows the default view, centered on the origin
     */
    public void resetView() {
        centerX = 0;
        centerY = 0;
        metersPerPixel = coordinateMax * AU / (plotWidth / 2.0);
    }

    /**
     * Centers the view on the star and zooms so every planet's orbit fits
     *
     * @param bodies - the bodies in the solar system
     */
    public void fitToBodies(List<SolarSystemBody> bodies) {

        SolarSystemBody star = null;
        double radius = 0;

        for (SolarSystemBody body : bodies) {
            if (body instanceof Star) {
                star = body;
            }
        }

        if (star == null) {
            resetView();
            return;
        }

        for (SolarSystemBody body : bodies) {
            if (body instanceof Planet) {
                double distance = Math.hypot(body.getX() - star.getX(), body.getY() - star.getY());
                radius = Math.max(radius, Math.max(distance, body.getDistanceFromCentralBody()));
            }
        }

        double starRadius = star.getDiameter() / 2;

        if (radius == 0) {                              // only a star, so show the inner solar system, or room around a giant star
            radius = Math.max(2 * AU, 3 * starRadius);
        }

        radius = Math.max(radius, 1.5 * starRadius);    // the whole star always fits

        centerX = star.getX();
        centerY = star.getY();
        metersPerPixel = radius * 1.15 / (plotWidth / 2.0);    // leaves a margin around the outermost orbit

    }

    private double toPixelX(double x) {
        return plotWidth / 2.0 + (x - centerX) / metersPerPixel;
    }

    private double toPixelY(double y) {
        return plotHeight / 2.0 - (y - centerY) / metersPerPixel;    // screen y is measured downward
    }

    private double toWorldX(double pixelX) {
        return centerX + (pixelX - plotWidth / 2.0) * metersPerPixel;
    }

    private double toWorldY(double pixelY) {
        return centerY - (pixelY - plotHeight / 2.0) * metersPerPixel;
    }

    /**
     * @param body - a body
     * @return - how large the body is drawn (pixels): its true size, but at least its point size
     */
    private double drawnSize(SolarSystemBody body) {
        return Math.min(4000, Math.max(body.getPointSize(), body.getDiameter() / metersPerPixel));
    }

    /**
     * Satellites are usually too close to their planet to see at solar system scale,
     * so their distance from the planet is stretched to at least a few pixels. The physics is not affected.
     *
     * @param body - a body
     * @return - pixels per meter for drawing the body's position relative to its central body
     */
    private double offsetScale(SolarSystemBody body) {

        if (!(body instanceof Satellite) || !(body.getParent() instanceof Planet)) {     // only moons of planets are stretched
            return 1 / metersPerPixel;
        }

        int rank = 0;                                   // spreads out satellites of the same planet, closest first
        for (SolarSystemBody other : bodies) {
            if (other instanceof Satellite && other != body && other.getParent() == body.getParent()
                    && other.getDistanceFromCentralBody() < body.getDistanceFromCentralBody()) {
                rank++;
            }
        }

        double minPixels = drawnSize(body.getParent()) / 2 + 10 + 8 * rank;

        return Math.max(1 / metersPerPixel, minPixels / body.getDistanceFromCentralBody());

    }

    /**
     * @param body - a body
     * @return - where the body is drawn {x, y} (pixels)
     */
    private double[] displayPosition(SolarSystemBody body) {

        if (!(body instanceof Satellite)) {
            return new double[] {toPixelX(body.getX()), toPixelY(body.getY())};
        }

        double[] parent = displayPosition(body.getParent());
        double scale = offsetScale(body);

        return new double[] {parent[0] + (body.getX() - body.getParent().getX()) * scale,
                parent[1] - (body.getY() - body.getParent().getY()) * scale};

    }

    /**
     * Draws a new frame
     *
     * @param bodies - the bodies to draw
     * @param status - the status line to show
     */
    public void render(List<SolarSystemBody> bodies, String status) {
        this.bodies = bodies;
        this.status = status;
        redraw();
    }

    /**
     * Draws the last bodies and status again, for example after the view changes
     */
    private void redraw() {

        Graphics2D g = offScreenGraphics;

        g.setColor(Color.black);                                            // background
        g.fillRect(0, 0, plotWidth, plotHeight);

        for (int i = 0; i < starfieldX.length; i++) {
            int b = starfieldBrightness[i];
            g.setColor(new Color(b, b, b));
            g.fillRect(starfieldX[i], starfieldY[i], 1, 1);
        }

        for (SolarSystemBody body : bodies) {                               // orbit trails, fading with age
            drawTrail(g, body);
        }

        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));

        for (SolarSystemBody body : bodies) {                               // the bodies and their names
            double[] p = displayPosition(body);
            double size = drawnSize(body);

            if (body instanceof Star) {                                     // a soft glow around the star
                for (int i = 3; i >= 1; i--) {
                    Color c = body.getColor();
                    g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 25));
                    double glow = size + 8 * i;
                    g.fill(new Ellipse2D.Double(p[0] - glow / 2, p[1] - glow / 2, glow, glow));
                }
            }

            g.setColor(body.getColor());
            g.fill(new Ellipse2D.Double(p[0] - size / 2, p[1] - size / 2, size, size));

            if (offsetScale(body) <= 1 / metersPerPixel) {                  // satellites are only named once zoomed in to their true distance
                g.setColor(new Color(200, 200, 200));
                double edge = size / 2 * Math.sqrt(0.5);                    // the upper-right edge of the circle, so large bodies keep their label close
                g.drawString(body.retName(), (float) (p[0] + edge + 3), (float) (p[1] - edge - 2));
            }
        }

        drawScaleBar(g);

        String[] lines = status.split("\n");                             // the first line is the status, any others are events
        for (int i = 0; i < lines.length; i++) {
            g.setColor(i == 0 ? Color.white : Color.orange);
            g.drawString(lines[i], 10, 18 + 16 * i);
        }
        g.setColor(Color.gray);
        g.drawString("Scroll: zoom    Drag: pan    Double-click: fit    Space: pause / resume", 10, plotHeight - 10);

        repaint();

    }

    /**
     * Draws a body's recent orbit around its central body
     *
     * @param g - where to draw
     * @param body - the body
     */
    private void drawTrail(Graphics2D g, SolarSystemBody body) {

        int count = body.getTrailCount();

        if (count < 2) {
            return;
        }

        double[] anchor = displayPosition(body.getParent());     // trails are stored relative to the central body
        double scale = offsetScale(body);
        Color c = body.getColor();

        g.setStroke(new BasicStroke(1.2f));

        double prevX = anchor[0] + body.getTrailX(0) * scale;
        double prevY = anchor[1] - body.getTrailY(0) * scale;

        for (int i = 1; i < count; i++) {
            double x = anchor[0] + body.getTrailX(i) * scale;
            double y = anchor[1] - body.getTrailY(i) * scale;
            int alpha = 20 + 140 * i / count;                     // older points are fainter
            g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha));
            g.draw(new Line2D.Double(prevX, prevY, x, y));
            prevX = x;
            prevY = y;
        }

    }

    /**
     * Draws a bar showing a round distance at the current zoom
     *
     * @param g - where to draw
     */
    private void drawScaleBar(Graphics2D g) {

        double target = 120 * metersPerPixel;                       // about 120 pixels long
        boolean useAU = target >= 0.01 * AU;
        double unit = useAU ? AU : 1000;
        double value = target / unit;

        double magnitude = Math.pow(10, Math.floor(Math.log10(value)));     // rounds down to 1, 2 or 5 times a power of ten
        double nice = value / magnitude >= 5 ? 5 * magnitude : value / magnitude >= 2 ? 2 * magnitude : magnitude;

        double length = nice * unit / metersPerPixel;
        int x = 10, y = plotHeight - 30;

        g.setColor(Color.lightGray);
        g.setStroke(new BasicStroke(1f));
        g.draw(new Line2D.Double(x, y, x + length, y));
        g.draw(new Line2D.Double(x, y - 4, x, y + 4));
        g.draw(new Line2D.Double(x + length, y - 4, x + length, y + 4));

        String label = useAU ? String.format("%s AU", trimNumber(nice)) : String.format("%s km", trimNumber(nice));
        g.drawString(label, (float) (x + length + 6), y + 4);

    }

    /**
     * @param value - a number
     * @return - the number without unnecessary decimals
     */
    private static String trimNumber(double value) {
        return value >= 1 ? String.format("%,.0f", value) : String.format("%.3f", value).replaceAll("0+$", "");
    }

    /**
     * Copies the off-screen image
     */
    public void paint(Graphics g) {
        g.drawImage(offScreenImage, 0, 0, this);
    }

    /**
     * Does not redraw the background
     */
    public void update(Graphics g) {
        paint(g);
    }

}

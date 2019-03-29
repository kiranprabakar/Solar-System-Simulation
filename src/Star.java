
import java.awt.*;

/**
 * Creates a star
 */
public class Star extends SolarSystemBody {

    private SolarSystemPlot plot;                       // the plot associated with this
    private Color color;                                // the color that will be used for display purposes

    private boolean pause;                              // checks if the user wants to stop the simulation
    private int pointSize;                              // size of the plot point

    private DataStorage ds;                             // the data store associated with this star

    /**
     * @param name - name of star
     * @param diameter - diameter of star
     * @param mass - mass of star
     * @param plot - plot associated with star
     * @param color - color for the plot
     * @param ds - data store
     */
    public Star(String name, double diameter, double mass, SolarSystemPlot plot, Color color, DataStorage ds) {

        super(name, diameter, 0, mass, 0, 0);       // star is the center of the solar system, so it is located at the origin
        setType("Star");
        this.plot = plot;
        this.color = color;
        this.ds = ds;
        setPointSize();

    }

    /**
     * @return - the star color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the point size of the star's points
     */
    public void setPointSize() {

        int i = ds.starNames.indexOf(retName());
        pointSize = ds.starPointSizes.get(i);

    }

    /**
     * @return - the point size
     */
    public int getPointSize() {
        return pointSize;
    }

    /**
     * @return - flag to stop the star thread
     */
    @Override
    public boolean toPause() {
        return pause;
    }

    /**
     * Determines if the thread should stop
     */
    @Override
    public void pause() {
        pause = true;
    }

    /**
     * This executes until the user wants to stop the simulation
     */
    public void run() {

        while (!toPause()) {                                    // checks if the simulation has not been stopped
            plot.addPoint(this.color, pointSize, 0, 0);
        }

    }
}


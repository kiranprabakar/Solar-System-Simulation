/*
 * Creates a Star based on parameters
 */

import java.awt.*;

public class Star extends SolarSystemBody {

    private double absoluteVisualMagnitude;               //Used for habitable zone measurements
    private double bolometricCorrectionConstant;          //Used for habiable zone measurements
    private double habitableZoneLowerBound;
    private double habitableZoneUpperBound;
    private String spectralClass;                         //Used for habitable zone measurements

    private SolarSystemPlot plot;
    private Color color;

    private boolean pause;
    private int pointSize;

    private DataStorage ds;

    public Star(String name, double diameter, double mass, SolarSystemPlot plot, Color color, DataStorage ds) {

        super(name, diameter, 0, mass, 0, 0);
        setType("Star");
        this.plot = plot;
        this.color = color;
        this.ds = ds;
        setPointSize();

    }

    public Color getColor() {
        return color;
    }

    public void setPointSize() {

        int i = ds.starNames.indexOf(retName());
        pointSize = ds.starPointSizes.get(i);

    }

    public int getPointSize() {
        return pointSize;
    }

    @Override
    public boolean toPause() {
        return pause;
    }

    @Override
    public void pause() {
        pause = true;
    }

    public void run() {

        while (!toPause()) {
            plot.addPoint(this.color, pointSize, 0, 0);
        }

        //Thread.currentThread().interrupt();

    }
}

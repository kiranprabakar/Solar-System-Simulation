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

    public Star(String name, double diameter, double mass, SolarSystemPlot plot, Color color) {

        super(name, diameter, 0, mass, 0, 0);
        setType("Star");
        this.plot = plot;
        this.color = color;

    }

    public Color getColor() {
        return color;
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

        while (!pause) {
            plot.addPoint(this.color, 10, 0, 0);
        }

    }
}

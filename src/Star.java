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

    //Need to fix the following two arrays because im no longer sure about the spectral classes and the bolometric constants (for habitable zone calculations)
    private final String[] SpectralClasses = {"B", "A", "F", "G", "K", "M"};
    private final double[] CorrectionConstants = {-2.0, -0.3, -0.15, -0.4, -0.8, -2.0};

    public Star(String name, double diameter, double mass, double absoluteVisualMagnitude, String spectralClass,
                SolarSystemPlot plot, Color color) throws SpectralClassNotValidException {
        super(name, diameter, 0, mass, 0, 0);
        setType("Star");
        this.absoluteVisualMagnitude = absoluteVisualMagnitude;
        this.spectralClass = spectralClass;
        int ind = getBolometricCorrectionConstantIndex();
        if (ind < 0) {
            throw new SpectralClassNotValidException("Invalid Spectral Class!");
        } else {
            this.bolometricCorrectionConstant = CorrectionConstants[ind];
        }
        this.plot = plot;
        this.color = color;
        setHabitableZone();
    }

    public int getBolometricCorrectionConstantIndex() {

        int ind = -1;
        for (int i = 0; i < SpectralClasses.length; i++) {
            if (this.spectralClass.equals(SpectralClasses[i])) {
                ind = i;
                break;
            }
        }

        return ind;

    }

    public void setHabitableZone() {

        final double bolometricSun = 4.72;
        final double absoluteLuminosiySun = 4.83;

        double bolometricMagnitude = getAbsoluteVisualMagnitude() + getBolometricCorrectionConstant();
        double absoluteLuminosityStar = absoluteLuminosiySun * Math.pow(10, (bolometricMagnitude - bolometricSun) / -POGSON_RATIO);
        this.habitableZoneLowerBound = Math.sqrt(absoluteLuminosityStar / 1.1) * 1.496 * Math.pow(10,11);
        this.habitableZoneUpperBound = Math.sqrt(absoluteLuminosityStar / 0.53) * 1.496 * Math.pow(10,11);

    }

    public double getAbsoluteVisualMagnitude() {
        return absoluteVisualMagnitude;
    }

    public double getBolometricCorrectionConstant() {
        return bolometricCorrectionConstant;
    }

    public String getSpectralClass() {
        return this.spectralClass;
    }

    public double getHabitableZoneLowerBound() {
        return this.habitableZoneLowerBound;
    }

    public double getHabitableZoneUpperBound() {
        return this.habitableZoneUpperBound;
    }

    public boolean equals(Star other) {
        return super.equals(other) && other.getAbsoluteVisualMagnitude() == getAbsoluteVisualMagnitude()
                && other.getSpectralClass().equals(getSpectralClass());
    }

    public void run() {

        double time = 0;

        while (time <= timeLimit) {
            plot.addPoint(this.color, 20, 0, 0);
            time += dt;
        }

    }
}

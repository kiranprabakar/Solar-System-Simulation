import java.awt.*;

public class Satellite extends SolarSystemBody implements SolarSystemInterface {

    private Planet planet;
    private SolarSystemPlot plot;
    private Color color;
    private double x, y;

    public Satellite(String name, double diameter, double distanceFromPlanet, double mass,
                     Planet planet, SolarSystemPlot plot, Color color, double initX, double initY) {
        super(name, diameter, distanceFromPlanet, mass);
        this.planet = planet;
        this.plot = plot;
        this.color = color;
        x = initX * AU;
        y = initY * AU;
    }

    public double getDistanceFromPlanet() {
        return getDistanceFromCentralBody();
    }

    public Planet getPlanet() {
        return planet;
    }

    public SolarSystemPlot getPlot() {
        return plot;
    }

    public Color getColor() {
        return color;
    }


}

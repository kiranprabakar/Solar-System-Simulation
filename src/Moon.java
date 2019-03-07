import java.awt.*;

public class Moon extends Satellite {

    public Moon(String name, double diameter, double distanceFromPlanet, double mass,
                Planet planet, SolarSystemPlot plot, Color color, double initX, double initY) {
        super(name, diameter, distanceFromPlanet, mass, planet, plot, color, initX, initY);
        setType("Moon");
    }

}

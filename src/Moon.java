import java.awt.*;

public class Moon extends Satellite {

    public Moon(String name, double diameter, double distanceFromPlanet, double mass,
                Planet planet, SolarSystemPlot plot, Color color, double initX, double initY, DataStorage ds) {
        super(name, diameter, distanceFromPlanet, mass, planet, plot, color, initX, initY, ds);
        setType("Moon");
    }

    public void run() {
        super.run();
    }

}

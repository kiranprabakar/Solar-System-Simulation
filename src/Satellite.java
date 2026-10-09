
import java.awt.*;

/**
 * Creates a satellite or moon to be added into the solar system
 */
public class Satellite extends SolarSystemBody {

    private SolarSystemBody body;               // the body which the satellite orbits

    /**
     * @param name - satellite name
     * @param type - "Satellite" or "Moon"
     * @param diameter - satellite diameter
     * @param distanceFromBody - semi-major axis of the orbit around the planet
     * @param mass - satellite mass
     * @param body - central planet
     * @param color - color of satellite
     * @param pointSize - the minimum size of the satellite on the display
     */
    public Satellite(String name, String type, double diameter, double distanceFromBody, double mass,
                     SolarSystemBody body, Color color, int pointSize) {
        super(name, diameter, distanceFromBody, mass, color, pointSize);
        setType(type);
        this.body = body;
    }

    /**
     * @return - the central planet
     */
    public SolarSystemBody getBody() {
        return body;
    }

    /**
     * @return - the central planet
     */
    @Override
    public SolarSystemBody getParent() {
        return body;
    }

}

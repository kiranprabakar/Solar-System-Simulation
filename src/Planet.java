
import java.awt.*;

/**
 * Creates a planet for the solar system
 */
public class Planet extends SolarSystemBody {

    private Star star;                                      // the star the planet orbits

    /**
     * @param name - name of the planet
     * @param diameter - diameter of the planet
     * @param distanceFromStar - semi-major axis of the orbit around the star
     * @param mass - mass of the planet
     * @param star - the solar system's star
     * @param color - the color associated with the planet
     * @param pointSize - the minimum size of the planet on the display
     */
    public Planet(String name, double diameter, double distanceFromStar, double mass, Star star, Color color, int pointSize) {
        super(name, diameter, distanceFromStar, mass, color, pointSize);
        setType("Planet");
        this.star = star;
    }

    /**
     * @return - the star the planet orbits
     */
    @Override
    public SolarSystemBody getParent() {
        return star;
    }

}

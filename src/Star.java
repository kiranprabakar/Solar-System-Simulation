
import java.awt.*;

/**
 * Creates a star
 */
public class Star extends SolarSystemBody {

    /**
     * @param name - name of star
     * @param diameter - diameter of star
     * @param mass - mass of star
     * @param color - color for the plot
     * @param pointSize - the minimum size of the star on the display
     */
    public Star(String name, double diameter, double mass, Color color, int pointSize) {

        super(name, diameter, 0, mass, color, pointSize);      // star is the center of the solar system, so it starts at the origin
        setType("Star");

    }

}

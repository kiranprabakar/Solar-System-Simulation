
import java.awt.*;

/**
 * Creates a planet for the solar system
 */
public class Planet extends SolarSystemBody {

    private boolean toPause;                                // determines of orbit needs to be paused

    private Star star;                                      // the star associated with the solar system
    private SolarSystemPlot plot;                           // the plot associated with the solar system

    private Color color;                                    // the color of each plot point
    private int pointSize;                                  // the size of each point
    private DataStorage ds;                                 // the data store associated with the planet

    private double divisor;                                 // makes visibility on the display easier

    /**
     * @param name - name of the planet
     * @param diameter - diameter of the planet
     * @param distanceFromStar - distance from the star
     * @param mass - mass of the planet
     * @param star - the solar system's star
     * @param plot - the plot to display the solar system
     * @param color - the color associated with the planet
     * @param initX - the initial x - coordinate (true distance / AU)
     * @param initY - the initial y - coordinate (true distance / AU)
     * @param ds - the data store that the planet will access
     */
    public Planet(String name, double diameter, double distanceFromStar, double mass, Star star,
                  SolarSystemPlot plot, Color color, double initX, double initY, DataStorage ds) {
        super(name, diameter, distanceFromStar, mass, initX, initY);
        setType("Planet");
        this.star = star;
        this.plot = plot;
        this.color = color;
        toPause = false;

        this.ds = ds;

        setPointSize();                         // sets the planet's point size
        setDivisor(distanceFromStar);           // sets the planet's divisor

    }

    /**
     * @param distance - distance from the star
     */
    public void setDivisor(double distance) {

        if (ds.planetDistancefromCentralBody.indexOf(distance) <= 3) {              // checks if the orbit radius is smaller than that of Mars
            divisor = innerPlanetDivisor;
        } else if (ds.planetDistancefromCentralBody.indexOf(distance) <= 5) {       // checks if the orbit radius is smaller than that of Saturn
            divisor = outerPlanetDivisor;
        } else {
            divisor = nepUrDivisor;
        }

    }

    /**
     * @return - the divisor
     */
    public double getDivisor() {
        return divisor;
    }

    /**
     * Sets the point size
     */
    public void setPointSize() {

        int i = ds.planetNames.indexOf(retName());
        pointSize = ds.planetPointSizes.get(i);

    }

    /**
     * @return - x - coordinate
     */
    public synchronized double getX() {
        return super.getX();
    }

    /**
     * @return - y - coordinate
     */
    public synchronized double getY() {
        return super.getY();
    }

    /**
     * @return - the planet's color
     */
    public synchronized Color getColor() {
        return color;
    }

    /**
     * @param initX - the new x - coordinate
     */
    public synchronized void setX(double initX) {
        super.setX(initX);
    }

    /**
     * @param initY - the new y - coordinate
     */
    public synchronized void setY(double initY) {
        super.setY(initY);
    }

    /**
     * Determines if the orbit needs to be paused
     */
    public synchronized void pause() {
        toPause = true;
    }

    /**
     * @return - checks if the orbit needs to stop
     */
    public synchronized boolean isToPause() {
        return toPause;
    }

    /**
     * @return - the display point size
     */
    public int getPointSize() {
        return pointSize;
    }

    /**
     * Orbit calculated using regular euler formula
     * Uses Newton's Law of Universal Gravitation and Newton's Second Law of Motion to determine orbit parameters
     */
    public void run() {

        double time = 0;                                        // will keep track of total steps in the orbit

        double distance = this.getDistanceFromCentralBody();    // orbit radius
        double starMass = star.getMass();                       // star's mass

        double prevX = getX();                                  // keeps track of the the previous x - coordinate
        double prevY = getY();                                  // keeps track of the previous y - coordinate

        double theta = Math.atan(getY() / getX());              // angle relative to the x - axis

        /*
         * Acceleration calculated with Newton's Law of Gravitation and Newton's Second Law
         * (1) Force = ThisBodyMass * acceleration
         * (2) GravitationalForce = GravitationalConstant * OtherBodyMass * ThisBodyMass / (distance between the center of mass of the two bodies) ^ 2
         *
         * Using (1) and (2): acceleration = GravitationalConstant * OtherBodyMass / (distance between the center of mass of the two bodies) ^ 2
         */
        double acceleration = G * starMass / Math.pow(distance, 2);


        /*
         * Velocity calculated using the definition of centripetal acceleration
         *      acceleration = (centripetal velocity) ^ 2 / radius
         *      -> (centripetal velocity) = sqrt(acceleration * radius)
         */
        double velocity = Math.sqrt(acceleration * distance);

        double v_y = velocity * Math.cos(theta);                                // y - component of valocity
        double v_x = -velocity * Math.sin(theta);                               // x - component of velocity

        while (!isToPause()) {                                                  // checks if orbit does not need to stop

            setX(getX() + v_x * (dt / ds.speedControl));                        // updates x - coordinate
            setY(getY() + v_y * (dt / ds.speedControl));                        // updates y - coordinate

            theta = Math.atan(getY() / getX());                                 // updates theta

            if (getX() < 0) {                                                   // updates theta if x < 0 because arctan range is (- pi /2, pi / 2)
                theta += Math.PI;
            }

            v_y = velocity * Math.cos(theta);                                   // updates y - component of velocity
            v_x = -velocity * Math.sin(theta);                                  // updates x - component of velocity

            time += dt;                                                         // updates the total steps

            if (time % (SolarSystemInterface.dt * ds.timeInterval) == 0) {      // plots at certain step intervals

                plot.addPoint(Color.black, pointSize, prevX / AU / divisor, prevY / AU / divisor);  // replace the previous point with a black point
                plot.addPoint(this.color, pointSize, getX() / AU / divisor, getY() / AU / divisor); // add a new point
                prevX = getX();
                prevY = getY();
                plot.repaint();                                                                            // updates the display

            }

        }

    }

}
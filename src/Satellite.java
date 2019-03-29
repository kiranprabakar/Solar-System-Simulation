
import java.awt.*;

/**
 * Creates a satellite to be added into the solar system
 */
public class Satellite extends SolarSystemBody {

    private SolarSystemBody body;               // the body which the satellite orbits
    private SolarSystemPlot plot;               // the plot associated with the satellite
    private Color color;                        // the color associated with the satellite
    private boolean pause;                      // determines if the orbit needs to be paused
    private DataStorage ds;                     // data store associated with the satellite
    private int pointSize, timeInterval;        // point size of the satellite and the time interval for plotting purposes
    private double divisor;                     // divisor for display purposes

    double relativeX, relativeY;                // the x- and y- coordinates relative to the central body

    /**
     * @param name - satellite name
     * @param diameter - satellite diameter
     * @param distanceFromBody - distance from planet
     * @param mass - satellite mass
     * @param body - central planet
     * @param plot - where satellite will be displayed
     * @param color - color of satellite
     * @param initX - the initial x - coordinate (true distance / AU)
     * @param initY - the initial y - coordinate (true distance / AU)
     * @param ds - the data store
     */
    public Satellite(String name, double diameter, double distanceFromBody, double mass,
                     SolarSystemBody body, SolarSystemPlot plot, Color color, double initX, double initY, DataStorage ds) {
        super(name, diameter, distanceFromBody, mass, initX, initY);
        this.body = body;
        this.plot = plot;
        this.color = color;
        this.ds = ds;
        relativeX = getX() - body.getX();               // sets relativeX and relativeY based on where the central planet is located
        relativeY = getY() - body.getY();
        setType();                                      // sets the type of satellite
        setTimeInterval();                              // sets the plotting interval
        setPointSize();                                 // sets the point size
        setDivisor();                                   // display purposes
    }

    /**
     * Determines if the object is a satellite or a moon
     */
    private void setType() {
        int index = ds.satelliteNames.indexOf(retName());
        String string = ds.satelliteType.get(index);
        super.setType(string);
    }

    /**
     * Determines the interval for plotting
     */
    public void setTimeInterval() {
        if (getType().equals("Satellite")) {
            timeInterval = ds.timeInterval / 10000;
        } else {
            timeInterval = ds.timeInterval;
        }
    }

    /**
     * Sets the size of the point
     */
    public void setPointSize() {
        int i = ds.satelliteNames.indexOf(retName());
        pointSize = ds.satellitePointSizes.get(i);
    }

    /**
     * Sets the divisor of the satellite
     */
    public void setDivisor() {
        if (getType().equals("Satellite")) {
            divisor = satelliteDivisor;
        } else {
            divisor = moonDivisor;
        }
    }

    /**
     * @return - the type of satellite
     */
    @Override
    public String getType() {
        return super.getType();
    }

    /**
     * @return - the divisor
     */
    public double getDivisor() {
        return divisor;
    }

    /**
     * @return - the point size
     */
    public int getPointSize() {
        return pointSize;
    }

    /**
     * @return - the central planet
     */
    public SolarSystemBody getBody() {
        return body;
    }

    /**
     * @return - the color associated with the body
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return - the x - coordinate
     */
    public double getX() {
        return super.getX();
    }

    /**
     * @return - the y - coordinate
     */
    public double getY() {
        return super.getY();
    }

    /**
     * @param initX - the new x - coordinate
     */
    public void setX(double initX) {
        super.setX(initX);
    }

    /**
     * @param initY - the new y - coordinate
     */
    public void setY(double initY) {
        super.setY(initY);
    }

    /**
     * @return - if the orbit needs to be stopped
     */
    public synchronized boolean toPause() {
        return pause;
    }

    /**
     * Pauses the orbit
     */
    public synchronized void pause() {
        pause = true;
    }

    /**
     * Orbit calculated using regular euler formula
     * Uses Newton's Law of Universal Gravitation and Newton's Second Law of Motion to determine orbit parameters
     */
    public void run() {

        double time = 0;                                                        // keeps track of total steps

        double radiusDistance = this.getDistanceFromCentralBody();              // distance from the central body
        double distance = radiusDistance;
        double centralBodyMass = body.getMass();                                // mass of the central body

        relativeX = getX() - body.getX();                                       // updates relativeX
        relativeY = getY() - body.getY();                                       // updates relativeY

        double prevX = relativeX;                                               // stores the previous relativeX value
        double prevY = relativeY;                                               // stores the previous relativeY value

        double prevBodyX = body.getX();                                         // stores the previous x - coordinate of the central body
        double prevBodyY = body.getY();                                         // stores the previous y - coordinate of the central body

        double theta = Math.atan(relativeY / relativeX);                        // the angle relative to the x - axis

        /*
         * Acceleration calculated with Newton's Law of Gravitation and Newton's Second Law
         * (1) Force = ThisBodyMass * acceleration
         * (2) GravitationalForce = GravitationalConstant * OtherBodyMass * ThisBodyMass / (distance between the center of mass of the two bodies) ^ 2
         *
         * Using (1) and (2): acceleration = GravitationalConstant * OtherBodyMass / (distance between the center of mass of the two bodies) ^ 2
         */
        double acceleration = G * centralBodyMass / Math.pow(distance, 2);

        /*
         * Velocity calculated using the definition of centripetal acceleration
         *      acceleration = (centripetal velocity) ^ 2 / radius
         *      -> (centripetal velocity) = sqrt(acceleration * radius)
         */
        double velocity = Math.sqrt(acceleration * distance);

        double v_y = velocity * Math.cos(theta);                                // y - component of velocity
        double v_x = -velocity * Math.sin(theta);                               // x - component of velocity

        while (!toPause()) {                                                    // checks if orbit should continue

            relativeX += v_x * (dt / ds.speedControl);
            relativeY += v_y * (dt / ds.speedControl);

            theta = Math.atan(relativeY / relativeX);                           // updates theta

            if (relativeX < 0) {
                theta += Math.PI;
            }

            v_y = velocity * Math.cos(theta);                                   // updates y - component of velocity
            v_x = -velocity * Math.sin(theta);                                  // updates x - component of velocity

            time += dt;                                                         // updates total steps

            if (time % (dt * timeInterval) == 0) {                              // plots at a given time interval

                plot.addPoint(Color.black, getPointSize(),                      // adds a black point at the previous location
                        prevX / AU / divisor + prevBodyX / body.getDivisor() / AU, prevY / AU / divisor + prevBodyY / body.getDivisor() / AU);

                plot.addPoint(this.color, getPointSize(),                       // adds new point
                        relativeX / AU / divisor + body.getX() / body.getDivisor() / AU, relativeY / AU / divisor + body.getY() / body.getDivisor() / AU);

                setX(body.getX() + relativeX);                                  // updates x value
                setY(body.getY() + relativeY);                                  // updates y value
                prevX = relativeX;                                              // updates previous x value
                prevY = relativeY;                                              // updates previous y value
                prevBodyX = body.getX();                                        // updates x value of central body
                prevBodyY = body.getY();                                        // updates y value of central body
                plot.repaint();                                                 // repaints the plot

            }

        }

    }


}

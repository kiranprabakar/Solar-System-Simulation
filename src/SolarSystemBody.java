/*
 * Lays the foundation for each body in the Solar System
 */

import java.awt.*;

/**
 * This is the parent class for all bodies that can be implemented.
 * Each body has a position and velocity that the solar system advances using Newtonian gravity
 */
public class SolarSystemBody implements SolarSystemInterface {

    private double diameter;                    // diameter of the body (meters)
    private String type;                        // what the body is
    private double distanceFromCentralBody;     // semi-major axis of the orbit around the central body (meters)
    private double mass;                        // mass of the body (kilograms)
    private String name;                        // name of the body
    private Color color;                        // the color used to draw the body
    private int pointSize;                      // the minimum size of the body on the display (pixels)

    private double x, y;                        // position of the body (meters)
    private double vx, vy;                      // velocity of the body (meters / second)
    private double ax, ay;                      // acceleration of the body (meters / second ^ 2)

    private double orbitalPeriod;               // time for one orbit around the central body (seconds), 0 if the body does not orbit

    /*
     * The trail stores recent positions relative to the central body, sampled evenly over the last orbit
     */
    private final double[] trailX = new double[trailLength];
    private final double[] trailY = new double[trailLength];
    private int trailCount, trailHead;          // number of stored trail points and where the next one goes
    private double nextTrailTime;               // simulation time at which the next trail point is recorded

    /**
     * Creates an instance of a body in the solar system, located at the origin and at rest
     *
     * @param name - name of the body
     * @param diameter - diameter of the body
     * @param distanceFromCentralBody - semi-major axis of the orbit around the central body
     * @param mass - mass of the body
     * @param color - the color used to draw the body
     * @param pointSize - the minimum size of the body on the display
     */
    public SolarSystemBody(String name, double diameter, double distanceFromCentralBody, double mass, Color color, int pointSize) {
        this.name = name;
        this.type = null;
        this.diameter = diameter;
        this.distanceFromCentralBody = distanceFromCentralBody;
        this.mass = mass;
        this.color = color;
        this.pointSize = pointSize;
    }

    /**
     * Places the body in an orbit around its central body, starting at the closest point of the orbit (periapsis)
     *
     * @param parent - the central body
     * @param eccentricity - how elliptical the orbit is (0 is a circle)
     * @param dirX - x - direction of the starting point from the central body
     * @param dirY - y - direction of the starting point from the central body
     */
    public void placeInOrbit(SolarSystemBody parent, double eccentricity, int dirX, int dirY) {

        double a = distanceFromCentralBody;                         // semi-major axis
        double periapsis = a * (1 - eccentricity);                  // closest distance to the central body
        double mu = G * (parent.getMass() + mass);                  // gravitational parameter of the pair

        /*
         * Vis-viva equation: speed ^ 2 = mu * (2 / r - 1 / a), which at periapsis is mu * (1 + e) / periapsis
         */
        double speed = Math.sqrt(mu * (1 + eccentricity) / periapsis);

        x = parent.getX() + dirX * periapsis;
        y = parent.getY() + dirY * periapsis;
        vx = parent.getVx() - dirY * speed;                         // perpendicular to the direction, so the orbit is counter-clockwise
        vy = parent.getVy() + dirX * speed;

        orbitalPeriod = 2 * Math.PI * Math.sqrt(a * a * a / mu);    // Kepler's third law

    }

    /**
     * @return - the body this one orbits, or null if it does not orbit anything
     */
    public SolarSystemBody getParent() {
        return null;
    }

    /**
     * Updates the velocity using the current acceleration (half of a velocity Verlet step)
     *
     * @param h - the time step (seconds)
     */
    public void kick(double h) {
        vx += ax * h;
        vy += ay * h;
    }

    /**
     * Updates the position using the current velocity
     *
     * @param h - the time step (seconds)
     */
    public void drift(double h) {
        x += vx * h;
        y += vy * h;
    }

    /**
     * Resets the acceleration before the forces are summed
     */
    public void clearAcceleration() {
        ax = 0;
        ay = 0;
    }

    /**
     * @param dax - x - acceleration to add
     * @param day - y - acceleration to add
     */
    public void addAcceleration(double dax, double day) {
        ax += dax;
        ay += day;
    }

    /**
     * @param dvx - x - velocity to add
     * @param dvy - y - velocity to add
     */
    public void addVelocity(double dvx, double dvy) {
        vx += dvx;
        vy += dvy;
    }

    /**
     * Records the current position relative to the central body if it is time for a new trail point
     *
     * @param simTime - the current simulation time (seconds)
     */
    public void recordTrail(double simTime) {

        SolarSystemBody parent = getParent();

        if (parent == null || orbitalPeriod <= 0 || simTime < nextTrailTime) {
            return;
        }

        trailX[trailHead] = x - parent.getX();
        trailY[trailHead] = y - parent.getY();
        trailHead = (trailHead + 1) % trailLength;
        trailCount = Math.min(trailCount + 1, trailLength);
        nextTrailTime = simTime + orbitalPeriod / trailLength;     // spreads the trail points evenly over one orbit

    }

    /**
     * @return - the number of stored trail points
     */
    public int getTrailCount() {
        return trailCount;
    }

    /**
     * @param i - trail point index, where 0 is the oldest
     * @return - the x - coordinate of the trail point relative to the central body
     */
    public double getTrailX(int i) {
        return trailX[(trailHead - trailCount + i + trailLength) % trailLength];
    }

    /**
     * @param i - trail point index, where 0 is the oldest
     * @return - the y - coordinate of the trail point relative to the central body
     */
    public double getTrailY(int i) {
        return trailY[(trailHead - trailCount + i + trailLength) % trailLength];
    }

    /**
     * @return - the body's type
     */
    public String getType() {
        return this.type;
    }

    /**
     * @param type - type of body
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return - the diameter of the body
     */
    public double getDiameter() {
        return this.diameter;
    }

    /**
     * @return - semi-major axis of the orbit around the central body
     */
    public double getDistanceFromCentralBody() {
        return this.distanceFromCentralBody;
    }

    /**
     * @return - the mass
     */
    public double getMass() {
        return this.mass;
    }

    /**
     * @return - name of the body
     */
    public String retName() {
        return this.name;
    }

    /**
     * @return - the color used to draw the body
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return - the minimum size of the body on the display
     */
    public int getPointSize() {
        return pointSize;
    }

    /**
     * @return - time for one orbit around the central body (seconds), 0 if the body does not orbit
     */
    public double getOrbitalPeriod() {
        return orbitalPeriod;
    }

    /**
     * @return - x - coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * @return - y - coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * @return - x - velocity
     */
    public double getVx() {
        return vx;
    }

    /**
     * @return - y - velocity
     */
    public double getVy() {
        return vy;
    }

}

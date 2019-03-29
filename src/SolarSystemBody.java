/*
 * Lays the foundation for each body in the Solar System
 */

/**
 * This is the parent class for all bodies that can be implemented.
 * Implements Runnable to allow for Multithreading operations
 */
public class SolarSystemBody implements SolarSystemInterface, Runnable {

    private double diameter;                    // diameter of the body
    private String type;                        // what the body is
    private double distanceFromCentralBody;     // distance from the center of the orbit
    private double mass;                        // mass of the body
    private String name;                        // name fo the body
    private double x;                           // x-coordinate of the body
    private double y;                           // y-coordinate of the body
    private boolean toPause;                    // determines if the orbit should be paused
    private double divisor;                     // how much the x and y coordinates should be divided by for better visibility

    /**
     * @param name - name of the body
     * @param diameter - diameter of the body
     * @param distanceFromCentralBody - distance from the center of the orbit
     * @param mass - mass of the body
     * @param initX - the initial x-coordinate of the body (true distance / AU)
     * @param initY - the initial y-coordinate of the body (true distance / AU)
     *
     *              Creates an instance of a body in the solar system
     */
    public SolarSystemBody(String name, double diameter, double distanceFromCentralBody, double mass, double initX, double initY) {
        this.name = name;
        this.type = null;
        this.diameter = diameter;
        this.distanceFromCentralBody = distanceFromCentralBody;
        this.mass = mass;
        x = initX * AU;
        y = initY * AU;
        toPause = false;
        divisor = 1;            // default divisor value
    }

    /**
     * @return - the divisor
     */
    public double getDivisor() {
        return divisor;
    }

    /**
     * @return - the body's type
     */
    public String getType() {
        return this.type;
    }

    /**
     * @return - the diameter of the body
     */
    public double getDiameter() {
        return this.diameter;
    }

    /**
     * @return - distance from the center of the orbit
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
     * @param type - type of body
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @param x - x - coordinate
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @param y - y - coordinate
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return - checks if orbit needs to stop
     */
    public synchronized boolean toPause() {
        return toPause;
    }

    /**
     * pauses the orbit
     */
    public synchronized void pause() {
        toPause = true;
    }

    /**
     * Will be implemented based on which type of body is in use
     */
    public void run() {

    }

}

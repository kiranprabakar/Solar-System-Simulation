/*
 * Lays the foundation for each body in the Solar System
 */

public class SolarSystemBody implements SolarSystemInterface, Runnable {

    private double diameter;
    private String type;
    private double distanceFromCentralBody;
    private double mass;
    private String name;
    private double x;
    private double y;
    private boolean toPause;
    private double divisor;

    public SolarSystemBody(String name, double diameter, double distanceFromCentralBody, double mass, double initX, double initY) {
        this.name = name;
        this.type = null;
        this.diameter = diameter;
        this.distanceFromCentralBody = distanceFromCentralBody;
        this.mass = mass;
        x = initX * AU;
        y = initY * AU;
        toPause = false;
        divisor = 1;
    }

    public double getDivisor() {
        return divisor;
    }

    public String getType() {
        return this.type;
    }

    public double getDiameter() {
        return this.diameter;
    }

    public double getDistanceFromCentralBody() {
        return this.distanceFromCentralBody;
    }

    public double getMass() {
        return this.mass;
    }

    public String retName() {
        return this.name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return getDiameter() / 2;
    }

    public double getVolume() {
        return 4 * 1.0 / 3 * Math.PI * Math.pow(getRadius(), 3);
    }

    public double getDensity() {
        return getMass() / getVolume();
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public boolean equals(SolarSystemBody other) {
        return other.getType().equals(getType()) && other.getDiameter() == getDiameter()
                && other.getDistanceFromCentralBody() == getDistanceFromCentralBody() && other.getMass() == getMass();
    }

    public synchronized boolean toPause() {
        return toPause;
    }

    public synchronized void pause() {
        toPause = true;
    }

    public void run() {

    }

}

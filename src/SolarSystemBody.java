/*
 * Lays the foundation for each body in the Solar System
 */

public class SolarSystemBody implements SolarSystemInterface, Runnable {

    private double diameter;
    private String type;
    private double distanceFromCentralBody;
    private double mass;
    private String name;

    public SolarSystemBody(String name, double diameter, double distanceFromCentralBody, double mass) {
        this.name = name;
        this.type = null;
        this.diameter = diameter;
        this.distanceFromCentralBody = distanceFromCentralBody;
        this.mass = mass;
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

    public boolean equals(SolarSystemBody other) {
        return other.getType().equals(getType()) && other.getDiameter() == getDiameter()
                && other.getDistanceFromCentralBody() == getDistanceFromCentralBody() && other.getMass() == getMass();
    }

    public void run() {

    }

}

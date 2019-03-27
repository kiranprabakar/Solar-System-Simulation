/*
 * Creates a Planet based on parameters
 */

import java.awt.*;
import java.text.DecimalFormat;

public class Planet extends SolarSystemBody {

    private boolean isHabitable;
    private boolean toPause;

    /*
     * Following variables are for the run() method
     */
    private Star star;
    private SolarSystemPlot plot;

    private Color color;
    private int pointSize;
    private DataStorage ds;

    private double divisor;

    public Planet(String name, double diameter, double distanceFromStar, double mass, Star star,
                  SolarSystemPlot plot, Color color, double initX, double initY, DataStorage ds) {
        super(name, diameter, distanceFromStar, mass, initX, initY);
        setType("Planet");
        this.star = star;
        //this.isHabitable = isHabitable(this.star.getHabitableZoneLowerBound(), this.star.getHabitableZoneUpperBound());
        this.plot = plot;
        this.color = color;
        toPause = false;

        this.ds = ds;

        setPointSize();
        setDivisor(distanceFromStar);

    }

    /*public boolean isHabitable(double lowerBound, double upperBound) {
        return this.getDistanceFromCentralBody() > lowerBound
                && this.getDistanceFromCentralBody() < upperBound;
    }*/

    public void setDivisor(double distance) {

        if (ds.planetDistancefromCentralBody.indexOf(distance) <= 3) {
            divisor = innerPlanetDivisor;
        } else if (ds.planetDistancefromCentralBody.indexOf(distance) <= 5) {
            divisor = outerPlanetDivisor;
        } else {
            divisor = nepUrDivisor;
        }

    }

    public double getDivisor() {
        return divisor;
    }

    public void setPointSize() {

        int i = ds.planetNames.indexOf(retName());
        int s = ds.planetNames.size();
        pointSize = ds.planetPointSizes.get(i);

    }

    public Star getStar() {
        return star;
    }

    public SolarSystemPlot getSolarSystemPlot() {
        return plot;
    }

    public synchronized double getX() {
        return super.getX();
    }

    public synchronized double getY() {
        return super.getY();
    }

    public synchronized Color getColor() {
        return color;
    }

    public synchronized void setX(double initX) {
        super.setX(initX);
    }

    public synchronized void setY(double initY) {
        super.setY(initY);
    }

    public void setStar(Star star) {
        this.star = star;
    }

    public void setPlot(SolarSystemPlot plot) {
        this.plot = plot;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public synchronized void pause() {
        toPause = true;
    }

    public synchronized boolean isToPause() {
        return toPause;
    }

    public int getPointSize() {
        return pointSize;
    }

    /*
     * Orbit calculated using regular euler formula
     * Possibilities for improvement:
     *     Range-Kutta (Improved Euler) to solve this
     *     Midpoint method to solve this (refer to simulation manual)
     *  Will need to implement an error checker for the sake of full orbits
     *
     *  Can use vis viva equation (AAE 251) v^2  = GM(2 / r - 1 / a)
     *      a = semi-major axis
     *      r = distance from center of central body to point on orbit (r = sqrt(x^2 + y^2))
     *
     *  Might use Verlet Algorithm instead of vis viva
     */
    public void run() {

        double time = 0;
        //double dt = SolarSystemInterface.dt / 100;

        double distance = this.getDistanceFromCentralBody();
        double starMass = star.getMass();

        double prevX = getX();
        double prevY = getY();

        double theta = Math.atan(getY() / getX());

        double acceleration = G * starMass / Math.pow(distance, 2);       //Acceleration constant (for now ...)

        double a_y = acceleration * Math.sin(theta);
        double a_x = acceleration * Math.cos(theta);
        double prevAx;
        double prevAy;

        double velocity = Math.sqrt(acceleration * distance);

        //double tangentialSpeed = Math.sqrt(acceleration * radiusDistance);   //Tangenital velocity constant (for now ...)
        double v_y = velocity * Math.cos(theta);
        double v_x = -velocity * Math.sin(theta);

        int count = 0;

        while (!isToPause()) {


            setX(getX() + v_x * (dt / ds.speedControl));
            setY(getY() + v_y * (dt / ds.speedControl));

            theta = Math.atan(getY() / getX());

            if (getX() < 0) {
                theta += Math.PI;
            }

            v_y = velocity * Math.cos(theta);
            v_x = -velocity * Math.sin(theta);

            time += dt;

            //This solves the blinking plot problem by only plotting fewer times
            if (time % (SolarSystemInterface.dt * ds.timeInterval) == 0) {

                plot.addPoint(Color.black, pointSize, prevX / AU / divisor, prevY / AU / divisor);
                plot.addPoint(this.color, pointSize, getX() / AU / divisor, getY() / AU / divisor);
                prevX = getX();
                prevY = getY();
                plot.repaint();

            }


            /*
             * This is the Verlet Algorithm implementation
             * Will likely come back to once threads and plotter are debugged
             */
            /*
            setX(getX() + v_x * dt + 0.5 * a_x * Math.pow(dt, 2));
            setY(getY() + v_y * dt + 0.5 * a_y * Math.pow(dt, 2));

            v_x += 0.5 * a_x * dt;
            v_y  += 0.5 * a_y * dt;

            distance = Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2));
            theta = Math.atan(getY() / getX());

            if (getX() < 0) {
                theta += Math.PI;
            }

            prevAx = a_x;
            prevAy = a_y;
            acceleration = G * starMass / Math.pow(distance, 2);
            a_y = -acceleration * Math.sin(theta);
            a_x = -acceleration * Math.cos(theta);

            v_x += 0.5 * (prevAx + a_x) * dt;
            v_y += 0.5 * (prevAy + a_y) * dt;

            /*if (!toPause()) {
                time += SolarSystemInterface.dt;
            }*/
        }

        Thread.currentThread().interrupt();

    }

}
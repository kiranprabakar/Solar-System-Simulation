/*
 * Creates a Planet based on parameters
 */

import java.awt.*;
import java.text.DecimalFormat;

public class Planet extends SolarSystemBody {

    private boolean isHabitable;

    /*
     * Following variables are for the run() method
     */
    private Star star;
    private SolarSystemPlot plot;
    private double initX;
    private double initY;

    private Color color;

    public Planet(String name, double diameter, double distanceFromStar, double mass, Star star, SolarSystemPlot plot, Color color, double initX, double initY) {
        super(name, diameter, distanceFromStar, mass);
        setType("Planet");
        this.star = star;
        this.isHabitable = isHabitable(this.star.getHabitableZoneLowerBound(), this.star.getHabitableZoneUpperBound());
        this.plot = plot;
        this.initX = initX;
        this.initY = initY;
        this.color = color;
    }

    public boolean isHabitable(double lowerBound, double upperBound) {
        return getDistanceFromStar() > lowerBound
                && getDistanceFromStar() < upperBound;
    }

    public boolean getHabitability() {
        return this.isHabitable;
    }

    public Star getStar() {
        return star;
    }

    public SolarSystemPlot getSolarSystemPlot() {
        return plot;
    }

    public double getInitX() {
        return initX;
    }

    public double getInitY() {
        return initY;
    }

    public void setInitX(double initX) {
        this.initX = initX;
    }

    public void setInitY(double initY) {
        this.initY = initY;
    }

    public boolean equals(Planet other) {
        return super.equals(other) && other.getHabitability() == getHabitability();
    }

    /*
     * Orbit calculated using regular euler formula
     * Possibilities for improvement:
     *     Range-Kutta (Improved Euler) to solve this
     *     Midpoint method to solve this (refer to simulation manual)
     *  Will need to imlement an error checker for the sake of full orbits
     *
     *  Can use vis viva equation (AAE 251) v^2 = GM(2 / r - 1 / a)
     *      a = semi-major axis
     *      r = distance from center pof central body to point on orbit (r = sqrt(x^2 + y^2))
     *
     */
    public void orbit() {

        double  dt = 1000;
        double  timeLimit = 100000000;
        double time = 0;

        double radiusDistance = this.getDistanceFromStar();
        double starMass = star.getMass();

        double x = initX * 1.496 * Math.pow(10,11);
        double y = initY * 1.496 * Math.pow(10,11);
        double theta = Math.atan(y / x);

        final double acceleration = G * starMass / Math.pow(radiusDistance, 2);       //Acceleration constant (for now ...)
        double a_y = acceleration * Math.cos(theta);
        double a_x = -acceleration * Math.sin(theta);

        final double tangentialSpeed = Math.sqrt(acceleration * radiusDistance);   //Tangenital velocity constant (for now ...)
        double v_y = tangentialSpeed * Math.cos(theta);
        double v_x = -tangentialSpeed * Math.sin(theta);

        DecimalFormat df = new DecimalFormat("#0.0");
        //DecimalFormat df2 = new DecimalFormat("0.0000");

        //mv^2/r = GmM/r^2
        while (time <= timeLimit) {

            //System.out.print("Time: " + df.format(time) + "\tX: " + x + "\tY: " + y + "\tTheta: " + df2.format(theta) + "\tCheck: " + (v_x < 0) + "\n");

           // plot.addPoint(Color.black, x / 1.496 / Math.pow(10,11), y / 1.496 / Math.pow(10,11));

            //plot.setColor(this.color);
            //plot.setPointSize(20/*getDiameter() / 1.496 / Math.pow(10,11)*/);

            plot.addPoint(this.color, 10, x / 1.496 / Math.pow(10,11), y / 1.496 / Math.pow(10,11));

            //plot.setPointSize(3);

            plot.addPoint(Color.black, 10, x / 1.496 / Math.pow(10,11), y / 1.496 / Math.pow(10,11));

            //plot.addPoint(Color.yellow, 3, x / 1.496 / Math.pow(10,11), y / 1.496 / Math.pow(10,11));

            //System.out.print("Name: " + retName() + "---Time: " + df.format(time) + "\tX: " + x + "\tY: " + y + "\n");

            x += v_x * dt;
            y += v_y * dt;

            theta = Math.atan(y / x);

            if (x < 0) {
                theta += Math.PI;
            }

            v_y = tangentialSpeed * Math.cos(theta);
            v_x = -tangentialSpeed * Math.sin(theta);

            time += dt;

            synchronized (star) {
                star.notify();
            }

        }

        //atomicBoolean.set(false);

        System.out.println(retName() + " is done!");

    }

    public void run() {
        orbit();
    }

}
import java.awt.*;

public class Projectile1 {

    private final double g = 9.8;

    private double a_y = -g;  //acceleration due to gravity
    private double initialVelocity;
    private double dt;
    private int initialHeight;
    private double time;

    public Projectile1(double dt, int initialHeight, double initialVelocity, double time) {
        this.dt = dt;
        this.initialHeight = initialHeight;
        this.initialVelocity = initialVelocity;
        this.time = time;
    }

    public void simulate() {
        double y = this.initialHeight;
        double v_y = this.initialVelocity;
        double time = this.time;
        SolarSystemPlot plot = new SolarSystemPlot("Projectile", 0, 5, 0, 20);
        while (y > 0) {
            //plot.addPoint(Color.black, time, y);
            y += v_y * this.dt;
            v_y += this.a_y * this.dt;
            time += dt;
        }
    }

    public static void main(String[] args) {
        Projectile1 projectile = new Projectile1(0.1, 10, 0, 0);
        projectile.simulate();
    }
}

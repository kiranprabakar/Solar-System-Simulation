import java.awt.*;
import java.text.DecimalFormat;

public class Satellite extends SolarSystemBody {

    private SolarSystemBody body;
    private SolarSystemPlot plot;
    private Color color;

    public Satellite(String name, double diameter, double distanceFromPlanet, double mass,
                     SolarSystemBody body, SolarSystemPlot plot, Color color, double initX, double initY) {
        super(name, diameter, distanceFromPlanet, mass, initX, initY);
        setType("Satellite");
        this.body = body;
        this.plot = plot;
        this.color = color;
    }

    public double getDistanceFromPlanet() {
        return getDistanceFromCentralBody();
    }

    public SolarSystemBody getBody() {
        return body;
    }

    public SolarSystemPlot getPlot() {
        return plot;
    }

    public Color getColor() {
        return color;
    }

    public double getX() {
        return super.getX();
    }

    public double getY() {
        return super.getY();
    }

    public void setX(double initX) {
        super.setX(initX);
    }

    public void setY(double initY) {
        super.setY(initY);
    }

    public synchronized void orbit() {

        double time = 0;

        double radiusDistance = this.getDistanceFromCentralBody();
        double distance = radiusDistance;
        double centralBodyMass = body.getMass();

        double relativeX = getX() - body.getX();
        double relativeY = getY() - body.getY();

        System.out.println(relativeX / AU);
        System.out.println(relativeY / AU);

        double prevX = relativeX;
        double prevY = relativeY;

        double theta = Math.atan(relativeY / relativeX);

        double acceleration = G * centralBodyMass / Math.pow(distance, 2);       //Acceleration constant (for now ...)
        double a_y = acceleration * Math.sin(theta);
        double a_x = acceleration * Math.cos(theta);

        double velocity = Math.sqrt(acceleration * distance);

        //double tangentialSpeed = Math.sqrt(acceleration * radiusDistance);   //Tangenital velocity constant (for now ...)
        double v_y = velocity * Math.cos(theta);
        double v_x = -velocity * Math.sin(theta);

        int count = 0;

        //mv^2/r = GmM/r^2
        while (time <= timeLimit) {

            relativeX += v_x * (dt / 100);
            relativeY += v_y * (dt / 100);

            theta = Math.atan(relativeY / relativeX);

            if (relativeX < 0) {
                theta += Math.PI;
            }

            v_y = velocity * Math.cos(theta);
            v_x = -velocity * Math.sin(theta);

            time += dt;

            /*
             * This solves the blinking plot problem by only plotting fewer times
             */
            if (time % (dt * 1000000) == 0) {
                //plot.addPoint(Color.black, 5, (prevX + body.getX()) / AU, (prevY + body.getY()) / AU);
                plot.addPoint(Color.black, 10, prevX / AU + 2, prevY / AU + 2);
                plot.addPoint(this.color, 10, relativeX / AU + 2, relativeX / AU + 2);
                //plot.addPoint(this.color, 5, (relativeX + body.getX()) / AU, (relativeX + body.getY()) / AU);
                prevX = relativeX;
                prevY = relativeY;
                plot.repaint();
            }

        }

        System.out.println(retName() + " is done!");

    }

    public void run() {
        orbit();
    }


}

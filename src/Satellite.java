import java.awt.*;

public class Satellite extends SolarSystemBody {

    private SolarSystemBody body;
    private SolarSystemPlot plot;
    private Color color;

    public Satellite(String name, double diameter, double distanceFromBody, double mass,
                     SolarSystemBody body, SolarSystemPlot plot, Color color, double initX, double initY) {
        super(name, diameter, distanceFromBody, mass, initX, initY);
        setType("Satellite");
        this.body = body;
        this.plot = plot;
        this.color = color;
    }

    public double getDistanceFromBody() {
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

        double prevX = relativeX;
        double prevY = relativeY;

        double prevBodyX = body.getX();
        double prevBodyY = body.getY();

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
        while (true) {

            relativeX += v_x * (dt);
            relativeY += v_y * (dt);

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

                plot.addPoint(Color.black, 5, prevX / AU + prevBodyX / AU, prevY / AU + prevBodyY / AU);
                plot.addPoint(this.color, 5, relativeX / AU + body.getX() / AU, relativeY / AU + body.getY() / AU);
                prevX = relativeX;
                prevY = relativeY;
                prevBodyX = body.getX();
                prevBodyY = body.getY();
                plot.repaint();

            }

        }
    }

    public void run() {
        orbit();
    }


}

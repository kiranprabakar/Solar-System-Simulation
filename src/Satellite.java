import java.awt.*;

public class Satellite extends SolarSystemBody {

    private SolarSystemBody body;
    private SolarSystemPlot plot;
    private Color color;
    private boolean pause;
    private DataStorage ds;
    private int pointSize;
    private double divisor;

    double relativeX, relativeY;

    public Satellite(String name, double diameter, double distanceFromBody, double mass,
                     SolarSystemBody body, SolarSystemPlot plot, Color color, double initX, double initY, DataStorage ds) {
        super(name, diameter, distanceFromBody, mass, initX, initY);
        this.body = body;
        this.plot = plot;
        this.color = color;
        this.ds = ds;
        relativeX = getX() - body.getX();
        relativeY = getY() - body.getY();
        setType();
        setPointSize();
        setDivisor();
    }

    public void setDivisor() {
        if (getType().equals("Satellite")) {
            divisor = satelliteDivisor;
        } else {
            divisor = moonDivisor;
        }
    }

    private void setType() {
        int index = ds.satelliteNames.indexOf(retName());
        String string = ds.satelliteType.get(index);
        super.setType(string);
    }

    public double getDivisor() {
        return divisor;
    }

    public void setPointSize() {
        int i = ds.satelliteNames.indexOf(retName());
        if (i < 0) {
            //Add code if name does not exist
        } else {
            pointSize = ds.satellitePointSizes.get(i);
        }
    }

    public int getPointSize() {
        return pointSize;
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

    public synchronized boolean toPause() {
        return pause;
    }

    public synchronized void pause() {
        pause = true;
    }

    public void run() {
        double time = 0;

        double radiusDistance = this.getDistanceFromCentralBody();
        double distance = radiusDistance;
        double centralBodyMass = body.getMass();

        relativeX = getX() - body.getX();
        relativeY = getY() - body.getY();

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

        //mv^2/r = GmM/r^2
        while (!toPause()) {

            relativeX += v_x * (dt / ds.speedControl);
            relativeY += v_y * (dt / ds.speedControl);

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
            if (time % (dt * ds.timeInterval) == 0) {

                plot.addPoint(Color.black, getPointSize(),
                        prevX / AU / divisor + prevBodyX / body.getDivisor() / AU, prevY / AU / divisor + prevBodyY / body.getDivisor() / AU);

                plot.addPoint(this.color, getPointSize(),
                        relativeX / AU / divisor + body.getX() / body.getDivisor() / AU, relativeY / AU / divisor + body.getY() / body.getDivisor() / AU);

                setX(relativeX);
                setY(relativeY);
                prevX = relativeX;
                prevY = relativeY;
                prevBodyX = body.getX();
                prevBodyY = body.getY();
                plot.repaint();

            }

        }

        Thread.currentThread().interrupt();

    }


}

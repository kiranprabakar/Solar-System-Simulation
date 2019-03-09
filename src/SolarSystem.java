import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.*;
import static java.lang.System.exit;

public class SolarSystem implements SolarSystemInterface {

    private Star star;
    private HashMap<String, Planet> planets;
    private HashMap<String, Satellite> satellites;
    private SolarSystemPlot plot;

    private ArrayList<String> planetNames = new ArrayList<>(Arrays.asList("Mercury", "Venus", "Earth",
            "Mars", "Jupiter", "Saturn", "Uranus", "Neptune"));
    private ArrayList<Double> diameters = new ArrayList<>(Arrays.asList(4.7894E6, 12.104E6, 12.742E6,
            6.779E6, 139.82E6, 116.46E6, 50.724E6, 49.244E6));
    private ArrayList<Double> distancefromCentralBody = new ArrayList<>(Arrays.asList(57.91E9, 108.2E9, 149.6E9,
            227.9E9, 778.5E9, 1.434E12, 2.871E12, 4.495E12));
    private ArrayList<Double> mass = new ArrayList<>(Arrays.asList(3.285E23, 4.867E24, 5.972E24,
            6.39E23, 1.898E27, 5.683E26, 8.681E25, 1.024E25));
    private ArrayList<Color> colors = new ArrayList<>(Arrays.asList(Color.gray, Color.magenta, Color.blue,
            Color.red, Color.orange, Color.pink, Color.cyan, Color.blue));


    public SolarSystem() {
        this.star = null;
        this.planets = new HashMap<>();
        this.satellites = new HashMap<>();
        plot = null;
    }

    public SolarSystem(Star star, HashMap<String, Planet> planets, HashMap<String, Satellite> satellites, SolarSystemPlot plot) {
        this.star = star;
        this.planets = planets;
        this.satellites = satellites;
        this.plot = plot;
    }

    public void addPlanet(Planet planet) throws SolarSystemException {

        if (planets.putIfAbsent(planet.retName(), planet) != null) {
            throw new SolarSystemException("Planet already exists!");
        }

    }

    public void removePlanet(Planet planet) throws SolarSystemException {

        if (planets.remove(planet.retName()) == null) {
            throw new SolarSystemException("Planet does not exist!");
        }

    }

    public void addSatellite(Satellite satellite) throws SolarSystemException {

        if (satellites.putIfAbsent(satellite.retName(), satellite) != null) {
            throw new SolarSystemException("Satellite already exists!");
        }

    }

    public void removeSatellite(Satellite satellite) throws SolarSystemException {

        if (satellites.remove(satellite.retName()) == null) {
            throw new SolarSystemException("Satellite does not exist!");
        }

    }

    public void addStar(Star star) throws SolarSystemException {

        if (this.star != null) {
            throw new SolarSystemException("Star already exists!");
        }

        this.star = star;

    }

    public Star getStar() throws SolarSystemException {

        if (star == null) {
            throw new SolarSystemException("No star in the system yet!");
        }

        return star;

    }

    public void addPlot(SolarSystemPlot plot) throws SolarSystemException {
        if (plot != null) {
            throw new SolarSystemException("Plot already exists!");
        }
        this.plot = plot;
    }



    public HashMap<String, Planet> getPlanets() {
        return this.planets;
    }

    public HashMap<String, Satellite> getSatellites() {
        return satellites;
    }

    /*
     * Probably need to look at Graphics2D vs Canvas for the plotting
     */

    public static void main(String[] args) {

        SolarSystem solarSystem = new SolarSystem();

        SolarSystemGUI gui = new SolarSystemGUI(solarSystem);

        SolarSystemPlot plot = new SolarSystemPlot("Orbit of Planets", -5, 5,
                -5, 5);

        try {
            solarSystem.addPlot(plot);
        } catch (SolarSystemException ss) {
            ss.printStackTrace();
        }

        try {
            solarSystem.addStar(new Star("Sun",1.391016 * Math.pow(10,9), 1.989 * Math.pow(10,30), 4.83, "G", plot, Color.yellow));
        }
        catch (SolarSystemException ss) {
            ss.printStackTrace();
            exit(0);
        }

        /*
         * Only gonna worry about 1 planet for now while I debug this paint stuff
         */
        Planet earth = null, mars = null, mercury = null;
        try {
            earth = new Planet("Earth", 12.742 * Math.pow(10, 6), 149.6 * Math.pow(10, 9), 5.972 * Math.pow(10, 24),
                    solarSystem.getStar(), plot, plot.colors[0], 0, 1);
            mars = new Planet("Mars", 6786604, 1.5 * 149.6 * Math.pow(10, 9), 0.11 * 5.972 * Math.pow(10, 24),
                    solarSystem.getStar(), plot, plot.colors[1], 1.5, 0);
            mercury = new Planet("Mercury", 4877922, 0.39 * 149.6 * Math.pow(10, 9), 3.285 * Math.pow(10, 23),
                    solarSystem.getStar(), plot, plot.colors[5], 0.39, 0);
        } catch (SolarSystemException ss) {
            ss.printStackTrace();
        }

        //public Satellite(String name, double diameter, double distanceFromPlanet, double mass,
        //                     SolarSystemBody body, SolarSystemPlot plot, Color color, double initX, double initY)
        //Need to redo the moon initial x
        //True distance from earth: 384400000 meters
        Moon moon = new Moon("Moon", (1737.4 * 2) * 1000, 0.05 * AU,
                earth.getMass() * 1.2298E-1, earth, plot, Color.GRAY, 0, 0.05 + earth.getY()/AU);

        try {
            solarSystem.addPlanet(earth);
            solarSystem.addPlanet(mercury);
            solarSystem.addPlanet(mars);
        } catch (SolarSystemException ss) {
            ss.printStackTrace();
            exit(0);
        }

        try {
            solarSystem.addSatellite(moon);
        } catch (SolarSystemException ss) {
            ss.printStackTrace();
            exit(0);
        }

        class SolarThreadFactory implements ThreadFactory {

            int size = 0;

            public Thread newThread(Runnable r) {
                size++;
                return new Thread(r);
            }

        }

        SolarThreadFactory threads = new SolarThreadFactory();

        ExecutorService executorService = Executors.newFixedThreadPool(solarSystem.getPlanets().size() + solarSystem.getSatellites().size() + 1, threads);

        try {
            executorService.execute(threads.newThread(solarSystem.getStar()));
        } catch (SolarSystemException ss) {
            ss.printStackTrace();
        }

        for (String name : solarSystem.getPlanets().keySet()) {
            executorService.execute(threads.newThread(solarSystem.getPlanets().get(name)));
        }

        for (String name : solarSystem.getSatellites().keySet()) {
            executorService.execute(threads.newThread(solarSystem.getSatellites().get(name)));
        }

        executorService.shutdown();

    }

}
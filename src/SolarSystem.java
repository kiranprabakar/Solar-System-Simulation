import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.*;
import static java.lang.System.exit;
import static java.lang.System.in;

public class SolarSystem implements SolarSystemInterface {

    private Star star;
    private HashMap<String, Planet> planets;
    private HashMap<String, Satellite> satellites;
    private SolarSystemPlot plot;
    private DataStorage ds;


    public SolarSystem() {
        this.star = null;
        this.planets = new HashMap<>();
        this.satellites = new HashMap<>();
        plot = null;
        ds = null;
    }

    public SolarSystem(Star star, HashMap<String, Planet> planets, HashMap<String, Satellite> satellites, SolarSystemPlot plot) {
        this.star = star;
        this.planets = planets;
        this.satellites = satellites;
        this.plot = plot;
    }

    public Planet newPlanet(String name) {

        int index = ds.planetNames.indexOf(name);

        if (index < 0) {
            //Add code to add new planet here (also will need to update database here)
        }

        return new Planet(name, ds.planetDiameters.get(index), ds.planetDistancefromCentralBody.get(index),
                ds.planetMass.get(index), star, plot, ds.planetColors.get(index),
                ds.planetXCoordinateSection.get(index) * ds.planetDistancefromCentralBody.get(index) / AU,
                ds.planetYCoordinateSection.get(index) * ds.planetDistancefromCentralBody.get(index) / AU);

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
        if (this.plot != null) {
            throw new SolarSystemException("Plot already exists!");
        }
        this.plot = plot;
    }

    public void addDataStorage(DataStorage ds) throws SolarSystemException {
        if (this.ds != null) {
            throw new SolarSystemException("DataStorage already exists!");
        }
        this.ds = ds;
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

        DataStorage dataStorage = new DataStorage();

        try {
            solarSystem.addPlot(plot);
            solarSystem.addDataStorage(dataStorage);
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

        Planet earth = solarSystem.newPlanet("Earth");
        Planet mars = solarSystem.newPlanet("Mars");
        Planet mercury = solarSystem.newPlanet("Mercury");

        //Need to redo the moon initial x
        //True distance from earth: 384400000 meters
        Satellite moon = new Satellite("Moon", (1737.4 * 2) * 1000, 0.05 * AU,
                earth.getMass() * 1.2298E-1, earth, plot, Color.RED, earth.getX() + 0.05, earth.getY());

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
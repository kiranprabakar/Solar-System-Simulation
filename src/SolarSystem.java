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

    private ExecutorService executorService;

    private boolean stopSimulation, startSimulation;


    public SolarSystem() {
        this.star = null;
        this.planets = new HashMap<>();
        this.satellites = new HashMap<>();
        plot = null;
        ds = null;

        executorService = null;

        stopSimulation = false;
        startSimulation = false;
    }

    public SolarSystem(Star star, HashMap<String, Planet> planets, HashMap<String, Satellite> satellites, SolarSystemPlot plot) {
        this.star = star;
        this.planets = planets;
        this.satellites = satellites;
        this.plot = plot;

        executorService = null;

        stopSimulation = false;
        startSimulation = false;
    }

    public Planet newPlanet(String name) {

        int index = ds.planetNames.indexOf(name);

        if (index < 0) {
            //Add code to add new planet here (also will need to update database here)
        }

        Planet planet = new Planet(name, ds.planetDiameters.get(index), ds.planetDistancefromCentralBody.get(index),
                ds.planetMass.get(index), star, plot, ds.planetColors.get(index),
                ds.planetXCoordinateSection.get(index) * ds.planetDistancefromCentralBody.get(index) / AU,
                ds.planetYCoordinateSection.get(index) * ds.planetDistancefromCentralBody.get(index) / AU);

        ds.satelliteCentralBody.add(planet);

        return planet;

    }

    public Star newStar(String name) {

        int index = ds.starNames.indexOf(name);

        if (index < 0) {
            //Add code to add new star here (also will need to update database here)
        }

        return new Star(name, ds.starDiameters.get(index), ds.starMass.get(index), plot, ds.starColors.get(index));

    }

    public Satellite newSatellite(String name) throws SolarSystemException {

        int index = ds.satelliteNames.indexOf(name);

        if (index < 0) {
            //Add code to add new star here (also will need to update database here)
        }

        double distance;

        if ((distance = ds.satelliteDistancefromCentralBody.get(index)) < (0.05 * AU)) {
            //Gotta make plot point bigger
        }

        if (ds.satelliteCentralBody.size() == 0) {
            return null;
        }

        boolean found = false;
        Planet planet = null;

        for (int i = 0; i < ds.satelliteCentralBody.size(); i++) {
            if (ds.satelliteCentralBody.get(i).retName().equals(ds.satelliteCentralBodyNames.get(index))) {
                found = true;
                planet = ds.satelliteCentralBody.get(i);
                break;
            }
        }

        if (!found) {
            throw new SolarSystemException();
        }

        return new Satellite(name, ds.satelliteDiameters.get(index), ds.satelliteDistancefromCentralBody.get(index),
                ds.satelliteMass.get(index), planet, plot, ds.satelliteColors.get(index),
                ds.satelliteXCoordinateSection.get(index) * ds.satelliteDistancefromCentralBody.get(index) / AU,
                ds.satelliteYCoordinateSection.get(index) * ds.satelliteDistancefromCentralBody.get(index) / AU);


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

    public SolarSystemPlot getPlot() {
        return plot;
    }

    public void setStartSimulation(boolean startSimulation) {
        this.startSimulation = startSimulation;
    }

    public boolean isStartSimulation() {
        return startSimulation;
    }

    public void setStopSimulation(boolean stopSimulation) {
        this.stopSimulation = stopSimulation;
    }

    public boolean isStopSimulation() {
        return stopSimulation;
    }

    public HashMap<String, Planet> getPlanets() {
        return this.planets;
    }

    public HashMap<String, Satellite> getSatellites() {
        return satellites;
    }

    public void startSimulation(boolean stopSimulation) {

        class SolarThreadFactory implements ThreadFactory {

            int size = 0;

            public Thread newThread(Runnable r) {
                size++;
                return new Thread(r);
            }

        }

        SolarThreadFactory threads = new SolarThreadFactory();

        executorService = Executors.newFixedThreadPool(bodyLimit, threads);

        try {
            executorService.execute(threads.newThread(getStar()));
        } catch (SolarSystemException ss) {
            ss.printStackTrace();
        }

        for (String name : getPlanets().keySet()) {
            executorService.execute(threads.newThread(getPlanets().get(name)));
        }

        for (String name : getSatellites().keySet()) {
            executorService.execute(threads.newThread(getSatellites().get(name)));
        }

    }

    public void stopSimulation() {

        try {
            getStar().pause();

        } catch (SolarSystemException ss) {
            ss.printStackTrace();
        }

        for (String name : getPlanets().keySet()) {
            getPlanets().get(name).pause();
        }

        for (String name : getSatellites().keySet()) {
            getSatellites().get(name).pause();
        }

        executorService.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            executorService.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }

        plot.clearThePlot();
        plot.repaint();

        star = null;

        planets = new HashMap<>();

        satellites = new HashMap<>();

    }


    public ExecutorService getExecutorService() {
        return executorService;
    }


    /**
     * Main method
     * Initiate and run from this class only
     **/

    public static void main(String[] args) {

        SolarSystem solarSystem = new SolarSystem();

        SolarSystemGUI gui = new SolarSystemGUI(solarSystem);

        //Title, xMin, xMax, yMin, yMax
        SolarSystemPlot plot = new SolarSystemPlot("Orbit of Planets", -coordinateMax, coordinateMax,
                -coordinateMax, coordinateMax);

        DataStorage dataStorage = new DataStorage();

        try {
            solarSystem.addPlot(plot);
            solarSystem.addDataStorage(dataStorage);
        } catch (SolarSystemException ss) {
            ss.printStackTrace();
        }

    }

}

import javax.net.ssl.SSLException;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

/**
 * The solar system will handle all of the bodies included
 */
public class SolarSystem implements SolarSystemInterface {

    private Star star;                                      // the star
    private HashMap<String, Planet> planets;                // the planets
    private HashMap<String, Satellite> satellites;          // the satellites
    private SolarSystemPlot plot;                           // displays the solar system
    private DataStorage ds;                                 // the data store associated with the solar system
    private ExecutorService executorService;                // will be used to execute all of the threads

    /**
     * Creates the solar system
     */
    public SolarSystem() {

        this.star = null;
        this.planets = new HashMap<>();
        this.satellites = new HashMap<>();
        plot = null;
        ds = null;
        executorService = null;

    }

    /**
     * @return - the star
     * @throws SolarSystemException - if the star has not been created yet
     */
    public Star getStar() throws SolarSystemException {

        if (star == null) {
            throw new SolarSystemException("No star in the system yet!");
        }

        return star;

    }

    /**
     * @return - the planets
     */
    public HashMap<String, Planet> getPlanets() {
        return this.planets;
    }

    /**
     * @return - the satellites
     */
    public HashMap<String, Satellite> getSatellites() {
        return satellites;
    }

    /**
     * @return - the plot
     */
    public SolarSystemPlot getPlot() {
        return plot;
    }

    /**
     * @return - the data store
     */
    public DataStorage getDs() {
        return ds;
    }


    /**
     * @return - the object that executes the threads
     */
    public ExecutorService getExecutorService() {
        return executorService;
    }



    /**
     * Only called if the star is one of the default ones provided
     *
     * @param name - name of the star
     * @return - a new star
     * @throws SolarSystemException - if an error occurs
     */
    public Star newStar(String name) throws SolarSystemException {

        int index = ds.starNames.indexOf(name);                     // finds the star name in the data store

        if (index < 0) {                                            // throws an exception if the star name cannot be found
            throw new SolarSystemException("Star does not exist!");
        }

        return new Star(name, ds.starDiameters.get(index), ds.starMass.get(index), plot, ds.starColors.get(index), ds); // creates a new star

    }

    /**
     * Only called if the planet is one of the default ones provided
     *
     * @param name - name of the planet
     * @return - a new planet
     * @throws SolarSystemException - if an error occurs
     */
    public Planet newPlanet(String name) throws SolarSystemException {

        int index = ds.planetNames.indexOf(name);                               // the index of the planet name in the data store

        if (index < 0) {                                                        // throw an exception if the planet name is not found
            throw new SolarSystemException("Planet does not exist!");
        }

        Planet planet = new Planet(name, ds.planetDiameters.get(index), ds.planetDistancefromCentralBody.get(index),    // creates the planet
                ds.planetMass.get(index), star, plot, ds.planetColors.get(index),
                ds.planetXCoordinateSection.get(index) * ds.planetDistancefromCentralBody.get(index) / AU,
                ds.planetYCoordinateSection.get(index) * ds.planetDistancefromCentralBody.get(index) / AU, ds);

        ds.satelliteCentralBody.add(planet);                                    // allows a satellite to orbit this planet
        ds.satelliteCentralBodyNames.add(planet.retName());

        return planet;

    }

    /**
     * Only called if the satellite is one of the default ones provided
     *
     * @param name - satellite name
     * @return - a new satellite
     * @throws SolarSystemException - if an error occurs
     */
    public Satellite newSatellite(String name) throws SolarSystemException {

        int index = ds.satelliteNames.indexOf(name);                            // the index of the planet name in the data store

        if (index < 0) {                                                        // throw an exception if the planet name is not found
            alert("Satellite does not exist");
            throw new SolarSystemException("Satellite does not exist!");
        }

        if (ds.satelliteCentralBody.indexOf(getPlanets().get(ds.satelliteCentralBodyNames.get(index))) < 0) {    // throws an exception if the central planet has not been added
            alert("The planet has not been added yet!");
            throw new SolarSystemException("The planet has not been added yet!");
        }

        Planet planet = null;

        for (int i = 0; i < ds.satelliteCentralBody.size(); i++) {              // finds the planet that the satellite orbits
            if (ds.satelliteCentralBody.get(i).retName().equals(ds.satelliteCentralBodyNames.get(index))) {
                planet = ds.satelliteCentralBody.get(i);
                break;
            }
        }

        return new Satellite(name, ds.satelliteDiameters.get(index), ds.satelliteDistancefromCentralBody.get(index),        // creates a new satellite
                ds.satelliteMass.get(index), planet, plot, ds.satelliteColors.get(index),
                ds.satelliteXCoordinateSection.get(index) * (ds.satelliteDistancefromCentralBody.get(index)) / AU + planet.getX() / AU,
                ds.satelliteYCoordinateSection.get(index) * (ds.satelliteDistancefromCentralBody.get(index)) / AU + planet.getY() / AU, ds);


    }

    /**
     * Creates a new star that is not already one of the default ones provided
     *
     * @param characteristics - a comma - separated string inputted by the user
     * @return - a new star
     * @throws SolarSystemException - if an error occurs
     */
    public Star createCustomStar(String characteristics) throws SolarSystemException {

        if (characteristics == null || characteristics.length() == 0) {             // throws an exception if no characteristic string was entered
            throw new SolarSystemException("No characteristic String found!");
        }

        String[] attributes = characteristics.split(",");                     // the list of attributes derived from the characteristics provided

        if (attributes.length != 4) {                                               // checks if user inputted the correct number of attributes
            alert("Wrong number of characteristics entered!");
            throw new SolarSystemException("Wrong number of characterisitcs entered!");
        }

        for (int i = 1; i < attributes.length; i++) {                               // makes sure all attributes can be read properly
            attributes[i] = attributes[i].substring(1);
        }

        String name = attributes[0];
        double diameter, mass;

        try {                                                                       // throws an exception if a non - double value was entered
            diameter = Double.parseDouble(attributes[1]);
            mass = Double.parseDouble(attributes[2]);
        } catch (Exception e) {
            alert("The diameter and mass fields must both be doubles!");
            throw new SolarSystemException("The diameter and mass fields must both be doubles!");
        }

        Color color = null;

        for (int i = 0; i < starTypes.length; i++) {                                // gets the color for the given type
            if (attributes[3].equals(starTypes[i])) {
                color = starColors[i];
                break;
            }
        }

        if (color == null) {                                                        // throws an exception if the color cannot be found,
                                                                                    // indicating that the type was entered incorrectly
            alert("Star type is invalid!");
            throw new SolarSystemException("Star type is invalid!");
        }

        ds.starNames.add(name);                                                     // updates the data store as necessary
        ds.starDiameters.add(diameter);
        ds.starMass.add(mass);
        ds.starColors.add(color);

        switch (attributes[3]) {                                                    // updates the point sizes that correspond to the star types

            case "Main sequence":
                ds.starPointSizes.add(15);
                break;
            case "Red giant":
                ds.starPointSizes.add(20);
                break;
            case "White dwarf":
                ds.starPointSizes.add(10);
                break;
            default:
                alert("Invalid type!");
                throw new SolarSystemException("Invalid type!");

        }

        return new Star(name, diameter, mass, plot, color, ds);                     // creates a new star

    }

    /**
     * Creates a new planet that is not already one of the default ones provided
     *
     * @param characteristics - a comma - separated string inputted by the user
     * @return - a new planet
     * @throws SolarSystemException - if an error occurs
     */
    public Planet createCustomPlanet(String characteristics) throws SolarSystemException {

        if (characteristics == null || characteristics.length() == 0) {             // throws an exception if no characteristic string was entered
            throw new SolarSystemException("No characteristic String found!");
        }

        String[] attributes = characteristics.split(",");

        if (attributes.length != 5) {                                               // checks if user inputted the correct number of attributes
            alert("Wrong number of characterisitcs entered!");
            throw new SolarSystemException("Wrong number of characterisitcs entered!");
        }

        if (ds.planetNames.indexOf(attributes[0]) >= 0) {
            alert("Planet with the same name already exists!");
            throw new SolarSystemException("Planet with the same name already exists!");
        }

        for (int i = 1; i < attributes.length; i++) {                               // makes sure all attributes can be read properly
            attributes[i] = attributes[i].substring(1);
        }

        if (ds.planetNames.indexOf(attributes[4]) < 0) {                            // checks if the similar planet exists
            alert("Similar planet does not exist!");
            throw new SolarSystemException("Similar planet does not exist!");
        }

        String name = attributes[0];
        double diameter, dist, mass;

        try {                                                                       // throws an exception if a non - double value was entered
            diameter = Double.parseDouble(attributes[1]);

            dist = Double.parseDouble(attributes[2]);

            mass = Double.parseDouble(attributes[3]);
        } catch (Exception e) {
            alert("The diameter, distance, and mass fields must all be doubles!");
            throw new SolarSystemException("The diameter, distance, and mass fields must all be doubles!");
        }

        Color color = ds.planetColors.get(ds.planetNames.indexOf(attributes[4]));   // gets the color for the given type

        ds.planetNames.add(name);                                                   // updates the data store as necessary
        ds.planetDiameters.add(diameter);
        ds.planetDistancefromCentralBody.add(dist);
        ds.planetMass.add(mass);
        ds.planetColors.add(color);
        ds.planetXCoordinateSection.add(1);
        ds.planetYCoordinateSection.add(0);
        ds.planetPointSizes.add(ds.planetPointSizes.get(ds.planetNames.indexOf(attributes[4])));

        int index = ds.planetNames.indexOf(name);

        Planet planet = new Planet(name, diameter, dist, mass, star, plot, color,           // creates a new planet
                ds.planetXCoordinateSection.get(index) * ds.planetDistancefromCentralBody.get(index) / AU,
                ds.planetYCoordinateSection.get(index) * ds.planetDistancefromCentralBody.get(index) / AU, ds);

        ds.satelliteCentralBody.add(planet);                                        // allows satellites to orbit this planet
        ds.satelliteCentralBodyNames.add(name);

        return planet;

    }

    /**
     * Creates a new satellite that is not already one of the default ones provided
     *
     * @param characteristics - a comma - separated string inputted by the user
     * @return - a new satellite
     * @throws SolarSystemException - if an error occurs
     */
    public Satellite createCustomSatellite(String characteristics) throws SolarSystemException {

        if (characteristics == null || characteristics.length() == 0) {             // throws an exception if no characteristic string was entered
            throw new SolarSystemException("No characteristic String found!");
        }

        String[] attributes = characteristics.split(",");

        if (attributes.length != 7) {                                               // checks if user inputted the correct number of attributes
            alert("Wrong number of characterisitcs entered!");
            throw new SolarSystemException("Wrong number of characterisitcs entered!");
        }

        if (ds.satelliteNames.indexOf(attributes[0]) >= 0) {
            alert("Satellite with the same name already exists!");
            throw new SolarSystemException("Satellite with the same name already exists!");
        }

        for (int i = 1; i < attributes.length; i++) {                               // makes sure all attributes can be read properly
            attributes[i] = attributes[i].substring(1);
        }

        String name = attributes[0];

        double diameter, dist, mass;

        try {                                                                       // throws an exception if a non - double value was entered

            diameter = Double.parseDouble(attributes[1]);

            dist = Double.parseDouble(attributes[2]);

            mass = Double.parseDouble(attributes[3]);

        } catch (Exception e) {
            alert("The diameter, distance, and mass fields must all be doubles!");
            throw new SolarSystemException("The diameter, distance, and mass fields must all be doubles!");
        }

        Color color;

        switch(attributes[5]) {                                                     // gets the color based on user input

            case "White":
                color = Color.white;
                break;
            case "Gray":
                color = Color.white;
                break;
            default:
                alert("Invalid Color!");
                throw new SolarSystemException("Invalid Color!");

        }

        String type = attributes[6];                                                // whether the body is a satellite or a moon

        int index = ds.planetNames.indexOf(attributes[4]);

        if (index < 0) {                                                            // checks if the central planet exists
            alert("Planet does not exist");
            throw new SolarSystemException("Planet does not exist!");
        }

        int ind = ds.satelliteCentralBodyNames.indexOf(ds.planetNames.get(index));

        if (ind < 0) {                                                              // occurs if the planet can be used for satellites, but has not been
                                                                                    // added to the correct data store location
            alert("Planet has not been added yet!");
            throw new SolarSystemException("Planet has not been added yet!");

        }

        boolean found = false;                                                      // checks if the planet has been added

        for (int i = 0; i < ds.satelliteCentralBody.size(); i++) {                  // looks through the planets that can have satellites orbiting them

            if (ds.satelliteCentralBody.get(i).retName().equals(ds.satelliteCentralBodyNames.get(ind))) {   // set found to true if found
                found = true;
            }

        }

        if (!found) {                                                               // alert the user if planet has not been added yet
            alert("Planet has not been added yet!");
            throw new SolarSystemException("Planet has not been added yet!");
        }

        Planet planet;
        if (ind > 1) {
            planet = ds.satelliteCentralBody.get(ind - 2);                       // gets the planet that the satellite orbits
        } else {
            planet = ds.satelliteCentralBody.get(ind);
        }

        ds.satelliteNames.add(name);                                               // updates the data store as necessary
        ds.satelliteType.add(type);
        ds.satelliteDiameters.add(diameter);
        ds.satelliteDistancefromCentralBody.add(dist);
        ds.satelliteMass.add(mass);
        ds.satelliteColors.add(color);
        ds.satelliteXCoordinateSection.add(0);
        ds.satelliteYCoordinateSection.add(1);
        ds.satellitePointSizes.add(3);

        Satellite satellite = new Satellite(name, diameter, dist, mass, planet, plot, color,            // creates a new satellite
                ds.satelliteXCoordinateSection.get(ind) * (ds.satelliteDistancefromCentralBody.get(ind)) / AU + planet.getX() / AU,
                ds.satelliteYCoordinateSection.get(ind) * (ds.satelliteDistancefromCentralBody.get(ind)) / AU + planet.getY() / AU, ds);

        return satellite;

    }


    /**
     * Sets the solar system's star
     *
     * @param star - the star to add
     * @throws SolarSystemException - if a star exists
     */
    public void addStar(Star star) throws SolarSystemException {

        if (this.star != null) {
            alert("Star already exists!");
            throw new SolarSystemException("Star already exists!");
        }

        this.star = star;

    }

    /**
     * Adds planet to the collection of planets
     *
     * @param planet - the planet to be added
     * @throws SolarSystemException - if the planet already exists
     */
    public void addPlanet(Planet planet) throws SolarSystemException {

        if (planets.putIfAbsent(planet.retName(), planet) != null) {
            alert("Planet already exists!");
            throw new SolarSystemException("Planet already exists!");
        }

    }

    /**
     * Adds satellite to the collection of satellites
     *
     * @param satellite - the satellite to be added
     * @throws SolarSystemException - if the satellite already exists
     */
    public void addSatellite(Satellite satellite) throws SolarSystemException {

        if (satellites.putIfAbsent(satellite.retName(), satellite) != null) {
            alert("Satellite already exists!");
            throw new SolarSystemException("Satellite already exists!");
        }

    }


    /**
     * Sets the plot for the solar system
     *
     * @param plot - the plot to add
     * @throws SolarSystemException - if a plot already exists
     */
    public void addPlot(SolarSystemPlot plot) throws SolarSystemException {
        if (this.plot != null) {
            throw new SolarSystemException("Plot already exists!");
        }
        this.plot = plot;
    }

    /**
     * Sets the data storage for the solar system
     *
     * @param ds - the data store to add
     * @throws SolarSystemException - if a data store already exists
     */
    public void addDataStorage(DataStorage ds) throws SolarSystemException {
        if (this.ds != null) {
            throw new SolarSystemException("DataStorage already exists!");
        }
        this.ds = ds;
    }


    /**
     * Starts the simulation
     */
    public void startSimulation() {

        /*
         * Creates a factory that creates all the threads in the solar system
         */
        class SolarThreadFactory implements ThreadFactory {

            int size = 0;                   // size of the factory

            public Thread newThread(Runnable r) {   // creates the thread
                size++;
                return new Thread(r);
            }

        }

        SolarThreadFactory threads = new SolarThreadFactory();                  // creates a new factory for threads

        executorService = Executors.newFixedThreadPool(ds.bodyLimit, threads);  // creates the executor service

        try {
            executorService.execute(threads.newThread(getStar()));              // executes the star
        } catch (SolarSystemException ss) {
            ss.printStackTrace();
        }

        for (String name : getPlanets().keySet()) {                             // executes the planets
            executorService.execute(threads.newThread(getPlanets().get(name)));
        }

        for (String name : getSatellites().keySet()) {                          // executes the satellites
            executorService.execute(threads.newThread(getSatellites().get(name)));
        }

    }

    /**
     * Stops the simulation and clears the display
     *
     * @param started - whether the simulation has been started or not
     */
    public void stopSimulation(boolean started) {

        if (started) {
            try {
                getStar().pause();                              // stops the star

            } catch (SolarSystemException ss) {
                ss.printStackTrace();
            }

            for (String name : getPlanets().keySet()) {         // stops the planets
                getPlanets().get(name).pause();
            }

            for (String name : getSatellites().keySet()) {      // stops the satellites
                getSatellites().get(name).pause();
            }

            executorService.shutdown();                         // disables new tasks from being submitted
            try {
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {      // wait for threads that exists to stop running
                    executorService.shutdownNow();                                          // shutdown all currently executing tasks
                    if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {  // wait again
                        System.err.println("Pool did not terminate");                       // print error message if termination failed
                    }
                }
            } catch (InterruptedException ie) {
                executorService.shutdownNow();                                              // shutdown if need to
                Thread.currentThread().interrupt();                                         // set current thread's interrupted status
            }

        }

        plot.clearThePlot();                                                                // gets rif of all the points on the display
        plot.repaint();                                                                     // repaints the display

        star = null;                                                                        // gets rid of the star

        planets = new HashMap<>();                                                          // gets rid of the planets

        satellites = new HashMap<>();                                                       // gets rid of the satellites

        ds = new DataStorage();

    }

    /**
     * Slows down the simulation by increasing the speed control by a factor of 2
     */
    public void slowSimulation() {

        ds.speedControl *= 2;

    }

    /**
     * Speeds up the simulation by decreasing the speed control by a factor of 2
     */
    public void speedUpSimulation() {

        ds.speedControl /= 2;

    }


    /**
     * Alerts the user of something
     *
     * @param s - text that will be alerted
     */
    public void alert(String s) {

        JOptionPane.showMessageDialog(null, s);

    }

}
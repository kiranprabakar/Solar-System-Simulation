
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The solar system will handle all of the bodies included and advance them with Newtonian gravity
 */
public class SolarSystem implements SolarSystemInterface {

    private Star star;                                      // the star
    private HashMap<String, Planet> planets;                // the planets
    private HashMap<String, Satellite> satellites;          // the satellites
    private SolarSystemPlot plot;                           // displays the solar system
    private DataStorage ds;                                 // the data store associated with the solar system

    private Timer timer;                                    // advances the simulation once per frame, null when stopped
    private boolean paused;                                 // whether the simulation has started but is frozen
    private double simTime;                                 // simulated time since the start (seconds)
    private long lastFrameTime;                             // when the previous frame ran (nanoseconds)
    private double achievedTimeScale;                       // simulated seconds per real second over the last frame
    private boolean speedCapped;                            // whether the last frame hit the step limit

    private List<SolarSystemBody[]> collisions = new ArrayList<>();     // pairs of bodies found touching in the last force calculation
    private String eventMessage;                            // a recent event to show on the display, such as a collision
    private long eventMessageExpires;                       // when the event message stops being shown (nanoseconds)

    /**
     * Creates the solar system
     */
    public SolarSystem() {

        this.star = null;
        this.planets = new LinkedHashMap<>();               // keeps the order the bodies were added in
        this.satellites = new LinkedHashMap<>();
        plot = null;
        ds = null;
        timer = null;

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
     * @return - every body in the solar system: the star, then the planets, then the satellites
     */
    public List<SolarSystemBody> getBodies() {

        List<SolarSystemBody> bodies = new ArrayList<>();

        if (star != null) {
            bodies.add(star);
        }

        bodies.addAll(planets.values());
        bodies.addAll(satellites.values());

        return bodies;

    }

    /**
     * @return - whether the simulation has started (it may be paused)
     */
    public boolean isRunning() {
        return timer != null;
    }

    /**
     * @return - whether the simulation has started but is frozen
     */
    public boolean isPaused() {
        return paused;
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

        return new Star(name, ds.starDiameters.get(index), ds.starMass.get(index), ds.starColors.get(index), ds.starPointSizes.get(index));

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

        requireOutside(name, ds.planetDiameters.get(index), ds.planetDistancefromCentralBody.get(index),               // the orbit cannot pass through the star
                ds.planetEccentricities.get(index), star);

        Planet planet = new Planet(name, ds.planetDiameters.get(index), ds.planetDistancefromCentralBody.get(index),    // creates the planet
                ds.planetMass.get(index), star, ds.planetColors.get(index), ds.planetPointSizes.get(index));

        planet.placeInOrbit(star, ds.planetEccentricities.get(index),                                                   // starts the planet's orbit
                ds.planetXCoordinateSection.get(index), ds.planetYCoordinateSection.get(index));

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

        int index = ds.satelliteNames.indexOf(name);                            // the index of the satellite name in the data store

        if (index < 0) {                                                        // throw an exception if the satellite name is not found
            alert("Satellite does not exist");
            throw new SolarSystemException("Satellite does not exist!");
        }

        Planet planet = getPlanets().get(ds.satelliteCentralBodyNames.get(index));     // the planet that the satellite orbits

        if (planet == null) {                                                   // throws an exception if the central planet has not been added
            alert("The planet has not been added yet!");
            throw new SolarSystemException("The planet has not been added yet!");
        }

        requireOutside(name, ds.satelliteDiameters.get(index), ds.satelliteDistancefromCentralBody.get(index),         // the orbit cannot pass through the planet
                ds.satelliteEccentricities.get(index), planet);

        Satellite satellite = new Satellite(name, ds.satelliteType.get(index), ds.satelliteDiameters.get(index),       // creates a new satellite
                ds.satelliteDistancefromCentralBody.get(index), ds.satelliteMass.get(index), planet,
                ds.satelliteColors.get(index), ds.satellitePointSizes.get(index));

        satellite.placeInOrbit(planet, ds.satelliteEccentricities.get(index),                                           // starts the satellite's orbit
                ds.satelliteXCoordinateSection.get(index), ds.satelliteYCoordinateSection.get(index));

        return satellite;

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
            throw new SolarSystemException("Wrong number of characteristics entered!");
        }

        for (int i = 0; i < attributes.length; i++) {                               // removes surrounding spaces so all attributes can be read properly
            attributes[i] = attributes[i].trim();
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

        requirePositive(diameter, mass);

        Color color = null;
        int pointSize = 0;

        for (int i = 0; i < starTypes.length; i++) {                                // gets the color and point size for the given type
            if (attributes[3].equals(starTypes[i])) {
                color = starColors[i];
                pointSize = starTypePointSizes[i];
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
        ds.starPointSizes.add(pointSize);

        return new Star(name, diameter, mass, color, pointSize);                    // creates a new star

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
            alert("Wrong number of characteristics entered!");
            throw new SolarSystemException("Wrong number of characteristics entered!");
        }

        for (int i = 0; i < attributes.length; i++) {                               // removes surrounding spaces so all attributes can be read properly
            attributes[i] = attributes[i].trim();
        }

        if (ds.planetNames.indexOf(attributes[0]) >= 0) {
            alert("Planet with the same name already exists!");
            throw new SolarSystemException("Planet with the same name already exists!");
        }

        int similar = ds.planetNames.indexOf(attributes[4]);                        // the default planet with a similar composition

        if (similar < 0) {                                                          // checks if the similar planet exists
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

        requirePositive(diameter, dist, mass);

        requireOutside(name, diameter, dist, 0, star);                              // the orbit cannot pass through the star

        Color color = ds.planetColors.get(similar);                                 // gets the color for the given type

        ds.planetNames.add(name);                                                   // updates the data store as necessary
        ds.planetDiameters.add(diameter);
        ds.planetDistancefromCentralBody.add(dist);
        ds.planetMass.add(mass);
        ds.planetColors.add(color);
        ds.planetXCoordinateSection.add(1);
        ds.planetYCoordinateSection.add(0);
        ds.planetPointSizes.add(ds.planetPointSizes.get(similar));
        ds.planetEccentricities.add(0.0);

        int index = ds.planetNames.indexOf(name);

        Planet planet = new Planet(name, diameter, dist, mass, star, color, ds.planetPointSizes.get(index));    // creates a new planet

        planet.placeInOrbit(star, ds.planetEccentricities.get(index),
                ds.planetXCoordinateSection.get(index), ds.planetYCoordinateSection.get(index));

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
            alert("Wrong number of characteristics entered!");
            throw new SolarSystemException("Wrong number of characteristics entered!");
        }

        for (int i = 0; i < attributes.length; i++) {                               // removes surrounding spaces so all attributes can be read properly
            attributes[i] = attributes[i].trim();
        }

        if (ds.satelliteNames.indexOf(attributes[0]) >= 0) {
            alert("Satellite with the same name already exists!");
            throw new SolarSystemException("Satellite with the same name already exists!");
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

        requirePositive(diameter, dist, mass);

        Color color;

        switch(attributes[5]) {                                                     // gets the color based on user input

            case "White":
                color = Color.white;
                break;
            case "Gray":
                color = Color.gray;
                break;
            default:
                alert("Invalid Color!");
                throw new SolarSystemException("Invalid Color!");

        }

        String type = attributes[6];                                                // whether the body is a satellite or a moon

        if (!type.equals("Moon") && !type.equals("Satellite")) {                    // alerts user if the entered type is invalid
            alert("Invalid type!");
            throw new SolarSystemException("Invalid type!");
        }

        if (ds.planetNames.indexOf(attributes[4]) < 0) {                            // checks if the central planet exists
            alert("Planet does not exist");
            throw new SolarSystemException("Planet does not exist!");
        }

        Planet planet = getPlanets().get(attributes[4]);                            // gets the planet that the satellite orbits

        if (planet == null) {                                                       // alert the user if planet has not been added to the solar system yet
            alert("Planet has not been added yet!");
            throw new SolarSystemException("Planet has not been added yet!");
        }

        requireOutside(name, diameter, dist, 0, planet);                            // the orbit cannot pass through the planet

        ds.satelliteNames.add(name);                                               // updates the data store as necessary
        ds.satelliteType.add(type);
        ds.satelliteDiameters.add(diameter);
        ds.satelliteDistancefromCentralBody.add(dist);
        ds.satelliteMass.add(mass);
        ds.satelliteColors.add(color);
        ds.satelliteCentralBodyNames.add(planet.retName());
        ds.satelliteXCoordinateSection.add(0);
        ds.satelliteYCoordinateSection.add(1);
        ds.satellitePointSizes.add(3);
        ds.satelliteEccentricities.add(0.0);

        int index = ds.satelliteNames.size() - 1;                                  // index of the new satellite in the data store

        Satellite satellite = new Satellite(name, type, diameter, dist, mass, planet, color, ds.satellitePointSizes.get(index));    // creates a new satellite

        satellite.placeInOrbit(planet, ds.satelliteEccentricities.get(index),
                ds.satelliteXCoordinateSection.get(index), ds.satelliteYCoordinateSection.get(index));

        return satellite;

    }

    /**
     * Makes sure an orbit stays outside the body it goes around, so the orbiting body is not swallowed
     *
     * @param name - name of the orbiting body
     * @param diameter - diameter of the orbiting body
     * @param distance - semi-major axis of the orbit
     * @param eccentricity - how elliptical the orbit is
     * @param parent - the body being orbited
     * @throws SolarSystemException - if the orbit would touch the parent
     */
    private void requireOutside(String name, double diameter, double distance, double eccentricity,
                                SolarSystemBody parent) throws SolarSystemException {

        double closest = distance * (1 - eccentricity);                             // closest approach (periapsis)

        if (closest <= (parent.getDiameter() + diameter) / 2) {
            String message = String.format("%s cannot orbit %s: at its closest it would be %s from %s's center, "
                            + "but %s's radius is %s, so it would be swallowed!", name, parent.retName(),
                    formatDistance(closest), parent.retName(), parent.retName(), formatDistance(parent.getDiameter() / 2));
            alert(message);
            throw new SolarSystemException(message);
        }

    }

    /**
     * Makes sure every value entered is greater than zero
     *
     * @param values - the values to check
     * @throws SolarSystemException - if a value is zero or negative
     */
    private void requirePositive(double... values) throws SolarSystemException {

        for (double value : values) {
            if (!(value > 0) || Double.isInfinite(value)) {
                alert("The diameter, distance, and mass fields must all be greater than zero!");
                throw new SolarSystemException("The diameter, distance, and mass fields must all be greater than zero!");
            }
        }

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
        bodyAdded();

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

        bodyAdded();

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

        bodyAdded();

    }

    /**
     * Prepares the simulation and display after a body joins the solar system
     */
    private void bodyAdded() {

        if (isRunning()) {
            List<SolarSystemBody> bodies = getBodies();
            removeNetMomentum(bodies);                      // keeps the system from drifting off the display
            computeAccelerations(bodies);                   // the new body needs an acceleration before the next step
        } else {
            plot.fitToBodies(getBodies());                  // zooms the display to fit all planets
        }

        render();

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

        List<SolarSystemBody> bodies = getBodies();

        removeNetMomentum(bodies);
        computeAccelerations(bodies);

        lastFrameTime = System.nanoTime();
        timer = new Timer(frameDelay, e -> advanceFrame());     // runs on the user interface thread, so no other synchronization is needed
        timer.start();
        paused = false;

    }

    /**
     * Freezes the simulation, keeping every body where it is
     */
    public void pauseSimulation() {

        if (!isRunning() || paused) {
            return;
        }

        timer.stop();
        paused = true;
        render();

    }

    /**
     * Continues a paused simulation from where it stopped
     */
    public void resumeSimulation() {

        if (!isRunning() || !paused) {
            return;
        }

        lastFrameTime = System.nanoTime();                      // the time spent paused is not simulated
        timer.start();
        paused = false;

    }

    /**
     * Advances the simulation by the amount of time that matches the real time since the last frame, then redraws
     */
    private void advanceFrame() {

        long now = System.nanoTime();
        double realSeconds = Math.min((now - lastFrameTime) / 1E9, 0.1);   // a stalled frame does not cause a huge jump
        lastFrameTime = now;

        List<SolarSystemBody> bodies = getBodies();

        double targetTime = ds.timeScale * realSeconds;                     // how much time to simulate this frame
        double maxStep = maxTimeStep(bodies);

        int steps = Math.max(1, (int) Math.ceil(targetTime / maxStep));
        double h = targetTime / steps;                                      // the time step, as large as possible while still accurate

        speedCapped = steps > maxStepsPerFrame;

        if (speedCapped) {                                                  // too much work for one frame, so simulate less time instead
            steps = maxStepsPerFrame;
            h = maxStep;
        }

        for (int i = 0; i < steps; i++) {
            step(bodies, h);
            simTime += h;

            if (!collisions.isEmpty()) {                                    // merges bodies that hit each other
                resolveCollisions();
                bodies = getBodies();
            }

            for (SolarSystemBody body : bodies) {
                body.recordTrail(simTime);
            }
        }

        achievedTimeScale = realSeconds > 0 ? steps * h / realSeconds : 0;

        render();

    }

    /**
     * @param bodies - the bodies in the solar system
     * @return - the largest time step that keeps the fastest orbit accurate
     */
    private double maxTimeStep(List<SolarSystemBody> bodies) {

        double shortestPeriod = Double.POSITIVE_INFINITY;

        for (SolarSystemBody body : bodies) {
            if (body.getOrbitalPeriod() > 0) {
                shortestPeriod = Math.min(shortestPeriod, body.getOrbitalPeriod());
            }
        }

        return shortestPeriod / stepsPerOrbit;

    }

    /**
     * Advances every body by one time step using the velocity Verlet method, which keeps orbits stable over long runs
     *
     * @param bodies - the bodies in the solar system
     * @param h - the time step (seconds)
     */
    private void step(List<SolarSystemBody> bodies, double h) {

        for (SolarSystemBody body : bodies) {           // half a velocity update, then a full position update
            body.kick(h / 2);
            body.drift(h);
        }

        computeAccelerations(bodies);                   // the forces at the new positions

        for (SolarSystemBody body : bodies) {           // the other half of the velocity update
            body.kick(h / 2);
        }

    }

    /**
     * Sums the gravitational pull of every body on every other body
     *
     * Newton's Law of Gravitation and Newton's Second Law
     * (1) Force = ThisBodyMass * acceleration
     * (2) GravitationalForce = GravitationalConstant * OtherBodyMass * ThisBodyMass / distance ^ 2
     *
     * Using (1) and (2): acceleration = GravitationalConstant * OtherBodyMass / distance ^ 2, directed towards the other body
     *
     * @param bodies - the bodies in the solar system
     */
    private void computeAccelerations(List<SolarSystemBody> bodies) {

        for (SolarSystemBody body : bodies) {
            body.clearAcceleration();
        }

        collisions.clear();

        for (int i = 0; i < bodies.size(); i++) {
            SolarSystemBody a = bodies.get(i);

            for (int j = i + 1; j < bodies.size(); j++) {
                SolarSystemBody b = bodies.get(j);

                double dx = b.getX() - a.getX();
                double dy = b.getY() - a.getY();
                double distanceSquared = dx * dx + dy * dy;
                double touching = (a.getDiameter() + b.getDiameter()) / 2;

                if (distanceSquared < touching * touching) {        // the bodies' surfaces overlap
                    collisions.add(new SolarSystemBody[] {a, b});
                }

                if (distanceSquared == 0) {             // two bodies in the same spot have no defined direction
                    continue;
                }

                double factor = G / (distanceSquared * Math.sqrt(distanceSquared));     // G / distance ^ 3, which also turns (dx, dy) into a unit direction

                a.addAcceleration(factor * b.getMass() * dx, factor * b.getMass() * dy);
                b.addAcceleration(-factor * a.getMass() * dx, -factor * a.getMass() * dy);
            }
        }

    }

    /**
     * Merges each pair of colliding bodies: the star, or else the more massive body, absorbs the other
     */
    private void resolveCollisions() {

        for (SolarSystemBody[] pair : collisions) {

            List<SolarSystemBody> bodies = getBodies();

            if (!bodies.contains(pair[0]) || !bodies.contains(pair[1])) {      // one of them was already absorbed this step
                continue;
            }

            boolean firstWins = pair[0] instanceof Star || (!(pair[1] instanceof Star) && pair[0].getMass() >= pair[1].getMass());
            SolarSystemBody survivor = firstWins ? pair[0] : pair[1];
            SolarSystemBody absorbed = firstWins ? pair[1] : pair[0];

            survivor.absorb(absorbed);

            if (absorbed instanceof Planet) {
                planets.remove(absorbed.retName());
            } else if (absorbed instanceof Satellite) {
                satellites.remove(absorbed.retName());
            }

            for (Satellite satellite : satellites.values()) {          // moons of a destroyed body now orbit whatever absorbed it
                if (satellite.getParent() == absorbed) {
                    satellite.setBody(survivor);
                }
            }

            showEvent(absorbed.retName() + (survivor instanceof Star ? " was swallowed by " : " crashed into ") + survivor.retName() + "!");

        }

        collisions.clear();
        computeAccelerations(getBodies());              // the absorbed bodies no longer pull on anything

    }

    /**
     * Shows a message on the display for a few seconds
     *
     * @param message - the message to show
     */
    private void showEvent(String message) {
        eventMessage = message;
        eventMessageExpires = System.nanoTime() + 6_000_000_000L;
    }

    /**
     * Gives the system zero total momentum so its center of mass stays still instead of drifting away
     *
     * @param bodies - the bodies in the solar system
     */
    private void removeNetMomentum(List<SolarSystemBody> bodies) {

        double totalMass = 0, momentumX = 0, momentumY = 0;

        for (SolarSystemBody body : bodies) {
            totalMass += body.getMass();
            momentumX += body.getMass() * body.getVx();
            momentumY += body.getMass() * body.getVy();
        }

        if (totalMass == 0) {
            return;
        }

        for (SolarSystemBody body : bodies) {
            body.addVelocity(-momentumX / totalMass, -momentumY / totalMass);
        }

    }

    /**
     * Redraws the display with the current bodies and status
     */
    public void render() {

        String speed;

        if (paused) {
            speed = "PAUSED (1 s = " + formatDuration(ds.timeScale) + " when resumed)";
        } else if (isRunning()) {
            speed = "1 s = " + formatDuration(achievedTimeScale) + (speedCapped ? " (max for these bodies)" : "");
        } else {
            speed = "1 s = " + formatDuration(ds.timeScale) + " (not running)";
        }

        String status = String.format("Time: %.1f days (%.2f years)   |   %s", simTime / 86400, simTime / YEAR, speed);

        if (eventMessage != null && System.nanoTime() < eventMessageExpires) {
            status += "\n" + eventMessage;
        }

        plot.render(getBodies(), status);

    }

    /**
     * @param meters - a distance
     * @return - the distance in readable units
     */
    private static String formatDistance(double meters) {

        if (meters >= 0.01 * AU) {
            return String.format("%.2f AU", meters / AU);
        } else {
            return String.format("%,.0f km", meters / 1000);
        }

    }

    /**
     * @param seconds - a length of time
     * @return - the length of time in readable units
     */
    private static String formatDuration(double seconds) {

        if (seconds < 120) {
            return String.format("%.0f s", seconds);
        } else if (seconds < 7200) {
            return String.format("%.0f min", seconds / 60);
        } else if (seconds < 2 * 86400) {
            return String.format("%.1f h", seconds / 3600);
        } else if (seconds < YEAR) {
            return String.format("%.1f days", seconds / 86400);
        } else {
            return String.format("%.2f years", seconds / YEAR);
        }

    }

    /**
     * Stops the simulation and clears the display
     *
     * @param started - whether the simulation has been started or not
     */
    public void stopSimulation(boolean started) {

        if (timer != null) {
            timer.stop();                                   // no more frames will run
            timer = null;
        }

        paused = false;

        star = null;                                        // gets rid of the star

        planets = new LinkedHashMap<>();                    // gets rid of the planets

        satellites = new LinkedHashMap<>();                 // gets rid of the satellites

        ds = new DataStorage();

        simTime = 0;
        eventMessage = null;
        collisions.clear();

        plot.resetView();
        render();                                           // clears the display

    }

    /**
     * Slows down the simulation by a factor of 2
     *
     * @return - false if the simulation cannot go any slower
     */
    public boolean slowSimulation() {

        if (ds.timeScale / 2 < minTimeScale) {
            return false;
        }

        ds.timeScale /= 2;
        render();
        return true;

    }

    /**
     * Speeds up the simulation by a factor of 2
     *
     * @return - false if the simulation cannot go any faster
     */
    public boolean speedUpSimulation() {

        if (ds.timeScale * 2 > maxTimeScale) {
            return false;
        }

        ds.timeScale *= 2;
        render();
        return true;

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

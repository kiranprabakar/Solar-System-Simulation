import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.*;
import static java.lang.System.exit;

public class SolarSystem implements SolarSystemInterface {

    private Star star;
    private ArrayList<Planet> planets;

    public SolarSystem() {
        this.star = null;
        this.planets = new ArrayList<Planet>();
    }

    public SolarSystem(Star star, ArrayList<Planet> planets) {
        this.star = star;
        this.planets = planets;
    }

    public void addPlanet(Planet planet) throws SolarSystemException {

        int size = planets.size();

        for (int i = 0; i < size; i++) {
            if (planets.get(i).equals(planet)) {
                throw new SolarSystemException("Planet already exists!");
            }
        }

        planets.add(planet);
    }

    public void removePlanet(Planet planet) {
        planets.remove(planet);
    }

    public void addStar(Star star) throws SolarSystemException {
        if (this.star != null) {
            throw new SolarSystemException("Star already exists!");
        }
        this.star = star;
    }

    public Star getStar() {
        return this.star;
    }

    public ArrayList<Planet> getPlanets() {
        return this.planets;
    }

    public ArrayList<SolarSystemBody> getBodies() {
        ArrayList<SolarSystemBody> bodies = new ArrayList<>();
        for (int i = 0; i < planets.size(); i++) {
            bodies.add(planets.get(i));
        }
        bodies.add(getStar());

        return bodies;
    }

    /*
     * Probably need to look at Graphics2D vs Canvas for the plotting
     */

    public static void main(String[] args) {

        SolarSystem solarSystem = new SolarSystem();

        SolarSystemPlot plot = new SolarSystemPlot("Orbit of Planets", -5, 5,
                -5, 5);

        try {
            solarSystem.addStar(new Star("Sun",1.391016 * Math.pow(10,9), 1.989 * Math.pow(10,30), 4.83, "G", plot, Color.yellow));
        }
        catch (SolarSystemException ss) { }

        Planet earth = new Planet("Earth",12.742 * Math.pow(10,6), 149.6 * Math.pow(10,9), 5.972 * Math.pow(10,24),
                solarSystem.getStar(), plot, plot.colors[0], 0, 1);
        Planet mars = new Planet("Mars", 6786604, 1.5 * 149.6 * Math.pow(10,9), 0.11 * 5.972 * Math.pow(10,24),
                solarSystem.getStar(), plot, plot.colors[1],1.5, 0);
        Planet mercury = new Planet("Mercury",4877922, 0.39 * 149.6 * Math.pow(10,9), 3.285 * Math.pow(10,23),
                solarSystem.getStar(), plot, plot.colors[5], 0.39, 0);

        try {
            solarSystem.addPlanet(earth);
            solarSystem.addPlanet(mercury);
            solarSystem.addPlanet(mars);
        } catch (SolarSystemException ss) {
            System.out.println("Planet with the same characteristics already added!");
            exit(1);
        }

        class SolarThreadFactory implements ThreadFactory {

            int size = 0;

            public Thread newThread(Runnable r) {
                size++;
                return new Thread(r);
            }
        }

        SolarThreadFactory threads = new SolarThreadFactory();

        ExecutorService executorService = Executors.newFixedThreadPool(solarSystem.getPlanets().size() + 1, threads);

        executorService.execute(threads.newThread(solarSystem.getStar()));

        for (int i = 0; i < solarSystem.getPlanets().size(); i++) {
            executorService.execute(threads.newThread(solarSystem.getPlanets().get(i)));
        }

        executorService.shutdown();

    }

}
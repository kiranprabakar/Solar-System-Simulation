import java.awt.*;
import java.util.ArrayList;

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
        bodies.add(getStar());
        for (int i = 0; i < planets.size(); i++) {
            bodies.add(planets.get(i));
        }
        return bodies;
    }

    public static void main(String[] args) {

        SolarSystem solarSystem = new SolarSystem();

        SolarSystemPlot plot = new SolarSystemPlot("Orbit of Planets", -2, 2,
                -2, 2);

        try {
            solarSystem.addStar(new Star("Sun",1.391016 * Math.pow(10,9), 1.989 * Math.pow(10,30), 4.83, "G", plot, Color.yellow));
        }
        catch (SolarSystemException ss) { }

        Planet earth = new Planet("Earth",12.742 * Math.pow(10,6), 149.6 * Math.pow(10,9), 5.972 * Math.pow(10,24),
                solarSystem.getStar(), plot, plot.colors[0], 0, 1);
        Planet mars = new Planet("Mars", 6786604, 1.5 * 149.6 * Math.pow(10,9), 0.11 * 5.972 * Math.pow(10,24),
                solarSystem.getStar(), plot, plot.colors[1],1.5, 0);
        Planet earth2 = new Planet("Earth",12.742 * Math.pow(10,6), 149.6 * Math.pow(10,9), 5.972 * Math.pow(10,24),
                solarSystem.getStar(), plot, plot.colors[0], 0, 1);

        //System.out.println(earth.equals(earth2));

        try {
            solarSystem.addPlanet(earth);
            //solarSystem.addPlanet(earth2);
            solarSystem.addPlanet(mars);
        } catch (SolarSystemException ss) {
            System.out.println("Planet with the same characteristics already added!");
            exit(1);
        }

        /*System.out.println(solarSystem.getStar().getHabitableZoneUpperBound());
        System.out.println(solarSystem.getStar().getHabitableZoneLowerBound());

        System.out.println(solarSystem.getPlanets().get(0).getDistanceFromStar());*/
        //System.out.println(solarSystem.getPlanets().get(1).getHabitability());

        for (int i = 0; i < solarSystem.getBodies().size(); i++) {
            solarSystem.getBodies().get(i).start();
        }

    }

}
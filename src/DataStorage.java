import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class serves as a storage of data that will be changed throughout the runtime of each instance of the solar system
 */
public class DataStorage {

    /*
     * This is the dataset for the planets
     */
    ArrayList<String> planetNames;
    ArrayList<Double> planetDiameters;
    ArrayList<Double> planetDistancefromCentralBody;        // distance from the star
    ArrayList<Double> planetMass;
    ArrayList<Color> planetColors;
    ArrayList<Integer> planetPointSizes;
    ArrayList<Double> planetEccentricities;                 // how elliptical each orbit is (0 is a circle)

    /*
     * The direction from the star in which each planet starts its orbit
     */
    ArrayList<Integer> planetXCoordinateSection;
    ArrayList<Integer> planetYCoordinateSection;




    /*
     * This is the dataset for the stars
     */
    ArrayList<String> starNames;
    ArrayList<Double> starDiameters;
    ArrayList<Double> starMass;
    ArrayList<Color> starColors;
    ArrayList<Integer> starPointSizes;




    /*
     * This is the dataset for the satellites
     */
    ArrayList<String> satelliteNames;
    ArrayList<String> satelliteType;
    ArrayList<Double> satelliteDiameters;
    ArrayList<Double> satelliteDistancefromCentralBody;     // distance from the central planet
    ArrayList<Double> satelliteMass;
    ArrayList<Color> satelliteColors;
    ArrayList<String> satelliteCentralBodyNames;            // the name of the planet that each satellite orbits
    ArrayList<Integer> satellitePointSizes;
    ArrayList<Double> satelliteEccentricities;              // how elliptical each orbit is (0 is a circle)

    /*
     * The direction from the planet in which each satellite starts its orbit
     */
    ArrayList<Integer> satelliteXCoordinateSection;
    ArrayList<Integer> satelliteYCoordinateSection;

    double timeScale;                       // simulated seconds per real second

    /**
     * Creates a new storage of data that will be used by many classes
     */
    public DataStorage() {

        /*
         * This is the dataset for the planets
         */
         planetNames = new ArrayList<>(Arrays.asList("Mercury", "Venus", "Earth",
                "Mars", "Jupiter", "Saturn", "Uranus", "Neptune"));
         planetDiameters = new ArrayList<>(Arrays.asList(4.7894E6, 12.104E6, 12.742E6,
                6.779E6, 139.82E6, 116.46E6, 50.724E6, 49.244E6));
         planetDistancefromCentralBody = new ArrayList<>(Arrays.asList(57.91E9, 108.2E9, 149.6E9,
                227.9E9, 778.5E9, 1.434E12, 2.871E12, 4.495E12));
         planetMass = new ArrayList<>(Arrays.asList(3.285E23, 4.867E24, 5.972E24,
                6.39E23, 1.898E27, 5.683E26, 8.681E25, 1.024E25));
         planetColors = new ArrayList<>(Arrays.asList(Color.gray, Color.magenta, Color.blue,
                Color.red, Color.orange, Color.pink, Color.cyan, Color.blue));
         planetPointSizes = new ArrayList<>(Arrays.asList(5, 5, 5,
                 5, 12, 10, 8, 7));
         planetEccentricities = new ArrayList<>(Arrays.asList(0.2056, 0.0068, 0.0167,
                0.0934, 0.0489, 0.0565, 0.0457, 0.0113));


        /*
         * The direction from the star in which each planet starts its orbit
         */
         planetXCoordinateSection = new ArrayList<>(Arrays.asList(1, 0, -1,
                0, 1 , 0, -1, 0));
         planetYCoordinateSection = new ArrayList<>(Arrays.asList(0, 1, 0,
                -1, 0, 1, 0, -1));



        /*
         * This is the dataset for the stars
         */
         starNames = new ArrayList<>(Arrays.asList("Sun", "Betelgeuse"));
         starDiameters = new ArrayList<>(Arrays.asList(1.391E9, 1.234E12));
         starMass = new ArrayList<>(Arrays.asList(1.989E30, 2.188E31));
         starColors = new ArrayList<>(Arrays.asList(Color.yellow, Color.red));
         starPointSizes = new ArrayList<>(Arrays.asList(15, 20));



        /*
         * This is the dataset for the satellites
         */
        satelliteNames = new ArrayList<>(Arrays.asList("ISS", "Moon"));
        satelliteType = new ArrayList<>(Arrays.asList("Satellite", "Moon"));
        satelliteDiameters = new ArrayList<>(Arrays.asList(108.5, 3.4742E6));
        satelliteDistancefromCentralBody = new ArrayList<>(Arrays.asList(6.779E6, 384.4E6));     // measured from the planet's center (ISS: Earth's radius + 408 km altitude)
        satelliteMass = new ArrayList<>(Arrays.asList(420E3, 7.34767309E22));
        satelliteColors = new ArrayList<>(Arrays.asList(Color.white, Color.gray));
        satelliteCentralBodyNames = new ArrayList<>(Arrays.asList("Earth", "Earth"));
        satellitePointSizes = new ArrayList<>(Arrays.asList(3,4));
        satelliteEccentricities = new ArrayList<>(Arrays.asList(0.0005, 0.0549));

        /*
         * The direction from the planet in which each satellite starts its orbit
         */
        satelliteXCoordinateSection = new ArrayList<>(Arrays.asList(0, 1));
        satelliteYCoordinateSection = new ArrayList<>(Arrays.asList(1, 0));

        timeScale = SolarSystemInterface.defaultTimeScale;

    }
    
}

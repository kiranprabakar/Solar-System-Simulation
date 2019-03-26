import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DataStorage {

    ArrayList<String> planetNames;
    ArrayList<Double> planetDiameters;
    ArrayList<Double> planetDistancefromCentralBody;
    ArrayList<Double> planetMass;
    ArrayList<Color> planetColors;
    ArrayList<Integer> planetPointSizes;

    /*
     * Multiply the x and y coordinate multiples by distance in AU to find the true coordinates for each body
     */
    ArrayList<Integer> planetXCoordinateSection;
    ArrayList<Integer> planetYCoordinateSection;


    /*
     * This is the dataset for the stars
     */
    ArrayList<String> starNames;
    ArrayList<Double> starDiameters;
    ArrayList<Double> starDistancefromCentralBody;
    ArrayList<Double> starMass;
    ArrayList<Color> starColors;
    ArrayList<Integer> starPointSizes;

    /*
     * Multiply the x and y coordinate sections by distance in AU to find the true coordinates for each body
     */
    ArrayList<Integer> starXCoordinateSection;
    ArrayList<Integer> starYCoordinateSection;



    /*
     * This is the dataset for the satellites
     */
    ArrayList<String> satelliteNames;
    ArrayList<String> satelliteType;
    ArrayList<Double> satelliteDiameters;
    ArrayList<Double> satelliteDistancefromCentralBody;
    ArrayList<Planet> satelliteCentralBody;
    ArrayList<Double> satelliteMass;
    ArrayList<Color> satelliteColors;
    ArrayList<String> satelliteCentralBodyNames;
    ArrayList<Integer> satellitePointSizes;

    /*
     * Multiply the x and y coordinate sections by distance in AU to find the true coordinates for each body
     */
    ArrayList<Integer> satelliteXCoordinateSection;
    ArrayList<Integer> satelliteYCoordinateSection;


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


        /*
         * Multiply the x and y coordinate multiples by distance in AU to find the true coordinates for each body
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
         starDistancefromCentralBody = new ArrayList<>(Arrays.asList(0.00, 0.00));
         starMass = new ArrayList<>(Arrays.asList(1.989E30, 2.188E31));
         starColors = new ArrayList<>(Arrays.asList(Color.yellow, Color.red));
         starPointSizes = new ArrayList<>(Arrays.asList(15, 20));

        /*
         * Multiply the x and y coordinate sections by distance in AU to find the true coordinates for each body
         */
         starXCoordinateSection = new ArrayList<>(Arrays.asList(0, 0));
         starYCoordinateSection = new ArrayList<>(Arrays.asList(0, 0));



        /*
         * This is the dataset for the satellites
         */
        satelliteNames = new ArrayList<>(Arrays.asList("ISS", "Moon"));
        satelliteType = new ArrayList<>(Arrays.asList("Satellite", "Moon"));
        satelliteDiameters = new ArrayList<>(Arrays.asList(108.5, 3.4742E6));
        satelliteDistancefromCentralBody = new ArrayList<>(Arrays.asList(408E3, 384.4E6));
        satelliteMass = new ArrayList<>(Arrays.asList(420E3, 7.34767309E22));
        satelliteColors = new ArrayList<>(Arrays.asList(Color.blue, Color.gray));
        satelliteCentralBody = new ArrayList<>();
        satelliteCentralBodyNames = new ArrayList<>(Arrays.asList("Earth", "Earth"));
        satellitePointSizes = new ArrayList<>(Arrays.asList(2,3));

        /*
         * Multiply the x and y coordinate sections by distance in AU to find the true coordinates for each body
         */
        satelliteXCoordinateSection = new ArrayList<>(Arrays.asList(0, 1));
        satelliteYCoordinateSection = new ArrayList<>(Arrays.asList(1, 0));


    }
    
}

/*
 * Defines constants to be used
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * These variables act as constants that will be used within most of the classes
 */
public interface SolarSystemInterface {

    double G = 6.674 * Math.pow(10,-11);        // Newton's universal gravitational constant

    //The astronomical unit is a measurement of the distance between the Earth and the Sun
    double AU = 1.496 * Math.pow(10,11);        // meters in one astronomical unit (will be used in conversions)

    double  dt = 1;                             // will be used as a factor for step size in orbits

    double innerPlanetDivisor = 0.5;            // determines how much the x and y coordinates of the inner planets need to be divided by to cleanly display on the plot
    double outerPlanetDivisor = 1;              // determines how much the x and y coordinates of the outer planets except for Uranus and Neptune need to be divided by to cleanly display on the plot
    double nepUrDivisor = 1;                    // determines how much the x and y coordinates of Uranus and Neptune need to be divided by to cleanly display on the plot

    double satelliteDivisor = 0.00001;          // determines how much the x and y coordinates of the satellites need to be divided by to cleanly display on the plot
    double moonDivisor = 0.01;                  // determines how much the x and y coordinates of the moons need to be divided by to cleanly display on the plot

    int plotWidth = 800;                        // width of the plot
    int plotHeight = 800;                       // height of the plot

    int coordinateMax = 35;                     // upper bound of the plot (translates to 35 AU)

    int minSpeedControl = 1;                    // will be used for speed control
    int maxSpeedControl = 400;

    ArrayList<String> planetNames = new ArrayList<>(Arrays.asList("Mercury", "Venus", "Earth",      // default planet names
            "Mars", "Jupiter", "Saturn", "Uranus", "Neptune"));

    ArrayList<String> satelliteCentralBodyNames = new ArrayList<>(Arrays.asList("Earth", "Earth")); // default planets that each satellite can orbit around

    String[] starTypes = {"Main sequence", "Red giant", "White dwarf"};                             // allowed types of stars
    Color[] starColors = {Color.yellow, Color.red, Color.white};                                    // color for each type of star

    /*
     * The text fields presented when the user selects to add a customized celestial body
     */
    String customPlanetIntro = "To add a planet, type each characteristic using this format:\n" +
            "(Name), (Diameter[meters]), (Distance From Star[meters]), (Mass[kilograms]), (Default Planet with a similar composition)\n" + "\n" +
            "Note: Please do not enter random characters or new lines. Close window when done.\n";

    String customStarIntro = "To add a star, type each characteristic using this format:\n" +
            "(Name), (Diameter[meters]), (Mass[kilograms]), (Type of star [Main sequence, Red giant, or White dwarf])\n" + "\n" +
            "Note: Please do not enter random characters or new lines. Close window when done.\n";

    String customSatelliteIntro = "To add a satellite, type each characteristic using this format:\n" +
            "(Name), (Diameter[meters]), (Distance From Planet[meters]), (Mass[kilograms]), (Name of Central Planet), (Color [White or Gray]), (Type [Moon or Satellite]\n" + "\n" +
            "Note: Please do not enter random characters or new lines. Close window when done.\n";

}

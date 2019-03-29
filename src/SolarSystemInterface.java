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

    String[] starTypes = {"Main sequence", "Red giant", "White dwarf"};                             // allowed types of stars
    Color[] starColors = {Color.yellow, Color.red, Color.white};                                    // color for each type of star

    /*
     * This string gives the user program usage info as soon as the program is launched
     */
    String intro = "Welcome to the Solar System Simulation! Please note the following:\n"
            + "\n" + "To begin, create a star and add bodies as you wish until the limit is reached.\n"
            + "A set of default bodies is provided if desired but custom bodies can be created as the user wishes.\n"
            + "To start the simulation, click on \"Start\"\n"
            + "To stop or clear the simulation, click on \"Stop / Clear\"\n"
            + "To speed up or slow down the simulation, click on \"Speed up\" or \"Slow down\"\n"
            + "\n" + "Keep in mind that bodies will not be placed in accurate positions on the display and the body sizes will not be proportional.\n"
            + "This is to account for the massive distances between bodies and the relative smallness of each body\n"
            + "\n" + "IMPORTANT NOTE: When creating custom bodies, follow the instructions carefully.\n"
            + "Failing to do so can cause the whole body to not be created. "
            + "This includes capitalization of the correct letters, commas in the correct spots, and spaces in the correct spots.\n"
            + "Additionally, the capacity of the solar system is limited by the processing power of this computer, which is accounted for in the program\n";

    /*
     * The text fields presented when the user selects to add a customized celestial body
     */
    String customStarIntro = "To add a star, type each characteristic using this format:\n"
            + "(Name), (Diameter[meters]), (Mass[kilograms]), (Type of star [Main sequence, Red giant, or White dwarf])\n"
            + "i.e: Sunny, 2E9, 2E30, White dwarf\n"
            + "\n" + "Note: Please do not enter random characters or new lines. Close window when done.\n";

    String customPlanetIntro = "To add a planet, type each characteristic using this format:\n"
            + "(Name), (Diameter[meters]), (Distance From Star[meters]), (Mass[kilograms]), (Default Planet with a similar composition)\n"
            + "i.e: Ert, 10E6, 200E9, 4.5E24, Earth\n"
            + "\n" + "Note: Please do not enter random characters or new lines. Close window when done.\n";

    String customSatelliteIntro = "To add a satellite, type each characteristic using this format:\n"
            + "(Name), (Diameter[meters]), (Distance From Planet[meters]), (Mass[kilograms]), (Name of Central Planet), (Color [White or Gray]), (Type [Moon or Satellite])\n"
            + "i.e: Mun, 2000000, 234E6, 4.5E21, Ert, Gray, Moon\n"
            + "\n" + "Note: Please do not enter random characters or new lines. Close window when done.\n";

}

/*
 * Defines constants to be used
 */

import java.awt.*;

/*
 * These variables act as constants that will be used within most of the classes
 */
public interface SolarSystemInterface {

    double G = 6.674 * Math.pow(10,-11);        // Newton's universal gravitational constant

    //The astronomical unit is a measurement of the distance between the Earth and the Sun
    double AU = 1.496 * Math.pow(10,11);        // meters in one astronomical unit (will be used in conversions)

    double YEAR = 365.25 * 86400;               // seconds in one year

    int stepsPerOrbit = 400;                    // the time step is small enough that the fastest orbit takes at least this many steps
    int maxStepsPerFrame = 20000;               // caps the work per frame, so very high speeds slow down instead of freezing the display
    int frameDelay = 16;                        // milliseconds between frames (about 60 frames per second)

    double defaultTimeScale = YEAR / 20;        // simulated seconds per real second (one Earth year every 20 seconds)
    double minTimeScale = 1;                    // real time
    double maxTimeScale = 1E10;

    int trailLength = 150;                      // number of points in each body's orbit trail

    int plotWidth = 800;                        // width of the plot
    int plotHeight = 800;                       // height of the plot

    int coordinateMax = 35;                     // half the width of the default view (translates to 35 AU)

    String[] starTypes = {"Main sequence", "Red giant", "White dwarf"};                             // allowed types of stars
    Color[] starColors = {Color.yellow, Color.red, Color.white};                                    // color for each type of star
    int[] starTypePointSizes = {15, 20, 10};                                                        // display size for each type of star

    /*
     * This string gives the user program usage info as soon as the program is launched
     */
    String intro = "Welcome to the Solar System Simulation! Please note the following:\n"
            + "\n" + "To begin, create a star and add bodies as you wish until the limit is reached.\n"
            + "A set of default bodies is provided if desired but custom bodies can be created as the user wishes.\n"
            + "To start the simulation, click on \"Start\"\n"
            + "To stop or clear the simulation, click on \"Stop / Clear\"\n"
            + "To speed up or slow down the simulation, click on \"Speed up\" or \"Slow down\"\n"
            + "\n" + "Every body pulls on every other body with real Newtonian gravity, and distances are drawn to true scale.\n"
            + "Scroll on the display to zoom, drag to pan, and double-click to fit all planets in view.\n"
            + "Moons and satellites are drawn slightly farther from their planet than they really are so they stay visible when zoomed out;\n"
            + "zoom in close to a planet to see their true distances. Bodies are drawn at least a few pixels wide so they can be seen.\n"
            + "\n" + "IMPORTANT NOTE: When creating custom bodies, follow the instructions carefully.\n"
            + "Failing to do so can cause the whole body to not be created. "
            + "This includes capitalization of the correct letters and commas in the correct spots.\n";

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

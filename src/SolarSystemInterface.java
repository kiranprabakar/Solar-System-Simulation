/*
 * Defines constants to be used
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public interface SolarSystemInterface {
    double G = 6.674 * Math.pow(10,-11);
    double POGSON_RATIO = 2.5;
    AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    double AU = 1.496 * Math.pow(10,11);
    CyclicBarrier gate = new CyclicBarrier(3);

    double  dt = 1;

    int bodyLimit = 6;

    double innerPlanetDivisor = 0.5;
    double outerPlanetDivisor = 1;
    double nepUrDivisor = 1;

    double satelliteDivisor = 0.05;
    double moonDivisor = 2;

    int plotWidth = 1000;
    int plotHeight = 1000;

    int coordinateMax = 35;

    String[] starTypes = {"Main Sequence", "Red giant", "White dwarf"};
    Color[] starColors = {Color.yellow, Color.red, Color.white};

    String customPlanetIntro = "To add a planet, type each characteristic using this format:\n" +
            "(Name), (Diameter[meters]), (Distance From Star[meters]), (Mass[kilograms]), (Default Planet with a similar composition)\n" + "\n" +
            "Note: Please do not enter random characters or new lines. Close window when done.\n";

    String customStarIntro = "To add a star, type each characteristic using this format:\n" +
            "(Name), (Diameter[meters]), (Mass[kilograms]), (Type of star [Main sequence, Red giant, or White dwarf])\n" + "\n" +
            "Note: Please do not enter random characters or new lines. Close window when done.\n";

    String customSatelliteIntro = "To add a satellite, type each characteristic using this format:\n" +
            "(Name), (Diameter[meters]), (Distance From Planet[meters]), (Mass[kilograms]), (Name of Central Planet), (Color [White or Gray])\n" + "\n" +
            "Note: Please do not enter random characters or new lines. Close window when done.\n";

}

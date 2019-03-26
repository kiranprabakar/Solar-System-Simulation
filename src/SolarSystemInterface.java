/*
 * Defines constants to be used
 */

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
    double  timeLimit = 1000000000;

    int bodyLimit = 6;

    double innerPlanetDivisor = 0.5;
    double outerPlanetDivisor = 1;
    double nepUrDivisor = 1;

    double satelliteDivisor = 0.05;
    double moonDivisor = 1;

    int plotWidth = 1000;
    int plotHeight = 1000;

    int coordinateMax = 35;

    int controlWidth = 400;
    int controlHeight = 400;

}



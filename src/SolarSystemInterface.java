/*
 * Defines constants to be used
 */

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

    boolean speedUp = false;

    int plotWidth = 1000;
    int plotHeight = 1000;

    int controlWidth = 400;
    int controlHeight = 400;

    String[] starNames = {"Sun", "Betelgeuse", "Alpha Centauri"};
}



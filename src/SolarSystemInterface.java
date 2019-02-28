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

    double  dt = 1000;
    double  timeLimit = 100000000;
}

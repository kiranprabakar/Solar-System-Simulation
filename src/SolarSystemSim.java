/**
 * Runs the Solar System
 *
 * @author - Kiran Prabakar
 */
public class SolarSystemSim implements SolarSystemInterface {

    /**
     * Main method
     * Initiate and run from this class only
     */
    public static void main(String[] args) {

        SolarSystem solarSystem = new SolarSystem();                                                    // creates a solar system

        SolarSystemPlot plot = new SolarSystemPlot("Solar System", -coordinateMax, coordinateMax,  // creates the solar system display
                -coordinateMax, coordinateMax);

        DataStorage dataStorage = new DataStorage();                                                    // creates a data store

        try {
            solarSystem.addPlot(plot);                                                                  // links the plot to the solar system
            solarSystem.addDataStorage(dataStorage);                                                    // links the data store to the solar system
        } catch (SolarSystemException ss) {
            ss.printStackTrace();
        }

        SolarSystemGUI gui = new SolarSystemGUI(solarSystem);                                           // creates the graphical user interface

    }

}

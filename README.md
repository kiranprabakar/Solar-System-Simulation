# Solar System Simulation
A basic simulation of a unary star system using Multithreading and User Interface elements. The solar system allows a user to add any star, planet(s), and satellite(s) the user wants. 

## How to run the simulation

Clone this repository to a directory on your computer. Then, go to the src subdirectory.
To compile on terminal, type "javac SolarSystemSim.java" and to run, type "java SolarSystemSim"

Once the simulation is running, read the first pop-up message carefully. It contains useful information that will make your experience easier. 

Every body pulls on every other body with real Newtonian gravity (velocity Verlet integration), and planets start on their real elliptical orbits. The display is drawn to true scale: scroll to zoom, drag to pan, and double-click to fit all planets in view. Moons and satellites are drawn slightly farther from their planet when zoomed out so they stay visible.

### Prerequisites

You will need to have Java downloaded on your computer. Installing the latest version of Java may help to avoid possible issues with compilation and running. To install Java, go to https://www.oracle.com/technetwork/java/javase/downloads/index.html and follow the instructions provided. 

## License

This project is licensed under the GNU General Public License v2.0 - see the LICENSE file for details.

## Acknowledgments

Thank you to Daniel V. Schroeder from the Physics Department at Weber State University for inspiration on how to display my solar system. 

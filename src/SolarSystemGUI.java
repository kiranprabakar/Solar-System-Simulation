import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SolarSystemGUI extends JFrame {

    private SolarSystem solarSystem;

    private Panel controlPanel;
    private Frame controlFrame;

    private Button addPlanet, addStar, addSatelliteMoon, addSatellite, addMoon;

    private Frame addPlanetsFrame;
    private Panel addPlanetsPanel;

    private Frame addStarFrame;
    private Panel addStarPanel;

    private Frame addSatelliteMoonFrame;
    private Panel addSatelliteMoonPanel;

    private Frame addSatelliteFrame;
    private Panel addSatellitePanel;

    private Frame addMoonFrame;
    private Panel addMoonPanel;

    private Button addMercury, addVenus, addEarth, addMars, addJupiter, addSaturn, addUranus, addNeptune, addCustomPlanet;
    private Button addSun, addCustomStar;
    private Button addISS, addCustomSatellite;
    private Button addMoonOfEarth, addCustomMoon;

    public SolarSystemGUI(SolarSystem solarSystem) {

        this.solarSystem = solarSystem;

        controlFrame = new Frame("Alter the Simulation here!");
        controlPanel = new Panel();

        controlFrame.add(controlPanel, BorderLayout.CENTER);

        controlFrame.setSize(100, 100);

        addPlanet = new Button("Add Planet");		// create a button to clear the plot
        addPlanet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addPlanetsFrame = new Frame("Choose which planets to add!");
                addPlanetsFrame.setSize(100, 100);

                addPlanetsPanel = new Panel();

                addPlanetsFrame.add(addPlanetsPanel, BorderLayout.CENTER);

                addMercury = new Button("Mercury");
                addVenus = new Button("Venus");
                addEarth = new Button("Earth");
                addMars = new Button("Mars");
                addJupiter = new Button("Jupiter");
                addSaturn = new Button("Saturn");
                addUranus = new Button("Uranus");
                addNeptune = new Button("Neptune");
                addCustomPlanet = new Button("Custom");

                addPlanetsPanel.add(addMercury);
                addPlanetsPanel.add(addVenus);
                addPlanetsPanel.add(addEarth);
                addPlanetsPanel.add(addMars);
                addPlanetsPanel.add(addJupiter);
                addPlanetsPanel.add(addSaturn);
                addPlanetsPanel.add(addUranus);
                addPlanetsPanel.add(addNeptune);
                addPlanetsPanel.add(addCustomPlanet);

                addMercury.addActionListener((new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    }
                }));

                addPlanetsFrame.addWindowListener(new WindowAdapter() {	// remove this if you don't want the program
                    public void windowClosing(WindowEvent e) {		// to quit when close-box is clicked
                        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    }});

                addPlanetsFrame.pack();

                addPlanetsFrame.setResizable(true);
                addPlanetsFrame.setVisible(true);
            }
        });// tell it what to do when the button is clicked
        this.controlPanel.add(addPlanet);

        addStar = new Button("Add Star");		// create a button to clear the plot
        addStar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addStarFrame = new Frame("Choose which star to add!");
                addStarFrame.setSize(100, 100);

                addStarPanel = new Panel();

                addStarFrame.add(addStarPanel, BorderLayout.CENTER);

                addSun = new Button("Sun");
                addCustomStar = new Button("Custom");

                addStarPanel.add(addSun);
                addStarPanel.add(addCustomStar);

                addSun.addActionListener((new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    }
                }));

                addStarFrame.pack();

                addStarFrame.setResizable(true);
                addStarFrame.setVisible(true);
            }
        });// tell it what to do when the button is clicked
        this.controlPanel.add(addStar);

        //efsdg
        addSatelliteMoon = new Button("Add Satellite or Moon");		// create a button to clear the plot
        addSatelliteMoon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addSatelliteMoonFrame = new Frame("Choose which type to add!");
                addSatelliteMoonFrame.setSize(100, 100);

                addSatelliteMoonPanel = new Panel();

                addSatelliteMoonFrame.add(addSatelliteMoonPanel, BorderLayout.CENTER);

                addSatellite = new Button("Satellite");
                addMoon = new Button("Moon");

                addSatelliteMoonPanel.add(addSatellite);
                addSatelliteMoonPanel.add(addMoon);

                addSatellite.addActionListener((new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addSatelliteFrame = new Frame("Choose which satellites to add!");
                        addSatelliteFrame.setSize(100, 100);

                        addSatellitePanel = new Panel();

                        addSatelliteFrame.add(addSatellitePanel, BorderLayout.CENTER);

                        addISS = new Button("ISS");
                        addCustomSatellite = new Button("Custom");

                        addSatellitePanel.add(addISS);
                        addSatellitePanel.add(addCustomSatellite);

                        addSatelliteFrame.pack();

                        addSatelliteFrame.setResizable(true);
                        addSatelliteFrame.setVisible(true);
                    }
                }));
                addSatelliteMoonPanel.add(addSatellite);

                addMoon.addActionListener((new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        addMoonFrame = new Frame("Choose which moons to add!");
                        addMoonFrame.setSize(100, 100);

                        addMoonPanel = new Panel();

                        addMoonFrame.add(addMoonPanel, BorderLayout.CENTER);

                        addMoonOfEarth = new Button("Earth's Moon");
                        addCustomMoon = new Button("Custom");

                        addMoonPanel.add(addMoonOfEarth);
                        addMoonPanel.add(addCustomMoon);

                        addMoonFrame.pack();

                        addMoonFrame.setResizable(true);
                        addMoonFrame.setVisible(true);

                    }

                }));

                addSatelliteMoonPanel.add(addMoon);

                addSatelliteMoonFrame.pack();

                addSatelliteMoonFrame.setResizable(true);
                addSatelliteMoonFrame.setVisible(true);

            }
        });// tell it what to do when the button is clicked

        this.controlPanel.add(addSatelliteMoon);
        //rehtf

        controlFrame.pack();

        controlFrame.setResizable(true);
        controlFrame.setVisible(true);
    }

}

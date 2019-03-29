
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This serves as the user interface to control the solar system
 */
public class SolarSystemGUI extends JFrame implements SolarSystemInterface {

    private SolarSystem solarSystem;                                                // the solar system associated with this

    private Panel controlPanel;                                                     // the main panel
    private Frame controlFrame;                                                     // the main frame

    private Button addPlanet, addStar, addSatelliteMoon;                            // buttons to add bodies
    private Button start, stop, slowDown, speedUp;                                  // buttons to alter the simulation

    private Frame addPlanetsFrame;                                                  // the frame to add planets
    private Panel addPlanetsPanel;                                                  // the panel to add planets

    private Frame addStarFrame;                                                     // the frame to add a star
    private Panel addStarPanel;                                                     // the panel to add a star

    private Frame addSatelliteMoonFrame;                                            // the frame to add a satellite or moon
    private Panel addSatelliteMoonPanel;                                            // the panel to add a satellite or moon

    private Frame addCustomFrame;                                                   // the frame to add a custom body
    private JTextArea addCustomText;                                                // the text field to add a custom body

    private Button addMercury, addVenus, addEarth, addMars, addJupiter, addSaturn, addUranus, addNeptune, addCustomPlanet;  // the buttons to add planets
    private Button addSun, addBetelgeuse, addCustomStar;                            // the buttons to add a star
    private Button addISS, addMoonOfEarth, addCustomSatellite;                      // the buttons to add satellites or moons

    private int bodyCount;                                                          // the amount of bodies in the solar system
    private boolean started;                                                        // whether the simulation has started or not
    private boolean starAdded;                                                      // whether a star has been added or not
    private int bodyLimit;                                                          // the body limit

    private String text;                                                            // instruction text to display in the custom text field

    /**
     * Creates the user interface to control the solar system simulation
     *
     * @param solarSystem - the solar system that will be manipulated
     */
    public SolarSystemGUI(SolarSystem solarSystem) {

        this.solarSystem = solarSystem;                                             // sets the solar system

        bodyLimit = solarSystem.getDs().bodyLimit;                              // gets the body limit

        bodyCount = 0;                                                              // initializes the body count

        started = false;                                                            // has not started yet
        starAdded = false;                                                          // star does not exist yet

        controlFrame = new Frame("Alter the Simulation here!");                // creates the control frame
        controlPanel = new Panel();                                                 // creates the control panel to hold all buttons

        controlFrame.add(controlPanel, BorderLayout.CENTER);                        // adds the panel

        controlFrame.setPreferredSize(new Dimension(800, 75));        // sets the size

        controlFrame.addWindowListener(new WindowAdapter() {                        // if the user wishes to close the window
            public void windowClosing(WindowEvent e) {
                System.exit(1);                                              // ends the application
            }
        });

        /*
         * Starts the simulation
         */
        start = new Button("Start");
        start.addActionListener(new ActionListener() {                              // what to do when the button is clicked
            @Override
            public void actionPerformed(ActionEvent e) {
                if (started) {
                    alert("Simulation is running!");
                } else {
                    if (starAdded) {
                        solarSystem.startSimulation();
                        started = true;                                             // sets started flag to true
                    } else {
                        alert("Star does not exist yet!");
                    }
                }
            }
        });

        this.controlPanel.add(start);

        /*
         * Stops the simulation
         */
        stop = new Button("Stop / Clear");
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                solarSystem.stopSimulation(started);
                started = false;                                                    // resets started flag to false
                starAdded = false;                                                  // resets starAdded flag to false
                bodyCount = 0;                                                      // resets body count

            }
        });

        this.controlPanel.add(stop);

        /*
         * Speeds up the simulation
         */
        speedUp = new Button("Speed Up");
        speedUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (started) {
                    if (solarSystem.getDs().speedControl > minSpeedControl) {       // makes sure simulation can go faster
                        solarSystem.speedUpSimulation();
                    } else {
                        alert("Cannot speed up anymore!");
                    }
                } else {
                    alert("Simulation is not running!");
                }
            }
        });

        this.controlPanel.add(speedUp);

        /*
         * Slows down the simulation
         */
        slowDown = new Button("Slow Down");
        slowDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (started) {
                    if (solarSystem.getDs().speedControl < maxSpeedControl) {       // makes sure simulation can go slower
                        solarSystem.slowSimulation();
                    } else {
                        alert("Cannot slow down anymore!");
                    }
                } else {
                    alert("Simulation is not running!");
                }
            }
        });

        this.controlPanel.add(slowDown);

        /*
         * Adds a star to the solar system
         */
        addStar = new Button("Add Star");
        addStar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!starAdded) {                                               // alerts the user if a star has been added
                    addStarFrame = new Frame("Choose which star to add!"); // frame to add the star
                    addStarFrame.setPreferredSize(new Dimension(600, 75));  // sets the size of the frame

                    addStarPanel = new Panel();                                 // panel that will hold the buttons

                    addStarFrame.add(addStarPanel, BorderLayout.CENTER);

                    addSun = new Button("Sun");                           // button to add the Sun
                    addBetelgeuse = new Button("Betelgeuse");             // button to add Betelgeuse
                    addCustomStar = new Button("Custom");                 // button to add a custom star

                    addStarPanel.add(addSun);
                    addStarPanel.add(addBetelgeuse);
                    addStarPanel.add(addCustomStar);

                    addSun.addActionListener((new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            createNewStar("Sun", false);            // add the Sun

                        }
                    }));

                    addBetelgeuse.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            createNewStar("Betelgeuse", false);     // add Betelgeuse

                        }
                    });

                    addCustomStar.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (!starAdded) {                                       // alert the user if a star has been added
                                addCustomFrame = new Frame("Custom Star");     // frame to create a custom star
                                addCustomText = new JTextArea(customStarIntro, 8, 75);  // text box to input characteristics
                                addCustomFrame.add(addCustomText);
                                addCustomFrame.pack();
                                addCustomFrame.setVisible(true);


                                addCustomFrame.addWindowListener(new WindowAdapter() {
                                    @Override
                                    public void windowClosing(WindowEvent e) {

                                        text = addCustomText.getText();                 // get the text from the text box
                                        String characteristics = text.substring(text.lastIndexOf("\n") + 1);    // get the input

                                        createNewStar(characteristics, true);    // create a custom star

                                        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // close the window when done
                                        addCustomFrame.setVisible(false);
                                        addCustomFrame.dispose();
                                    }
                                });
                            } else {
                                alert("Star already exists!");
                            }
                        }
                    });

                    addStarFrame.addWindowListener(new WindowAdapter() {    // close the window when done
                        public void windowClosing(WindowEvent e) {
                            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            addStarFrame.setVisible(false);
                            addStarFrame.dispose();
                        }
                    });

                    addStarFrame.pack();                                    // make the frame big enough to include all elements of the panel

                    addStarFrame.setResizable(true);                        // allow the user to modify the size of the frame
                    addStarFrame.setVisible(true);                          // allow the user to see the frame
                } else {
                    alert("Star already exists!");
                }
            }
        });

        this.controlPanel.add(addStar);

        /*
         * Adds planets to the solar system
         */
        addPlanet = new Button("Add Planet");
        addPlanet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (starAdded) {                                                            // alerts the user if a star has not been added
                    addPlanetsFrame = new Frame("Choose which planets to add!");       // frame to add the planets
                    addPlanetsFrame.setPreferredSize(new Dimension(600, 75)); // set the size of the frame

                    addPlanetsPanel = new Panel();                                          // panel to hold the buttons

                    addPlanetsFrame.add(addPlanetsPanel, BorderLayout.CENTER);              // add the panel to the frame

                    addMercury = new Button("Mercury");                               // buttons to add the eight planets as well as custom planets
                    addVenus = new Button("Venus");
                    addEarth = new Button("Earth");
                    addMars = new Button("Mars");
                    addJupiter = new Button("Jupiter");
                    addSaturn = new Button("Saturn");
                    addUranus = new Button("Uranus");
                    addNeptune = new Button("Neptune");
                    addCustomPlanet = new Button("Custom");

                    addPlanetsPanel.add(addMercury);                                        // adds the buttons to the panel
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

                            createNewPlanet("Mercury", false);                  // add Mercury to the solar system

                        }
                    }));

                    addVenus.addActionListener((new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            createNewPlanet("Venus", false);                    // add Venus to the solar system

                        }
                    }));

                    addEarth.addActionListener((new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            createNewPlanet("Earth", false);                    // add Earth to the solar system

                        }
                    }));

                    addMars.addActionListener((new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            createNewPlanet("Mars", false);                     // add Mars to the solar system

                        }
                    }));

                    addJupiter.addActionListener((new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            createNewPlanet("Jupiter", false);                  // add Jupiter to the solar system

                        }
                    }));

                    addSaturn.addActionListener((new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            createNewPlanet("Saturn", false);                   // add Saturn to the solar system

                        }
                    }));

                    addUranus.addActionListener((new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            createNewPlanet("Uranus", false);                   // add Uranus to the solar system

                        }
                    }));

                    addNeptune.addActionListener((new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            createNewPlanet("Neptune", false);                  // add Neptune to the solar system

                        }
                    }));

                    addCustomPlanet.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            addCustomFrame = new Frame("Custom Planet");                // frame to add a custom planet
                            addCustomText = new JTextArea(customPlanetIntro, 8, 75);    // text box for the custom planet
                            addCustomFrame.add(addCustomText);                              // add the text box to the frame
                            addCustomFrame.pack();                                          // make frame big enough for the text box
                            addCustomFrame.setVisible(true);                                // allow the text box to be seen


                            addCustomFrame.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosing(WindowEvent e) {

                                    text = addCustomText.getText();                         // get the text from the text box
                                    String characteristics = text.substring(text.lastIndexOf("\n") + 1);    // get the characteristics of the custom planet

                                    createNewPlanet(characteristics, true);         // add a custom planet to the solar system

                                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);         // close the window
                                    addCustomFrame.setVisible(false);
                                    addCustomFrame.dispose();
                                }
                            });

                        }
                    });

                    addPlanetsFrame.addWindowListener(new WindowAdapter() {                 // close the window
                        public void windowClosing(WindowEvent e) {
                            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            addPlanetsFrame.setVisible(false);
                            addPlanetsFrame.dispose();
                        }
                    });

                    addPlanetsFrame.pack();

                    addPlanetsFrame.setResizable(true);
                    addPlanetsFrame.setVisible(true);
                } else {
                    alert("Star does not exist yet!");
                }
            }
        });

        this.controlPanel.add(addPlanet);

        /*
         * Adds satellites and moons to the solar system
         */
        addSatelliteMoon = new Button("Add Satellite or Moon");
        addSatelliteMoon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (starAdded) {                                                            // alerts the user if a star has not been added yet
                    addSatelliteMoonFrame = new Frame("Choose which type to add!");     // frame to add a satellite or a moon
                    addSatelliteMoonFrame.setPreferredSize(new Dimension(600, 75)); // set frame size

                    addSatelliteMoonPanel = new Panel();                                    // panel to hold buttons

                    addSatelliteMoonFrame.add(addSatelliteMoonPanel, BorderLayout.CENTER);  // add the panel to the frame

                    addMoonOfEarth = new Button("Earth's Moon");                    // creates new buttons
                    addISS = new Button("ISS");
                    addCustomSatellite = new Button("Custom");

                    addMoonOfEarth.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            createNewSatellite("Moon", false);              // add the Moon

                        }
                    });

                    addISS.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            createNewSatellite("ISS", false);               // add the ISS

                        }
                    });

                    addCustomSatellite.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            addCustomFrame = new Frame("Custom Satellite");         // frame for a custom satellite
                            addCustomText = new JTextArea(customSatelliteIntro, 8, 75);     // text box for the custom satellite
                            addCustomFrame.add(addCustomText);                          // adds the text box
                            addCustomFrame.pack();                                      // allows the text box to be seen
                            addCustomFrame.setVisible(true);                            // makes the text box visible

                            addCustomFrame.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosing(WindowEvent e) {

                                    text = addCustomText.getText();                     // get the text from the text box
                                    String characteristics = text.substring(text.lastIndexOf("\n") + 1);    // get the characteristic input for the satellite

                                    createNewSatellite(characteristics, true);          // add the custom satellite

                                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             // close the text box
                                    addCustomFrame.setVisible(false);
                                    addCustomFrame.dispose();
                                }
                            });
                        }
                    });

                    addSatelliteMoonPanel.add(addMoonOfEarth);                                  // add the buttons to the panel
                    addSatelliteMoonPanel.add(addISS);
                    addSatelliteMoonPanel.add(addCustomSatellite);

                    addSatelliteMoonFrame.addWindowListener(new WindowAdapter() {               // close the window
                        public void windowClosing(WindowEvent e) {
                            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            addSatelliteMoonFrame.setVisible(false);
                            addSatelliteMoonFrame.dispose();
                        }
                    });

                    addSatelliteMoonFrame.pack();

                    addSatelliteMoonFrame.setResizable(true);
                    addSatelliteMoonFrame.setVisible(true);
                } else {
                    alert("Star does not exist yet!");
                }
            }
        });

        this.controlPanel.add(addSatelliteMoon);

        controlFrame.pack();

        controlFrame.setResizable(true);
        controlFrame.setVisible(true);
    }

    /**
     * Adds a star into the simulation
     *
     * @param string - either the name of the star or the characteristic string entered by the user
     * @param custom - whether the star is custom or default
     */
    private void createNewStar(String string, boolean custom) {

        try {
            if (!starAdded) {                                                       // alerts user if star already exists
                Star star;
                if (!custom) {
                    star = solarSystem.newStar(string);                             // creates new star if not custom
                } else {
                    star = solarSystem.createCustomStar(string);                    // creates a custom star
                }
                solarSystem.addStar(star);                                          // adds the star to the solar system
                solarSystem.getPlot().addPoint(star.getColor(), star.getPointSize(), star.getX(), star.getY()); // plots the star in the system
                solarSystem.getPlot().repaint();                                    // repaints the plot
                ++bodyCount;                                                        // increases the body count
                starAdded = true;                                                   // indicates that a star has been added
            } else {
                alert("A star already exists!");
            }
        } catch (SolarSystemException ss) {                                         // throw exception if error occurs
            ss.printStackTrace();
        }

    }

    /**
     * Adds a new planet to the solar system
     *
     * @param string - either the name of the planet or the characteristic sting entered by the user
     * @param custom - whether the planet is custom or default
     */
    private void createNewPlanet(String string, boolean custom) {

        try {
            Planet planet;
            if (!custom) {                                                      // creates a default planet
                planet = solarSystem.newPlanet(string);
            } else {                                                            // creates a customized planet
                planet = solarSystem.createCustomPlanet(string);
            }
            if (!started) {
                if (bodyCount < bodyLimit) {                                    // alerts the user if the body count is greater than or equal to the body limit
                    solarSystem.addPlanet(planet);                              // adds planet to the solar system
                    solarSystem.getPlot().addPoint(planet.getColor(), planet.getPointSize(),    // plots the planet
                            planet.getX() / AU / planet.getDivisor(), planet.getY() / AU / planet.getDivisor());
                    solarSystem.getPlot().repaint();                            // repaints the plot
                    ++bodyCount;                                                // updates body count
                } else {
                    alert("Sorry, no more bodies can be added!");
                }
            } else {
                if (bodyCount < bodyLimit) {                                    // alerts the user if the body count is greater than or equal to the body limit
                    solarSystem.addPlanet(planet);                              // adds planet to the solar system
                    solarSystem.getExecutorService().execute(planet);           // begins the planet's orbit
                    ++bodyCount;                                                // updates body count
                } else {
                    alert("Sorry, no more bodies can be added!");
                }
            }
        } catch (SolarSystemException ss) {
            ss.printStackTrace();
        }

    }

    /**
     * Adds a new satellite to the solar system
     *
     * @param string - either the name or characteristic string gof the satellite
     * @param custom - whether the satellite is default or customized
     */
    public void createNewSatellite(String string, boolean custom) {

        try {
            if (!started) {                                                 // checks if simulation has started
                if (bodyCount < bodyLimit) {                                // alerts the user if the body count is greater than or equal to the body limit
                    try {
                        Satellite satellite;
                        if (!custom) {
                            satellite = solarSystem.newSatellite(string);           // creates default satellite
                        } else {
                            satellite = solarSystem.createCustomSatellite(string);  // creates a custom satellite
                        }
                        solarSystem.addSatellite(satellite);                        // adds the satellite to the solar system
                        solarSystem.getPlot().addPoint(satellite.getColor(), satellite.getPointSize(),              // plots the satellite
                                satellite.relativeX / AU / satellite.getDivisor() + satellite.getBody().getX() / AU / satellite.getBody().getDivisor(),
                                satellite.relativeY / AU / satellite.getDivisor() + satellite.getBody().getY() / AU / satellite.getBody().getDivisor());
                        solarSystem.getPlot().repaint();
                        ++bodyCount;                                                // updates the body count
                    } catch (Exception s) {
                        s.printStackTrace();
                    }
                } else {
                    alert("Sorry, no more bodies can be added!");
                }
            } else {                                                                // most of the code is similar to above
                if (bodyCount < bodyLimit) {
                    try {
                        Satellite satellite;
                        if (!custom) {
                            satellite = solarSystem.newSatellite(string);
                        } else {
                            satellite = solarSystem.createCustomSatellite(string);
                        }
                        solarSystem.addSatellite(satellite);
                        solarSystem.getExecutorService().execute(satellite);        // runs the created satellite
                        ++bodyCount;
                    } catch (Exception s) {
                        s.printStackTrace();
                    }
                } else {
                    alert("Cannot add any more bodies!");
                }
            }
        } catch (Exception ss) {
            ss.printStackTrace();
            alert("Sorry, this satellite already exists");
        }

    }

    /**
     * Alerts the user of a message
     *
     * @param alert - message to be alerted
     */
    private void alert(String alert) {

        JOptionPane.showMessageDialog(null, alert);         // creates a pop-up

    }

}

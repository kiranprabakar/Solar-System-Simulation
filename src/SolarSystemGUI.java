import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SolarSystemGUI extends JFrame implements SolarSystemInterface {

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

    private Button start, stop;
    private Button addMercury, addVenus, addEarth, addMars, addJupiter, addSaturn, addUranus, addNeptune, addCustomPlanet;
    private Button addSun, addCustomStar;
    private Button addISS, addCustomSatellite;
    private Button addMoonOfEarth, addCustomMoon;

    private int bodyCount;
    private boolean started, starAdded;

    public SolarSystemGUI(SolarSystem solarSystem) {

        this.solarSystem = solarSystem;

        bodyCount = 0;

        started = false;
        starAdded = false;

        controlFrame = new Frame("Alter the Simulation here!");
        controlPanel = new Panel();

        controlFrame.add(controlPanel, BorderLayout.CENTER);

        controlFrame.setSize(100, 100);

        controlFrame.addWindowListener(new WindowAdapter() {    // remove this if you don't want the program
            public void windowClosing(WindowEvent e) {        // to quit when close-box is clicked
                System.exit(1);
            }
        });

        start = new Button("Start");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (started) {
                    alert("Simulation is running!");
                } else {
                    if (starAdded) {
                        solarSystem.startSimulation(false);
                        started = true;
                    } else {
                        alert("Star does not exist yet!");
                    }
                }
            }
        });

        this.controlPanel.add(start);

        stop = new Button("Stop");
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                /*
                 * Need to figure out how to stop the threads immediately
                 * Working but needs debugging
                 */

                alert("Cannot stop this right now! Please click the X button to end the application.");

                //solarSystem.stopSimulation();
            }
        });

        this.controlPanel.add(stop);

        addPlanet = new Button("Add Planet");		// create a button to clear the plot
        addPlanet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (starAdded) {
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
                            try {
                                if (!started) {
                                    if (bodyCount < bodyLimit) {
                                        Planet planet = solarSystem.newPlanet("Mercury");
                                        solarSystem.addPlanet(planet);
                                        solarSystem.getPlot().addPoint(planet.getColor(), 7, planet.getX() / AU, planet.getY() / AU);
                                        solarSystem.getPlot().repaint();
                                        alert("Added Mercury");
                                        ++bodyCount;
                                    } else {
                                        alert("Sorry, no more bodies can be added!");
                                    }
                                } else {
                                    if (bodyCount < bodyLimit) {
                                        Planet planet = solarSystem.newPlanet("Mercury");
                                        solarSystem.addPlanet(planet);
                                        solarSystem.getExecutorService().execute(planet);
                                        alert("Added Mercury");
                                        ++bodyCount;
                                    } else {
                                        alert("Sorry, no more bodies can be added!");
                                    }
                                }
                            } catch (SolarSystemException ss) {
                                ss.printStackTrace();
                            }
                        }
                    }));

                    addVenus.addActionListener((new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (!started) {
                                    if (bodyCount < bodyLimit) {
                                        Planet planet = solarSystem.newPlanet("Venus");
                                        solarSystem.addPlanet(planet);
                                        solarSystem.getPlot().addPoint(planet.getColor(), 7, planet.getX() / AU, planet.getY() / AU);
                                        solarSystem.getPlot().repaint();
                                        ++bodyCount;
                                    } else {
                                        alert("Sorry, no more bodies can be added!");
                                    }
                                } else {
                                    if (bodyCount < bodyLimit) {
                                        Planet planet = solarSystem.newPlanet("Venus");
                                        solarSystem.addPlanet(planet);
                                        solarSystem.getExecutorService().execute(planet);
                                        ++bodyCount;
                                    } else {
                                        alert("Sorry, no more bodies can be added!");
                                    }
                                }
                            } catch (SolarSystemException ss) {
                                ss.printStackTrace();
                            }
                        }
                    }));

                    addEarth.addActionListener((new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (!started) {
                                    if (bodyCount < bodyLimit) {
                                        Planet planet = solarSystem.newPlanet("Earth");
                                        solarSystem.addPlanet(planet);
                                        solarSystem.getPlot().addPoint(planet.getColor(), 7, planet.getX() / AU, planet.getY() / AU);
                                        solarSystem.getPlot().repaint();
                                        ++bodyCount;
                                    } else {
                                        alert("Sorry, no more bodies can be added!");
                                    }
                                } else {
                                    if (bodyCount < bodyLimit) {
                                        Planet planet = solarSystem.newPlanet("Earth");
                                        solarSystem.addPlanet(planet);
                                        solarSystem.getExecutorService().execute(planet);
                                        ++bodyCount;
                                    } else {
                                        alert("Sorry, no more bodies can be added!");
                                    }
                                }
                            } catch (SolarSystemException ss) {
                                ss.printStackTrace();
                            }
                        }
                    }));

                    addMars.addActionListener((new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (!started) {
                                    if (bodyCount < bodyLimit) {
                                        Planet planet = solarSystem.newPlanet("Mars");
                                        solarSystem.addPlanet(planet);
                                        solarSystem.getPlot().addPoint(planet.getColor(), 7, planet.getX() / AU, planet.getY() / AU);
                                        solarSystem.getPlot().repaint();
                                        ++bodyCount;
                                    } else {
                                        alert("Sorry, no more bodies can be added!");
                                    }
                                } else {
                                    if (bodyCount < bodyLimit) {
                                        Planet planet = solarSystem.newPlanet("Mars");
                                        solarSystem.addPlanet(planet);
                                        solarSystem.getExecutorService().execute(planet);
                                        ++bodyCount;
                                    } else {
                                        alert("Sorry, no more bodies can be added!");
                                    }
                                }
                            } catch (SolarSystemException ss) {
                                ss.printStackTrace();
                            }
                        }
                    }));

                    addJupiter.addActionListener((new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (!started) {
                                    if (bodyCount < bodyLimit) {
                                        Planet planet = solarSystem.newPlanet("Jupiter");
                                        solarSystem.addPlanet(planet);
                                        solarSystem.getPlot().addPoint(planet.getColor(), 7, planet.getX() / AU, planet.getY() / AU);
                                        solarSystem.getPlot().repaint();
                                        ++bodyCount;
                                    } else {
                                        alert("Sorry, no more bodies can be added!");
                                    }
                                } else {
                                    if (bodyCount < bodyLimit) {
                                        Planet planet = solarSystem.newPlanet("Jupiter");
                                        solarSystem.addPlanet(planet);
                                        solarSystem.getExecutorService().execute(planet);
                                        ++bodyCount;
                                    } else {
                                        alert("Sorry, no more bodies can be added!");
                                    }
                                }
                            } catch (SolarSystemException ss) {
                                ss.printStackTrace();
                            }
                        }
                    }));

                    addSaturn.addActionListener((new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (!started) {
                                    if (bodyCount < bodyLimit) {
                                        Planet planet = solarSystem.newPlanet("Saturn");
                                        solarSystem.addPlanet(planet);
                                        solarSystem.getPlot().addPoint(planet.getColor(), 7, planet.getX() / AU, planet.getY() / AU);
                                        solarSystem.getPlot().repaint();
                                        ++bodyCount;
                                    } else {
                                        alert("Sorry, no more bodies can be added!");
                                    }
                                } else {
                                    if (bodyCount < bodyLimit) {
                                        Planet planet = solarSystem.newPlanet("Saturn");
                                        solarSystem.addPlanet(planet);
                                        solarSystem.getExecutorService().execute(planet);
                                        ++bodyCount;
                                    } else {
                                        alert("Sorry, no more bodies can be added!");
                                    }
                                }
                            } catch (SolarSystemException ss) {
                                ss.printStackTrace();
                            }
                        }
                    }));

                    addUranus.addActionListener((new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (!started) {
                                    if (bodyCount < bodyLimit) {
                                        Planet planet = solarSystem.newPlanet("Uranus");
                                        solarSystem.addPlanet(planet);
                                        solarSystem.getPlot().addPoint(planet.getColor(), 7, planet.getX() / AU, planet.getY() / AU);
                                        solarSystem.getPlot().repaint();
                                        ++bodyCount;
                                    } else {
                                        alert("Sorry, no more bodies can be added!");
                                    }
                                } else {
                                    if (bodyCount < bodyLimit) {
                                        Planet planet = solarSystem.newPlanet("Uranus");
                                        solarSystem.addPlanet(planet);
                                        solarSystem.getExecutorService().execute(planet);
                                        ++bodyCount;
                                    } else {
                                        alert("Sorry, no more bodies can be added!");
                                    }
                                }
                            } catch (SolarSystemException ss) {
                                ss.printStackTrace();
                            }
                        }
                    }));

                    addNeptune.addActionListener((new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (!started) {
                                    if (bodyCount < bodyLimit) {
                                        Planet planet = solarSystem.newPlanet("Neptune");
                                        solarSystem.addPlanet(planet);
                                        solarSystem.getPlot().addPoint(planet.getColor(), 7, planet.getX() / AU, planet.getY() / AU);
                                        solarSystem.getPlot().repaint();
                                        ++bodyCount;
                                    } else {
                                        alert("Sorry, no more bodies can be added!");
                                    }
                                } else {
                                    if (bodyCount < bodyLimit) {
                                        Planet planet = solarSystem.newPlanet("Neptune");
                                        solarSystem.addPlanet(planet);
                                        solarSystem.getExecutorService().execute(planet);
                                        ++bodyCount;
                                    } else {
                                        alert("Sorry, no more bodies can be added!");
                                    }
                                }
                            } catch (SolarSystemException ss) {
                                ss.printStackTrace();
                            }
                        }
                    }));

                    addPlanetsFrame.addWindowListener(new WindowAdapter() {    // remove this if you don't want the program
                        public void windowClosing(WindowEvent e) {        // to quit when close-box is clicked
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
        });// tell it what to do when the button is clicked
        this.controlPanel.add(addPlanet);

        addStar = new Button("Add Star");		// create a button to clear the plot
        addStar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!starAdded) {
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
                            try {
                                Star star = solarSystem.newStar("Sun");
                                if (!started) {
                                    solarSystem.addStar(star);
                                    solarSystem.getPlot().addPoint(star.getColor(), 10, star.getX(), star.getY());
                                    solarSystem.getPlot().repaint();
                                    ++bodyCount;
                                } else {
                                    solarSystem.addStar(star);
                                    solarSystem.getExecutorService().execute(star);
                                    ++bodyCount;
                                }
                                starAdded = true;
                            } catch (SolarSystemException ss) {
                                ss.printStackTrace();
                            }

                        }
                    }));

                    addStarFrame.addWindowListener(new WindowAdapter() {    // remove this if you don't want the program
                        public void windowClosing(WindowEvent e) {        // to quit when close-box is clicked
                            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            addStarFrame.setVisible(false);
                            addStarFrame.dispose();
                        }
                    });

                    addStarFrame.pack();

                    addStarFrame.setResizable(true);
                    addStarFrame.setVisible(true);
                } else {
                    alert("Star already exists!");
                }
            }
        });// tell it what to do when the button is clicked
        this.controlPanel.add(addStar);

        addSatelliteMoon = new Button("Add Satellite or Moon");		// create a button to clear the plot
        addSatelliteMoon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (starAdded) {
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

                            addSatelliteFrame.addWindowListener(new WindowAdapter() {    // remove this if you don't want the program
                                public void windowClosing(WindowEvent e) {        // to quit when close-box is clicked
                                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                    addSatelliteFrame.setVisible(false);
                                    addSatelliteFrame.dispose();
                                }
                            });

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

                            addMoonFrame.addWindowListener(new WindowAdapter() {    // remove this if you don't want the program
                                public void windowClosing(WindowEvent e) {        // to quit when close-box is clicked
                                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                    addMoonFrame.setVisible(false);
                                    addMoonFrame.dispose();
                                }
                            });

                            addMoonFrame.pack();

                            addMoonFrame.setResizable(true);
                            addMoonFrame.setVisible(true);

                        }

                    }));

                    addSatelliteMoonFrame.addWindowListener(new WindowAdapter() {    // remove this if you don't want the program
                        public void windowClosing(WindowEvent e) {        // to quit when close-box is clicked
                            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            addSatelliteMoonFrame.setVisible(false);
                            addSatelliteMoonFrame.dispose();
                        }
                    });

                    addSatelliteMoonPanel.add(addMoon);

                    addSatelliteMoonFrame.pack();

                    addSatelliteMoonFrame.setResizable(true);
                    addSatelliteMoonFrame.setVisible(true);
                } else {
                    alert("Star does not exist yet!");
                }
            }
        });// tell it what to do when the button is clicked

        this.controlPanel.add(addSatelliteMoon);
        //rehtf

        controlFrame.pack();

        controlFrame.setResizable(true);
        controlFrame.setVisible(true);
    }

    public void alert(String alert) {

        JOptionPane.showMessageDialog(null, alert);

    }

}

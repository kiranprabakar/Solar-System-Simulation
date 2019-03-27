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

    private Frame addCustomFrame;
    private JTextArea addCustomText;

    private Button start, stop;
    private Button addMercury, addVenus, addEarth, addMars, addJupiter, addSaturn, addUranus, addNeptune, addCustomPlanet;
    private Button addSun, addCustomStar;
    private Button addISS, addCustomSatellite;
    private Button addMoonOfEarth, addCustomMoon;

    private int bodyCount;
    private boolean started, starAdded;

    private String text;

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

                if (started) {
                    solarSystem.stopSimulation();
                    started = false;
                    starAdded = false;
                    bodyCount = 0;
                } else {
                    alert("Simulation is not running!");
                }

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
                                        solarSystem.getPlot().addPoint(planet.getColor(), planet.getPointSize(),
                                                planet.getX() / AU / planet.getDivisor(), planet.getY() / AU / planet.getDivisor());
                                        solarSystem.getPlot().repaint();
                                        ++bodyCount;
                                    } else {
                                        alert("Sorry, no more bodies can be added!");
                                    }
                                } else {
                                    if (bodyCount < bodyLimit) {
                                        Planet planet = solarSystem.newPlanet("Mercury");
                                        solarSystem.addPlanet(planet);
                                        solarSystem.getExecutorService().execute(planet);
                                        ++bodyCount;
                                    } else {
                                        alert("Sorry, no more bodies can be added!");
                                    }
                                }
                            } catch (SolarSystemException ss) {
                                alert("Sorry, this planet already exists");
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
                                        solarSystem.getPlot().addPoint(planet.getColor(), planet.getPointSize(),
                                                planet.getX() / AU / planet.getDivisor(), planet.getY() / AU / planet.getDivisor());
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
                                alert("Sorry, this planet already exists");
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
                                        solarSystem.getPlot().addPoint(planet.getColor(), planet.getPointSize(),
                                                planet.getX() / AU / planet.getDivisor(), planet.getY() / AU / planet.getDivisor());
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
                                alert("Sorry, this planet already exists");
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
                                        solarSystem.getPlot().addPoint(planet.getColor(), planet.getPointSize(),
                                                planet.getX() / AU / planet.getDivisor(), planet.getY() / AU / planet.getDivisor());
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
                                alert("Sorry, this planet already exists");
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
                                        solarSystem.getPlot().addPoint(planet.getColor(), planet.getPointSize(),
                                                planet.getX() / AU / planet.getDivisor(), planet.getY() / AU / planet.getDivisor());
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
                                alert("Sorry, this planet already exists");
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
                                        solarSystem.getPlot().addPoint(planet.getColor(), planet.getPointSize(),
                                                planet.getX() / AU / planet.getDivisor(), planet.getY() / AU / planet.getDivisor());
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
                                alert("Sorry, this planet already exists");
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
                                        solarSystem.getPlot().addPoint(planet.getColor(), planet.getPointSize(),
                                                planet.getX() / AU / planet.getDivisor(), planet.getY() / AU / planet.getDivisor());
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
                                alert("Sorry, this planet already exists");
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
                                        solarSystem.getPlot().addPoint(planet.getColor(), planet.getPointSize(),
                                                planet.getX() / AU / planet.getDivisor(), planet.getY() / AU / planet.getDivisor());
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
                                alert("Sorry, this planet already exists");
                            }
                        }
                    }));

                    addCustomPlanet.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            addCustomFrame = new Frame("Custom Planet");
                            addCustomText = new JTextArea(customPlanetIntro, 8, 75);
                            addCustomFrame.add(addCustomText);
                            addCustomFrame.pack();
                            addCustomFrame.setVisible(true);


                            addCustomFrame.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosing(WindowEvent e) {

                                    text = addCustomText.getText();
                                    String characteristics = text.substring(text.lastIndexOf("\n") + 1);

                                    try {
                                        if (!started) {
                                            if (bodyCount < bodyLimit) {
                                                try {
                                                    Planet planet = solarSystem.createCustomPlanet(characteristics);
                                                    solarSystem.addPlanet(planet);
                                                    solarSystem.getPlot().addPoint(planet.getColor(), planet.getPointSize(),
                                                            planet.getX() / AU / planet.getDivisor(), planet.getY() / AU / planet.getDivisor());
                                                    solarSystem.getPlot().repaint();
                                                    ++bodyCount;
                                                } catch (SolarSystemException ss) {
                                                    ss.printStackTrace();
                                                }
                                            } else {
                                                alert("Sorry, no more bodies can be added!");
                                            }
                                        } else {
                                            if (bodyCount < bodyLimit) {
                                                Planet planet = solarSystem.createCustomPlanet(characteristics);
                                                solarSystem.addPlanet(planet);
                                                solarSystem.getExecutorService().execute(planet);
                                                ++bodyCount;
                                            } else {
                                                alert("Sorry, no more bodies can be added!");
                                            }
                                        }
                                    } catch (SolarSystemException ss) {
                                        alert("Sorry, this planet already exists");
                                    }

                                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                    addCustomFrame.setVisible(false);
                                    addCustomFrame.dispose();
                                }
                            });

                        }
                    });

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
                                    solarSystem.getPlot().addPoint(star.getColor(), star.getPointSize(), star.getX(), star.getY());
                                    solarSystem.getPlot().repaint();
                                    ++bodyCount;
                                } else {
                                    solarSystem.addStar(star);
                                    solarSystem.getExecutorService().execute(star);
                                    ++bodyCount;
                                }
                                starAdded = true;
                            } catch (SolarSystemException ss) {
                                alert("Sorry, this star already exists");
                            }

                        }
                    }));

                    addCustomStar.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            addCustomFrame = new Frame("Custom Star");
                            addCustomText = new JTextArea(customStarIntro, 8, 75);
                            addCustomFrame.add(addCustomText);
                            addCustomFrame.pack();
                            addCustomFrame.setVisible(true);


                            addCustomFrame.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosing(WindowEvent e) {

                                    text = addCustomText.getText();
                                    String characteristics = text.substring(text.lastIndexOf("\n") + 1);

                                    try {
                                        if (!started) {
                                            if (bodyCount < bodyLimit) {
                                                Star star = solarSystem.createCustomStar(characteristics);
                                                try {
                                                    solarSystem.addStar(star);
                                                    solarSystem.getPlot().addPoint(star.getColor(), star.getPointSize(),
                                                            0,0);
                                                    solarSystem.getPlot().repaint();
                                                    starAdded = true;
                                                    ++bodyCount;
                                                } catch (SolarSystemException ss) {
                                                    ss.printStackTrace();
                                                }
                                            } else {
                                                alert("Sorry, no more bodies can be added!");
                                            }
                                        } else {
                                            if (bodyCount < bodyLimit) {
                                                try {
                                                    Star star = solarSystem.createCustomStar(characteristics);
                                                    solarSystem.addStar(star);
                                                    solarSystem.getExecutorService().execute(star);
                                                    starAdded = true;
                                                    ++bodyCount;
                                                } catch (SolarSystemException ss) {
                                                    ss.printStackTrace();
                                                }
                                            } else {
                                                alert("Sorry, no more bodies can be added!");
                                            }
                                        }
                                    } catch (SolarSystemException ss) {
                                        alert("Sorry, this star already exists");
                                    }

                                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                    addCustomFrame.setVisible(false);
                                    addCustomFrame.dispose();
                                }
                            });
                        }
                    });

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
        });
        this.controlPanel.add(addStar);

        addSatelliteMoon = new Button("Add Satellite or Moon");
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

                            addISS.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        if (!started) {
                                            if (bodyCount < bodyLimit) {
                                                try {
                                                    Satellite satellite = solarSystem.newSatellite("ISS");
                                                    solarSystem.addSatellite(satellite);
                                                    solarSystem.getPlot().addPoint(satellite.getColor(), satellite.getPointSize(),
                                                            satellite.getX() / AU / satellite.getDivisor() + satellite.getBody().getX() / AU / satellite.getBody().getDivisor(),
                                                            satellite.getY() / AU / satellite.getDivisor() + satellite.getBody().getY() / AU / satellite.getBody().getDivisor());
                                                    solarSystem.getPlot().repaint();
                                                    ++bodyCount;
                                                } catch (Exception s) {
                                                    s.printStackTrace();
                                                    alert("Planet has not been added yet!");
                                                }
                                            } else {
                                                alert("Sorry, no more bodies can be added!");
                                            }
                                        } else {
                                            try {
                                                Satellite satellite = solarSystem.newSatellite("ISS");
                                                solarSystem.addSatellite(satellite);
                                                solarSystem.getExecutorService().execute(satellite);
                                                ++bodyCount;
                                            } catch (Exception s) {
                                                System.out.println("...");

                                                alert("Planet has not been added yet!");
                                            }
                                        }
                                    } catch (Exception ss) {
                                        alert("Sorry, this satellite already exists");
                                    }
                                }
                            });

                            addCustomSatellite.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    addCustomFrame = new Frame("Custom Star");
                                    addCustomText = new JTextArea(customSatelliteIntro, 8, 75);
                                    addCustomFrame.add(addCustomText);
                                    addCustomFrame.pack();
                                    addCustomFrame.setVisible(true);


                                    addCustomFrame.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {

                                            text = addCustomText.getText();
                                            String characteristics = text.substring(text.lastIndexOf("\n") + 1);

                                            try {
                                                if (!started) {
                                                    if (bodyCount < bodyLimit) {
                                                        try {
                                                            Satellite satellite = solarSystem.createCustomSatellite(characteristics);
                                                            solarSystem.addSatellite(satellite);
                                                            solarSystem.getPlot().addPoint(satellite.getColor(), satellite.getPointSize(),
                                                                    0,0);
                                                            solarSystem.getPlot().repaint();
                                                            ++bodyCount;
                                                        } catch (SolarSystemException ss) {
                                                            ss.printStackTrace();
                                                        }
                                                    } else {
                                                        alert("Sorry, no more bodies can be added!");
                                                    }
                                                } else {
                                                    if (bodyCount < bodyLimit) {
                                                        try {
                                                            Satellite satellite = solarSystem.createCustomSatellite(characteristics);
                                                            solarSystem.addSatellite(satellite);
                                                            solarSystem.getExecutorService().execute(satellite);
                                                            ++bodyCount;
                                                        } catch (SolarSystemException ss) {
                                                            ss.printStackTrace();
                                                        }
                                                    } else {
                                                        alert("Sorry, no more bodies can be added!");
                                                    }
                                                }
                                            } catch (Exception ss) {
                                                ss.printStackTrace();
                                            }

                                            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                            addCustomFrame.setVisible(false);
                                            addCustomFrame.dispose();
                                        }
                                    });
                                }
                            });

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

                            addMoonOfEarth.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        if (!started) {
                                            if (bodyCount < bodyLimit) {
                                                try {
                                                    Satellite satellite = solarSystem.newSatellite("Moon");
                                                    solarSystem.addSatellite(satellite);
                                                    solarSystem.getPlot().addPoint(satellite.getColor(), satellite.getPointSize(),
                                                            satellite.relativeX / AU / satellite.getDivisor() + satellite.getBody().getX() / AU / satellite.getBody().getDivisor(),
                                                            satellite.relativeY / AU / satellite.getDivisor() + satellite.getBody().getY() / AU / satellite.getBody().getDivisor());
                                                    solarSystem.getPlot().repaint();
                                                    ++bodyCount;
                                                } catch (Exception s) {
                                                    s.printStackTrace();
                                                    alert("Planet has not been added yet!");
                                                }
                                            } else {
                                                alert("Sorry, no more bodies can be added!");
                                            }
                                        } else {
                                            try {
                                                Satellite satellite = solarSystem.newSatellite("Moon");
                                                solarSystem.addSatellite(satellite);
                                                solarSystem.getExecutorService().execute(satellite);
                                                ++bodyCount;
                                            } catch (Exception s) {
                                                alert("Planet has not been added yet!");
                                            }
                                        }
                                    } catch (Exception ss) {
                                        alert("Sorry, this satellite already exists");
                                    }
                                }
                            });

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

        controlFrame.pack();

        controlFrame.setResizable(true);
        controlFrame.setVisible(true);
    }

    private void alert(String alert) {

        JOptionPane.showMessageDialog(null, alert);

    }

}

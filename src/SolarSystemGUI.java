import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SolarSystemGUI extends JFrame {

    private Panel controlPanel;
    private Frame controlFrame;

    private Frame addPlanetsFrame;
    private Panel addPlanetsPanel;

    public SolarSystemGUI() {
        controlFrame = new Frame("Alter the Simulation before it starts here!");
        controlPanel = new Panel();

        controlFrame.add(controlPanel, BorderLayout.CENTER);

        Button addPlanet = new Button("Add Planet");		// create a button to clear the plot
        addPlanet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addPlanetsFrame = new Frame();
                addPlanetsPanel = new Panel();
                //Need to do more work here, left this class for the satellite class...
            }
        });											// tell it what to do when the button is clicked
        this.controlPanel.add(addPlanet);


    }



}

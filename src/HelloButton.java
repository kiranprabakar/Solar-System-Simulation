import java.awt.*;
import java.awt.event.*;

class HelloButton {
    HelloButton() {
        Frame myFrame = new Frame("See the button!");
        myFrame.addWindowListener(new WindowAdapter() {    //close window when x is pressed
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        Panel myPanel = new Panel();
        Button myButton = new Button("Press me!");
        myButton.addActionListener(new ActionListener() {           //Print hello world when pressed
            public void actionPerformed(ActionEvent e) {
                System.out.println("Hello, world!");
            }
        });
        myFrame.add(myPanel);
        myPanel.add(myButton);
        myFrame.pack();
        myFrame.setVisible(true);
    }

    public static void main(String[] arg) {
        new HelloButton();
    }
}
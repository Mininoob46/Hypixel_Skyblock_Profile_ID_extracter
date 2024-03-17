package de.hype.bbsentials.profileidfromlogs;

import javax.swing.*;
import java.awt.*;
import de.hype.bbsentials.profileidfromlogs.MainGui.*;

public class SimplePopup {
    private JFrame frame;
    private JTextArea label;

    // Constructor
    public SimplePopup(String initialText) {
        // Create the frame
        frame = new JFrame("Profile ID Extracter Info");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Create the label with initial text
        System.out.println(initialText);
        label = new JTextArea(initialText);
        label.setMargin(new Insets(0, 10, 0, 0));
        label.setColumns(50);
        label.setRows(50);

        // Add label to frame
        frame.getContentPane().add(label);
        frame.add(label);
        // Set frame size and visibility
        frame.setSize(600, 300);
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }

    // Method to change the text shown in the popup
    public void setText(String newText) {
        System.out.println(newText);
        label.append("\n" + newText); // Append the new text
    }

    public void setClosable(boolean closable){
        if (closable) frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            else frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }
}

package de.hype.bbsentials.profileidfromlogs;

import javax.swing.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static de.hype.bbsentials.profileidfromlogs.Utils.*;
import static de.hype.bbsentials.profileidfromlogs.MainGui.*;

public class Main {
    public static void main(String[] args) {
        // Create and show the Swing GUI
        MainGui gui = new MainGui();
        gui.setVisible(true);
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
                //ignored
            }
        }
    }
}


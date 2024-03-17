package de.hype.bbsentials.profileidfromlogs;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.time.Instant;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static de.hype.bbsentials.profileidfromlogs.Utils.*;

public class MainGui extends JFrame {
    private JButton searchLogButton;
    private JPanel panel1;
    private JButton hypixelAPIKeyButton;
    private JTextField usernameTextField;
    private JPasswordField apiKeyField;

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    public MainGui() {
        setTitle("Profile ID Extractor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        add(panel1);
        setLocationRelativeTo(null);
        hypixelAPIKeyButton.addActionListener(e -> {
            openURLInBrowser("https://developer.hypixel.net/dashboard"); //open the api developer
        });
        searchLogButton.addActionListener(e -> {
            doSearch();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGui gui = new MainGui();
            gui.setVisible(true); // Show the GUI
        });
    }

    public String getHypixelAPIKey() {
        return apiKeyField.getText();
    }

    public String getUsername() {
        return usernameTextField.getText();
    }

    public void doSearch() {
        Core core = new Core(getHypixelAPIKey(), getUsername());
        Instant startingTime = Instant.now();
        SimplePopup popup = new SimplePopup("Starting Log Search");

        // Create a new thread to perform the search operation
        Thread searchThread = new Thread(() -> {
            List<String> files = searchUserLogs(getUserHome());
            popup.setText("Looking for logs took: " + (Instant.now().toEpochMilli() - startingTime.toEpochMilli()) + "ms");
            Set<String> profileIds = extractProfileIdsFromLogs(files, true);
            System.out.println("All Keys: \n" + String.join("\n", profileIds));
            popup.setText("Looking for profile ids in the logs took: " + (Instant.now().toEpochMilli() - startingTime.toEpochMilli()) + "ms");
            popup.setText("Starting profile id verification");
            List<String> profileIdList = new ArrayList<>(profileIds);
            int listSize = profileIdList.size();
            List<Profile> validProfiles = new ArrayList<>();
            String mcuuid = core.getMcuuid();
            for (int i = 0; i < profileIdList.size(); i++) {
                String profileId = profileIdList.get(i);
                System.out.println("Verifying Profile id: " + profileId + " → " + (i + 1) + "/" + listSize);
                Profile prof = core.getProfile(profileId);
                if (prof != null && prof.isValid(mcuuid)) {
                    validProfiles.add(prof);
                }
                // Update GUI with progress or partial results
                String progressText = "Progress: " + (i + 1) + "/" + listSize;
                SwingUtilities.invokeLater(() -> popup.setText(progressText));
            }
            validProfiles.sort(Comparator.comparingInt(Profile::getBingoId));
            System.out.println("Everything combined took: " + (Instant.now().toEpochMilli() - startingTime.toEpochMilli()) + "ms");
            // Update GUI with final result
            SwingUtilities.invokeLater(() -> popup.setText("Result: \n" + validProfiles.stream().map(Profile::getDisplayString).collect(Collectors.joining("\n"))));
            popup.setClosable(true);
        });

        // Start the search thread
        searchThread.start();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setAutoscrolls(false);
        panel1.setEnabled(true);
        searchLogButton = new JButton();
        searchLogButton.setEnabled(true);
        Font searchLogButtonFont = this.$$$getFont$$$(null, Font.BOLD, -1, searchLogButton.getFont());
        if (searchLogButtonFont != null) searchLogButton.setFont(searchLogButtonFont);
        searchLogButton.setMargin(new Insets(0, 0, 0, 0));
        searchLogButton.setText("Search Log");
        panel1.add(searchLogButton, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        hypixelAPIKeyButton = new JButton();
        hypixelAPIKeyButton.setText("Hypixel API Key:");
        panel1.add(hypixelAPIKeyButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        usernameTextField = new JTextField();
        usernameTextField.setText("");
        panel1.add(usernameTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        apiKeyField = new JPasswordField();
        panel1.add(apiKeyField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Minecraft Username:");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        }
        else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            }
            else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}

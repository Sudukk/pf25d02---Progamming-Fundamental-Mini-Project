import java.awt.*;
import javax.swing.*;

public class HomeMenu extends JPanel {
    public HomeMenu(Menus menus) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        JButton playBtn = new JButton("start a game");
        // TODO: add settings menu
        JButton settingsBtn = new JButton("settings");

        playBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonsPanel.add(playBtn);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(settingsBtn);

        gbc.gridx = 0;
        gbc.gridy = 0;

        playBtn.addActionListener(_ -> menus.showMenu("gameMenu"));

        add(buttonsPanel, gbc);
    }
}

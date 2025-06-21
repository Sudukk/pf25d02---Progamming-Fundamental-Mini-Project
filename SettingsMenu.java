import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;

public class SettingsMenu extends JPanel {
    public class VolumesMenu extends JPanel {
        JLabel SFXVolumeLable;
        JSlider SFXVolume;

        JLabel musicVolumeLable;
        JSlider musicVolume;

        public VolumesMenu() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            add(Box.createRigidArea(new Dimension(0, 15)));

            SFXVolumeLable = new JLabel("SFX Volume");
            SFXVolumeLable.setAlignmentX(Component.CENTER_ALIGNMENT);
            SFXVolumeLable.setFont(new Font("SegoeUI", Font.BOLD, 18));
            SFXVolumeLable.setForeground(Color.WHITE);

            SFXVolume = new JSlider(0, 100);
            SFXVolume.setBackground(GameConstants.COLOR_BG);
            SFXVolume.setForeground(Color.WHITE);
            SFXVolume.setMajorTickSpacing(25);
            SFXVolume.setMinorTickSpacing(5);
            SFXVolume.setPaintTicks(true);
            SFXVolume.setPaintLabels(true);
            SFXVolume.setValue((int) SoundEffect.sfxVolume); // added
            SFXVolume.addChangeListener(e -> {
                SoundEffect.updateAllSFXVolume(SFXVolume.getValue());
            });
            musicVolumeLable = new JLabel("Music Volume");
            musicVolumeLable.setAlignmentX(Component.CENTER_ALIGNMENT);
            musicVolumeLable.setFont(new Font("SegoeUI", Font.BOLD, 18));
            musicVolumeLable.setForeground(Color.WHITE);

            musicVolume = new JSlider(0, 100);
            musicVolume.setBackground(GameConstants.COLOR_BG);
            musicVolume.setForeground(Color.WHITE);
            musicVolume.setMajorTickSpacing(25);
            musicVolume.setMinorTickSpacing(5);
            musicVolume.setPaintTicks(true);
            musicVolume.setPaintLabels(true);
            musicVolume.setValue((int) SoundEffect.musicVolume); // added
            musicVolume.addChangeListener(e -> {
                SoundEffect.musicVolume = musicVolume.getValue();
                SoundEffect.updateBGMusicVolume(musicVolume.getValue());
            });

            add(Box.createRigidArea(new Dimension(0, 15)));
            add(SFXVolumeLable);
            add(SFXVolume);
            add(Box.createRigidArea(new Dimension(0, 15)));
            add(musicVolumeLable);
            add(musicVolume);
            add(Box.createRigidArea(new Dimension(0, 15)));
        }
    }

    public class IconMenu extends JPanel {
        JLabel XIconLabel;
        JButton XIconBtn;
        JLabel OIconLabel;
        JButton OIconBtn;

        public IconMenu() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setOpaque(false);

            XIconLabel = new JLabel("None", SwingConstants.CENTER);
            OIconLabel = new JLabel("None", SwingConstants.CENTER);

            XIconBtn = new JButton("Set X Icon");
            OIconBtn = new JButton("Set O Icon");

            //perilaku button
            XIconBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String path = getSelectedFilePath();
                    if (!path.isEmpty()) {
                        Seed.CROSS.changeIcon(path); // Assuming this exists
                        XIconLabel.setText(path);
                    }
                }
            });

            OIconBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String path = getSelectedFilePath();
                    if (!path.isEmpty()) {
                        Seed.NOUGHT.changeIcon(path); // Assuming this exists
                        OIconLabel.setText(path);
                    }
                }
            });

            //XIcon Button
            XIconBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            XIconBtn.setAlignmentY(10);
            XIconBtn.setFont(new Font("SegoeUI", Font.BOLD, 20));
            XIconBtn.setMaximumSize(new Dimension(240, 50));
            XIconBtn.setBackground(GameConstants.COLOR_BG);
            XIconBtn.setForeground(Color.WHITE);
            XIconBtn.setFocusPainted(false);

            XIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            XIconLabel.setFont(new Font("SegoeUI", Font.ITALIC, 16));
            XIconLabel.setForeground(Color.WHITE);

            //OIcon Button
            OIconBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            OIconBtn.setAlignmentY(10);
            OIconBtn.setFont(new Font("SegoeUI", Font.BOLD, 20));
            OIconBtn.setMaximumSize(new Dimension(240, 50));
            OIconBtn.setBackground(GameConstants.COLOR_BG);
            OIconBtn.setForeground(Color.WHITE);
            OIconBtn.setFocusPainted(false);

            OIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            OIconLabel.setFont(new Font("SegoeUI", Font.ITALIC, 16));
            OIconLabel.setForeground(Color.WHITE);

            //        pakai glue untuk memusatkan secara vertikal
            add(Box.createVerticalGlue());
            add(XIconBtn);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(XIconLabel);
            add(Box.createRigidArea(new Dimension(0, 30)));
            add(OIconBtn);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(OIconLabel);
            add(Box.createVerticalGlue());
        }

        private String getSelectedFilePath() {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Choose Icon");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setAcceptAllFileFilterUsed(true);
            chooser.addChoosableFileFilter(
                    new javax.swing.filechooser.FileNameExtensionFilter("PNG Images", "png"));

            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                return selectedFile.getPath();
            }
            return "";
        }
    }

    JLabel settingsTitle;
    JPanel menus;
    JButton backBtn;

    public SettingsMenu(JFrame frame) {
        setLayout(new BorderLayout());
        frame.setBackground(GameConstants.COLOR_BG);

        settingsTitle = new JLabel("Option Menu", SwingConstants.CENTER);
        settingsTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        backBtn = new JButton("Back to Main Menu");
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setAlignmentY(10);
        backBtn.setFont(new Font("SegoeUI", Font.BOLD, 20));
        backBtn.setMaximumSize(new Dimension(240, 50));
        backBtn.setBackground(Color.LIGHT_GRAY);
        backBtn.setForeground(Color.BLACK);
        backBtn.setFocusPainted(false);

        backBtn.addActionListener(e -> {
            frame.setContentPane(new StartMenu(frame));
            frame.revalidate();
            frame.repaint();
        });

        VolumesMenu volumes = new VolumesMenu();
        volumes.setBackground(GameConstants.COLOR_BG);
        // UIMenu uimenu = new UIMenu();
        IconMenu iconmenu = new IconMenu();
        iconmenu.setBackground(GameConstants.COLOR_BG);

        menus = new JPanel();
        menus.setBackground(GameConstants.COLOR_BG);

        menus.setLayout(new BorderLayout());
        menus.add(volumes, BorderLayout.NORTH);
        // menus.add(uimenu, BorderLayout.CENTER);
        menus.add(iconmenu, BorderLayout.CENTER);

        add(settingsTitle, BorderLayout.NORTH);
        add(menus, BorderLayout.CENTER);
        add(backBtn, BorderLayout.SOUTH);
    }
}

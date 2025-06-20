import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.reflect.Field;
import java.io.File;
import java.util.ArrayList;

public class SettingsMenu extends JPanel {
    public class VolumesMenu extends JPanel {
        JLabel masterSFXLable;
        JSlider masterSFX;

        JLabel SFXVolumeLable;
        JSlider SFXVolume;

        JLabel musicVolumeLable;
        JSlider musicVolume;

        public VolumesMenu() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            add(Box.createRigidArea(new Dimension(0, 15)));

            masterSFXLable = new JLabel("Master SFX: ");
            masterSFX = new JSlider(0, 100);
            masterSFX.setMajorTickSpacing(25);
            masterSFX.setMinorTickSpacing(5);
            masterSFX.setPaintTicks(true);
            masterSFX.setPaintLabels(true);

            SFXVolumeLable = new JLabel("SFX Volume: ");
            SFXVolume = new JSlider(0, 100);
            SFXVolume.setMajorTickSpacing(25);
            SFXVolume.setMinorTickSpacing(5);
            SFXVolume.setPaintTicks(true);
            SFXVolume.setPaintLabels(true);

            musicVolumeLable = new JLabel("Music Volume: ");
            musicVolume = new JSlider(0, 100);
            musicVolume.setMajorTickSpacing(25);
            musicVolume.setMinorTickSpacing(5);
            musicVolume.setPaintTicks(true);
            musicVolume.setPaintLabels(true);

            add(masterSFXLable);
            add(masterSFX);
            add(Box.createRigidArea(new Dimension(0, 15)));
            add(SFXVolumeLable);
            add(SFXVolume);
            add(Box.createRigidArea(new Dimension(0, 15)));
            add(musicVolumeLable);
            add(musicVolume);
            add(Box.createRigidArea(new Dimension(0, 15)));
        }
    }

    public class UIMenu extends JPanel {
        JLabel bgColorLabel;
        JComboBox<String> bgColorDropdown;
        JLabel textSizeLabel;
        JComboBox<String> textSizeDropdown;

        public UIMenu() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            add(Box.createRigidArea(new Dimension(0, 15)));

            String[] bgColors = getAvailableColors();
            bgColorDropdown = new JComboBox<>(bgColors);

            bgColorLabel = new JLabel("Selected: ");
            bgColorDropdown.addActionListener(_ -> {
                String selectedColor = (String) bgColorDropdown.getSelectedItem();
                bgColorLabel.setText(bgColorLabel.getText() + selectedColor);
            });

            String[] textSizes = { "small", "medium", "large" };
            textSizeDropdown = new JComboBox<>(textSizes);

            textSizeLabel = new JLabel("Selected: ");
            textSizeDropdown.addActionListener(_ -> {
                String selectedText = (String) textSizeDropdown.getSelectedItem();
                textSizeLabel.setText(textSizeLabel.getText() + selectedText);
            });

            add(bgColorLabel);
            add(bgColorDropdown);
            add(Box.createRigidArea(new Dimension(0, 15)));
            add(textSizeLabel);
            add(textSizeDropdown);
            add(Box.createRigidArea(new Dimension(0, 15)));
        }
    }

    public class IconMenu extends JPanel {
        JLabel XIconLabel;
        JButton XIconBtn;
        JLabel OIconLabel;
        JButton OIconBtn;

        public IconMenu() {
            setLayout(new BorderLayout());

            XIconBtn = new JButton("X's Icon: ");

            OIconBtn = new JButton("O's Icon: ");

            XIconBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // TODO Auto-generated method stub
                    String path = getSelectedFilePath();
                    String curr = XIconLabel.getText();
                    curr = path;
                    XIconLabel.setText(curr);
                }
            });

            OIconBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // TODO Auto-generated method stub
                    String path = getSelectedFilePath();
                    String curr = OIconLabel.getText();
                    curr = path;
                    OIconLabel.setText(curr);

                }
            });

            add(XIconBtn, BorderLayout.WEST);
            add(OIconBtn, BorderLayout.EAST);
        }

        private String getSelectedFilePath() {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("choose");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            chooser.setAcceptAllFileFilterUsed(true);
            chooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("jpg", "png"));

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

    public SettingsMenu() {
        setLayout(new BorderLayout());

        settingsTitle = new JLabel("Option Menu", SwingConstants.CENTER);
        settingsTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        backBtn = new JButton("back to main menu");
        backBtn.addMouseListener(new MouseAdapter() {
            // TODO: go back to main menu
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });

        VolumesMenu volumes = new VolumesMenu();
        UIMenu uimenu = new UIMenu();
        IconMenu iconmenu = new IconMenu();

        menus = new JPanel();
        menus.setLayout(new BorderLayout());
        menus.add(volumes, BorderLayout.NORTH);
        menus.add(uimenu, BorderLayout.CENTER);
        menus.add(iconmenu, BorderLayout.SOUTH);

        add(settingsTitle, BorderLayout.NORTH);
        add(menus, BorderLayout.CENTER);
        add(backBtn, BorderLayout.SOUTH);
    }

    private String[] getAvailableColors() {
        ArrayList<String> colors = new ArrayList<>();

        Field[] fields = Color.class.getFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.getType() == Color.class) {
                if (i % 2 == 0) {
                    colors.add(field.getName());
                }
            }
        }

        String[] colorsArray = new String[colors.size()];
        colorsArray = colors.toArray(colorsArray);

        return colorsArray;
    }
}

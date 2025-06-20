import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class OptionsPanel extends JPanel {
    public OptionsPanel(JFrame frame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Options");
        title.setAlignmentX(CENTER_ALIGNMENT);
        add(title);

        addSlider("Master Volume", OptionsManager.getMasterVolume(), v -> {
            OptionsManager.setMasterVolume(v);
            // SoundManager.updateVolumes();
        });

        addSlider("SFX Volume", OptionsManager.getSfxVolume(), v -> {
            OptionsManager.setSfxVolume(v);
            // SoundManager.updateVolumes();
        });

        addSlider("Music Volume", OptionsManager.getMusicVolume(), v -> {
            OptionsManager.setMusicVolume(v);
            // SoundManager.updateVolumes();
        });

        addTextButton("BG Color", OptionsManager.getBgColor(), () -> {
            String[] choices = { "black", "white" };
            String choice = (String) JOptionPane.showInputDialog(
                    this, "Select BG Color", "BG Color",
                    JOptionPane.PLAIN_MESSAGE, null, choices, OptionsManager.getBgColor());
            if (choice != null)
                OptionsManager.setBgColor(choice);
        });

        addTextButton("Text Size", OptionsManager.getTextSize(), () -> {
            String[] choices = { "small", "medium", "large" };
            String choice = (String) JOptionPane.showInputDialog(
                    this, "Select Text Size", "Text Size",
                    JOptionPane.PLAIN_MESSAGE, null, choices, OptionsManager.getTextSize());
            if (choice != null)
                OptionsManager.setTextSize(choice);
        });

        addImageChooser("X Icon", path -> OptionsManager.setXIconPath(path));
        addImageChooser("O Icon", path -> OptionsManager.setOIconPath(path));

        JButton back = new JButton("Back");
        back.setAlignmentX(CENTER_ALIGNMENT);
        back.setBackground(Color.GREEN);
        back.addActionListener(e -> frame.dispose());
        add(Box.createVerticalStrut(10));
        add(back);
    }

    private void addSlider(String label, float initVal, Consumer<Float> onChange) {
        JLabel lbl = new JLabel(label);
        lbl.setAlignmentX(CENTER_ALIGNMENT);
        add(lbl);

        JSlider slider = new JSlider(0, 100, (int) (initVal * 100));
        slider.setMaximumSize(new Dimension(200, 40));
        slider.setAlignmentX(CENTER_ALIGNMENT);
        slider.addChangeListener(e -> {
            float val = slider.getValue() / 100f;
            onChange.accept(val);
        });
        add(slider);
    }

    private void addTextButton(String label, String initialText, Runnable onClick) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(label));
        JButton btn = new JButton(initialText);
        btn.addActionListener(e -> {
            onClick.run();
            btn.setText(label.equals("BG Color") ? OptionsManager.getBgColor() : OptionsManager.getTextSize());
        });
        panel.add(btn);
        add(panel);
    }

    private void addImageChooser(String label, Consumer<String> onFileChosen) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(label));
        JButton btn = new JButton("Select Image");
        btn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "png", "gif"));
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                onFileChosen.accept(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        panel.add(btn);
        add(panel);
    }

    interface Consumer<T> {
        void accept(T t);
    }
}

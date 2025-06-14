import java.awt.*;
import javax.swing.*;

public class Menus extends JPanel {
    public Menus() {
        setLayout(new CardLayout());

        add("homeMenu", new HomeMenu(this));
    }

    public void showMenu(String name) {
        CardLayout cl = (CardLayout) this.getLayout();
        cl.show(this, name);
    }
}

import javax.swing.*;
import java.awt.*;
public class Artist extends JPanel {
    private int radius = 20;
    public void setRadius (int r) {
        radius = r;
        repaint();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.drawOval(10,10,2*radius,2*radius);
    }
}

import javax.swing.*;
import java.awt.*;

public class CirclesArt extends JPanel{
    private int rad1 = 20;
    private int rad2 = 10;
    private int length = 20;
    private String color = "black";

    public void setRad1(int x) {
        rad1 = x;
        repaint();
    }

    public void setRad2(int x) {
        rad2 = x;
        repaint();
    }

    public void setLength(int x) {
        length = x;
        repaint();
    }

    public void setColor(String color) {
        this.color = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.drawOval(10, 10, 2*rad1, 2*rad1);
        int d = (int)(Math.sqrt(2)*length);
        g.drawOval(10 + d, 10 + d, 2*rad2, 2*rad2);
        double x= Math.sqrt(2 * Math.pow(10 + d + rad2 - (10 + rad1), 2));
        double sr = rad1+rad2;
        if (color.equals("red") && sr>=x)
            g.setColor(Color.RED);
        double help = 2;
                //g.drawOval();*/
    }
}

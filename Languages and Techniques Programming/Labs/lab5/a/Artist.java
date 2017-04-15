import javax.swing.*;
import java.awt.*;

public class Artist extends JPanel {
    private int width = 5;
    private int length = 5;
    private String color = "Black";

    public void setwidth(int x) {
        width = x;
        repaint();
    }

    public void setlength(int x) {
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
        int help;
        if (color.equals("Black")) {
            g.setColor(Color.BLACK);
            help = 0;
        }
        else {
            g.setColor((Color.WHITE));
            help = 1;
        }
        for (int k = 0; k < width; k++)
        {
            for (int l = 0; l < length; l++)
            {
                if ((k+l) % 2 == help)
                    g.setColor(Color.BLACK);
                else g.setColor(Color.WHITE);
                g.fillRect(30 + k * 20, 10 + l * 20, 20, 20);
            }
        }
    }
}

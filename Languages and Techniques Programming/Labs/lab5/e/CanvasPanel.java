import javax.swing.*;
import java.awt.*;

/**
 * Created by asstarra on 20.03.15.
 */
public class CanvasPanel extends JPanel {
    private int k1 = 30, k2 = 50, start = 10, color=0;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        switch(color) {
            case 0: {
                g.setColor(Color.black);
                break;
            }
            case 1: {
                g.setColor(Color.red);
                break;
            }
            case 2: {
                g.setColor(Color.green);
                break;
            }
            case 3: {
                g.setColor(Color.blue);
                break;
            }
            case 4: {
                g.setColor(Color.yellow);
                break;
            }
            default: {
                g.setColor(Color.white);
                break;
            }
        }
        g.fillRect(start, start + k1, k2, k2);
        g.fillRect(start + k2, start, k1, k1);
        g.fillPolygon(new int[]{start + k1 + k2, start + k1 + 2 * k2, start + 2 * k2, start + k2}, new int[]{start + k1, start + 2 * k1, start + 2 * k1 + k2, start + k1 + k2}, 4);
    }

    public void setKatetA(int katetA) {
        this.k1 = katetA;
        repaint();
    }

    public void setKatetB(int katetB) {
        this.k2 = katetB;
        repaint();
    }

    public void setColor(int color) {
        this.color = color;
        repaint();
    }
}

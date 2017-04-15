import javax.swing.*;
import java.awt.*;

public class CanvasPenal extends JPanel
{
    private int count_w = 4, count_h = 3;
    private boolean color = false;

    public void setCount_w(int h)
    {
        count_h = h;
        repaint();
    }

    public void setCount_h(int w)
    {
        count_w = w;
        repaint();
    }

    public void setColor(boolean c)
    {
        if(color != c) {
            color = c;
            repaint();
        }
    }
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        int q = color ? 0 : 1;
        for (int k = 0; k < count_w; k ++)
        {
            for (int l = 0; l < count_h; l ++)
            {
                if ((k + l) % 2 == q)
                {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(30 + k * 20, 10 + l * 20, 20, 20);
            }
        }
    }
}

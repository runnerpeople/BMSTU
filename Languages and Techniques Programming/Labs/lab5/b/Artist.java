import javax.swing.*;
import java.awt.*;

public class Artist extends JPanel{
    private int a = 100;
    private int b = 100;
    private int c = 100;
    private String color = "Red";

    public void setLine(int a,int b,int c) {
        this.a=a;
        this.b=b;
        this.c=c;
        repaint();
    }

    public void setLine2(String color) {
        this.color=color;
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (color.equals("Red"))
            g.setColor(Color.RED);
        else if (color.equals("Blue"))
            g.setColor(Color.BLUE);
        else if (color.equals("Green"))
            g.setColor(Color.GREEN);
        else if (color.equals("White"))
            g.setColor(Color.WHITE);
        else if (color.equals("Black"))
            g.setColor(Color.BLACK);
        else if (color.equals("Cyan"))
            g.setColor(Color.CYAN);
        else if (color.equals("Orange"))
            g.setColor(Color.ORANGE);
        else if (color.equals("Yellow"))
            g.setColor(Color.YELLOW);
        if(a+b>c && b+c>a && c+a>b) {
            g.drawLine(20, 20, a, 20);
            int x = (b * b - c * c + a * a - 400) / (2 * a - 20);
            int y = (int) Math.sqrt(Math.abs((b - x + 20) * (b + x - 20))) + 20;
            g.drawLine(20, 20, x, y);
            g.drawLine(a, 20, x, y);
        }
        else {
            g.setColor(Color.RED);
            g.drawString("Mistake in rule: a+b>c", 100,100);
        }
    }
}

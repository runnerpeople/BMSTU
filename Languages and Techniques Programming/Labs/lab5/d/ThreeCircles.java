import com.sun.prism.*;
import javax.swing.*;
import java.awt.*;
import java.awt.BasicStroke;
import java.awt.Graphics;
public class ThreeCircles extends JPanel {
    private int aRadius = 20, bRadius = 20, cRadius = 20;
    private boolean multicolor = false;
    public void setaRadius (int r){
        aRadius = r;
        repaint();
    }
    public void setbRadius (int r){
        bRadius = r;
        repaint();
    }
    public void setcRadius (int r){
        cRadius = r;
        repaint();
    }
    public void setMulticolor () {
        multicolor = !multicolor;
        repaint();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int centerHeight  = this.getHeight() / 2;
        int centerWidth = this.getWidth() / 2;
        int a = aRadius + bRadius;
        int b = bRadius + cRadius;
        int c = cRadius + aRadius;
        double p = (a + b + c) / 2;
        double S = Math.sqrt(p * (p - a) * (p - b) * (p - c));
        double h  =  (2 * S / a);
        int x  = (int) Math.sqrt(c*c - h*h);
        Graphics2D g2 = (Graphics2D) g;
        BasicStroke myStroke = new BasicStroke(3.0f);
        g2.setStroke(myStroke);
        g2.setColor(Color.BLACK);
        if (multicolor) g2.setColor(Color.red);
        g2.drawOval(centerWidth  - 2*aRadius,centerHeight  - aRadius,2*aRadius,2*aRadius);
        if (multicolor) g2.setColor(Color.yellow);
        g2.drawOval(centerWidth ,centerHeight  - bRadius,2*bRadius,2*bRadius);
        if (multicolor) g2.setColor(Color.green);
        g2.drawOval(centerWidth - (aRadius - x + cRadius), centerHeight + (int)h - cRadius, 2*cRadius, 2*cRadius);
        
    }
}

import javax.swing.*;
import java.awt.*;
import java.util.*;
class Circle {
    private static Random rand = new Random();
    private static final Color[] COLORS = new Color[] {
    Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA
    };
    private int x, y, r, speed;
    private Color color;
    private long createTime;
    public Circle(int x, int y, int r) {
        this.r = r;
        this.x = x;
        this.y = y;
        speed = (int) Math.pow(-1, rand.nextInt(2)) * 30;
        color = COLORS[rand.nextInt(COLORS.length)];
        createTime = System.currentTimeMillis();
    }
    private int y() {
        long curTime = System.currentTimeMillis();
        return y;
    }
    private int x() {
        long curTime = System.currentTimeMillis();
        return x + (int)((curTime - createTime)*speed/1000);
    }
    public void paint(Graphics g) {
        g.setColor(color);
        g.fillOval(x() - r, y() - r, 2*r, 2*r);
    }
    public void paintDragging (Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x() - r, y() - r, 2*r, 2*r);
    }
    public boolean hit(int boardWidth) { return (x()- 2*r > boardWidth || x() + r <= 0); }
    
    public void reverseSpeed() {
        this.speed = - this.speed;
    }
}
public class FlyingCircles extends JPanel {
    private ArrayList <Circle> cs = new ArrayList<>();
    private int x, y, radius;
    private boolean flag = false;
    public void add (int x, int y, int radius) {
        cs.add(new Circle(x, y, radius));
        this.flag = false;
    }
    public void step() {
        repaint();
    }
    public void setDrawDragging (int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.flag = true;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        Iterator<Circle> it = cs.iterator();
        while (it.hasNext()) {
            Circle temp = it.next();
            if (temp.hit(width)) it.remove();
        }
        if (flag) {
            new Circle(x, y, radius).paintDragging(g);
        }
        for (int i = cs.size() - 1; i >= 0; i--) cs.get(i).paint(g);
        Toolkit.getDefaultToolkit().sync();
    }
}

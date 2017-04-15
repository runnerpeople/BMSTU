import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

class Circle {
    private static Random rand = new Random();

    private int x, y, r, speed;
    private long createTime;

    public Circle(int x,int y) {
        r = 3;
        this.x = x;
        this.y = y;
        speed = rand.nextInt(70)+10;
        createTime = System.currentTimeMillis();
    }

    private int x() {
        long curTime = System.currentTimeMillis();
        return x + (int)((curTime - createTime)*speed/1000);
    }

    public void paint(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillOval(x()-3, y-3, 20, 20);
    }

    public boolean inside(int mx, int my) {
        int dx = x() - mx, dy = y - my;
        return Math.sqrt(dx*dx + dy*dy) <= r;
    }

    public boolean lost(int boardHeight) { return x()-3 > boardHeight; }
}

class Square {
    private static Random rand = new Random();

    private int x,y,speed;
    private long createTime;

    public Square(int x,int y) {
        this.x=x;
        this.y=y;
        speed = rand.nextInt(70)+10;
        createTime = System.currentTimeMillis();
    }

    private int x() {
        long curTime=System.currentTimeMillis();
        return x + (int)((curTime-createTime)*speed/1000);
    }

    public void paint(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect((int) (x() - 2.5), (int) (y - 2.5), 20, 20);

    }

    public boolean inside(int mx, int my) {
        int dx = x() - mx, dy = y - my;
        return Math.sqrt(dx*dx + dy*dy) <= y;
    }

    public boolean lost(int boardHeight) { return x()-2.5 > boardHeight; }
}

public class Board extends JPanel {
    private static final long DT = 100;

    private long lastTime = System.currentTimeMillis();
    private ArrayList<Circle> circles = new ArrayList<>();
    private ArrayList<Square> squares = new ArrayList<>();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = circles.size()-1; i >= 0; i--) circles.get(i).paint(g);
        for (int i = squares.size()-1; i >= 0; i--) squares.get(i).paint(g);
        Toolkit.getDefaultToolkit().sync();
    }

    public void step1() {
        long curTime = System.currentTimeMillis();
        if (curTime - lastTime > DT) {
            lastTime = curTime;
        }

        int height = getWidth();
        Iterator<Circle> it = circles.iterator();
        Iterator<Square> it1=squares.iterator();
        while (it.hasNext()) {
            if (it.next().lost(height)) it.remove();
        }
        while (it1.hasNext()) {
            if (it1.next().lost(height)) it1.remove();
        }
        repaint();
    }

    public void paint_circles(int x,int y) {
        circles.add(new Circle(x,y));
    }

    public void paint_squares(int x,int y) {
        squares.add(new Square(x,y));
    }

    public boolean hit(int x, int y) {
        Iterator<Circle> it = circles.iterator();
        while (it.hasNext()) {
            if (it.next().inside(x, y)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public boolean hit1(int x, int y) {
        Iterator<Square> it = squares.iterator();
        while (it.hasNext()) {
            if (it.next().inside(x, y)) {
                it.remove();
                return true;
            }
        }
        return false;
    }
}

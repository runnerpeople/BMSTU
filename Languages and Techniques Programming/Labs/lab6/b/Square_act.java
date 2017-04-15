import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

class Square {

    private static Random rand = new Random();
    private static final Color[] COLORS = new Color[]
            {
                    Color.RED,
                    Color.ORANGE,
                    Color.YELLOW,
                    Color.GREEN,
                    Color.BLUE,
                    Color.MAGENTA,
                    Color.PINK,
                    Color.CYAN
            };
    private Color color;

    private int a, b;       //стороны прямоугольника
    private int x, y;       //координаты центра прямоугольника

    private long createTime;

    public Square(int boardWidth, int boardLength) {
        a = rand.nextInt(90) + 10;
        b = rand.nextInt(90) + 10;
        x = rand.nextInt(boardWidth);
        y = rand.nextInt(boardLength);
        color = COLORS[rand.nextInt(COLORS.length)];
        createTime = System.currentTimeMillis();
    }

    public boolean inside(int mx, int my)
    {
        return ((mx >= x) && (my >= y) && (mx <= x + a) && (my <= y + b));
    }

    public void paint(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, a, b);
    }
}

public class Square_act extends JPanel {

    private static final long DT = 1000;
    private long lastTime = System.currentTimeMillis();


    private ArrayList<Square> cs = new ArrayList<>();
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = cs.size()-1; i >= 0; i--)
            cs.get(i).paint(g);
        Toolkit.getDefaultToolkit().sync();
    }


    public void step() {
        long curTime = System.currentTimeMillis();
        if (curTime - lastTime >= DT) {
            lastTime = curTime;
            cs.add(new Square(getWidth(), getHeight()));
            cs.add(new Square(getWidth(), getHeight()));
            cs.add(new Square(getWidth(), getHeight()));
            cs.add(new Square(getWidth(), getHeight()));
            cs.add(new Square(getWidth(), getHeight()));
        }
        repaint();
    }

    public boolean hit(int x, int y) {
        Iterator<Square> it = cs.iterator();
        while (it.hasNext()) {
            if (it.next().inside(x, y)) {
                it.remove();
                return true;
            }
        }
        return false;
    }
}

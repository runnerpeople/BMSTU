import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
public class Main {
    private Timer timer;
    private JPanel mainPanel;
    private FlyingCircles flyingCircles;
    private int x, y;
    public Main() {
        timer = new Timer(25, (ActionEvent e) -> {
            flyingCircles.step();
        });
        timer.start();
        flyingCircles.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });
        flyingCircles.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                flyingCircles.add(x, y, (int) Math.sqrt((e.getX() - x) * (e.getX() - x) + (e.getY() - y) * (e.getY() - y)));
            }
        });
        flyingCircles.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                flyingCircles.setDrawDragging(x, y, (int) Math.sqrt((e.getX() - x) * (e.getX() - x) + (e.getY() - y) * (e.getY() - y)));
            }
        });
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("PictureForm");
        frame.setContentPane(new Main().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

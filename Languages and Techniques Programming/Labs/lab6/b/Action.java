import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;


public class Action {

    private JPanel panel1;
    private JLabel scoreLabel;
    private Square_act square;
    private int score = 0;

    private Timer timer ;

    public Action() {
        timer = new Timer(10, (ActionEvent e) -> {
            square.step();
        });
        timer.start();
        square.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (square.hit(e.getX(), e.getY())) scoreLabel.setText("" + (++score));
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Action");
        frame.setContentPane(new Action().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

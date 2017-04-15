import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainForm {
    private JPanel mainPanel;
    private Board board;
    private JLabel Square;
    private JLabel Circles;
    private JLabel Score;
    private JLabel Score1;

    private Timer timer;

    private int score=0;
    private int score1=0;

    int test=1;

    public MainForm() {
        timer = new Timer(25, (ActionEvent e) -> {
            board.step1();
        });
        timer.start();
        board.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    int x = e.getX();
                    int y = e.getY();
                    board.paint_circles(x,y);
                }
                if(SwingUtilities.isLeftMouseButton(e)) {
                    int x = e.getX();
                    int y = e.getY();
                    board.paint_squares(x, y);
                }
                if(SwingUtilities.isMiddleMouseButton(e)) {
                    if (board.hit(e.getX(), e.getY())) Score1.setText("" + (++score1));
                    if (board.hit1(e.getX(), e.getY())) Score.setText("" + (++score));
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

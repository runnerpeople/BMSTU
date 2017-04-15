import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class PictureForm {
    private JPanel mainPanel;
    private JSpinner aRadiusSpinner;
    private JLabel aRadius;
    private JLabel bRadius;
    private JSpinner bRadiusSpinner;
    private JSpinner cRadiusSpinner;
    private ThreeCircles ThreeCircles;
    private JRadioButton colorButton;
    public PictureForm() {
        aRadiusSpinner.setValue(20);
        bRadiusSpinner.setValue(20);
        cRadiusSpinner.setValue(20);
        aRadiusSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ThreeCircles.setaRadius((int) aRadiusSpinner.getValue());
            }
        });
        bRadiusSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ThreeCircles.setbRadius((int) bRadiusSpinner.getValue());
            }
        });
        cRadiusSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ThreeCircles.setcRadius((int) cRadiusSpinner.getValue());
            }
        });
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ThreeCircles.setMulticolor();
            }
        });
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("PictureForm");
        frame.setContentPane(new PictureForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Circles {
    private JPanel panel1;
    private JSpinner radius2Spinner;
    private JSpinner radius1Spinner;
    private CirclesArt circlesArt1;
    private JSpinner lengthSpinner;
    private JCheckBox redCheckBox;

    public Circles() {
        radius1Spinner.setValue(20);
        radius2Spinner.setValue(10);
        lengthSpinner.setValue(20);
        radius1Spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                circlesArt1.setRad1((Integer) radius1Spinner.getValue());
            }
        });

        radius2Spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                circlesArt1.setRad2((Integer) radius2Spinner.getValue());
            }
        });

        lengthSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                circlesArt1.setLength((Integer) lengthSpinner.getValue());
            }
        });

        redCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(redCheckBox.isSelected())
                    circlesArt1.setColor("red");
                else circlesArt1.setColor("black");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Circles");
        frame.setContentPane(new Circles().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

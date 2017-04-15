import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class PictureForm {
    private JPanel panel1;
    private JSpinner spinnerlength;
    private JSpinner spinnercwidth;
    private JComboBox comboBoxcolor;
    private Artist artist;

    public PictureForm() {
        spinnerlength.setValue(5);
        spinnercwidth.setValue(5);
        spinnerlength.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                artist.setlength((Integer) spinnerlength.getValue());
            }
        });
        spinnercwidth.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                artist.setwidth((Integer) spinnercwidth.getValue());
            }
        });
        comboBoxcolor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                artist.setColor(comboBoxcolor.getItemAt(comboBoxcolor.getSelectedIndex()) + "");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("PictureForm");
        frame.setContentPane(new PictureForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

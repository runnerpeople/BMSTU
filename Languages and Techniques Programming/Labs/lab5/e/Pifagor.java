import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Pifagor {
    private JPanel picturePanel;
    private JSpinner katetASpinner;
    private JSpinner katetBSpinner;
    private CanvasPanel pifagorPanel;
    private JComboBox colourComboBox;

    public Pifagor() {
        katetASpinner.setValue(30);
        katetBSpinner.setValue(50);
        katetASpinner.addChangeListener(e -> {
            int k1  = (int)katetASpinner.getValue();
            pifagorPanel.setKatetA(k1);
        });
        /*katetASpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int k1  = (int)katetASpinner.getValue();
                pifagorPanel.setKatetA(k1);
            }
        });*/

        katetBSpinner.addChangeListener(e -> {
            int k2  = (int)katetBSpinner.getValue();
            pifagorPanel.setKatetB(k2);
        });
        colourComboBox.setSelectedIndex(0);
        colourComboBox.addActionListener(e -> {
            int color = colourComboBox.getSelectedIndex();
            pifagorPanel.setColor(color);
        });
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Pifagor");
        frame.setContentPane(new Pifagor().picturePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}

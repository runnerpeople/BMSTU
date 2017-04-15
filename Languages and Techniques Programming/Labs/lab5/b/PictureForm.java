import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PictureForm {
    private JPanel mainPanel;
    private Artist TriangleArtist;
    private JLabel Side2;
    private JLabel Side1;
    private JLabel Side3;
    private JLabel Color;
    private JLabel Paint;
    private JComboBox color;
    private JSlider slider1;
    private JLabel value1;
    private JLabel value2;
    private JLabel value3;
    private JSlider slider2;
    private JSlider slider3;

 /*   private class RadiusListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
      //      TriangleArtist.setLine((int) Spinner1.getValue(),(int) Spinner2.getValue(),(int) Spinner3.getValue(),(int)y1Spinner.getValue(),(int)y2Spinner.getValue(),(int)y3Spinner.getValue());
        }
     } */

    private class SliderListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
                value1.setText(slider1.getValue() + "");
                value2.setText(slider2.getValue() + "");
                value3.setText(slider3.getValue() + "");
                TriangleArtist.setLine(slider1.getValue(),slider2.getValue(),slider3.getValue());
        }
    }

    private class ColorListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            TriangleArtist.setLine2(color.getItemAt(color.getSelectedIndex()) + "");
        }
    }

    public PictureForm() {
      /*  radiusSpinner.addChangeListener(new ChangeListener() {
      @Override
        public void stateChanged(ChangeEvent e) {
            circleArtist.setRadius((int)radiusSpinner.getValue());
        }
       }); */
        //mainPanel.setSize(1000,1000);
        slider1.setValue(100);
        slider2.setValue(100);
        slider3.setValue(100);
        value1.setText(slider1.getValue() + "");
        value2.setText(slider2.getValue() + "");
        value3.setText(slider3.getValue() + "");
        slider1.addChangeListener(new SliderListener());
        slider2.addChangeListener(new SliderListener());
        slider3.addChangeListener(new SliderListener());
        color.addActionListener(new ColorListener());
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("PictureForm");
        frame.setContentPane(new PictureForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

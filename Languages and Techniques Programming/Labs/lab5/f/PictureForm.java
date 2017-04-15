import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PictureForm {

    private JPanel picturePanel;
    private JSpinner radiusSpinner;
    private CanvasPanel cubePanel;
    private JCheckBox color;

    private class RadiusChangedListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            int r = (int) radiusSpinner.getValue();
            cubePanel.setRadius(r);
        }
    }

    private class ColorChangedListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            boolean c = color.isSelected();
            cubePanel.setColor(c);
        }
    }

    public PictureForm() {
        radiusSpinner.setValue(3);
/*        radiusSpinner.addChangeListener(new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            int r = (int)radiusSpinner.getValue();
            cubePanel.setRadius(r);
        }
        }); */
        radiusSpinner.addChangeListener(new RadiusChangedListener());
        color.addChangeListener(new ColorChangedListener());

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("PictureForm");
        frame.setContentPane(new PictureForm().picturePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

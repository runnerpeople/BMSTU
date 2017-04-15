import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PictureForm
{
    private JPanel picturePenal;
    private CanvasPenal deskPanel;
    private JCheckBox checkBox;
    private JSpinner spinner_h;
    private JSpinner spinner_w;


    //изменение заливки доски
    private class ColorChangedListener implements ChangeListener
    {
        @Override
        public void stateChanged(ChangeEvent e)
        {
            boolean c = checkBox.isSelected();
            deskPanel.setColor(c);
        }
    }


    //изменение размеров
    private class SpinnerhChangedListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            int n = (int)spinner_h.getValue();           //n - ширина
            deskPanel.setCount_h(n);
        }
    }

    private class SpinnerwChangedListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            int m = (int)spinner_w.getValue();           //n - ширина
            deskPanel.setCount_w(m);
        }
    }

    public PictureForm()
    {
        spinner_w.setValue(4);
        spinner_h.setValue(3);
        spinner_w.addChangeListener(new SpinnerwChangedListener());
        spinner_h.addChangeListener(new SpinnerhChangedListener());
        checkBox.addChangeListener(new ColorChangedListener());
    }


    public static void main(String[] args)
    {
        JFrame frame = new JFrame("PictureForm");
        frame.setContentPane(new PictureForm().picturePenal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

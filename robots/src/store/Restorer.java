package store;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;

public class Restorer {
    public static void restoreInternalFrame(JInternalFrame frame, WindowPosition data) {
        Dimension size = new Dimension();
        size.width = data.getWidth();
        size.height = data.getHeight();
        frame.setSize(size);
        try {
            frame.setIcon(data.isHide());
        } catch (PropertyVetoException ignored) {}
        frame.setLocation(data.getX(), data.getY());
        frame.setVisible(true);
    }
}

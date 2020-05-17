package store;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.Map;

public class Restorer {
    public static void restore(JInternalFrame frame, Map<String, WindowState> store, String windowName) {
        if (store.containsKey(windowName)) {
            WindowState data = store.get(windowName);
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
}

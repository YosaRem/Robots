package gui;

import store.Restorable;
import store.Restorer;
import store.Storable;
import store.WindowPosition;

import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.Map;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame implements Storable, Restorable
{
    private final GameVisualizer m_visualizer;
    public final String windowName = "GameWindow";

    public GameWindow() 
    {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    @Override
    public void restoreOrDefault(Map<String, WindowPosition> store, WindowPosition defaultPosition) {
        Restorer.restoreInternalFrame(this, store.getOrDefault(windowName, defaultPosition));
    }

    @Override
    public WindowPosition getDataForStore() {
        return new WindowPosition(
                windowName,
                this.getSize().width,
                this.getSize().height,
                this.isIcon,
                this.getLocation().x,
                this.getLocation().y
        );
    }
}

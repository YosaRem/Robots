package gui;

import store.HasState;
import store.WindowState;

import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.Map;
import java.util.Timer;


import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import robot.Robot;

public class GameWindow extends JInternalFrame implements HasState {
    public static final String WINDOW_NAME = "GameWindow";

    public GameWindow(Robot robot)
    {
        super("Игровое поле", true, true, true, true);
        Timer timer = new Timer("event generator", true);
        robot.synchronizeWithTimer(timer);
        GameVisualizer m_visualizer = new GameVisualizer(robot);
        m_visualizer.synchronizeWithTimer(timer);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    @Override
    public void setState(Map<String, WindowState> store) {
        if (store.containsKey(WINDOW_NAME)) {
            WindowState data = store.get(WINDOW_NAME);
            Dimension size = new Dimension();
            size.width = data.getWidth();
            size.height = data.getHeight();
            this.setSize(size);
            try {
                this.setIcon(data.isHide());
            } catch (PropertyVetoException ignored) {}
            this.setLocation(data.getX(), data.getY());
            this.setVisible(true);
        }
    }

    @Override
    public WindowState getState() {
        return new WindowState(
                WINDOW_NAME,
                this.getSize().width,
                this.getSize().height,
                this.isIcon,
                this.getLocation().x,
                this.getLocation().y
        );
    }
}

package gui;

import lacal.Localizator;
import store.HasState;
import store.Restorer;
import store.WindowState;

import java.awt.*;
import java.util.Map;
import java.util.Timer;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import robot.Robot;

public class GameWindow extends JInternalFrame implements HasState {
    private static final String WINDOW_NAME = "GameWindow";

    public GameWindow(Robot robot)
    {
        super(Localizator.getLangBundle().getString("GameWindowName"), true, true, true, true);
        setLocation(300, 300);
        setPreferredSize(new Dimension(800, 500));
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
        Restorer.restore(this, store, WINDOW_NAME);
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

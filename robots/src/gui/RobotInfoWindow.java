package gui;

import robot.Observer;
import robot.Robot;
import store.HasState;
import store.WindowState;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.Map;

public class RobotInfoWindow extends JInternalFrame implements HasState, Observer {
    private static final String WINDOW_NAME = "RobotInfoName";

    public RobotInfoWindow() {
        super("Окно информации о роботе", true, true, true, true);
        this.setSize(new Dimension(200, 200));
        this.setLocation(10, 10);
    }

    private void printRobotStatus(Robot robot) {
        Point robotPosition = robot.getRobotPosition();

        this.setLocation(robotPosition.x, robotPosition.y);
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
    public void objectModified(Object obj) {
        if (obj instanceof Robot) {
            printRobotStatus((Robot) obj);
        }
    }
}

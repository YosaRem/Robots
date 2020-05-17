package gui;

import robot.Observer;
import robot.Robot;
import store.HasState;
import store.Restorer;
import store.WindowState;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * This class print out information about robot's position.
 */
public class RobotInfoWindow extends JInternalFrame implements HasState, Observer {
    private static final String WINDOW_NAME = "RobotInfoName";
    private final JTextField infoField;

    public RobotInfoWindow() {
        super("Окно информации о роботе", true, true, true, true);
        this.setSize(new Dimension(200, 200));
        infoField = new JTextField();
        infoField.setEditable(false);
        infoField.setText("x: 0\ny: 0");
        this.add(infoField);
        this.setLocation(10, 10);
    }

    private void printRobotStatus(Robot robot) {
        Point robotPosition = robot.getRobotPosition();
        infoField.setText(String.format(
                "x: %d;  y: %d;  Направление: %f;",
                robotPosition.x, robotPosition.y,
                robot.getRobotDirection()
        ));
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
        Restorer.restore(this, store, WINDOW_NAME);
    }

    @Override
    public void objectModified(Object obj) {
        if (obj instanceof Robot) {
            printRobotStatus((Robot) obj);
        }
    }
}

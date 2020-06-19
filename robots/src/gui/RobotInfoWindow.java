package gui;

import lacal.Localizator;
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
        super(Localizator.getLangBundle().getString("InfoWindowName"), true, true, true, true);
        setLocation(300, 100);
        setSize(new Dimension(300, 100));
        infoField = new JTextField();
        infoField.setEditable(false);
        add(infoField);
    }

    private void printRobotStatus(Robot robot) {
        Point robotPosition = robot.getRobotPosition();
        infoField.setText(String.format(
                "x: %d;  y: %d;  %s: %f;",
                robotPosition.x, robotPosition.y,
                Localizator.getLangBundle().getString("Destination"),
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

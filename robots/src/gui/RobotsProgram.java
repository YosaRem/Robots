package gui;

import robot.Robot;
import robot.Target;
import store.PositionStore;

import java.awt.*;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RobotsProgram
{
    public static void main(String[] args) {
      try {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
      } catch (Exception e) {
        e.printStackTrace();
      }
      SwingUtilities.invokeLater(() -> {
        Robot robot = new Robot(300, 200, new Target(new Point(350, 200)));
        MainApplicationFrame frame = new MainApplicationFrame(robot);
        frame.pack();
        frame.restore(new PositionStore(frame, System.getProperty("user.home")));
        frame.setVisible(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
      });
    }}

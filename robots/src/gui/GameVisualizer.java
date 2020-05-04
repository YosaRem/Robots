package gui;

import robot.Robot;
import robot.Target;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel
{
    private final Robot robot;
    
    public GameVisualizer(Robot robot) {
        this.robot = robot;
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                robot.updateTarget(new Target(e.getPoint()));
                repaint();
            }
        });
        setDoubleBuffered(true);
    }

    public void synchronizeWithTimer(Timer timer) {
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onRedrawEvent();
            }
        }, 0, 50);
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g; 
        drawRobot(g2d);
        Point targetPosition = robot.getTarget().getTargetPosition();
        drawTarget(g2d, targetPosition.x, targetPosition.y);
    }
    
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private void drawRobot(Graphics2D g) {
        Point robotPosition = robot.getRobotPosition();
        AffineTransform t = AffineTransform.getRotateInstance(robot.getRobotDirection(), robotPosition.x, robotPosition.y);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotPosition.x, robotPosition.y, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotPosition.x, robotPosition.y, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotPosition.x  + 10, robotPosition.y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotPosition.x  + 10, robotPosition.y, 5, 5);
    }
    
    private void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0); 
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}

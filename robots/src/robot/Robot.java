package robot;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Robot {
    private volatile double robotPositionX;
    private volatile double robotPositionY;
    private volatile double robotDirection;

    private Target target;

    private static final double MAX_VELOCITY = 0.1;
    private static final double MAX_ANGULAR_VELOCITY = 0.001;
    private static final double VELOCITY_DURATION = 10;

    public Robot(double robotPositionX, double robotPositionY, Target target) {
        this.robotPositionX = robotPositionX;
        this.robotPositionY = robotPositionY;
        this.target = target;
        robotDirection = 0;
        Timer timer = new Timer("events generator", true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onModelUpdateEvent();
            }
        }, 0, 10);

    }

    public Point getRobotPosition() {
        return new Point(round(robotPositionX), round(robotPositionY));
    }

    public double getRobotDirection() {
        return robotDirection;
    }

    public Target getTarget() { return target; }

    public void updateTarget(Target target) {
        this.target = target;
    }

    private void onModelUpdateEvent() {
        Point targetPosition = target.getTargetPosition();
        if (distance(targetPosition.x, targetPosition.y, robotPositionX, robotPositionY) < 0.5) { return; }
        double angleToTarget = angleTo(
                robotPositionX, robotPositionY,
                targetPosition.x, targetPosition.y
        );
        moveRobot((angleToTarget >= robotDirection) ? MAX_ANGULAR_VELOCITY : -MAX_ANGULAR_VELOCITY);
    }

    private void moveRobot(double angularVelocity) {
        angularVelocity = applyLimits(angularVelocity, -MAX_ANGULAR_VELOCITY, MAX_ANGULAR_VELOCITY);
        double newX = robotPositionX + MAX_VELOCITY / angularVelocity *
                (Math.sin(robotDirection + angularVelocity * VELOCITY_DURATION) -
                        Math.sin(robotDirection));
        if (!Double.isFinite(newX)) {
            newX = robotPositionX + MAX_VELOCITY * VELOCITY_DURATION * Math.cos(robotDirection);
        }
        double newY = robotPositionY - MAX_VELOCITY / angularVelocity *
                (Math.cos(robotDirection + angularVelocity * VELOCITY_DURATION) -
                        Math.cos(robotDirection));
        if (!Double.isFinite(newY)) {
            newY = robotPositionY + MAX_VELOCITY * VELOCITY_DURATION * Math.sin(robotDirection);
        }
        robotPositionX = newX;
        robotPositionY = newY;
        double newDirection = asNormalizedRadians(robotDirection + angularVelocity * VELOCITY_DURATION);
        robotDirection = newDirection;
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private static int round(double value) {
        return (int)(value + 0.5);
    }

    private static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        return Math.min(value, max);
    }

    private static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2*Math.PI;
        }
        while (angle >= 2*Math.PI) {
            angle -= 2*Math.PI;
        }
        return angle;
    }
}

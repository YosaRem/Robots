package robot;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Robot implements Observable {
    private final List<Observer> observers = new ArrayList<>();
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
        robotDirection = 1;
    }

    public void synchronizeWithTimer(Timer timer) {
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

    public Target getTarget() {
        return target;
    }

    public void updateTarget(Target target) {
        this.target = target;
    }

    private void onModelUpdateEvent() {
        Point targetPosition = target.getTargetPosition();
        double distance = distance(robotPositionX, robotPositionY,
                targetPosition.x, targetPosition.y);
        if (distance < 0.5) {
            return;
        }
        double angleToTarget = asNormalizedRadians(angleTo(
                robotPositionX, robotPositionY,
                targetPosition.x, targetPosition.y
        ));
        recalculateRobotCoordinate(getShortDirection(angleToTarget));
    }

    private void recalculateRobotCoordinate(double angularVelocity) {
        double newX = robotPositionX + MAX_VELOCITY / angularVelocity *
                (Math.sin(robotDirection  + angularVelocity * VELOCITY_DURATION) -
                        Math.sin(robotDirection));
        double newY = robotPositionY - MAX_VELOCITY / angularVelocity *
                (Math.cos(robotDirection  + angularVelocity * VELOCITY_DURATION) -
                        Math.cos(robotDirection));
        double newDirection = asNormalizedRadians(robotDirection + angularVelocity * VELOCITY_DURATION);
        robotPositionX = newX;
        robotPositionY = newY;
        robotDirection = newDirection;
        notifyObservers();
    }

    private double getShortDirectionNegativeAngle(double angleToTarget) {
        if (robotDirection < 0) {
            if (angleToTarget < robotDirection) {
                return -MAX_ANGULAR_VELOCITY;
            } else {
                return MAX_ANGULAR_VELOCITY;
            }
        } else {
            if (Math.abs(angleToTarget) + robotDirection < Math.PI) {
                return -MAX_ANGULAR_VELOCITY;
            } else {
                return MAX_ANGULAR_VELOCITY;
            }
        }
    }

    private double getShortDirectionPositiveAngle(double angleToTarget) {
        if (robotDirection > 0) {
            if (angleToTarget > robotDirection) {
                return MAX_ANGULAR_VELOCITY;
            } else {
                return -MAX_ANGULAR_VELOCITY;
            }
        } else {
            if (angleToTarget + Math.abs(robotDirection) < Math.PI) {
                return MAX_ANGULAR_VELOCITY;
            } else {
                return -MAX_ANGULAR_VELOCITY;
            }
        }
    }

    private double getShortDirection(double angleToTarget) {
        if (angleToTarget < 0) {
            return getShortDirectionNegativeAngle(angleToTarget);
        } else {
            return getShortDirectionPositiveAngle(angleToTarget);
        }
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return Math.atan2(diffY, diffX);
    }

    private static int round(double value) {
        return (int) (value + 0.5);
    }

    private static double asNormalizedRadians(double angle) {
        while (angle < -Math.PI) {
            angle += 2 * Math.PI;
        }
        while (angle >= Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    @Override
    public synchronized void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.objectModified(this);
        }
    }
}

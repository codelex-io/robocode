package io.codelex;

import robocode.AdvancedRobot;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;

import java.awt.*;
import java.util.Random;

import static io.codelex.Calculations.findAbsoluteBearing;
import static java.awt.Color.BLACK;
import static java.lang.Math.toDegrees;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class CodelexRobot extends AdvancedRobot {
    private static final Random RANDOM = new Random();

    private int scannedX = 0;
    private int scannedY = 0;
    private int destinationX = 0;
    private int destinationY = 0;

    @Override
    public void run() {
        Color brandGreen = new Color(76, 175, 80);
        setBodyColor(BLACK);
        setGunColor(brandGreen);
        setBulletColor(brandGreen);

        setRadarColor(Color.black);
        setScanColor(Color.yellow);

        while (true) {
            move();
        }
    }

    private void goToRandomLocation() {
        if (destinationX == 0) {
            findDestination();
        }
        if (RANDOM.nextInt() % 2 == 0) {
            if (RANDOM.nextInt() % 2 == 0 && scannedX != 0) {
                destinationX = scannedX;
                destinationY = scannedY;
            } else {
                findDestination();
            }
        }

        goTo(destinationX, destinationY);
    }

    private void findDestination() {
        destinationX = RANDOM.nextInt() % ((int) getBattleFieldWidth() - (getSentryBorderSize() * 2));
        destinationX += getSentryBorderSize();
        destinationY = RANDOM.nextInt() % ((int) getBattleFieldHeight() - getSentryBorderSize() * 2);
        destinationY += getSentryBorderSize();
    }

    private void goTo(double x, double y) {
        double dist = 20;
        double angle = toDegrees(findAbsoluteBearing(getX(), getY(), x, y));
        double r = turnTo(angle);
        ahead(dist * r);
    }

    private int turnTo(double angle) {
        double ang;
        int dir;
        ang = Calculations.shortestAngle(getHeading() - angle);
        if (ang > 90) {
            ang -= 180;
            dir = -1;
        } else if (ang < -90) {
            ang += 180;
            dir = -1;
        } else {
            dir = 1;
        }
        setTurnLeft(ang);
        return dir;
    }

    private void move() {
        goToRandomLocation();
        scan();
        execute();
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        if (!e.isSentryRobot()) {
            double angle = Math.toRadians((getHeading() + e.getBearing()) % 360);
            scannedX = (int) (getX() + Math.sin(angle) * e.getDistance());
            scannedY = (int) (getY() + Math.cos(angle) * e.getDistance());
            double absoluteBearing = this.getHeading() + e.getBearing();
            double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - this.getGunHeading());
            if (Math.abs(bearingFromGun) <= 3.0D) {
                this.turnGunRight(bearingFromGun);
                if (this.getGunHeat() == 0.0D) {
                    this.fire(Math.min(3.0D - Math.abs(bearingFromGun), this.getEnergy() - 0.1D));
                }
            } else {
                this.turnGunRight(bearingFromGun);
            }

            if (bearingFromGun == 0.0D) {
                this.scan();
            }

            if ((RANDOM.nextInt() % 3) == 0) move();
        }
    }

    @Override
    public void onHitRobot(HitRobotEvent e) {
        this.turnRight(e.getBearing());
        if (e.getEnergy() > 16.0D) {
            this.fire(3.0D);
        } else if (e.getEnergy() > 10.0D) {
            this.fire(2.0D);
        } else if (e.getEnergy() > 4.0D) {
            this.fire(1.0D);
        } else if (e.getEnergy() > 2.0D) {
            this.fire(0.5D);
        } else if (e.getEnergy() > 0.4D) {
            this.fire(0.1D);
        }

        if ((RANDOM.nextInt() % 3) == 0) {
            move();
        }
    }
}

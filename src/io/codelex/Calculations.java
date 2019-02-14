package io.codelex;

import static java.lang.Math.PI;

class Calculations {
    static double shortestAngle(double ang) {
        if (ang > PI) {
            ang -= 2 * PI;
        }
        if (ang < -PI) {
            ang += 2 * PI;
        }
        return ang;
    }

    static double findAbsoluteBearing(double x1, double y1, double x2, double y2) {
        double xo = x2 - x1;
        double yo = y2 - y1;
        double h = getDistance(x1, y1, x2, y2);
        if (xo > 0 && yo > 0) {
            return Math.asin(xo / h);
        }
        if (xo > 0 && yo < 0) {
            return PI - Math.asin(xo / h);
        }
        if (xo < 0 && yo < 0) {
            return PI + Math.asin(-xo / h);
        }
        if (xo < 0 && yo > 0) {
            return 2.0 * PI - Math.asin(-xo / h);
        }
        return 0;
    }

    private static double getDistance(double x1, double y1, double x2, double y2) {
        double xo = x2 - x1;
        double yo = y2 - y1;
        return Math.sqrt(xo * xo + yo * yo);
    }
}

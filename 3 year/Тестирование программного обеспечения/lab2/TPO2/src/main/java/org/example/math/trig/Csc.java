package org.example.math.trig;

import static org.example.math.trig.Sin.sin;

public class Csc {
    public static double csc(double x) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("Invalid argument: x cannot be NaN or Infinity.");
        }
        double res = sin(x);
        if (res == 0) {
            throw new ArithmeticException("Secant is undefined for x = " + x);
        }
        return 1 / res;
    }
}

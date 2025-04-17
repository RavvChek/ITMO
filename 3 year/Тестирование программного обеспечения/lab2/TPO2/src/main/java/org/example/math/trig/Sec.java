package org.example.math.trig;

import static org.example.math.trig.Cos.cos;

public class Sec {
    public static double sec(double x) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("Invalid argument: x cannot be NaN or Infinity.");
        }
        double res = cos(x);
        if (Math.abs(res) < 1e-10) {
            throw new ArithmeticException("Secant is undefined for x = " + x);
        }
        return 1 / res;
    }
}

package org.example.math.trig;

import static org.example.math.trig.Cos.cos;
import static org.example.math.trig.Sin.sin;

public class Tan {
    public static double tan(double x) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("Invalid argument: x cannot be NaN or Infinity.");
        }
        double res = cos(x);
        if (res == 0) {
            throw new ArithmeticException("Secant is undefined for x = " + x);
        }
        return sin(x) / res;
    }
}

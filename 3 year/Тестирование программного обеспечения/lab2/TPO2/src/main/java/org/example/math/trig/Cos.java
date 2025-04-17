package org.example.math.trig;

import static org.example.math.trig.Sin.sin;

public class Cos {
    public static Double cos(double x) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("Invalid argument: x cannot be NaN or Infinity.");
        }
        return sin(x + Math.PI / 2);
    }
}

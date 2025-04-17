package org.example.math.trig;

import static org.example.math.trig.Cos.cos;
import static org.example.math.trig.Sin.sin;

public class Cot {
    public static double cot(double x) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("Invalid argument: x cannot be NaN or Infinity.");
        }
        double res = sin(x);
        if (res == 0) {
            throw new ArithmeticException("Undefined value: cot(x) is undefined when sin(x) = 0.");
        }
        return cos(x) / res;
    }
}

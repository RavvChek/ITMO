package org.example.math.trig;

import org.example.solver.Solver;

public class Sin {
    public static double sin(double x) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("Invalid argument: x cannot be NaN or Infinity.");
        }
        return sinTaylor(x);
    }

    private static double sinTaylor(double x) {
        double epsilon = Solver.getEpsilon();
        x = (x + Math.PI) % (2 * Math.PI) - Math.PI;

        if (Math.abs(x) < epsilon) {
            return 0.0;
        }

        double term = x;
        double sum = term;
        int n = 1;
        while (Math.abs(term) > epsilon) {
            term *= -x * x / (2 * n * (2 * n + 1));
            sum += term;
            n++;
        }
        return sum;
    }
}

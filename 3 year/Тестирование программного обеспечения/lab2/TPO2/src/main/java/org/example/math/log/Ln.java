package org.example.math.log;

import org.example.solver.Solver;

public class Ln {
    public static double ln(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("x must be greater than 0 to compute the natural logarithm.");
        }
        return lnTaylor(x);
    }

    private static double lnTaylor(double x) {
        if (x <= 0) {
            throw new ArithmeticException("ln undefined for this x = " + x);
        }
        if (x == 1.0) {
            return 0.0;
        }

        double t = (x - 1) / (x + 1);
        double tSquared = t * t;
        double factor = 1 - tSquared;
        double term = t;
        double sum = term;
        int n = 1;

        while (true) {
            term *= tSquared * (2 * n - 1) / (2 * n + 1);
            if (Math.abs(term) < Solver.getEpsilon() * factor) {
                break;
            }
            sum += term;
            n++;
        }

        return 2 * sum;
    }

}

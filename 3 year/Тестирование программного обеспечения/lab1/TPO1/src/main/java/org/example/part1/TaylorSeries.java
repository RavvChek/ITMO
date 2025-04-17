package org.example.part1;

import java.util.ArrayList;

public class TaylorSeries {
    double epsilon;
    ArrayList<Double> terms = new ArrayList<>();

    public TaylorSeries(double epsilon) {
        this.epsilon = epsilon;
    }

    public Double sinTaylor(double x /*вычисляемое значение*/) {
        int count_terms = calcRequiredCountTerms(x);
        x = x % (2 * Math.PI);
        double result = 0.0;
        for (int n = 0; n < count_terms; n++) {
            double term = Math.pow(-1, n) * Math.pow(x, 2 * n + 1) / factorial(2 * n + 1);
            terms.add(term);
            result += term;
        }
        return result;
    }

    private int calcRequiredCountTerms(double x) {
        int n = 0;
        double term;

        do {
            n++;
            int exponent = 2 * n + 1;
            term = Math.pow(Math.abs(x), exponent) / factorial(exponent);
        } while (term >= epsilon);
        return n + 1;
    }

    private static long factorial(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setTerms(ArrayList<Double> terms) {
        this.terms = terms;
    }

    public ArrayList<Double> getTerms () {
        return terms;
    }
}

package trig;

import org.example.math.trig.Sin;
import org.example.solver.Solver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SinTest {
    private static final double epsilon = 1e-10;

    @BeforeAll
    public static void setUp() {
        Solver.setEpsilon(epsilon);
    }

    @Test
    @DisplayName("sin(0) = 0")
    void testSinZero() {
        double x = 0;
        double expected = 0.0;
        double result = Sin.sin(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("sin(pi) = 0")
    void testSinPi() {
        double x = Math.PI;
        double expected = 0.0;
        double result = Sin.sin(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("sin(pi / 2) = 1")
    void testSinHalfPi() {
        double x = Math.PI / 2;
        double expected = 1.0;
        double result = Sin.sin(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("sin(x) id odd")
    void testOddFunction() {
        double x = 1.5;
        double resultPositive = Sin.sin(x);
        double resultNegative = Sin.sin(-x);
        assertEquals(resultPositive, -resultNegative, epsilon);
    }

    @Test
    @DisplayName("SinFunction.sin(x) VS Math.sin(x)")
    void testAgainstLibrary() {
        for (double x = -2 * Math.PI; x <= 2 * Math.PI; x += 0.001) {
            double expected = Math.sin(x);
            double result = Sin.sin(x);
            assertEquals(expected, result, epsilon, "Failed at x = " + x);
        }
    }
}
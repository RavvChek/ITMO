package log;

import org.example.math.log.Ln;
import org.example.solver.Solver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LnTest {
    private static final double epsilon = 1e-10;

    @BeforeAll
    public static void setUp() {
        Solver.setEpsilon(epsilon);
    }

    @Test
    @DisplayName("ln(1) = 0")
    void testLnOne() {
        double x = 1.0;
        double expected = 0.0;
        double result = Ln.ln(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("ln(e) = 1")
    void testLnE() {
        double x = Math.E;
        double expected = 1.0;
        double result = Ln.ln(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("ln(1/e) = -1")
    void testLnInverseE() {
        double x = 1 / Math.E;
        double expected = -1.0;
        double result = Ln.ln(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("Property ln(a*b) = ln(a) + ln(b)")
    void testLogarithmProperty() {
        double a = 2.5;
        double b = 3.2;
        double lnProduct = Ln.ln(a * b);
        double sumLogs = Ln.ln(a) + Ln.ln(b);
        assertEquals(lnProduct, sumLogs, epsilon);
    }

    @Test
    @DisplayName("LnFunction.ln(x) VS Math.log(x)")
    void testAgainstLibrary() {
        for (double x = 0.1; x <= 2.0; x += 0.001) {
            double expected = Math.log(x);
            double result = Ln.ln(x);
            assertEquals(expected, result, epsilon * 100, "Failed at x = " + x);
        }
    }

    @Test
    @DisplayName("handling invalid values")
    void testInvalidInput() {
        assertThrows(IllegalArgumentException.class,
                () -> Ln.ln(-1.0));
        assertThrows(IllegalArgumentException.class,
                () -> Ln.ln(0.0));
    }
}
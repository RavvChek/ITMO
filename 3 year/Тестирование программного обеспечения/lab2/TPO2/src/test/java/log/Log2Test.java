package log;

import org.example.math.log.Ln;
import org.example.math.log.Log2;
import org.example.solver.Solver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

public class Log2Test {
    private static final double epsilon = 1e-10;

    @BeforeAll
    public static void setUp() {
        Solver.setEpsilon(epsilon);
    }

    @Test
    @DisplayName("Mock test to check the base scenario")
    void log2ShouldCallLnWithCorrectArguments() {
        try (MockedStatic<Ln> mockedLn = mockStatic(Ln.class)) {
            double x = 4.0;
            mockedLn.when(() -> Ln.ln(x)).thenReturn(1.3862943611198906);
            mockedLn.when(() -> Ln.ln(2.0)).thenReturn(0.6931471805599453);

            double result = Log2.log2(x);

            assertEquals(2.0, result, epsilon);
            mockedLn.verify(() -> Ln.ln(x));
            mockedLn.verify(() -> Ln.ln(2.0));
        }
    }

    @ParameterizedTest
    @MethodSource("testData")
    @DisplayName("Parameterized test for checking different values")
    void log2ShouldCallLnWithParameters(double x, double epsilon) {
        try (MockedStatic<Ln> mockedLn = mockStatic(Ln.class)) {
            double mockLnX = Math.random();
            double mockLn2 = Math.random();

            mockedLn.when(() -> Ln.ln(x)).thenReturn(mockLnX);
            mockedLn.when(() -> Ln.ln(2.0)).thenReturn(mockLn2);

            double expected = mockLnX / mockLn2;
            double actual = Log2.log2(x);

            assertEquals(expected, actual, epsilon);
            mockedLn.verify(() -> Ln.ln(x));
            mockedLn.verify(() -> Ln.ln(2.0));
        }
    }

    @Test
    @DisplayName("log2(1) = 0")
    void testLog2One() {
        double x = 1.0;
        double expected = 0.0;
        double result = Log2.log2(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("log2(2) = 1")
    void testLog2Two() {
        double x = 2.0;
        double expected = 1.0;
        double result = Log2.log2(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("log2(0.5) = -1")
    void testLog2Half() {
        double x = 0.5;
        double expected = -1.0;
        double result = Log2.log2(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("property log2(a * b) = log2(a) + log2(b)")
    void testLogarithmProperty() {
        double a = 3.0;
        double b = 9.0;
        double log2Product = Log2.log2(a * b);
        double sumLogs = Log2.log2(a) + Log2.log2(b);
        assertEquals(log2Product, sumLogs, epsilon * 100);
    }

    @Test
    @DisplayName("Log2Function.log2(x) VS Math.log(x) / Math.log(2)")
    void testAgainstLibrary() {
        for (double x = 0.1; x <= 10.0; x += 0.001) {
            double expected = Math.log(x) / Math.log(2);
            double result = Log2.log2(x);
            assertEquals(expected, result, epsilon * 10, "Failed at x = " + x);
        }
    }

    @Test
    @DisplayName("handling invalid values")
    void testInvalidInput() {
        assertThrows(IllegalArgumentException.class,
                () -> Log2.log2(-1.0));
        assertThrows(IllegalArgumentException.class,
                () -> Log2.log2(0.0));
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(1.0, epsilon),
                Arguments.of(4.0, epsilon),
                Arguments.of(8.0, epsilon),
                Arguments.of(16.0, epsilon),
                Arguments.of(0.5, epsilon),
                Arguments.of(0.25, epsilon),
                Arguments.of(0.125, epsilon),
                Arguments.of(1e-5, epsilon)
        );
    }
}

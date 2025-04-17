package log;

import org.example.math.log.Ln;
import org.example.math.log.Log5;
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

public class Log5Test {
    private static final double epsilon = 1e-10;

    @BeforeAll
    public static void setUp() {
        Solver.setEpsilon(epsilon);
    }

    @Test
    @DisplayName("Mock test to check the base scenario")
    void log5ShouldCallLnWithCorrectArguments() {
        try (MockedStatic<Ln> mockedLn = mockStatic(Ln.class)) {
            double x = 25.0;
            mockedLn.when(() -> Ln.ln(x)).thenReturn(3.2188758248682006);
            mockedLn.when(() -> Ln.ln(5.0)).thenReturn(1.6094379124341003);

            double result = Log5.log5(x);

            assertEquals(2.0, result, epsilon);
            mockedLn.verify(() -> Ln.ln(x));
            mockedLn.verify(() -> Ln.ln(5.0));
        }
    }

    @ParameterizedTest
    @MethodSource("testData")
    @DisplayName("Parameterized test for checking different values")
    void log5ShouldCallLnWithParameters(double x, double epsilon) {
        try (MockedStatic<Ln> mockedLn = mockStatic(Ln.class)) {
            double mockLnX = Math.random();
            double mockLn5 = Math.random();

            mockedLn.when(() -> Ln.ln(x)).thenReturn(mockLnX);
            mockedLn.when(() -> Ln.ln(5.0)).thenReturn(mockLn5);

            double expected = mockLnX / mockLn5;
            double actual = Log5.log5(x);

            assertEquals(expected, actual, epsilon);
            mockedLn.verify(() -> Ln.ln(x));
            mockedLn.verify(() -> Ln.ln(5.0));
        }
    }

    @Test
    @DisplayName("log5(1) = 0")
    void testLog5One() {
        double x = 1.0;
        double expected = 0.0;
        double result = Log5.log5(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("log5(5) = 1")
    void testLog5Five() {
        double x = 5.0;
        double expected = 1.0;
        double result = Log5.log5(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("log5(0.2) = -1")
    void testLog5Fifth() {
        double x = 0.2;
        double expected = -1.0;
        double result = Log5.log5(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("Property (a * b) = log5(a) + log5(b)")
    void testLogarithmProperty() {
        double a = 5.0;
        double b = 25.0;
        double log5Product = Log5.log5(a * b);
        double sumLogs = Log5.log5(a) + Log5.log5(b);
        assertEquals(log5Product, sumLogs, epsilon * 100);
    }

    @Test
    @DisplayName("Log5Function.log5(x) VS Math.log(x) / Math.log(5)")
    void testAgainstLibrary() {
        for (double x = 0.1; x <= 10.0; x += 0.001) {
            double expected = Math.log(x) / Math.log(5);
            double result = Log5.log5(x);
            assertEquals(expected, result, epsilon * 10, "Failed at x = " + x);
        }
    }

    @Test
    @DisplayName("handling invalid values")
    void testInvalidInput() {
        assertThrows(IllegalArgumentException.class,
                () -> Log5.log5(-1.0));
        assertThrows(IllegalArgumentException.class,
                () -> Log5.log5(0.0));
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(1.0, epsilon),
                Arguments.of(25.0, epsilon),
                Arguments.of(0.2, epsilon),
                Arguments.of(125.0, epsilon),
                Arguments.of(0.04, epsilon),
                Arguments.of(625.0, epsilon),
                Arguments.of(1e-5, epsilon)
        );
    }
}

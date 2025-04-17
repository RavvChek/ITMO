package log;

import org.example.math.log.Ln;
import org.example.math.log.Log3;
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


public class Log3Test {
    private static final double epsilon = 1e-10;

    @BeforeAll
    public static void setUp() {
        Solver.setEpsilon(epsilon);
    }

    @Test
    @DisplayName("Mock test to check the base scenario")
    void log3ShouldCallLnWithCorrectArguments() {
        try (MockedStatic<Ln> mockedLn = mockStatic(Ln.class)) {
            double x = 9.0;
            mockedLn.when(() -> Ln.ln(x)).thenReturn(2.1972245773362196);
            mockedLn.when(() -> Ln.ln(3.0)).thenReturn(1.0986122886681098);

            double result = Log3.log3(x);

            assertEquals(2.0, result, epsilon);
            mockedLn.verify(() -> Ln.ln(x));
            mockedLn.verify(() -> Ln.ln(3.0));
        }
    }

    @ParameterizedTest
    @MethodSource("testData")
    @DisplayName("Parameterized test for checking different values")
    void log3ShouldCallLnWithParameters(double x, double epsilon) {
        try (MockedStatic<Ln> mockedLn = mockStatic(Ln.class)) {
            double mockLnX = Math.random();
            double mockLn3 = Math.random();

            mockedLn.when(() -> Ln.ln(x)).thenReturn(mockLnX);
            mockedLn.when(() -> Ln.ln(3.0)).thenReturn(mockLn3);

            double expected = mockLnX / mockLn3;
            double actual = Log3.log3(x);

            assertEquals(expected, actual, epsilon);
            mockedLn.verify(() -> Ln.ln(x));
            mockedLn.verify(() -> Ln.ln(3.0));
        }
    }

    @Test
    @DisplayName("log3(1) = 0")
    void testLog3One() {
        double x = 1.0;
        double expected = 0.0;
        double result = Log3.log3(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("log3(3) = 1")
    void testLog3Three() {
        double x = 3.0;
        double expected = 1.0;
        double result = Log3.log3(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("log3(0.33) = -1")
    void testLog3Third() {
        double x = 0.3333333333;
        double expected = -1.0;
        double result = Log3.log3(x);
        assertEquals(expected, result, epsilon * 100);
    }

    @Test
    @DisplayName("property log3(a * b) = log3(a) + log3(b)")
    void testLogarithmProperty() {
        double a = 4.0;
        double b = 9.0;
        double log3Product = Log3.log3(a * b);
        double sumLogs = Log3.log3(a) + Log3.log3(b);
        assertEquals(log3Product, sumLogs, epsilon * 100);
    }

    @Test
    @DisplayName("Log3Function.log3(x) VS Math.log(x) / Math.log(3)")
    void testAgainstLibrary() {
        for (double x = 0.1; x <= 10.0; x += 0.001) {
            double expected = Math.log(x) / Math.log(3);
            double result = Log3.log3(x);
            assertEquals(expected, result, epsilon * 10, "Failed at x = " + x);
        }
    }

    @Test
    @DisplayName("handling invalid values")
    void testInvalidInput() {
        assertThrows(IllegalArgumentException.class,
                () -> Log3.log3(-1.0));
        assertThrows(IllegalArgumentException.class,
                () -> Log3.log3(0.0));
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(1.0, epsilon),
                Arguments.of(9.0, epsilon),
                Arguments.of(27.0, epsilon),
                Arguments.of(0.3333333333, epsilon),
                Arguments.of(0.1111111111, epsilon),
                Arguments.of(81.0, epsilon),
                Arguments.of(1e-5, epsilon)
        );
    }
}

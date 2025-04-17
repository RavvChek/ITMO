package trig;

import org.example.math.trig.Cos;
import org.example.math.trig.Csc;
import org.example.math.trig.Sin;
import org.example.solver.Solver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

public class CscTest {
    private static final double epsilon = 1e-10;

    @BeforeAll
    public static void setUp() {
        Solver.setEpsilon(epsilon);
    }

    @Test
    @DisplayName("Mock test to check the base scenario")
    void cscShouldUseSin() {
        try (MockedStatic<Sin> mockedSin = mockStatic(Sin.class)) {
            double x = Math.PI / 2;

            mockedSin.when(() -> Sin.sin(x)).thenReturn(1.0);

            double result = Csc.csc(x);

            assertEquals(1.0, result, epsilon);
            mockedSin.verify(() -> Sin.sin(x));
        }
    }

    @ParameterizedTest
    @MethodSource("testData")
    @DisplayName("Parametrized test to checking different values")
    void cscShouldCallSinWithCorrectParameters(double x, double epsilon) {
        try (MockedStatic<Sin> mockedSin = mockStatic(Sin.class)) {
            double mockedSinX = 1.0 / 2.0;

            mockedSin.when(() -> Sin.sin(x)).thenReturn(mockedSinX);

            double expected = 2;
            double result = Csc.csc(x);

            assertEquals(expected, result, epsilon);
            mockedSin.verify(() -> Sin.sin(x));
        }
    }

    @Test
    @DisplayName("csc(pi / 2) = 1")
    void testCscHalfPi() {
        double x = Math.PI / 2;
        double expected = 1.0;
        double result = Csc.csc(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("csc(pi) is undefined")
    void testCscPi() {
        double x = 0;
        assertThrows(ArithmeticException.class, () -> Csc.csc(x));
    }

    @Test
    @DisplayName("csc(x) is an odd function")
    void testOddFunction() {
        double x = 1.5;
        double resultPositive = Csc.csc(x);
        double resultNegative = Csc.csc(-x);
        assertEquals(resultPositive, -resultNegative, epsilon);
    }

    @Test
    @DisplayName("CscFunction.csc(x) VS 1 / Math.sin(x)")
    void testAgainstLibrary() {
        for (double x = -Math.PI + 0.1; x <= Math.PI; x += 0.001) {
            double expected = 1 / Math.sin(x);
            double result = Csc.csc(x);
            assertEquals(expected, result, epsilon * 100000, "Failed at x = " + x);
        }
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(-5 * Math.PI / 6, epsilon),
                Arguments.of(-Math.PI / 2, epsilon),
                Arguments.of(Math.PI / 6, epsilon),
                Arguments.of(Math.PI / 3, epsilon),
                Arguments.of(2 * Math.PI / 3, epsilon),
                Arguments.of(5 * Math.PI / 6, epsilon),
                Arguments.of(3.0, epsilon),
                Arguments.of(-2.0, epsilon)
        );
    }
}

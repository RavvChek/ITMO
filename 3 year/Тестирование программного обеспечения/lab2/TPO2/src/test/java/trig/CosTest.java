package trig;

import org.example.math.trig.Cos;
import org.example.math.trig.Sin;
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
import static org.mockito.Mockito.mockStatic;

public class CosTest {
    private static final double epsilon = 1e-10;

    @BeforeAll
    public static void setUp() {
        Solver.setEpsilon(epsilon);
    }

    @Test
    @DisplayName("Mock test to check the base scenario")
    void cosShouldCallSinWithShiftedArgument() {
        try (MockedStatic<Sin> mockedSin = mockStatic(Sin.class)) {
            mockedSin.when(() -> Sin.sin(Math.PI))
                    .thenReturn(0.0);

            double result = Cos.cos(Math.PI / 2);

            assertEquals(0.0, result, epsilon);
            mockedSin.verify(() -> Sin.sin(Math.PI));
        }
    }

    @ParameterizedTest
    @MethodSource("testData")
    @DisplayName("Parameterized test for checking different values")
    void cosShouldCallSinWithCorrectParameters(double x, double epsilon) {
        try (MockedStatic<Sin> mockedSin = mockStatic(Sin.class)) {
            double expectedArgument = x + Math.PI / 2;

            double mockValue = Math.random();
            mockedSin.when(() -> Sin.sin(expectedArgument))
                    .thenReturn(mockValue);

            double result = Cos.cos(x);

            mockedSin.verify(() -> Sin.sin(expectedArgument));
            assertEquals(mockValue, result, epsilon);
        }
    }

    @Test
    @DisplayName("cos(0) = 1")
    void testCosZero() {
        double x = 0;
        double expected = 1.0;
        double result = Cos.cos(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("cos(pi) = -1")
    void testCosPi() {
        double x = Math.PI;
        double expected = -1.0;
        double result = Cos.cos(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("cos(pi / 2) = 0")
    void testCosHalfPi() {
        double x = Math.PI / 2;
        double expected = 0.0;
        double result = Cos.cos(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("cos(x) is an even function")
    void testEvenFunction() {
        double x = 1.5;
        double resultPositive = Cos.cos(x);
        double resultNegative = Cos.cos(-x);
        assertEquals(resultPositive, resultNegative, epsilon);
    }

    @Test
    @DisplayName("CosFunction.cos(x) VS Math.cos(x)")
    void testAgainstLibrary() {
        for (double x = -2 * Math.PI; x <= 2 * Math.PI; x += 0.001) {
            double expected = Math.cos(x);
            double result = Cos.cos(x);
            assertEquals(expected, result, epsilon, "Failed at x = " + x);
        }
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(0.0, epsilon),
                Arguments.of(Math.PI / 3, epsilon),
                Arguments.of(Math.PI / 4, epsilon),
                Arguments.of(Math.PI / 2, epsilon),
                Arguments.of(3 * Math.PI / 2, epsilon),
                Arguments.of(2 * Math.PI, epsilon),
                Arguments.of(-Math.PI / 4, epsilon),
                Arguments.of(100 * Math.PI, epsilon)
        );
    }
}

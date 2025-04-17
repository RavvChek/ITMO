package trig;

import org.example.math.trig.Cos;
import org.example.math.trig.Cot;
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

public class CotTest {
    private static final double epsilon = 1e-10;

    @BeforeAll
    public static void setUp() {
        Solver.setEpsilon(epsilon);
    }

    @Test
    @DisplayName("Mock test to check the base scenario")
    void cotShouldUseSinAndCos() {
        try (MockedStatic<Sin> mockedSin = mockStatic(Sin.class)) {
            double x = Math.PI / 4;
            double sinValue = 0.7071067812;


            mockedSin.when(() -> Sin.sin(x)).thenReturn(sinValue);
            mockedSin.when(() -> Sin.sin(x + Math.PI / 2)).thenReturn(sinValue);


            double result = Cot.cot(x);

            assertEquals(1.0, result, epsilon);
            mockedSin.verify(() -> Sin.sin(x));
            mockedSin.verify(() -> Sin.sin(x + Math.PI / 2));


        }
    }

    @ParameterizedTest
    @MethodSource("testData")
    @DisplayName("Parameterized test for checking different values")
    void cosShouldCallSinWithCorrectParameters(double x, double epsilon) {
        try (MockedStatic<Sin> mockedSin = mockStatic(Sin.class)) {
            double mockedSinX = 1.0 / 2.0;
            double mockedCosX = 1.0 / 2.0;

            mockedSin.when(() -> Sin.sin(x)).thenReturn(mockedSinX);
            mockedSin.when(() -> Sin.sin(x + Math.PI / 2)).thenReturn(mockedCosX);

            double expectedResult = 1;
            double result = Cot.cot(x);

            assertEquals(expectedResult, result, epsilon);
            mockedSin.verify(() -> Sin.sin(x));
            mockedSin.verify(() -> Sin.sin(x + Math.PI / 2));
        }
    }

    @ParameterizedTest
    @MethodSource("testData")
    @DisplayName("Test Cot with mocked both Sin and Cos")
    void testCotWithMockedSinAndCos(double x, double epsilon) {
        try (MockedStatic<Sin> mockedSin = mockStatic(Sin.class); MockedStatic<Cos> mockedCos = mockStatic(Cos.class)) {
            double mockedSinX = Math.sqrt(2) / 2;
            double mockedCosX = Math.sqrt(2) / 2;

            mockedSin.when(() -> Sin.sin(x)).thenReturn(mockedSinX);
            mockedCos.when(() -> Cos.cos(x)).thenReturn(mockedCosX);

            double expectedResult = 1;
            double result = Cot.cot(x);

            assertEquals(expectedResult, result, epsilon);
            mockedSin.verify(() -> Sin.sin(x));
            mockedCos.verify(() -> Cos.cos(x));
        }
    }

    @Test
    @DisplayName("cot(pi / 4) = 1")
    void testCotPiOver4() {
        double x = Math.PI / 4;
        double expected = 1.0;
        double result = Cot.cot(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("cot(pi / 2) = 0")
    void testCotHalfPi() {
        double x = Math.PI / 2;
        double expected = 0.0;
        double result = Cot.cot(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("cot(x) is an odd function")
    void testOddFunction() {
        double x = 1.5;
        double resultPositive = Cot.cot(x);
        double resultNegative = Cot.cot(-x);
        assertEquals(resultPositive, -resultNegative, epsilon);
    }

    @Test
    @DisplayName("CotFunction.cot(x) VS 1 / Math.tan(x)")
    void testAgainstLibrary() {
        for (double x = 0.1; x <= 10.0; x += 0.001) {
            double expected = 1 / Math.tan(x);
            double result = Cot.cot(x);

            assertEquals(expected, result, epsilon * 100000, "Failed at x = " + x);
        }
    }

    @Test
    @DisplayName("handling invalid values for cot(x)")
    void testInvalidInput() {
        assertThrows(ArithmeticException.class,
                () -> Cot.cot(0.0));
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(-Math.PI / 3, epsilon),
                Arguments.of(-Math.PI / 6, epsilon),
                Arguments.of(0.0, epsilon),
                Arguments.of(Math.PI / 6, epsilon),
                Arguments.of(Math.PI / 4, epsilon),
                Arguments.of(Math.PI / 3, epsilon),
                Arguments.of(2 * Math.PI / 3, epsilon),
                Arguments.of(5 * Math.PI / 6, epsilon),
                Arguments.of(7 * Math.PI / 6, epsilon),
                Arguments.of(4.0, epsilon),
                Arguments.of(-2.0, epsilon)
        );
    }

}

package trig;

import org.example.math.trig.Cos;
import org.example.math.trig.Cot;
import org.example.math.trig.Sin;
import org.example.math.trig.Tan;
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

public class TanTest {
    private static final double epsilon = 1e-10;

    @BeforeAll
    public static void setUp() {
        Solver.setEpsilon(epsilon);
    }

    @Test
    @DisplayName("Mock test to check the base scenario")
    void tanShouldUseSinAndCos() {
        try (MockedStatic<Sin> mockedSin = mockStatic(Sin.class)) {
            double x = Math.PI / 4;

            mockedSin.when(() -> Sin.sin(x)).thenReturn(0.7071067812);
            mockedSin.when(() -> Sin.sin(x + Math.PI / 2)).thenReturn(0.7071067812);

            double result = Tan.tan(x);

            assertEquals(1.0, result, epsilon);
            mockedSin.verify(() -> Sin.sin(x));
            mockedSin.verify(() -> Sin.sin(x + Math.PI / 2));

        }
    }

    @ParameterizedTest
    @MethodSource("testData")
    @DisplayName("Parameterized test for checking different values")
    void tanShouldCallSinWithCorrectParameters(double x, double epsilon) {
        try (MockedStatic<Sin> mockedSin = mockStatic(Sin.class)) {
            double mockedSinX = Math.sqrt(3) / 2;
            double mockedCosX = Math.sqrt(3) / 2;

            mockedSin.when(() -> Sin.sin(x)).thenReturn(mockedSinX);
            mockedSin.when(() -> Sin.sin(x + Math.PI / 2)).thenReturn(mockedCosX);

            double expectedResult = 1;
            double result = Tan.tan(x);

            assertEquals(expectedResult, result, epsilon);
            mockedSin.verify(() -> Sin.sin(x));
            mockedSin.verify(() -> Sin.sin(x + Math.PI / 2));
        }
    }

    @ParameterizedTest
    @MethodSource("testData")
    @DisplayName("Test Tan with mocked both Sin and Cos")
    void testTanWithMockedSinAndCos(double x, double epsilon) {
        try (MockedStatic<Sin> mockedSin = mockStatic(Sin.class); MockedStatic<Cos> mockedCos = mockStatic(Cos.class)) {
            double mockedSinX = 1.0 / 2.0;
            double mockedCosX = 1.0 / 2.0;

            mockedSin.when(() -> Sin.sin(x)).thenReturn(mockedSinX);
            mockedCos.when(() -> Cos.cos(x)).thenReturn(mockedCosX);

            double expectedResult = mockedSinX / mockedCosX;
            double result = Tan.tan(x);

            assertEquals(expectedResult, result, epsilon);
            mockedSin.verify(() -> Sin.sin(x));
            mockedCos.verify(() -> Cos.cos(x));
        }
    }

    @Test
    @DisplayName("tan(0) = 0")
    void testTanZero() {
        double x = 0;
        double expected = 0.0;
        double result = Tan.tan(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("tan(pi / 4) = 1")
    void testTanPiOver4() {
        double x = Math.PI / 4;
        double expected = 1.0;
        double result = Tan.tan(x);
        assertEquals(expected, result, epsilon);
    }


    @Test
    @DisplayName("TanFunction.tan(x) VS Math.tan(x)")
    void testAgainstLibrary() {
        for (double x = -Math.PI / 2 + 0.001; x < Math.PI / 2; x += 0.001) {
            double expected = Math.tan(x);
            double result = Tan.tan(x);
            assertEquals(expected, result, epsilon * 10000, "Failed at x = " + x);
        }
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(-Math.PI / 3, epsilon),
                Arguments.of(-0.1, epsilon),
                Arguments.of(-Math.PI / 6, epsilon),
                Arguments.of(-Math.PI / 4, epsilon),
                Arguments.of(-2 * Math.PI / 3, epsilon),
                Arguments.of(-5 * Math.PI / 6, epsilon),
                Arguments.of(-4.0, epsilon),
                Arguments.of(0.0, epsilon)
        ).filter(args -> Math.abs(Math.cos((Double) args.get()[0])) > 1e-9);
    }

}

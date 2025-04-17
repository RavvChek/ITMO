package trig;

import org.example.math.trig.Cos;
import org.example.math.trig.Sec;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

public class SecTest {
    private static final double epsilon = 1e-10;

    @BeforeAll
    public static void setUp() {
        Solver.setEpsilon(epsilon);
    }

    @Test
    @DisplayName("Mock test to check the base scenario")
    void secShouldUseCos() {
        try (MockedStatic<Cos> mockedCos = mockStatic(Cos.class)) {
            double x = Math.PI;

            mockedCos.when(() -> Cos.cos(x)).thenReturn(-1.0);

            double result = Sec.sec(x);

            assertEquals(-1.0, result, epsilon);
            mockedCos.verify(() -> Cos.cos(x));
        }
    }

    @ParameterizedTest
    @MethodSource("testData")
    @DisplayName("Parametrized test to checking different values")
    void secShouldCallSinWithCorrectParameters1(double x, double epsilon) {
        try (MockedStatic<Cos> mockedCos = mockStatic(Cos.class)) {
            double mockedCosX = 1.0 / 2.0;

            mockedCos.when(() -> Cos.cos(x)).thenReturn(mockedCosX);

            double expected = 2;
            double result = Sec.sec(x);

            assertEquals(expected, result, epsilon);
            mockedCos.verify(() -> Cos.cos(x));
        }
    }


    @Test
    @DisplayName("sec(0) = 1")
    void testSecZero() {
        double x = 0;
        double expected = 1.0;
        double result = Sec.sec(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("sec(pi / 3) = 2")
    void testSecPiOver3() {
        double x = Math.PI / 3;
        double expected = 2.0;
        double result = Sec.sec(x);
        assertEquals(expected, result, epsilon);
    }

    @Test
    @DisplayName("sec(pi / 2) is undefined")
    void testSecHalfPi() {
        double x = Math.PI / 2;
        assertThrows(ArithmeticException.class, () -> Sec.sec(x));
    }

    @Test
    @DisplayName("SecFunction.sec(x) VS 1 / Math.cos(x)")
    void testAgainstLibrary() {
        for (double x = -Math.PI / 2 + 0.001; x < Math.PI / 2; x += 0.001) {
            double expected = 1 / Math.cos(x);
            double result = Sec.sec(x);
            assertEquals(expected, result, epsilon * 10000, "Failed at x = " + x);
        }
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(-Math.PI / 3, epsilon),
                Arguments.of(0.1, epsilon),
                Arguments.of(Math.PI / 6, epsilon),
                Arguments.of(Math.PI / 4, epsilon),
                Arguments.of(2 * Math.PI / 3, epsilon),
                Arguments.of(5 * Math.PI / 6, epsilon),
                Arguments.of(4.0, epsilon),
                Arguments.of(-2.0, epsilon)
        ).filter(args -> Math.abs(Math.cos((Double) args.get()[0])) > 1e-9);
    }
}

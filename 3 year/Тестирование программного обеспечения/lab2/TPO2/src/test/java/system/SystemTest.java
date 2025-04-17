package system;

import org.example.math.log.Ln;
import org.example.math.log.Log2;
import org.example.math.log.Log3;
import org.example.math.log.Log5;
import org.example.math.trig.*;
import org.example.solver.Solver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SystemTest {

    @BeforeAll
    static void setUp() {
        Solver.setEpsilon(1e-10);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testSecondEquationWithAllStubs.csv", delimiter = ';', useHeadersInDisplayName = true)
    void testSecondEquationWithAllStubs(double x, double ln, double log2, double log3, double log5, double expected) {
        try (MockedStatic<Ln> lnMock = mockStatic(Ln.class);
             MockedStatic<Log2> log2Mock = mockStatic(Log2.class);
             MockedStatic<Log3> log3Mock = mockStatic(Log3.class);
             MockedStatic<Log5> log5Mock = mockStatic(Log5.class)) {

            lnMock.when(() -> Ln.ln(x)).thenReturn(ln);
            log2Mock.when(() -> Log2.log2(x)).thenReturn(log2);
            log3Mock.when(() -> Log3.log3(x)).thenReturn(log3);
            log5Mock.when(() -> Log5.log5(x)).thenReturn(log5);

            double result = Solver.secondEquation(x);
            assertEquals(expected, result, 1e-3);

            lnMock.verify(() -> Ln.ln(x), atLeastOnce());
            log2Mock.verify(() -> Log2.log2(x), atLeastOnce());
            log3Mock.verify(() -> Log3.log3(x), atLeastOnce());
            log5Mock.verify(() -> Log5.log5(x), atLeastOnce());
        }
    }


    @ParameterizedTest
    @CsvFileSource(resources = "/testSecondEquationWithLog5.csv", delimiter = ';', useHeadersInDisplayName = true)
    void testSecondEquationWithLog5(double x, double ln, double log2, double log3, double expected) {
        try (MockedStatic<Ln> lnMock = mockStatic(Ln.class);
             MockedStatic<Log2> log2Mock = mockStatic(Log2.class);
             MockedStatic<Log3> log3Mock = mockStatic(Log3.class)) {

            lnMock.when(() -> Ln.ln(x)).thenReturn(ln);
            log2Mock.when(() -> Log2.log2(x)).thenReturn(log2);
            log3Mock.when(() -> Log3.log3(x)).thenReturn(log3);
            lnMock.when(() -> Ln.ln(5)).thenReturn(1.609437912);


            double result = Solver.secondEquation(x);
            assertEquals(expected, result, 1e-3);

            lnMock.verify(() -> Ln.ln(x), atLeastOnce());
            log2Mock.verify(() -> Log2.log2(x), atLeastOnce());
            log3Mock.verify(() -> Log3.log3(x), atLeastOnce());
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testSecondEquationWithLog5AndLog3.csv", delimiter = ';', useHeadersInDisplayName = true)
    void testSecondEquationWithLog5AndLog3(double x, double ln, double log2, double expected) {
        try (MockedStatic<Ln> lnMock = mockStatic(Ln.class);
             MockedStatic<Log2> log2Mock = mockStatic(Log2.class)) {

            lnMock.when(() -> Ln.ln(x)).thenReturn(ln);
            log2Mock.when(() -> Log2.log2(x)).thenReturn(log2);
            lnMock.when(() -> Ln.ln(3.0)).thenReturn(1.098612288668);
            lnMock.when(() -> Ln.ln(5.0)).thenReturn(1.609437912);


            double result = Solver.secondEquation(x);
            assertEquals(expected, result, 1e-3);

            lnMock.verify(() -> Ln.ln(x), atLeastOnce());
            log2Mock.verify(() -> Log2.log2(x), atLeastOnce());
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testSecondEquationWithLog5Log3AndLog2.csv", delimiter = ';', useHeadersInDisplayName = true)
    void testSecondEquationWithLog5Log3AndLog2(double x, double ln, double expected) {
        try (MockedStatic<Ln> lnMock = mockStatic(Ln.class)) {

            lnMock.when(() -> Ln.ln(x)).thenReturn(ln);
            lnMock.when(() -> Ln.ln(2.0)).thenReturn(0.693147180559);
            lnMock.when(() -> Ln.ln(3.0)).thenReturn(1.098612288668);
            lnMock.when(() -> Ln.ln(5.0)).thenReturn(1.609437912);


            double result = Solver.secondEquation(x);
            assertEquals(expected, result, 1e-3);

            lnMock.verify(() -> Ln.ln(x), atLeastOnce());
        }
    }


    @ParameterizedTest
    @CsvFileSource(resources = "/testSecondEquationWithALlFunc.csv", delimiter = ';', useHeadersInDisplayName = true)
    void testSecondEquationWithALlFunc(double x, double expected) {
        double result = Solver.secondEquation(x);
        assertEquals(result, expected, 1e-3);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testFirstEquationWithAllStubs.csv", delimiter = ';', useHeadersInDisplayName = true)
    void testFirstEquationWithAllStubs(double x, double sin, double cos, double csc, double sec, double cot, double tan, double expected) {
        try (MockedStatic<Sin> sinMock = mockStatic(Sin.class);
             MockedStatic<Cos> cosMock = mockStatic(Cos.class);
             MockedStatic<Csc> cscMock = mockStatic(Csc.class);
             MockedStatic<Sec> secMock = mockStatic(Sec.class);
             MockedStatic<Cot> cotMock = mockStatic(Cot.class);
             MockedStatic<Tan> tanMock = mockStatic(Tan.class)) {

            sinMock.when(() -> Sin.sin(x)).thenReturn(sin);
            cosMock.when(() -> Cos.cos(x)).thenReturn(cos);
            cscMock.when(() -> Csc.csc(x)).thenReturn(csc);
            secMock.when(() -> Sec.sec(x)).thenReturn(sec);
            tanMock.when(() -> Tan.tan(x)).thenReturn(tan);
            cotMock.when(() -> Cot.cot(x)).thenReturn(cot);

            double result = Solver.firstEquation(x);
            assertEquals(result, expected, 1e-3);

            sinMock.verify(() -> Sin.sin(x), atLeastOnce());
            cosMock.verify(() -> Cos.cos(x), atLeastOnce());
            cscMock.verify(() -> Csc.csc(x), atLeastOnce());
            secMock.verify(() -> Sec.sec(x), atLeastOnce());
            tanMock.verify(() -> Tan.tan(x), atLeastOnce());
            cotMock.verify(() -> Cot.cot(x), atLeastOnce());

        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testFirstEquationWithSec.csv", delimiter = ';', useHeadersInDisplayName = true)
    void testFirstEquationWithSec(double x, double sin, double cos, double csc, double cot, double tan, double expected) {
        try (MockedStatic<Sin> sinMock = mockStatic(Sin.class);
             MockedStatic<Cos> cosMock = mockStatic(Cos.class);
             MockedStatic<Csc> cscMock = mockStatic(Csc.class);
             MockedStatic<Cot> cotMock = mockStatic(Cot.class);
             MockedStatic<Tan> tanMock = mockStatic(Tan.class)) {

            sinMock.when(() -> Sin.sin(x)).thenReturn(sin);
            cosMock.when(() -> Cos.cos(x)).thenReturn(cos);
            cscMock.when(() -> Csc.csc(x)).thenReturn(csc);
            tanMock.when(() -> Tan.tan(x)).thenReturn(tan);
            cotMock.when(() -> Cot.cot(x)).thenReturn(cot);

            double result = Solver.firstEquation(x);
            assertEquals(result, expected, 1e-3);

            sinMock.verify(() -> Sin.sin(x), atLeastOnce());
            cosMock.verify(() -> Cos.cos(x), atLeastOnce());
            cscMock.verify(() -> Csc.csc(x), atLeastOnce());
            tanMock.verify(() -> Tan.tan(x), atLeastOnce());
            cotMock.verify(() -> Cot.cot(x), atLeastOnce());

        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testFirstEquationWithCscAndSec.csv", delimiter = ';', useHeadersInDisplayName = true)
    void testFirstEquationWithCscAndSec(double x, double sin, double cos, double cot, double tan, double expected) {
        try (MockedStatic<Sin> sinMock = mockStatic(Sin.class);
             MockedStatic<Cos> cosMock = mockStatic(Cos.class);
             MockedStatic<Cot> cotMock = mockStatic(Cot.class);
             MockedStatic<Tan> tanMock = mockStatic(Tan.class)) {

            sinMock.when(() -> Sin.sin(x)).thenReturn(sin);
            cosMock.when(() -> Cos.cos(x)).thenReturn(cos);
            tanMock.when(() -> Tan.tan(x)).thenReturn(tan);
            cotMock.when(() -> Cot.cot(x)).thenReturn(cot);

            double result = Solver.firstEquation(x);
            assertEquals(result, expected, 1e-3);

            sinMock.verify(() -> Sin.sin(x), atLeastOnce());
            cosMock.verify(() -> Cos.cos(x), atLeastOnce());
            tanMock.verify(() -> Tan.tan(x), atLeastOnce());
            cotMock.verify(() -> Cot.cot(x), atLeastOnce());

        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testFirstEquationWithCscSecCot.csv", delimiter = ';', useHeadersInDisplayName = true)
    void testFirstEquationWithCscSecCot(double x, double sin, double cos, double tan, double expected) {
        try (MockedStatic<Sin> sinMock = mockStatic(Sin.class);
             MockedStatic<Cos> cosMock = mockStatic(Cos.class);
             MockedStatic<Tan> tanMock = mockStatic(Tan.class)) {

            sinMock.when(() -> Sin.sin(x)).thenReturn(sin);
            cosMock.when(() -> Cos.cos(x)).thenReturn(cos);
            tanMock.when(() -> Tan.tan(x)).thenReturn(tan);

            double result = Solver.firstEquation(x);
            assertEquals(result, expected, 1e-3);

            sinMock.verify(() -> Sin.sin(x), atLeastOnce());
            cosMock.verify(() -> Cos.cos(x), atLeastOnce());
            tanMock.verify(() -> Tan.tan(x), atLeastOnce());

        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testFirstEquationWithCscSecCotTan.csv", delimiter = ';', useHeadersInDisplayName = true)
    void testFirstEquationWithCscSecCotTan(double x, double sin, double cos, double expected) {
        try (MockedStatic<Sin> sinMock = mockStatic(Sin.class);
             MockedStatic<Cos> cosMock = mockStatic(Cos.class)) {

            sinMock.when(() -> Sin.sin(x)).thenReturn(sin);
            cosMock.when(() -> Cos.cos(x)).thenReturn(cos);

            double result = Solver.firstEquation(x);
            assertEquals(result, expected, 1e-3);

            sinMock.verify(() -> Sin.sin(x), atLeastOnce());
            cosMock.verify(() -> Cos.cos(x), atLeastOnce());

        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testFirstEquationWithCscSecCotTanCos.csv", delimiter = ';', useHeadersInDisplayName = true)
    void testFirstEquationWithCscSecCotTanCos(double x, double sin, double expected) {
        try (MockedStatic<Sin> sinMock = mockStatic(Sin.class)) {

            sinMock.when(() -> Sin.sin(x)).thenReturn(sin);
            sinMock.when(() -> Sin.sin(x + Math.PI / 2)).thenReturn(Math.sin(x + Math.PI / 2));

            double result = Solver.firstEquation(x);
            assertEquals(result, expected, 1e-3);

            sinMock.verify(() -> Sin.sin(x), atLeastOnce());
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testFirstEquationWithAllFunc.csv", delimiter = ';', useHeadersInDisplayName = true)
    void testFirstEquationWithAllFunc(double x, double expected) {
        double result = Solver.firstEquation(x);
        assertEquals(result, expected, 1000);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/testFirstEquationWithAllFunc.csv", delimiter = ';', useHeadersInDisplayName = true)
    void testPeriod(double x, double expected) {
        double result = Solver.firstEquation(x);
        double result_with_period = Solver.firstEquation(x + 2 * Math.PI);

        assertEquals(result, result_with_period, 1e-2);
    }

    @Test
    void testDotsSecondEquationWithZero() {
        double x = 0.34777;
        double expected = 0.0;
        double result = Solver.secondEquation(x);
        assertEquals(result, expected, 1e-3);

        x = 2.87545;
        result = Solver.secondEquation(x);
        assertEquals(result, expected, 1e-3);
    }

    @Test
    void testNegSecondEquationBeforeZero() {
        double x = 0.265;
        double result = Solver.secondEquation(x);
        double expected = 2.96677;
        assertEquals(result, expected, 1e-3);


    }

    @Test
    void testNegFirstEquationBeforeDot() {

        double x = -3.811;
        double result = Solver.firstEquation(x);
        double expected = 11.7326;
        assertEquals(result, expected, 1e-3);
    }

    @Test
    void testPosFirstEquation() {
        double x = -1.053;
        double result = Solver.firstEquation(x);
        double expected = -0.96671;
        assertEquals(result, expected, 1e-3);
    }

    @Test
    void testDotsSecondEquationInflexion() {
        double x = 0.67085;
        double result = Solver.secondEquation(x);
        double expected = -2.86459;
        assertEquals(result, expected, 1e-3);

        x = 1.49065;
        result = Solver.secondEquation(x);
        expected = 2.86459;
        assertEquals(result, expected, 1e-3);
    }

    @Test
    void testDotsFirstEquationWithZero() {
        double x = -1.03757;
        double result = Solver.firstEquation(x);
        double expected = 0;
        assertEquals(result, expected, 1e-3);

        x = -2.53239;
        result = Solver.firstEquation(x);
        assertEquals(result, expected, 1e-3);
    }


    @Test
    void testDotsFirstEquationInflexion() {
        double x = -3.92156;
        double result = Solver.firstEquation(x);
        double expected = 10.26437;
        assertEquals(result, expected, 1e-3);

        x = -5.4903;
        result = Solver.firstEquation(x);
        expected = 10.04103;
        assertEquals(result, expected, 1e-3);

        x += 2 * Math.PI;
        result = Solver.firstEquation(x);
    }
}

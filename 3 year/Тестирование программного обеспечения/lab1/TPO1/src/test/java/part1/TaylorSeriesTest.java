package part1;

import org.example.part1.TaylorSeries;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaylorSeriesTest {
    TaylorSeries taylorSeries;

    @BeforeAll
    static void setUpAll() {
        System.out.println("Running tests of the Taylor Series for sin(x)...");
    }

    @BeforeEach
    void setUp() {
        taylorSeries = new TaylorSeries(0.00001);
    }

    @Test
    @DisplayName("Check boundary values sin(x)")
    void sinTaylorBoundaryValuesTest() {
        System.out.println("Argument: " + 0.0 + " degrees\nReal value: " + Math.sin(0.0) + "\n" + "Approximate value: " + taylorSeries.sinTaylor(0.0) + "\n");
        assertEquals(Math.sin(0.0), taylorSeries.sinTaylor(0.0), taylorSeries.getEpsilon());
        System.out.println("Argument: " + 180 + " degrees\nReal value: " + String.format("%.5f", Math.sin(Math.PI)) + "\n" + "Approximate value: " + String.format("%.5f", taylorSeries.sinTaylor(Math.PI)) + "\n");
        assertEquals(Math.sin(Math.PI), taylorSeries.sinTaylor(Math.PI), taylorSeries.getEpsilon());
        System.out.println("Argument: " + -180 + " degrees\nReal value: " + String.format("%.5f", Math.sin(-Math.PI)) + "\n" + "Approximate value: " + String.format("%.5f", taylorSeries.sinTaylor(-Math.PI)) + "\n");
        assertEquals(Math.sin(-Math.PI), taylorSeries.sinTaylor(-Math.PI), taylorSeries.getEpsilon());
        System.out.println("Argument: " + -90 + " degrees\nReal value: " + String.format("%.5f", Math.sin(-Math.PI / 2)) + "\n" + "Approximate value: " + String.format("%.5f", taylorSeries.sinTaylor(-Math.PI / 2)) + "\n");
        assertEquals(Math.sin(-Math.PI / 2), taylorSeries.sinTaylor(-Math.PI / 2), taylorSeries.getEpsilon());
        System.out.println("Argument: " + 90 + " degrees\nReal value: " + String.format("%.5f", Math.sin(Math.PI / 2)) + "\n" + "Approximate value: " + String.format("%.5f", taylorSeries.sinTaylor(Math.PI / 2)) + "\n");
        assertEquals(Math.sin(Math.PI / 2), taylorSeries.sinTaylor(Math.PI / 2), taylorSeries.getEpsilon());
    }

    @Test
    @DisplayName("Check base table values sin(x)")
    void sinTaylorBaseTableValuesTest() {
        System.out.println("Argument: " + 45 + " degrees\nReal value: " + String.format("%.5f", Math.sin(Math.PI / 4)) + "\n" + "Approximate value: " + String.format("%.5f", taylorSeries.sinTaylor(Math.PI / 4)) + "\n");
        assertEquals(Math.sin(Math.PI / 4), taylorSeries.sinTaylor(Math.PI / 4), taylorSeries.getEpsilon());
        System.out.println("Argument: " + -45 + " degrees\nReal value: " + String.format("%.5f", Math.sin(-Math.PI / 4)) + "\n" + "Approximate value: " + String.format("%.5f", taylorSeries.sinTaylor(-Math.PI / 4)) + "\n");
        assertEquals(Math.sin(-Math.PI / 4), taylorSeries.sinTaylor(-Math.PI / 4), taylorSeries.getEpsilon());
        System.out.println("Argument: " + 60 + " degrees\nReal value: " + String.format("%.5f", Math.sin(Math.PI / 3)) + "\n" + "Approximate value: " + String.format("%.5f", taylorSeries.sinTaylor(Math.PI / 3)) + "\n");
        assertEquals(Math.sin(Math.PI / 3), taylorSeries.sinTaylor(Math.PI / 3), taylorSeries.getEpsilon());
        System.out.println("Argument: " + -60 + " degrees\nReal value: " + String.format("%.5f", Math.sin(-Math.PI / 3)) + "\n" + "Approximate value: " + String.format("%.5f", taylorSeries.sinTaylor(-Math.PI / 3)) + "\n");
        assertEquals(Math.sin(-Math.PI / 3), taylorSeries.sinTaylor(-Math.PI / 3), taylorSeries.getEpsilon());
        System.out.println("Argument: " + 30 + " degrees\nReal value: " + String.format("%.5f", Math.sin(Math.PI / 6)) + "\n" + "Approximate value: " + String.format("%.5f", taylorSeries.sinTaylor(Math.PI / 6)) + "\n");
        assertEquals(Math.sin(Math.PI / 6), taylorSeries.sinTaylor(Math.PI / 6), taylorSeries.getEpsilon());
        System.out.println("Argument: " + 135 + " degrees\nReal value: " + String.format("%.5f", Math.sin(3 * Math.PI / 4)) + "\n" + "Approximate value: " + String.format("%.5f", taylorSeries.sinTaylor(3 * Math.PI / 4)) + "\n");
        assertEquals(Math.sin(3 * Math.PI / 4), taylorSeries.sinTaylor(3 * Math.PI / 4), taylorSeries.getEpsilon());
    }

    @Test
    @DisplayName("Check random values sin(x)")
    void sinTaylorRandomValuesTest() {
        System.out.println("Argument: " + 53 + " degrees\nReal value: " + String.format("%.5f", Math.sin(53.0 / 180.0 * Math.PI)) + "\n" + "Approximate value: " + String.format("%.5f", taylorSeries.sinTaylor(53.0 / 180.0 * Math.PI)) + "\n");
        assertEquals(Math.sin(53.0 / 180.0 * Math.PI), taylorSeries.sinTaylor(53.0 / 180.0 * Math.PI), taylorSeries.getEpsilon());
        System.out.println("Argument: " + 190 + " degrees\nReal value: " + String.format("%.5f", Math.sin(190.0 / 180.0 * Math.PI)) + "\n" + "Approximate value: " + String.format("%.5f", taylorSeries.sinTaylor(190.0 / 180.0 * Math.PI)) + "\n");
        assertEquals(Math.sin(190.0 / 180.0 * Math.PI), taylorSeries.sinTaylor(190.0 / 180.0 * Math.PI), taylorSeries.getEpsilon());
        System.out.println("Argument: " + -261 + " degrees\nReal value: " + String.format("%.5f", Math.sin(-261.0 / 180.0 * Math.PI)) + "\n" + "Approximate value: " + String.format("%.5f", taylorSeries.sinTaylor(-261.0 / 180.0 * Math.PI)) + "\n");
        assertEquals(Math.sin(-261.0 / 180.0 * Math.PI), taylorSeries.sinTaylor(-261.0 / 180.0 * Math.PI), taylorSeries.getEpsilon() * 10);
    }

    @Test
    @DisplayName("Check count of terms sin(x)")
    void sinTaylorCountOfTerms() {
        taylorSeries.sinTaylor(Math.PI);
        assertEquals(9, taylorSeries.getTerms().size());
    }

    @AfterEach
    void clear() {
        taylorSeries.setTerms(new ArrayList<>());
    }

    @AfterAll
    static void end() {
        System.out.println("Ending tests of the Taylor Series for sin(x)...");
    }
}

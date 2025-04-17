package org.example.solver;


import java.math.BigDecimal;
import java.math.RoundingMode;

import org.example.math.log.Ln;
import org.example.math.log.Log2;
import org.example.math.log.Log3;
import org.example.math.log.Log5;
import org.example.math.trig.*;
import org.example.utils.CsvWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.example.math.log.Ln.ln;
import static org.example.math.log.Log2.log2;
import static org.example.math.log.Log3.log3;
import static org.example.math.log.Log5.log5;
import static org.example.math.trig.Cos.cos;
import static org.example.math.trig.Cot.cot;
import static org.example.math.trig.Csc.csc;
import static org.example.math.trig.Sec.sec;
import static org.example.math.trig.Sin.sin;
import static org.example.math.trig.Tan.tan;

public class Solver {
    private static double start;
    private static double end;
    private static double step;
    private static double epsilon;
    private static char delimiter;

    private static CsvWriter csvWriter;


    private static final Map<String, Function<Double, Double>> functions = new HashMap<>();

    static {
        functions.put("sin", Sin::sin);
        functions.put("cos", Cos::cos);
        functions.put("csc", Csc::csc);
        functions.put("sec", Sec::sec);
        functions.put("tan", Tan::tan);
        functions.put("cot", Cot::cot);
        functions.put("ln", Ln::ln);
        functions.put("log2", Log2::log2);
        functions.put("log3", Log3::log3);
        functions.put("log5", Log5::log5);
        functions.put("system", Solver::solvingSystem);
    }


    public static double firstEquation(double x) {
        return ((Math.pow(tan(x) / sin(x), 2) - sec(x) + tan(x) + cot(x) * cot(x) * csc(x)) / ((tan(x) * Math.pow(cos(x), 2)) / sec(x)))
                - Math.pow(sec(x) - cos(x), 3) + sec(x) + (cot(x) / sec(x) * (sin(x) - (cos(x) - cos(x))));
    }

    public static double secondEquation(double x) {
        return ((Math.pow((((log2(x) / log5(x)) - (ln(x) - ln(x))) - (Math.pow(log2(x), 2))), 3)) * log3(x));
    }

    public static double solvingSystem(double x) {
        if (x <= 0) {
            double arg = x;
            if (x % (Math.PI / 2) == 0) {
                arg = x - 0.1;
            }
            return firstEquation(arg);
        } else {
            return secondEquation(x);
        }
    }

    public static double calculate(String func, double x) {
        Function<Double, Double> function = functions.get(func.toLowerCase());
        if (function == null) {
            throw new IllegalArgumentException("Function not found" + func);
        }
        return function.apply(x);
    }

    public static void runFunc(String fileName, String func, double start, double end, double step, double epsilon, char delimiter) throws IOException {
        setEpsilon(epsilon);
        csvWriter = new CsvWriter(fileName);

        BigDecimal bdStart = BigDecimal.valueOf(start);
        BigDecimal bdEnd = BigDecimal.valueOf(end);
        BigDecimal bdStep = BigDecimal.valueOf(step);

        for (BigDecimal x = bdStart; x.compareTo(bdEnd) <= 0; x = x.add(bdStep)) {
            double xVal = x.doubleValue();
            double result = Solver.calculate(func, xVal);
            csvWriter.writeResultsToCsv(xVal, result, delimiter, 3);
        }

        csvWriter.close();
    }


    public static double getStart() {
        return start;
    }

    public static void setStart(double start) {
        Solver.start = start;
    }

    public static double getEnd() {
        return end;
    }

    public static void setEnd(double end) {
        Solver.end = end;
    }

    public static double getStep() {
        return step;
    }

    public static void setStep(double step) {
        Solver.step = step;
    }

    public static double getEpsilon() {
        return epsilon;
    }

    public static void setEpsilon(double epsilon) {
        Solver.epsilon = epsilon;
    }

    public static char getDelimiter() {
        return delimiter;
    }

    public static void setDelimiter(char delimiter) {
        Solver.delimiter = delimiter;
    }
}

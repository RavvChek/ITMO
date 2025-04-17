package org.example.utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CsvPlotter extends JFrame {

    private static final double MAX_ABS_Y = 10;

    public CsvPlotter(String filename) {
        int index = filename.lastIndexOf('/');
        String title = (index != -1) ? filename.substring(index + 1) : "Graph";

        setTitle("Graphic from CSV");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        XYSeries series = new XYSeries("Function from CSV");

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null) {
                line = line.replace(',', '.');

                if (skipHeader && (line.contains("X") || line.contains("Y"))) {
                    skipHeader = false;
                    continue;
                }

                String[] values = line.split(";");
                if (values.length >= 2) {
                    try {
                        double x = Double.parseDouble(values[0]);
                        double y = Double.parseDouble(values[1]);

                        if (Double.isFinite(y) && Math.abs(y) < MAX_ABS_Y) {
                            series.add(x, y);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Невозможно распарсить строку: " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                title, "X", "Y", dataset
        );

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);

        plot.setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(chart);
        setContentPane(chartPanel);
    }
}

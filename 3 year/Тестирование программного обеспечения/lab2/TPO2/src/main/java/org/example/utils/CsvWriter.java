package org.example.utils;

import java.io.FileWriter;
import java.io.IOException;

public class CsvWriter {
    private final FileWriter writer;
    private final String fileName;

    public CsvWriter(String filName) throws IOException {
        this.fileName = filName;
        writer = new FileWriter(fileName);
    }

    public void writeResultsToCsv(double x, double result, char delimiter, int decimalPlaces) throws IOException {
        String formattedX = String.format("%." + decimalPlaces + "f", x);
        String formattedResult = String.format("%." + decimalPlaces + "f", result);
        writer.write(formattedX + delimiter + formattedResult + "\n");
    }


    public void writeToCsv(String str) throws IOException {
        writer.write(str);
    }

    public String getFileName() {
        return fileName;
    }

    public void close() throws IOException {
        writer.close();
    }
}

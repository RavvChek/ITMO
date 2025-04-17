package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        try {
            manager.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


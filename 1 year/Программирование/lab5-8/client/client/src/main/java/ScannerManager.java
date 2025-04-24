import java.util.Scanner;


public class ScannerManager {
    private static Scanner Scanner;
    private static boolean fileMode = false;


    public static Scanner getScanner() {
        return Scanner;
    }


    public static void setScanner(java.util.Scanner scanner) {
        Scanner = scanner;
    }


    public static boolean isFileMode() {
        return fileMode;
    }


    public static void setFileMode() {
        ScannerManager.fileMode = true;
    }


    public static void setUserMode() {
        ScannerManager.fileMode = false;
    }
}
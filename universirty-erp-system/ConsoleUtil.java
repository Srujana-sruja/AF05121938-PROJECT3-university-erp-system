package util;

import java.util.Scanner;

/**
 * Shared Scanner + small input-helper methods used across all modules.
 */
public class ConsoleUtil {

    private static final Scanner scanner = new Scanner(System.in);

    /* ── raw reads ─────────────────────────────────────── */

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.println("  ✗  Please enter a valid integer.");
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  ✗  Please enter a valid number.");
            }
        }
    }

    /* ── UI helpers ─────────────────────────────────────── */

    public static void printHeader(String title) {
        int w = 60;
        String border = "═".repeat(w);
        System.out.println("\n╔" + border + "╗");
        int pad = (w - title.length()) / 2;
        System.out.printf("║%" + (pad + title.length()) + "s%" + (w - pad - title.length()) + "s║%n", title, "");
        System.out.println("╚" + border + "╝");
    }

    public static void printSectionLine() {
        System.out.println("─".repeat(62));
    }

    public static void pause() {
        readLine("\n  Press ENTER to continue...");
    }

    public static void printSuccess(String msg) { System.out.println("  ✔  " + msg); }
    public static void printError(String msg)   { System.out.println("  ✗  " + msg); }
    public static void printInfo(String msg)    { System.out.println("  ℹ  " + msg); }
}

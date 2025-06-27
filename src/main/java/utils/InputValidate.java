package main.java.utils;

import java.util.*;
import java.util.stream.Collectors;

public class InputValidate {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Reads an integer from the user between min and max (inclusive).
     * Keeps asking until a valid number is provided.
     */
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt+' ');
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.printf("\u274C Please enter a number between %d and %d.%n", min, max);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("\u274C Invalid number. Try again.");
            }
        }
    }

    /**
     * Reads a comma-separated list of doubles from the user.
     * Keeps asking until the list is valid.
     */
    public static List<Double> readDoubleList(String prompt) {
        while (true) {
            System.out.print(prompt + "➜ ");
            String line = scanner.nextLine().trim();
            try {
                List<Double> list = Arrays.stream(line.split(","))
                        .map(String::trim)
                        .map(Double::parseDouble)
                        .collect(Collectors.toList());
                if (list.isEmpty()) throw new IllegalArgumentException();
                return list;
            } catch (Exception e) {
                System.out.println("\u274C Invalid input. Use comma-separated decimal numbers like 1.2, 3.4.");
            }
        }
    }

}

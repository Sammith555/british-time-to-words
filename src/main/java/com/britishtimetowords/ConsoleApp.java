package com.britishtimetowords;

import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleApp {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("H:mm");

    private final TimeToWords timeToWords;
    private final Scanner scanner;
    private final PrintStream output;

    public ConsoleApp(TimeToWords timeToWords, Scanner scanner) {
        this(timeToWords, scanner, System.out);
    }

    public ConsoleApp(TimeToWords timeToWords, Scanner scanner, PrintStream output) {
        this.timeToWords = timeToWords;
        this.scanner = scanner;
        this.output = output;
    }

    public void run() {
        output.println("British Time to Words");
        output.println("Enter a time in 24-hour H:MM format (e.g. 14:05), or type 'quit'/'exit' to stop.");

        while (true) {
            output.print("> ");
            output.flush();

            if (!scanner.hasNextLine()) {
                break;
            }

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                LocalTime time = LocalTime.parse(input, TIME_FORMAT);
                output.println(timeToWords.toWords(time));
            } catch (DateTimeParseException e) {
                output.println("Error: Invalid time format. Please use H:MM, e.g. 14:05.");
            } catch (IllegalArgumentException e) {
                output.println("Error: " + e.getMessage());
            }
        }
    }
}

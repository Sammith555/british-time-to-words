package com.britishtimetowords;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleApp {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("H:mm");

    private final TimeToWords timeToWords;
    private final Scanner scanner;

    public ConsoleApp(TimeToWords timeToWords, Scanner scanner) {
        this.timeToWords = timeToWords;
        this.scanner = scanner;
    }

    public void run() {
        System.out.println("British Time to Words");
        System.out.println("Enter a time in 24-hour HH:MM format (e.g. 14:05), or type 'quit'/'exit' to stop.");

        while (true) {
            System.out.print("> ");
            System.out.flush();

            if (!scanner.hasNextLine()) {
                break;
            }

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                LocalTime time = LocalTime.parse(input, TIME_FORMAT);
                System.out.println(timeToWords.toWords(time));
            } catch (DateTimeParseException e) {
                System.out.println("Error: Invalid time format. Please use HH:MM, e.g. 14:05.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}

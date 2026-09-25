package com.britishtimetowords;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BritishTimeToWords implements CommandLineRunner {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("H:mm");

    public static void main(String[] args) {
        SpringApplication.run(BritishTimeToWords.class, args);
    }

    @Override
    public void run(String... args) {
        TimeToWords timeToWords = new TimeToWords();
        Scanner scanner = new Scanner(System.in);

        System.out.println("British Time to Words");
        System.out.println("Enter a time in 24-hour HH:MM format (e.g. 14:05), or type 'quit'/'exit' to stop.");
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                LocalTime time = LocalTime.parse(input, TIME_FORMAT);
                System.out.println(timeToWords.toWords(time.getHour(), time.getMinute()));
            } catch (DateTimeParseException e) {
                System.out.println("Error: Invalid time format. Please use HH:MM, e.g. 14:05.");
                continue;
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid number format");
                continue;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                continue;
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error: Invalid time format");
                continue;
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
                continue;
            }

            
        }

        scanner.close(); // Close the scanner after exiting the loop
    }
}
package com.britishtimetowords;

import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BritishTimeToWords implements CommandLineRunner {
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
                String[] parts = input.split(":");
                int hour = Integer.parseInt(parts[0]);
                int minute = Integer.parseInt(parts[1]);

                System.out.println(timeToWords.toWords(hour, minute));
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
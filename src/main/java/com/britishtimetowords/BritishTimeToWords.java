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
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a time (HH:MM): ");
        String input = scanner.nextLine().trim();

        String[] parts = input.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        System.out.println("Hour: " + hour + ", Minute: " + minute);
        scanner.close();
    }
}
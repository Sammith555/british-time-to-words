package com.britishtimetowords;

import java.util.Scanner;

public class BritishTimeToWords {

    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)) {
            new ConsoleApp(new TimeToWords(), scanner, System.out).run();
        }
        
    }
}
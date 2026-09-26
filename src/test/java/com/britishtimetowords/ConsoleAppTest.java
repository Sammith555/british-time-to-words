package com.britishtimetowords;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Test;

class ConsoleAppTest {

    @Test
    void printsWelcomeMessageAndResultForValidInput() {
        String output = runConsole("14:05\nquit\n");

        assertTrue(output.contains("British Time to Words"));
        assertTrue(output.contains("Enter a time in 24-hour H:MM format"));
        assertTrue(output.contains("> "));
        assertTrue(output.contains("five past two"));
    }

    @Test
    void acceptsQuitCommand() {
        String output = runConsole("quit\n");

        assertFalse(output.contains("Invalid time format"));
        assertFalse(output.contains("Error:"));
    }

    @Test
    void acceptsExitCommandIgnoringCaseAndWhitespace() {
        String output = runConsole("  ExIt  \n");

        assertFalse(output.contains("Invalid time format"));
        assertFalse(output.contains("Error:"));
    }

    @Test
    void reportsInvalidTextAndContinuesProcessing() {
        String output = runConsole("hello\n14:05\nquit\n");

        assertTrue(output.contains("Error: Invalid time format"));
        assertTrue(output.contains("five past two"));
    }

    @Test
    void rejectsInvalidHour() {
        String output = runConsole("25:00\nquit\n");

        assertTrue(output.contains("Error: Invalid time format"));
    }

    @Test
    void rejectsInvalidMinute() {
        String output = runConsole("12:75\nquit\n");

        assertTrue(output.contains("Error: Invalid time format"));
    }

    @Test
    void rejectsMissingMinuteZero() {
        String output = runConsole("12:5\nquit\n");

        assertTrue(output.contains("Error: Invalid time format"));
    }

    @Test
    void rejectsUnexpectedFormats() {
        String output = runConsole(
                "1400\n" +
                        "12:00:00\n" +
                        "14:05 extra\n" +
                        "quit\n");

        assertTrue(output.contains("Error: Invalid time format"));
    }

    @Test
    void trimsWhitespaceAroundValidInput() {
        String output = runConsole("  14:05  \nquit\n");

        assertTrue(output.contains("five past two"));
    }

    @Test
    void handlesMidnightNoonAndLastMinute() {
        String output = runConsole(
                "00:00\n" +
                        "12:00\n" +
                        "23:59\n" +
                        "quit\n");

        assertTrue(output.contains("midnight"));
        assertTrue(output.contains("noon"));
        assertTrue(output.contains("eleven fifty-nine"));
    }

    @Test
    void handlesEndOfInputWithoutQuitCommand() {
        String output = runConsole("14:05\n");

        assertTrue(output.contains("five past two"));
    }

    @Test
    void rejectsBlankInput() {
        String output = runConsole("\nquit\n");

        assertTrue(output.contains("Error: Invalid time format"));
    }

    @DisplayName("Malformed strings fail to parse via the app's time format")
    @ParameterizedTest
    @ValueSource(strings = { "abc", "25:00", "12:75", "12:5", "1400", "", "12:00:00" })
    void rejectsMalformedTimeStrings(String input) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("H:mm");
        assertThrows(DateTimeParseException.class, () -> LocalTime.parse(input, format));
    }

    private String runConsole(String input) {
        ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();

        try (
                Scanner scanner = new Scanner(input);
                PrintStream output = new PrintStream(capturedOutput)) {
            ConsoleApp consoleApp = new ConsoleApp(
                    new TimeToWords(),
                    scanner,
                    output);

            consoleApp.run();
            output.flush();

            return capturedOutput.toString();
        }
    }
}
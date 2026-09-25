package com.britishtimetowords;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class TimeToWords {

    private static final String[] ONES = {
        "zero", "one", "two", "three", "four", "five", "six", "seven",
        "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen",
        "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"
    };

    // Order matters: earlier rules take precedence; the last rule is the catch-all.
    private final List<TimeToWordsRule> rules = List.of(
        this::specificTimeToWords,
        this::oClockToWords,
        this::quarterAndHalfToWords,
        this::multiplesOfFiveToWords,
        this::actualReadingToWords
    );

    public String toWords(LocalTime localTime) {
        return rules.stream()
            .map(rule -> rule.convert(localTime))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "Unsupported time: " + localTime.toString()));
    }

    private Optional<String> specificTimeToWords(LocalTime localTime) {
        int hour = localTime.getHour();
        int minute = localTime.getMinute();
        // Implement the logic for specific times like "noon" or "midnight"
        if (hour == 0 && minute == 0) {
            return Optional.of("midnight");
        } else if (hour == 12 && minute == 0) {
            return Optional.of("noon");
        }

        return Optional.empty();
    }

    private Optional<String> oClockToWords(LocalTime localTime) {
        if (localTime.getMinute() == 0) {
            int hourRecalc = hourRecalc(localTime.getHour());
            return Optional.of(String.format("%s o'clock", ONES[hourRecalc]));
        }

        return Optional.empty();
    }

    private Optional<String> quarterAndHalfToWords(LocalTime localTime) {
        int hour = localTime.getHour();
        int minute = localTime.getMinute();
        int hourRecalc = hourRecalc(hour);
        int nextHour = nextTwelveHour(hour);

        if (minute == 15) {
            return Optional.of("quarter past " + ONES[hourRecalc]);
        } else if (minute == 30) {
            return Optional.of("half past " + ONES[hourRecalc]);
        } else if (minute == 45) {
            return Optional.of("quarter to " + ONES[nextHour]);
        }

        return Optional.empty();
    }

    private Optional<String> multiplesOfFiveToWords(LocalTime localTime) {
        int hour = localTime.getHour();
        int minute = localTime.getMinute();
        int hourRecalc = hourRecalc(hour);
        int nextHour = nextTwelveHour(hour);

        if (minute % 5 == 0) {
            if (minute < 30) {
                return Optional.of(minuteToWords(minute) + " past " + ONES[hourRecalc]);
            } else {
                return Optional.of(minuteToWords(60 - minute) + " to " + ONES[nextHour]);
            }
        }

        return Optional.empty();
    }

    private Optional<String> actualReadingToWords(LocalTime localTime) {
        int hour = localTime.getHour();
        int hourRecalc = hour % 12 == 0 ? 12 : hour % 12;

        return Optional.of(ONES[hourRecalc] + " " + numberToWords(localTime.getMinute()));
    }

    private String numberToWords(int number) {
        if (number < 20) {
            return ONES[number];
        }

        int tens = number / 10;
        int ones = number % 10;
        return ones == 0 ? minuteToWords(tens * 10) : minuteToWords(tens * 10) + "-" + ONES[ones];
    }

    private String minuteToWords(int minute) {
        switch (minute) {
            case 5: return "five";
            case 10: return "ten";
            case 20: return "twenty";
            case 25: return "twenty-five";
            case 30: return "thirty";
            case 40: return "forty";
            case 50: return "fifty";
            default: throw new IllegalArgumentException("Unsupported minute: " + minute);
        }
    }

    private int hourRecalc(int hour) {
        return hour % 12 == 0 ? 12 : hour % 12;
    }

    private int nextTwelveHour(int hour) {
        int hourRecalc = hourRecalc(hour);
        return (hourRecalc % 12) + 1;
    }
}

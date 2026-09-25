package com.britishtimetowords;

public class TimeToWords {

    public String toWords(int hour, int minute) {
        if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Invalid time");
        }

        String specificTime = specificTimeToWords(hour, minute);
        if (specificTime != null) {
            return specificTime;
        }
        // Implement the general logic for converting time to words here
        return String.format("%02d:%02d", hour, minute);
    }

    private String specificTimeToWords(int hour, int minute) {
        // Implement the logic for specific times like "noon" or "midnight"
        if (hour == 0 && minute == 0) {
            return "midnight";
        } else if (hour == 12 && minute == 0) {
            return "noon";
        }

        return null;
    }
}

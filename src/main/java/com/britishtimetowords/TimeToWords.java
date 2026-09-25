package com.britishtimetowords;

public class TimeToWords {

    private static final String[] ONES = {
        "zero", "one", "two", "three", "four", "five", "six", "seven",
        "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen",
        "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"
    };

    public String toWords(int hour, int minute) {
        if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Invalid time");
        }

        String specificTime = specificTimeToWords(hour, minute);
        if (specificTime != null) {
            return specificTime;
        }

        String oClock = oClockToWords(hour, minute);
        if (oClock != null) {
            return oClock;
        }

        String quarterAndHalf = quarterAndHalfToWords(hour, minute);
        if (quarterAndHalf != null) {
            return quarterAndHalf;
        }

        String multiplesOfFive = multiplesOfFiveToWords(hour, minute);
        if (multiplesOfFive != null) {
            return multiplesOfFive;
        }

        String actualReading = actualReadingToWords(hour, minute);
        if (actualReading != null) {
            return actualReading;
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

    private String oClockToWords(int hour, int minute) {
        if (minute == 0) {
            int hourRecalc = hour % 12 == 0 ? 12 : hour % 12;
            return String.format("%s o'clock", ONES[hourRecalc]);
        }

        return null;
    }

    private String quarterAndHalfToWords(int hour, int minute) {
        int hourRecalc = hour % 12 == 0 ? 12 : hour % 12;
        int nextHour = (hourRecalc % 12) + 1;

        if (minute == 15) {
            return "quarter past " + ONES[hourRecalc];
        } else if (minute == 30) {
            return "half past " + ONES[hourRecalc];
        } else if (minute == 45) {
            return "quarter to " + ONES[nextHour];
        }

        return null;
    }

    private String multiplesOfFiveToWords(int hour, int minute) {
        int hourRecalc = hour % 12 == 0 ? 12 : hour % 12;
        int nextHour = (hourRecalc % 12) + 1;

        if (minute % 5 == 0) {
            if (minute < 30) {
                return minuteToWords(minute) + " past " + ONES[hourRecalc];
            } else {
                return minuteToWords(60 - minute) + " to " + ONES[nextHour];
            }
        }

        return null;
    }

    private String actualReadingToWords(int hour, int minute) {
        int hourRecalc = hour % 12 == 0 ? 12 : hour % 12;

        return ONES[hourRecalc] + " " + numberToWords(minute);
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
}

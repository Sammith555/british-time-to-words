package com.britishtimetowords;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Assertions;

class TimeToWordsTest {

    private final TimeToWords timeToWords = new TimeToWords();

    @DisplayName("Given examples from the specification")
    @ParameterizedTest(name = "{0}:{1} -> \"{2}\"")
    @CsvSource({
            "1, 0, one o'clock",
            "2, 5, five past two",
            "3, 10, ten past three",
            "4, 15, quarter past four",
            "5, 20, twenty past five",
            "6, 25, twenty-five past six",
            "6, 32, six thirty-two",
            "7, 30, half past seven",
            "7, 35, twenty-five to eight",
            "8, 40, twenty to nine",
            "9, 45, quarter to ten",
            "10, 50, ten to eleven",
            "11, 55, five to twelve",
            "0, 0, midnight",
            "12, 0, noon"
    })
    void convertsSpecificationExamples(int hour, int minute, String expected) {
        assertEquals(expected, timeToWords.toWords(LocalTime.of(hour, minute)));
    }

    @Test
    @DisplayName("Midnight and noon are distinct from other :00/:01 times on the same hour")
    void distinguishesMidnightAndNoonFromNearbyTimes() {
        assertEquals("midnight", timeToWords.toWords(LocalTime.of(0, 0)));
        assertEquals("twelve one", timeToWords.toWords(LocalTime.of(0, 1)));
        assertEquals("noon", timeToWords.toWords(LocalTime.of(12, 0)));
        assertEquals("twelve one", timeToWords.toWords(LocalTime.of(12, 1)));
    }

    @DisplayName("24-hour times roll over correctly to 12-hour spoken form")
    @ParameterizedTest(name = "{0}:{1} -> \"{2}\"")
    @CsvSource({
            "13, 0, one o'clock",
            "18, 30, half past six",
            "0, 15, quarter past twelve",
            "23, 59, eleven fifty-nine"
    })
    void convertsTwentyFourHourTimesToTwelveHourForm(int hour, int minute, String expected) {
        assertEquals(expected, timeToWords.toWords(LocalTime.of(hour, minute)));
    }

    @DisplayName("Quarter-to / half-past hour rollover at hour boundaries")
    @ParameterizedTest(name = "{0}:{1} -> \"{2}\"")
    @CsvSource({
            "11, 45, quarter to twelve",
            "12, 45, quarter to one",
            "0, 45, quarter to one",
            "23, 45, quarter to twelve"
    })
    void rollsOverHourCorrectlyForQuarterTo(int hour, int minute, String expected) {
        assertEquals(expected, timeToWords.toWords(LocalTime.of(hour, minute)));
    }

    @DisplayName("All 'past' minute markers (5, 10, 20, 25)")
    @ParameterizedTest(name = "6:{0} -> \"{1}\"")
    @CsvSource({
            "5, five past six",
            "10, ten past six",
            "20, twenty past six",
            "25, twenty-five past six"
    })
    void convertsPastMinuteMarkers(int minute, String expected) {
        assertEquals(expected, timeToWords.toWords(LocalTime.of(6, minute)));
    }

    @DisplayName("All 'to' minute markers (35, 40, 50, 55)")
    @ParameterizedTest(name = "6:{0} -> \"{1}\"")
    @CsvSource({
            "35, twenty-five to seven",
            "40, twenty to seven",
            "50, ten to seven",
            "55, five to seven"
    })
    void convertsToMinuteMarkers(int minute, String expected) {
        assertEquals(expected, timeToWords.toWords(LocalTime.of(6, minute)));
    }

    @DisplayName("Irregular (non-multiple-of-5) minutes are read literally")
    @ParameterizedTest(name = "{0}:{1} -> \"{2}\"")
    @CsvSource({
            "6, 1, six one",
            "6, 2, six two",
            "6, 32, six thirty-two",
            "0, 7, twelve seven",
            "23, 59, eleven fifty-nine",
            "1, 21, one twenty-one"
    })
    void readsIrregularMinutesLiterally(int hour, int minute, String expected) {
        assertEquals(expected, timeToWords.toWords(LocalTime.of(hour, minute)));
    }

    @Test
    @DisplayName("Every minute of the day produces a non-blank result without throwing")
    void neverFailsOrReturnsBlankForAnyTimeOfDay() {
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute++) {
                LocalTime time = LocalTime.of(hour, minute);
                String result = timeToWords.toWords(time);
                assertNotNull(result, "result for " + time);
                assertFalse(result.isBlank(), "result for " + time);
            }
        }
    }

    @DisplayName("Boundary hours at the edges of the day")
    @ParameterizedTest(name = "{0}:{1} -> \"{2}\"")
    @CsvSource({
            "0, 0, midnight",
            "23, 0, eleven o'clock",
            "1, 0, one o'clock",
            "0, 30, half past twelve",
            "12, 30, half past twelve",
            "0, 59, twelve fifty-nine",
            "12, 59, twelve fifty-nine"
    })
    void handlesBoundaryHoursOfTheDay(int hour, int minute, String expected) {
        assertEquals(expected, timeToWords.toWords(LocalTime.of(hour, minute)));
    }

    @DisplayName("Every hour produces a distinct spoken hour word at :00")
    @Test
    void allTwelveHourWordsAreDistinctAtOClock() {
        java.util.Set<String> distinctWords = new java.util.HashSet<>();
        for (int hour = 0; hour < 24; hour++) {
            if (hour == 0 || hour == 12)
                continue; // midnight/noon short-circuit before o'clock rule
            distinctWords.add(timeToWords.toWords(LocalTime.of(hour, 0)));
        }
        assertEquals(11, distinctWords.size()); // hours 1-11 and 13-23 collapse to 11 unique 12-hour words
    }

    @Test
    @DisplayName("LocalTime itself rejects out-of-range values before reaching TimeToWords")
    void localTimeRejectsInvalidHourAndMinute() {
        assertThrows(java.time.DateTimeException.class, () -> LocalTime.of(24, 0));
        assertThrows(java.time.DateTimeException.class, () -> LocalTime.of(-1, 0));
        assertThrows(java.time.DateTimeException.class, () -> LocalTime.of(0, 60));
        assertThrows(java.time.DateTimeException.class, () -> LocalTime.of(0, -1));
    }

    @DisplayName("Literal minute values use the correct number words")
    @ParameterizedTest(name = "{0}:{1} -> \"{2}\"")
    @CsvSource({
            "6, 3, six three",
            "6, 4, six four",
            "6, 6, six six",
            "6, 7, six seven",
            "6, 8, six eight",
            "6, 9, six nine",
            "6, 11, six eleven",
            "6, 12, six twelve",
            "6, 13, six thirteen",
            "6, 14, six fourteen",
            "6, 16, six sixteen",
            "6, 17, six seventeen",
            "6, 18, six eighteen",
            "6, 19, six nineteen",
            "6, 21, six twenty-one",
            "6, 22, six twenty-two",
            "6, 29, six twenty-nine",
            "6, 31, six thirty-one",
            "6, 34, six thirty-four",
            "6, 38, six thirty-eight",
            "6, 41, six forty-one",
            "6, 46, six forty-six",
            "6, 51, six fifty-one",
            "6, 58, six fifty-eight",
            "6, 59, six fifty-nine"
    })
    void convertsLiteralMinuteValues(int hour, int minute, String expected) {
        assertEquals(expected, timeToWords.toWords(LocalTime.of(hour, minute)));
    }

    @DisplayName("To expressions use the following hour")
    @ParameterizedTest(name = "{0}:{1} -> \"{2}\"")
    @CsvSource({
            "1, 35, twenty-five to two",
            "5, 40, twenty to six",
            "10, 50, ten to eleven",
            "11, 55, five to twelve",
            "12, 35, twenty-five to one",
            "13, 40, twenty to two",
            "23, 50, ten to twelve",
            "0, 55, five to one"
    })
    void usesNextHourForToExpressions(int hour, int minute, String expected) {
        assertEquals(expected, timeToWords.toWords(LocalTime.of(hour, minute)));
    }

    @ParameterizedTest
    @CsvSource({
            "6, 13, six thirteen",
            "6, 21, six twenty-one",
            "6, 34, six thirty-four",
            "6, 41, six forty-one",
            "6, 59, six fifty-nine"
    })
    void convertsUnroundedMinuteValues(int hour, int minute, String expected) {
        assertEquals(expected, timeToWords.toWords(LocalTime.of(hour, minute)));
    }

    @Test
    @DisplayName("Every valid time produces a non-blank spoken result")
    void everyValidTimeProducesSpokenOutput() {
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute++) {
                String result = timeToWords.toWords(LocalTime.of(hour, minute));

                assertNotNull(result);
                assertFalse(result.isBlank());
            }
        }
    }

    @Test
    void rejectsNullTime() {
        assertThrows(NullPointerException.class, () -> timeToWords.toWords(null));
    }
}
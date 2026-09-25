package com.britishtimetowords;
import java.time.LocalTime;
import java.util.Optional;

@FunctionalInterface
public interface TimeToWordsRule {
    Optional<String> convert(LocalTime localTime);
}

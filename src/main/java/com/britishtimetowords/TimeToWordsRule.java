package com.britishtimetowords;
import java.util.Optional;

@FunctionalInterface
public interface TimeToWordsRule {
    Optional<String> convert(int hour, int minute);
}

# british-time-to-words

A standalone Java console application that converts 24-hour times into words using British conventions.

## Requirements

- Java 21 or later
- No separate Gradle installation is required
- The repository includes the Gradle Wrapper

Check your Java version:

```bash
java --version
```

## Run the application

From the project root, run:

```bash
./gradlew run
```

On Windows:

```bat
gradlew.bat run
```

The application displays:

```text
British Time to Words
Enter a time in 24-hour H:MM format (e.g. 14:05), or type 'quit'/'exit' to stop.
>
```

Enter a time using `H:mm` format. The hour may have one or two digits, but the minutes must have two digits.

Example:

```text
> 14:05
five past two
```

## Examples

| Input | Output |
| --- | --- |
| `1:00` | `one o'clock` |
| `2:05` | `five past two` |
| `4:15` | `quarter past four` |
| `7:30` | `half past seven` |
| `9:45` | `quarter to ten` |
| `14:05` | `five past two` |
| `23:59` | `eleven fifty-nine` |
| `00:00` | `midnight` |
| `12:00` | `noon` |

## Exit the application

Type either command:

```text
quit
```

or:

```text
exit
```

These commands are case-insensitive and may include surrounding whitespace.

## Invalid input

Invalid input displays an error and allows another attempt:

```text
> 12:75
Error: Invalid time format. Please use H:MM, e.g. 14:05.
```

Examples of invalid input include:

```text
abc
12:75
12:5
1400
12:00:00
```

## Run the tests

Run all tests:

```bash
./gradlew test
```

Clean previous build output and run all tests:

```bash
./gradlew clean test
```

The tests cover:

- British time-to-words conversion rules
- Midnight and noon
- 12-hour and 24-hour inputs
- Quarter-past, half-past, and quarter-to times
- Hour rollover at boundaries
- Irregular minute values
- Invalid console input
- Quit and exit commands
- Whitespace handling
- End-of-input handling
- Console output

## Project structure

```text
src/main/java/com/britishtimetowords/
├── BritishTimeToWords.java  # Application entry point
├── ConsoleApp.java          # Console input/output loop
├── TimeToWords.java         # Time conversion logic
└── TimeToWordsRule.java     # Conversion rule interface

src/test/java/com/britishtimetowords/
├── ConsoleAppTest.java      # Console input and output tests
└── TimeToWordsTest.java     # Conversion logic tests
```

## Troubleshooting

On Linux or macOS, if the Gradle Wrapper is not executable:

```bash
chmod +x gradlew
```

Then run:

```bash
./gradlew run
```

This application is a plain Java console application. It does not require Spring Boot or Docker.
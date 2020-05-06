package ru.siberteam.statistic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TextStatistic {
    private static final Logger LOG = LogManager.getLogger(TextStatistic.class);

    private List<CharStatistic> charStatisticList;

    private final DecimalFormat outputFormat;

    public TextStatistic() {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        this.outputFormat = new DecimalFormat("##.##", otherSymbols);
    }

    public void collectCharStatistic(String fileName, Comparator<CharStatistic> sortMode) {
        Map<Character, Integer> charMap = this.characterMapFromFile(fileName);
        charStatisticList = new ArrayList<>();

        if (charMap.isEmpty()) {
            LOG.error("The file {} is empty or no valid characters!", fileName);
            return;
        }
        long numberChars = charMap.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        if (numberChars <= 0) {
            LOG.error("Failed to count the number of characters in the file {}", fileName);
            return;
        }
        charStatisticList = charMap.entrySet().stream()
                .map(entry -> new CharStatistic(entry.getKey(), entry.getValue(),
                        (double) entry.getValue() / numberChars * 100))
                .sorted(sortMode)
                .collect(Collectors.toList());
    }

    private Map<Character, Integer> characterMapFromFile(String fileName) {
        Map<Character, Integer> charMap;
        try (Stream<String> stringStream = Files.lines(Paths.get(fileName))) {
            charMap = stringStream
                    .flatMapToInt(String::codePoints)
                    .filter(Character::isLetterOrDigit)
                    .mapToObj(i -> (char) i)
                    .collect(Collectors.toMap(Function.identity(), ch -> 1, Integer::sum));

        } catch (IOException e) {
            LOG.error("Unsuccessful attempt to read a file:", e);
            charMap = new HashMap<>();
        }
        return charMap;
    }

    private String makeBarChart(int count, String chartBarString) {
        return IntStream.range(0, count)
                .mapToObj(i->chartBarString)
                .collect(Collectors.joining());
    }

    private String makeString(CharStatistic charStatistic) {
        return charStatistic.getCh() +
                " (" + outputFormat.format(charStatistic.getPercent()) + "%) " +
                makeBarChart((int) Math.round(charStatistic.getPercent()), "#");
    }

    public void writeToFile(String fileName, int outputLimitation) {
        if (charStatisticList.isEmpty()) {
            LOG.error("No data to record");
            return;
        }
        try {
            List<String> lines = charStatisticList.stream()
                    .map(this::makeString)
                    .limit(outputLimitation > 0 ? outputLimitation : charStatisticList.size())
                    .collect(Collectors.toList());
            Files.write(Paths.get(fileName), lines);
        } catch (IOException e) {
            LOG.error("Unsuccessful attempt to write to file:", e);
        }
    }
}
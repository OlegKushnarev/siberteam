package ru.siberteam.statistic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.siberteam.sort.SortMode;

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
        outputFormat = new DecimalFormat("##.##", otherSymbols);
    }

    public void collectCharStatistic(String fileName, SortMode sortMode) throws IOException {
        Map<Character, Integer> charMap = characterMapFromFile(fileName);
        charStatisticList = new ArrayList<>();

        if (charMap.isEmpty()) {
            LOG.error("The file {} is empty or no valid characters!", fileName);
            return;
        }
        long numberChars = charMap.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        CharStatisticFactory statisticFactory = new CharStatisticFactory(numberChars);
        charStatisticList = charMap.entrySet().stream()
                .map(statisticFactory::createCharStatistic)
                .sorted((Comparator<CharStatistic>) sortMode.getSortOrder())
                .collect(Collectors.toList());
    }

    private Map<Character, Integer> characterMapFromFile(String fileName) throws IOException {
        try (Stream<String> stringStream = Files.lines(Paths.get(fileName))) {
            return stringStream
                    .flatMapToInt(String::codePoints)
                    .filter(Character::isLetterOrDigit)
                    .mapToObj(i -> (char) i)
                    .collect(Collectors.toMap(Function.identity(), ch -> 1, Integer::sum));
        }
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

    public void writeToFile(String fileName, int outputLimitation) throws IOException {
        if (charStatisticList.isEmpty()) {
            LOG.error("No data to record");
            return;
        }

        List<String> lines = charStatisticList.stream()
                .map(this::makeString)
                .limit(outputLimitation > 0 ? outputLimitation : charStatisticList.size())
                .collect(Collectors.toList());
        Files.write(Paths.get(fileName), lines);
    }
}
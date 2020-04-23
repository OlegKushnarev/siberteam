package ru.siberteam.statistic;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.stream.Collectors;

public class TextStatistic {
    private static final Logger LOG = LogManager.getLogger(TextStatistic.class);
    List<CharStatistic> charStatisticList;

    public TextStatistic(String fileName) {
        Map<Character, Integer> charMap = this.characterMapFromFile(fileName);

        if (!charMap.isEmpty()) {
            long numberChars = charMap.values().stream()
                    .mapToInt(Integer::intValue)
                    .sum();

            if (numberChars > 0) {
                this.charStatisticList = charMap.entrySet().stream()
                        .map(entry -> new CharStatistic(entry.getKey(), entry.getValue(),
                                (double) entry.getValue() / numberChars * 100))
                        .sorted((o1, o2) -> o2.getNumber() - o1.getNumber())
                        .collect(Collectors.toList());
            }
        }
    }

    public void setPercentFormat(String percentPattern, char percentSeparator) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(percentSeparator);
        DecimalFormat outputFormat = new DecimalFormat(percentPattern, otherSymbols);
        CharStatistic.setPercentOutputFormat(outputFormat);
    }

    private Map<Character, Integer> characterMapFromFile(String fileName) {
        Map<Character, Integer> charMap = new HashMap<>();

        try {
            Files.lines(Paths.get(fileName))
                    .forEach(str -> str.codePoints()
                            .filter(i -> !Character.isWhitespace(i))
                            .mapToObj(i -> (char) i)
                            .forEach(ch -> charMap.merge(ch, 1, Integer::sum))
                    );
        } catch (IOException e) {
            LOG.error("StackTrace:", e);
        }
        return charMap;
    }

    public void writeToFile(String fileName) {
        if (this.charStatisticList != null && !this.charStatisticList.isEmpty()) {
            try {
                Files.write(Paths.get(fileName), this.charStatisticList.stream()
                        .map(CharStatistic::toString)
                        .collect(Collectors.toList()));
            } catch (IOException e) {
                LOG.error("StackTrace:", e);
            }
        }
    }
}
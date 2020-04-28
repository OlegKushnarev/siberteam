package ru.siberteam.statistic;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TextStatistic {
    private static final Logger LOG = LogManager.getLogger(TextStatistic.class);
    private List<CharStatistic> charStatisticList;

    private DecimalFormat outputFormat = new DecimalFormat("##.##");

    public void collectCharStatistic(String fileName) {
        Map<Character, Integer> charMap = this.characterMapFromFile(fileName);

        if (charMap != null && !charMap.isEmpty()) {
            long numberChars = charMap.values().stream()
                    .mapToInt(Integer::intValue)
                    .sum();

            if (numberChars > 0) {
                this.charStatisticList = charMap.entrySet().stream()
                        .map(entry -> new CharStatistic(entry.getKey(), entry.getValue(),
                                (double) entry.getValue() / numberChars * 100))
                        .sorted()
                        .collect(Collectors.toList());
            }
        }
    }

    public void setPercentFormat(String percentPattern, char percentSeparator) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(percentSeparator);
        this.outputFormat = new DecimalFormat(percentPattern, otherSymbols);
    }

    private Map<Character, Integer> characterMapFromFile(String fileName) {
        try (Stream<String> stringStream = Files.lines(Paths.get(fileName))) {
            return stringStream
                    .flatMapToInt(str -> str.codePoints()
                            .filter(i -> !Character.isWhitespace(i) && Character.isLetterOrDigit(i)))
                    .mapToObj(i -> (char) i)
                    .collect(Collectors.toMap(Function.identity(), ch -> 1, Integer::sum));

        } catch (IOException e) {
            LOG.error("Unsuccessful attempt to read a file:", e);
        }
        return null;
    }

    private String makeBarChart(int count, String chartBarString) {
        return IntStream.range(0, count)
                .collect(StringBuilder::new,
                        (sb, i) -> sb.append(chartBarString),
                        StringBuilder::append
                ).toString();
    }

    private String makeString(CharStatistic charStatistic) {
        return charStatistic.getCh() +
                " (" + this.outputFormat.format(charStatistic.getPercent()) + "%) " +
                this.makeBarChart((int) Math.round(charStatistic.getPercent()), "#");
    }

    public void writeToFile(String fileName, int outputLimitation) {
        if (this.charStatisticList != null && !this.charStatisticList.isEmpty()) {
            try {
                Files.write(Paths.get(fileName), this.charStatisticList.stream()
                        .map(this::makeString)
                        .limit((outputLimitation > 0 && outputLimitation < this.charStatisticList.size()) ?
                                outputLimitation : this.charStatisticList.size())
                        .collect(Collectors.toList()));
            } catch (IOException e) {
                LOG.error("Unsuccessful attempt to write to file:", e);
            }
        }
    }
}
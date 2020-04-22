package ru.siberteam.statistics;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class CharacterStatistics {
    private static final Logger LOG = LogManager.getLogger(CharacterStatistics.class);
    private final List<CharStatistics> charStatisticsList = new ArrayList<>();

    public CharacterStatistics(String fileName) {
        Map<Character, Integer> charMap = this.characterMapFromFile(fileName);
        int numberChars = getCharactersNumber(charMap.values());

        for (Map.Entry<Character, Integer> entry : charMap.entrySet()) {
            this.charStatisticsList.add(new CharStatistics(entry.getKey(), entry.getValue(),
                    (double) entry.getValue() / numberChars * 100));
        }
        charStatisticsList.sort((o1, o2) -> o2.getNumber() - o1.getNumber());
    }

    private Map<Character, Integer> characterMapFromFile(String fileName) {
        Map<Character, Integer> charMap = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
            while (reader.ready()) {
                for (Character ch :
                        reader.readLine().toCharArray()) {
                    if (!Character.isWhitespace(ch)) {
                        int count = 0;
                        if (charMap.containsKey(ch)) {
                            count = charMap.get(ch);
                        }
                        charMap.put(ch, ++count);
                    }
                }
            }
        } catch (IOException e) {
            LOG.debug("Unsuccessful attempt to read a file " + e.getMessage());
        }
        return charMap;
    }

    private int getCharactersNumber(Collection<Integer> counts) {
        int count = 0;
        for (Integer number :
                counts) {
            count += number;
        }
        return count;
    }

    private String makeBarChart(int count, String symbol) {
        StringBuilder barChar = new StringBuilder();
        for (int i = 0; i < count; ++i) {
            barChar.append(symbol);
        }
        return barChar.toString();
    }

    public void writeToFile(String fileName) {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(fileName))) {
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
            otherSymbols.setDecimalSeparator('.');
            DecimalFormat outputFormat = new DecimalFormat("##.#", otherSymbols);

            for (CharStatistics charStatistic :
                    this.charStatisticsList) {
                bufferedWriter.write(charStatistic.getCh() + " " +
                        "(" + outputFormat.format(charStatistic.getPercent()) + "%) " +
                        makeBarChart((int) Math.round(charStatistic.getPercent()), "#") +
                        System.lineSeparator());
            }
        } catch (IOException e) {
            LOG.debug("Unsuccessful attempt to write to file " + e.getMessage());
        }
    }
}
package ru.siberteam.statistic;

import java.util.Map;

public class CharStatisticFactory {
    private final long numberChars;

    public CharStatisticFactory(long numberChars) {
        this.numberChars = numberChars;
    }

    public CharStatistic createCharStatistic(Map.Entry<Character, Integer> entry) {
        return new CharStatistic(entry.getKey(), entry.getValue(),
                (double) entry.getValue() / numberChars * 100);
    }
}
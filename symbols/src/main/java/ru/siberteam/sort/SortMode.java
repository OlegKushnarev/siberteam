package ru.siberteam.sort;

import ru.siberteam.statistic.CharStatistic;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public enum SortMode {
    A((o1, o2) -> o1.getNumber() - o2.getNumber(), "Use the letter \"A\" to sort ascending."),
    D(CharStatistic::compareTo, "Use the letter \"D\" to sort descending." +
            "By default, descending sorting is used.");

    private final Comparator<CharStatistic> sortFunction;

    private final String description;

    public Comparator<CharStatistic> getSortFunction() {
        return this.sortFunction;
    }

    public static String description() {
        return Arrays.stream(SortMode.values())
                .map(Enum::name)
                .map(SortMode::valueOf)
                .map(sortMode -> sortMode.description)
                .collect(Collectors.joining(" "));
    }

    SortMode(Comparator<CharStatistic> sortFunction, String description) {
        this.sortFunction = sortFunction;
        this.description = description;
    }
}
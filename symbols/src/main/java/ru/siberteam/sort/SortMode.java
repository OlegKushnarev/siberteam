package ru.siberteam.sort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public enum SortMode {
    ASC(Comparator.reverseOrder(), "Use \"ASC\" to sort ascending."),
    DESC(Comparator.naturalOrder(), "Use \"DESC\" to sort descending." +
            "By default, descending sorting is used.");

    private final Comparator<?> sortOrder;

    private final String description;

    SortMode(Comparator<?> sortOrder, String description) {
        this.sortOrder = sortOrder;
        this.description = description;
    }

    public Comparator<?> getSortOrder() {
        return this.sortOrder;
    }

    public static String description() {
        return Arrays.stream(SortMode.values())
                .map(sortMode -> sortMode.description)
                .collect(Collectors.joining(" "));
    }

    public static String possibleValues() {
        return Arrays.stream(SortMode.values())
                .map(Enum::name)
                .collect(Collectors.joining(" "));
    }
}
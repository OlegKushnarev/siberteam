package ru.siberteam.sorter;

import java.util.Comparator;
import java.util.stream.Stream;

public class WordLengthSorter extends Sorter {
    @Override
    public Stream<String> sort(Stream<String> stringStream) {
        return stringStream
                .sorted(Comparator.comparing(String::length));
    }
}

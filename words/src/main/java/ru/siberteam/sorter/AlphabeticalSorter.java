package ru.siberteam.sorter;

import java.util.stream.Stream;

public class AlphabeticalSorter extends Sorter {
    @Override
    public Stream<String> sort(Stream<String> stringStream) {
        return stringStream
                .sorted(String::compareToIgnoreCase);
    }
}
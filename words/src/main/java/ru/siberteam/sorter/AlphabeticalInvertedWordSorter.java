package ru.siberteam.sorter;

import java.util.stream.Stream;

public class AlphabeticalInvertedWordSorter extends AlphabeticalSorter {
    @Override
    public Stream<String> sort(Stream<String> stringStream) {
        return super.sort(stringStream
                .map(str -> new StringBuilder(str).reverse().toString()));
    }
}
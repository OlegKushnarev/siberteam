package ru.siberteam.sorter;

import java.util.stream.Stream;

public class AlphabeticalInvertedWordSorter extends Sorter {
    @Override
    public Stream<String> sort(Stream<String> stringStream) {
        return stringStream
                .map(str -> new StringBuilder(str).reverse().toString())
                .sorted(String::compareToIgnoreCase);
    }

    @Override
    public String sortDescription() {
        return "Use " + this.getClass().getName() + " to sort alphabetically inverted words." + System.lineSeparator();
    }
}
package ru.siberteam.sorter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class VowelNumberSorter extends Sorter {
    private final Set<Character> vowels = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u', 'y', 'а', 'я', 'у', 'ю', 'и', 'ы', 'э', 'е', 'о', 'ё'));

    private long getVowelCount(String str) {
        return str.toLowerCase()
                .codePoints()
                .mapToObj(i -> (char) i)
                .filter(vowels::contains)
                .count();
    }

    @Override
    protected Stream<String> sort(Stream<String> stringStream) {
        return stringStream
                .sorted(Comparator.comparing(this::getVowelCount));
    }

    @Override
    public String sortDescription() {
        return "Use " + this.getClass().getName() + " to sort words by the number of vowels in a word." + System.lineSeparator();
    }
}
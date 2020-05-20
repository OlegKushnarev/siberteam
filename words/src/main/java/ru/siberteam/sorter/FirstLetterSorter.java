package ru.siberteam.sorter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class FirstLetterSorter extends Sorter {
    private final Set<Character> vowels = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u', 'y', 'а', 'я', 'у', 'ю', 'и', 'ы', 'э', 'е', 'о', 'ё'));

    private boolean firstLetterIsVowel(String str) {
        return str.toLowerCase()
                .codePoints()
                .mapToObj(i -> (char) i)
                .findFirst()
                .filter(vowels::contains).isPresent();
    }

    @Override
    protected Stream<String> sort(Stream<String> stringStream) {
        return stringStream
                .sorted(Comparator.comparing(this::firstLetterIsVowel).reversed());
    }

    @Override
    public String sortDescription() {
        return "Use " + this.getClass().getName() + " to sort words by first letter (vowel or consonant)." + System.lineSeparator();
    }
}
package ru.siberteam.sorter;

import ru.siberteam.description.Description;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

@Description("Use ru.siberteam.sorter.VowelNumberSorter to sort words by the number of vowels in a word.")
public class VowelNumberSorter implements Sorter {
    private final Set<Character> vowels = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u', 'y', 'а', 'я', 'у', 'ю', 'и', 'ы', 'э', 'е', 'о', 'ё'));

    private long getVowelCount(String str) {
        return str.toLowerCase()
                .codePoints()
                .mapToObj(i -> (char) i)
                .filter(vowels::contains)
                .count();
    }

    @Override
    public int compare(String str1, String str2) {
        Comparator<String> vowelCountComparator = Comparator.comparing(this::getVowelCount);
        return vowelCountComparator.compare(str1, str2);
    }
}
package ru.siberteam.sorter;

import ru.siberteam.description.Description;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

@Description("Use ru.siberteam.sorter.FirstLetterSorter to sort words by first letter (vowel or consonant).")
public class FirstLetterSorter implements Sorter {
    private final Set<Character> vowels = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u', 'y', 'а', 'я', 'у', 'ю', 'и', 'ы', 'э', 'е', 'о', 'ё'));

    private boolean firstLetterIsVowel(String str) {
        return str.toLowerCase()
                .codePoints()
                .mapToObj(i -> (char) i)
                .findFirst()
                .filter(vowels::contains).isPresent();
    }

    @Override
    public int comparator(String str1, String str2) {
        Comparator<String> firstLetterIsVowelComparator = Comparator.comparing(this::firstLetterIsVowel).reversed();
        return firstLetterIsVowelComparator.compare(str1, str2);
    }
}
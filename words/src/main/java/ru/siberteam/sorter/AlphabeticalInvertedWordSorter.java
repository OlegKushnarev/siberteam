package ru.siberteam.sorter;

import ru.siberteam.description.Description;

@Description("Use ru.siberteam.sorter.AlphabeticalInvertedWordSorter to sort alphabetically inverted words.")
public class AlphabeticalInvertedWordSorter implements Sorter {

    @Override
    public String mapper(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    @Override
    public int comparator(String str1, String str2) {
        return str1.compareToIgnoreCase(str2);
    }
}
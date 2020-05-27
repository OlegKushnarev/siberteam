package ru.siberteam.sorter;

import ru.siberteam.description.Description;

import java.util.Comparator;

@Description("Use ru.siberteam.sorter.WordLengthSorter to sort by word length.")
public class WordLengthSorter implements Sorter {

    @Override
    public int compare(String str1, String str2) {
        Comparator<String> strLengthComparator = Comparator.comparing(String::length);
        return strLengthComparator.compare(str1, str2);
    }
}
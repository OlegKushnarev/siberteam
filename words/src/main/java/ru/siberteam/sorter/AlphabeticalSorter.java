package ru.siberteam.sorter;

import ru.siberteam.description.Description;

@Description("Use ru.siberteam.sorter.AlphabeticalSorter to sort alphabetically.")
public class AlphabeticalSorter implements Sorter {

    @Override
    public int compare(String str1, String str2) {
        return str1.compareToIgnoreCase(str2);
    }
}
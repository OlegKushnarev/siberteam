package ru.siberteam.sorter;

public interface Sorter {

    default String transform(String str) {
        return str;
    }

    int compare(String str1, String str2);
}
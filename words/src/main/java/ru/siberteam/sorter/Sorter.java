package ru.siberteam.sorter;

public interface Sorter {
    default boolean predicate(String str) {
        return true;
    }

    default String mapper(String str) {
        return str;
    }

    int comparator(String str1, String str2);
}
package ru.siberteam.sorter;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

public abstract class Sorter {
    Iterator<String> streamIterator;

    protected abstract Stream<String> sort(Stream<String> stringStream);

    private boolean thisWord(String str) {
        return str
                .codePoints()
                .mapToObj(i -> (char) i)
                .allMatch(Character::isLetter);
    }

    public void sortWords(Stream<String> stringStream) {
        Stream<String> strStream = stringStream
                .flatMap(str -> Arrays.stream(str.split(" ")))
                .filter(this::thisWord);
        streamIterator = sort(strStream)
                .iterator();
    }

    public boolean hasNext() {
        return streamIterator.hasNext();
    }

    public String next() {
        return streamIterator.next() + System.lineSeparator();
    }
}
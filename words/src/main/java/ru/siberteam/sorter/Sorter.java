package ru.siberteam.sorter;

import java.util.Arrays;
import java.util.stream.Stream;

public abstract class Sorter {
    protected abstract Stream<String> sort(Stream<String> stringStream);

    public abstract String sortDescription();

    private boolean isWord(String str) {
        return str
                .codePoints()
                .mapToObj(i -> (char) i)
                .allMatch(Character::isLetter);
    }

    public Stream<String> sortWords(Stream<String> stringStream) {
        Stream<String> strStream = stringStream
                .flatMap(str -> Arrays.stream(str.split(" ")))
                .distinct()
                .filter(str -> this.isWord(str) && !str.isEmpty());
        return sort(strStream);
    }
}
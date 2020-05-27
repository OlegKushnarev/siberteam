package ru.siberteam.launcher;

import ru.siberteam.sorter.Sorter;

import java.util.Arrays;
import java.util.stream.Stream;

public class SortLauncher {
    private final Sorter sorter;

    public SortLauncher(Sorter sorter) {
        this.sorter = sorter;
    }

    private boolean isWord(String str) {
        return str
                .codePoints()
                .mapToObj(i -> (char) i)
                .allMatch(Character::isLetter);
    }

    public Stream<String> launchSort(Stream<String> stringStream) {
        return stringStream
                .flatMap(str -> Arrays.stream(str.split(" ")))
                .filter(str -> !str.isEmpty())
                .filter(this::isWord)
                .map(String::toLowerCase)
                .distinct()
                .map(sorter::transform)
                .sorted(sorter::compare);
    }

    public String getSorterName() {
        return sorter.getClass().getSimpleName();
    }
}
